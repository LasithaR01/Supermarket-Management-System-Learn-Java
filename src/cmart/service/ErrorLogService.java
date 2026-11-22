package cmart.service;

import cmart.dao.ErrorLogDao;
import cmart.model.AppUser;
import cmart.security.Permission;
import cmart.security.Role;
import cmart.security.SessionContext;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Application fault log for Super Admin monitoring.
 */
public class ErrorLogService {

    private final ErrorLogDao errorLogDao = new ErrorLogDao();

    public void log(String level, String source, String message, String detail) {
        String user = "";
        AppUser current = SessionContext.currentUser();
        if (current != null && current.getUsername() != null) {
            user = current.getUsername();
        }
        try {
            errorLogDao.insert(level, source, message, detail, user);
        } catch (SQLException ignored) {
            // Never break main flow
        }
    }

    public void error(String source, String message, Throwable error) {
        String detail = error == null ? "" : String.valueOf(error.getMessage());
        log("ERROR", source, message, detail);
    }

    public int countLast24Hours() throws SQLException {
        requireMonitor();
        return errorLogDao.countSinceHours(24);
    }

    public List<String[]> recent(int limit) throws SQLException {
        requireMonitor();
        return errorLogDao.findRecent(limit);
    }

    private void requireMonitor() {
        SessionContext.require(Permission.SYSTEM);
        AppUser user = SessionContext.currentUser();
        if (user == null || user.getRole() != Role.SUPER_ADMIN) {
            throw new SecurityException("Super Admin only.");
        }
    }
}
