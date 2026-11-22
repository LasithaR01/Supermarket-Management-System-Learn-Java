package cmart.service;

import cmart.dao.AuditDao;
import cmart.model.AppUser;
import cmart.security.Permission;
import cmart.security.Role;
import cmart.security.SessionContext;
import cmart.util.CsvExporter;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Security audit trail — login, lockout, password, role, and critical actions.
 */
public class AuditService {

    public static final String LOGIN_OK = "LOGIN_OK";
    public static final String LOGIN_FAIL = "LOGIN_FAIL";
    public static final String LOGIN_LOCKED = "LOGIN_LOCKED";
    public static final String LOGOUT = "LOGOUT";
    public static final String PASSWORD_CHANGE = "PASSWORD_CHANGE";
    public static final String PASSWORD_RESET = "PASSWORD_RESET";
    public static final String USER_CREATE = "USER_CREATE";
    public static final String USER_UPDATE = "USER_UPDATE";
    public static final String USER_UNLOCK = "USER_UNLOCK";
    public static final String RECOVERY_PIN_SET = "RECOVERY_PIN_SET";
    public static final String PRODUCT_DELETE = "PRODUCT_DELETE";
    public static final String SALE_DISCOUNT = "SALE_DISCOUNT";
    public static final String REFUND = "REFUND";
    public static final String BACKUP = "BACKUP";
    public static final String RESTORE = "RESTORE";
    public static final String EXPORT = "EXPORT";
    public static final String ERROR_LOG_CLEAR = "ERROR_LOG_CLEAR";
    public static final String MAINTENANCE = "MAINTENANCE";
    public static final String ACL_CHANGE = "ACL_CHANGE";
    public static final String USER_FORCE_PASSWORD = "USER_FORCE_PASSWORD";

    private final AuditDao auditDao = new AuditDao();

    public void record(String eventType, String username, String detail) {
        String actor = "";
        AppUser current = SessionContext.currentUser();
        if (current != null && current.getUsername() != null) {
            actor = current.getUsername();
        }
        try {
            auditDao.insert(eventType, username, actor, detail);
        } catch (SQLException ignored) {
            // Never block the main flow if audit write fails
        }
    }

    public List<String[]> recentEvents(int limit) throws SQLException {
        requireAuditViewer();
        return auditDao.findRecent(limit);
    }

    public List<String[]> search(String eventType, String usernameContains, int limit) throws SQLException {
        requireAuditViewer();
        return auditDao.search(eventType, usernameContains, limit);
    }

    public File exportCsv(File file, String eventType, String usernameContains, int limit)
            throws SQLException, IOException {
        requireAuditViewer();
        AppUser user = SessionContext.currentUser();
        if (user == null || user.getRole() != Role.SUPER_ADMIN) {
            throw new SecurityException("Audit export is Super Admin only.");
        }
        List<String[]> rows = auditDao.search(eventType, usernameContains, limit);
        CsvExporter.write(file, new String[] {
            "when", "event", "user", "actor", "detail"
        }, rows);
        record(EXPORT, "", "Exported audit CSV " + file.getName() + " (" + rows.size() + " rows)");
        return file;
    }

    private void requireAuditViewer() {
        SessionContext.require(Permission.DASHBOARD);
        AppUser user = SessionContext.currentUser();
        if (user == null) {
            throw new SecurityException("Sign in required.");
        }
        if (user.getRole() != Role.SUPER_ADMIN && user.getRole() != Role.STORE_OWNER) {
            throw new SecurityException("Audit is for Store Owner / Super Admin only.");
        }
    }
}
