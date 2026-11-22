package cmart.service;

import cmart.dao.ErrorLogDao;
import cmart.model.AppUser;
import cmart.security.Permission;
import cmart.security.Role;
import cmart.security.SessionContext;
import cmart.util.CsvExporter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Application fault log for Super Admin monitoring.
 */
public class ErrorLogService {

    private final ErrorLogDao errorLogDao = new ErrorLogDao();
    private final AuditService auditService = new AuditService();

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
        String detail = "";
        if (error != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            error.printStackTrace(pw);
            pw.flush();
            detail = sw.toString();
        }
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

    public String[] detail(int errorId) throws SQLException {
        requireMonitor();
        return errorLogDao.findById(errorId);
    }

    public int clearAll() throws SQLException {
        requireMonitor();
        int n = errorLogDao.deleteAll();
        auditService.record(AuditService.ERROR_LOG_CLEAR, "", "Cleared all error log rows (" + n + ")");
        return n;
    }

    public int clearOlderThanDays(int days) throws SQLException {
        requireMonitor();
        int n = errorLogDao.deleteOlderThanDays(days);
        auditService.record(AuditService.ERROR_LOG_CLEAR, "", "Cleared errors older than " + days + "d (" + n + ")");
        return n;
    }

    public File exportCsv(File file, int limit) throws SQLException, IOException {
        requireMonitor();
        List<String[]> rows = errorLogDao.findRecent(limit);
        List<String[]> export = new ArrayList<String[]>();
        for (int i = 0; i < rows.size(); i++) {
            String[] r = rows.get(i);
            // id, when, level, source, message, user — fetch detail
            String[] full = errorLogDao.findById(Integer.parseInt(r[0]));
            if (full != null) {
                export.add(new String[] {
                    full[0], full[1], full[2], full[3], full[4], full[5], full[6]
                });
            }
        }
        CsvExporter.write(file, new String[] {
            "id", "when", "level", "source", "message", "detail", "user"
        }, export);
        auditService.record(AuditService.EXPORT, "", "Exported error log CSV " + file.getName());
        return file;
    }

    private void requireMonitor() {
        SessionContext.require(Permission.SYSTEM);
        AppUser user = SessionContext.currentUser();
        if (user == null || user.getRole() != Role.SUPER_ADMIN) {
            throw new SecurityException("Super Admin only.");
        }
    }
}
