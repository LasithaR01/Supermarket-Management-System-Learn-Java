package cmart.service;

import cmart.config.DatabaseConfig;
import cmart.dao.BackupLogDao;
import cmart.model.AppUser;
import cmart.security.Permission;
import cmart.security.SessionContext;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Runs {@code mysqldump} when available on PATH. Requires MySQL client tools installed.
 */
public class BackupService {

    private final BackupLogDao backupLogDao = new BackupLogDao();
    private final AuditService auditService = new AuditService();
    private final ErrorLogService errorLogService = new ErrorLogService();

    public File backupToFolder(File folder) throws IOException, InterruptedException {
        SessionContext.require(Permission.BACKUP);
        String by = currentUsername();
        if (folder == null) {
            throw new IllegalArgumentException("Backup folder is required.");
        }
        if (!folder.exists() && !folder.mkdirs()) {
            IOException err = new IOException("Cannot create folder: " + folder.getAbsolutePath());
            logFail(by, err.getMessage());
            throw err;
        }

        String url = DatabaseConfig.url();
        String dbName = extractDatabaseName(url);
        String stamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File out = new File(folder, "cmart_backup_" + stamp + ".sql");

        ProcessBuilder pb = new ProcessBuilder(
                "mysqldump",
                "-u", DatabaseConfig.user(),
                "--password=" + DatabaseConfig.password(),
                "--databases", dbName,
                "--result-file=" + out.getAbsolutePath()
        );
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            int code = process.waitFor();
            if (code != 0 || !out.isFile() || out.length() == 0) {
                throw new IOException("mysqldump failed (exit " + code
                        + "). Install MySQL client tools and ensure mysqldump is on PATH.");
            }
            try {
                backupLogDao.insert("OK", out.getAbsolutePath(), "Backup completed", by);
            } catch (Exception ignored) {
            }
            auditService.record(AuditService.BACKUP, by, "OK " + out.getName());
            return out;
        } catch (IOException e) {
            logFail(by, e.getMessage());
            throw e;
        } catch (InterruptedException e) {
            logFail(by, e.getMessage());
            throw e;
        }
    }

    private void logFail(String by, String message) {
        try {
            backupLogDao.insert("FAIL", "", message == null ? "" : message, by);
        } catch (Exception ignored) {
        }
        errorLogService.error("BackupService", "Backup failed", new Exception(message));
        auditService.record(AuditService.BACKUP, by, "FAIL " + (message == null ? "" : message));
    }

    private static String currentUsername() {
        AppUser user = SessionContext.currentUser();
        return user == null || user.getUsername() == null ? "" : user.getUsername();
    }

    private static String extractDatabaseName(String jdbcUrl) {
        if (jdbcUrl == null) {
            return "cmartdb";
        }
        int slash = jdbcUrl.lastIndexOf('/');
        if (slash < 0 || slash + 1 >= jdbcUrl.length()) {
            return "cmartdb";
        }
        String tail = jdbcUrl.substring(slash + 1);
        int q = tail.indexOf('?');
        return q >= 0 ? tail.substring(0, q) : tail;
    }
}
