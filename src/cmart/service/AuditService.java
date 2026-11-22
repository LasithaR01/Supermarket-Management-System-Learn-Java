package cmart.service;

import cmart.dao.AuditDao;
import cmart.model.AppUser;
import cmart.security.Permission;
import cmart.security.SessionContext;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Security audit trail — login, lockout, password, and role changes.
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
    public static final String RECOVERY_PIN_SET = "RECOVERY_PIN_SET";

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
        SessionContext.require(Permission.DASHBOARD);
        AppUser user = SessionContext.currentUser();
        if (user == null) {
            return Collections.emptyList();
        }
        // Owner and Super Admin can review; others get empty
        if (user.getRole() != cmart.security.Role.SUPER_ADMIN
                && user.getRole() != cmart.security.Role.STORE_OWNER) {
            return Collections.emptyList();
        }
        return auditDao.findRecent(limit);
    }
}
