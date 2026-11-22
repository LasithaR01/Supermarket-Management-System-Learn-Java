package cmart.service;

import cmart.config.AppInfo;
import cmart.config.DatabaseConfig;
import cmart.config.SettingKeys;
import cmart.dao.BackupLogDao;
import cmart.dao.SettingDao;
import cmart.dao.UserDao;
import cmart.db.ConnectionFactory;
import cmart.model.AppUser;
import cmart.security.PasswordHasher;
import cmart.security.Permission;
import cmart.security.Role;
import cmart.security.SessionContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Super Admin system health, unlock, demo-password warnings, and ops status.
 */
public class SystemMonitorService {

    private final UserDao userDao = new UserDao();
    private final SettingDao settingDao = new SettingDao();
    private final BackupLogDao backupLogDao = new BackupLogDao();
    private final ErrorLogService errorLogService = new ErrorLogService();
    private final AuditService auditService = new AuditService();
    private final AuthService authService = new AuthService();

    public void requireSuperAdmin() {
        SessionContext.require(Permission.SYSTEM);
        AppUser user = SessionContext.currentUser();
        if (user == null || user.getRole() != Role.SUPER_ADMIN) {
            throw new SecurityException("Super Admin only.");
        }
    }

    public String appVersion() {
        return AppInfo.APP_VERSION;
    }

    public String schemaLabel() {
        return AppInfo.SCHEMA_LABEL;
    }

    public String dbHealth() {
        requireSuperAdmin();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            if (con == null || con.isClosed()) {
                return "FAIL — no connection (check config/db.properties)";
            }
            String url = DatabaseConfig.url();
            return "OK — connected (" + (url == null ? "?" : url) + ")";
        } catch (Exception e) {
            errorLogService.error("SystemMonitor", "DB health check failed", e);
            return "FAIL — " + e.getMessage();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public int lockedUserCount() throws SQLException {
        requireSuperAdmin();
        return userDao.countLockedNow();
    }

    public List<AppUser> lockedUsers() throws SQLException {
        requireSuperAdmin();
        return userDao.findLockedNow();
    }

    public void unlockUser(int userId) throws SQLException {
        requireSuperAdmin();
        AppUser user = userDao.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        userDao.unlockUser(userId);
        auditService.record(AuditService.USER_UNLOCK, user.getUsername(), "Unlocked by Super Admin");
    }

    public int errorsLast24h() throws SQLException {
        return errorLogService.countLast24Hours();
    }

    public boolean recoveryPinConfigured() throws SQLException {
        requireSuperAdmin();
        return authService.isRecoveryPinConfigured();
    }

    public String[] latestBackup() throws SQLException {
        requireSuperAdmin();
        return backupLogDao.findLatest();
    }

    public List<String[]> recentBackups(int limit) throws SQLException {
        requireSuperAdmin();
        return backupLogDao.findRecent(limit);
    }

    public List<String> demoPasswordWarnings() throws SQLException {
        requireSuperAdmin();
        List<String> warnings = new ArrayList<String>();
        checkDemo(warnings, AuthService.BOOTSTRAP_SUPER, "super123");
        checkDemo(warnings, AuthService.BOOTSTRAP_OWNER, "owner123");
        checkDemo(warnings, AuthService.BOOTSTRAP_CASHIER, "cashier123");
        checkDemo(warnings, AuthService.BOOTSTRAP_STORE, "store123");
        return warnings;
    }

    private void checkDemo(List<String> warnings, String username, String plain) throws SQLException {
        AppUser user = userDao.findByUsername(username);
        if (user == null || !user.isActive()) {
            return;
        }
        char[] pwd = plain.toCharArray();
        try {
            if (PasswordHasher.matches(pwd, user.getPasswordSalt(), user.getPasswordHash())) {
                warnings.add(username + " still uses default password " + plain);
            }
        } finally {
            java.util.Arrays.fill(pwd, '\0');
        }
    }

    public String configHealth() {
        requireSuperAdmin();
        StringBuilder sb = new StringBuilder();
        try {
            String url = DatabaseConfig.url();
            String user = DatabaseConfig.user();
            if (url == null || url.trim().isEmpty() || user == null || user.trim().isEmpty()) {
                sb.append("db.properties incomplete. ");
            } else {
                sb.append("db.properties loaded. ");
            }
        } catch (Exception e) {
            sb.append("db.properties error: ").append(e.getMessage()).append(" ");
        }
        try {
            String pinHash = settingDao.findValue(SettingKeys.RECOVERY_PIN_HASH);
            if (pinHash == null || pinHash.trim().isEmpty()) {
                sb.append("Recovery PIN not set.");
            } else {
                sb.append("Recovery PIN configured.");
            }
        } catch (SQLException e) {
            sb.append("Cannot read settings.");
        }
        return sb.toString().trim();
    }
}
