package cmart.util;

import java.io.File;

/**
 * Resolves Jasper report files from the project resources folder.
 */
public final class ReportPaths {

    private ReportPaths() {
    }

    public static String jasper(String fileName) {
        File primary = new File("src/cmart/resources/reports", fileName);
        if (primary.isFile()) {
            return primary.getPath();
        }
        File legacy = new File("src/reports", fileName);
        if (legacy.isFile()) {
            return legacy.getPath();
        }
        return primary.getPath();
    }
}
