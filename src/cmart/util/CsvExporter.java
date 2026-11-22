package cmart.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Minimal CSV writer (UTF-8) for Super Admin exports.
 */
public final class CsvExporter {

    private CsvExporter() {
    }

    public static void write(File file, String[] headers, List<String[]> rows) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File is required.");
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), Charset.forName("UTF-8")));
            if (headers != null) {
                writeLine(writer, headers);
            }
            if (rows != null) {
                for (int i = 0; i < rows.size(); i++) {
                    writeLine(writer, rows.get(i));
                }
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private static void writeLine(BufferedWriter writer, String[] cells) throws IOException {
        if (cells == null) {
            writer.newLine();
            return;
        }
        for (int i = 0; i < cells.length; i++) {
            if (i > 0) {
                writer.write(',');
            }
            writer.write(escape(cells[i]));
        }
        writer.newLine();
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        boolean needQuotes = value.indexOf(',') >= 0 || value.indexOf('"') >= 0
                || value.indexOf('\n') >= 0 || value.indexOf('\r') >= 0;
        String escaped = value.replace("\"", "\"\"");
        return needQuotes ? "\"" + escaped + "\"" : escaped;
    }
}
