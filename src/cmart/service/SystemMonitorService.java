package cmart.service;

import cmart.config.AppInfo;
import cmart.config.DatabaseConfig;
import cmart.config.SettingKeys;
import cmart.dao.BackupLogDao;
import cmart.dao.SettingDao;
import cmart.dao.UserDao;
import cmart.db.ConnectionFactory;
import cmart.db.SchemaMigrator;
import cmart.model.AppUser;
import cmart.security.PasswordHasher;
import cmart.security.Permission;
import cmart.security.Role;
import cmart.security.SessionContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Super Admin system health, unlock, emergency user ops, ACL, maintenance, schema status.
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

    public List<AppUser> allUsersPublic() throws SQLException {
        requireSuperAdmin();
        List<AppUser> raw = userDao.findAll();
        List<AppUser> safe = new ArrayList<AppUser>();
        for (AppUser source : raw) {
            AppUser view = new AppUser();
            view.setId(source.getId());
            view.setUsername(source.getUsername());
            view.setDisplayName(source.getDisplayName());
            view.setRole(source.getRole());
            view.setActive(source.isActive());
            view.setMustChangePassword(source.isMustChangePassword());
            view.setLockedUntil(source.getLockedUntil());
            safe.add(view);
        }
        return safe;
    }

    public void deactivateUser(int userId) throws SQLException {
        requireSuperAdmin();
        AppUser existing = userDao.findById(userId);
        if (existing == null) {
            throw new IllegalArgumentException("User not found.");
        }
        AppUser current = SessionContext.currentUser();
        if (current != null && current.getId() == userId) {
            throw new IllegalArgumentException("You cannot deactivate your own account while signed in.");
        }
        if (existing.getRole() == Role.SUPER_ADMIN && userDao.countByRole(Role.SUPER_ADMIN) <= 1) {
            throw new IllegalArgumentException("Cannot deactivate the last Super Admin.");
        }
        if (existing.getRole() == Role.STORE_OWNER && userDao.countByRole(Role.STORE_OWNER) <= 1
                && existing.isActive()) {
            throw new IllegalArgumentException("Cannot deactivate the last active Store Owner.");
        }
        userDao.updateProfile(userId, existing.getDisplayName(), existing.getRole(), false);
        auditService.record(AuditService.USER_UPDATE, existing.getUsername(),
                "Deactivated by Super Admin (emergency)");
    }

    public void activateUser(int userId) throws SQLException {
        requireSuperAdmin();
        AppUser existing = userDao.findById(userId);
        if (existing == null) {
            throw new IllegalArgumentException("User not found.");
        }
        userDao.updateProfile(userId, existing.getDisplayName(), existing.getRole(), true);
        auditService.record(AuditService.USER_UPDATE, existing.getUsername(),
                "Reactivated by Super Admin");
    }

    public void forcePasswordChange(int userId) throws SQLException {
        requireSuperAdmin();
        AppUser existing = userDao.findById(userId);
        if (existing == null) {
            throw new IllegalArgumentException("User not found.");
        }
        userDao.setMustChangePassword(userId, true);
        auditService.record(AuditService.USER_FORCE_PASSWORD, existing.getUsername(),
                "must_change_password forced by Super Admin");
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

    public boolean isMaintenanceMode() throws SQLException {
        String value = settingDao.findValue(SettingKeys.MAINTENANCE_MODE);
        return value != null && Boolean.parseBoolean(value.trim());
    }

    public void setMaintenanceMode(boolean enabled) throws SQLException {
        requireSuperAdmin();
        settingDao.upsert(SettingKeys.MAINTENANCE_MODE, enabled ? "true" : "false");
        auditService.record(AuditService.MAINTENANCE, "",
                enabled ? "Maintenance mode ON" : "Maintenance mode OFF");
    }

    public Map<String, Boolean> aclFlags() throws SQLException {
        requireSuperAdmin();
        Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
        map.put(SettingKeys.ACL_ALLOW_DISCOUNT, readBool(SettingKeys.ACL_ALLOW_DISCOUNT, true));
        map.put(SettingKeys.ACL_ALLOW_PRODUCT_DELETE, readBool(SettingKeys.ACL_ALLOW_PRODUCT_DELETE, true));
        map.put(SettingKeys.ACL_ALLOW_REFUND, readBool(SettingKeys.ACL_ALLOW_REFUND, true));
        return map;
    }

    public void setAclFlag(String key, boolean allowed) throws SQLException {
        requireSuperAdmin();
        if (!SettingKeys.ACL_ALLOW_DISCOUNT.equals(key)
                && !SettingKeys.ACL_ALLOW_PRODUCT_DELETE.equals(key)
                && !SettingKeys.ACL_ALLOW_REFUND.equals(key)) {
            throw new IllegalArgumentException("Unknown ACL key.");
        }
        settingDao.upsert(key, allowed ? "true" : "false");
        auditService.record(AuditService.ACL_CHANGE, "", key + "=" + allowed);
    }

    public String schemaStatusText() throws SQLException {
        requireSuperAdmin();
        String last = settingDao.findValue(SettingKeys.SCHEMA_LAST_MIGRATE);
        String ver = settingDao.findValue(SettingKeys.SCHEMA_VERSION);
        StringBuilder sb = new StringBuilder();
        sb.append("Expected schema version: ").append(AppInfo.SCHEMA_VERSION).append('\n');
        sb.append("Stored schema version: ").append(ver == null ? "(none)" : ver).append('\n');
        sb.append("Last migrate: ").append(last == null ? "(never recorded)" : last).append('\n');
        if (ver != null && !AppInfo.SCHEMA_VERSION.equals(ver.trim())) {
            sb.append("Status: VERSION MISMATCH — restart app to re-run migrator.\n");
        } else {
            sb.append("Status: OK\n");
        }
        sb.append('\n').append("Tables:\n");
        List<String> tables = SchemaMigrator.tableStatus();
        for (int i = 0; i < tables.size(); i++) {
            sb.append("  ").append(tables.get(i)).append('\n');
        }
        return sb.toString();
    }

    private boolean readBool(String key, boolean fallback) throws SQLException {
        String value = settingDao.findValue(key);
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return Boolean.parseBoolean(value.trim());
    }
}
