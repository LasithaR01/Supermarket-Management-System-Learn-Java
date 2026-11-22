package cmart.util;

import java.io.File;

/**
 * Resolves Jasper report files from the project resources folder.
 */
public final class ReportPaths {

    private ReportPaths() {
    }

    public static String jasper(String fileName) {
        return new File("src/cmart/resources/reports", fileName).getPath();
    }
}
