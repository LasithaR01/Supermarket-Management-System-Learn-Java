package cmart.service;

import cmart.config.DatabaseConfig;
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

    public File backupToFolder(File folder) throws IOException, InterruptedException {
        SessionContext.require(Permission.BACKUP);
        if (folder == null) {
            throw new IllegalArgumentException("Backup folder is required.");
        }
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Cannot create folder: " + folder.getAbsolutePath());
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
        Process process = pb.start();
        int code = process.waitFor();
        if (code != 0 || !out.isFile() || out.length() == 0) {
            throw new IOException("mysqldump failed (exit " + code
                    + "). Install MySQL client tools and ensure mysqldump is on PATH.");
        }
        return out;
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
