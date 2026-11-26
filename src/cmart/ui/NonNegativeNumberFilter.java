package cmart.ui;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

/**
 * Blocks a minus sign so a quantity field cannot become negative.
 * Other characters are left alone so invalid text can still be caught
 * with the usual "enter a valid quantity" alert on Add to Cart.
 */
public class NonNegativeNumberFilter extends DocumentFilter {

    public static void install(JTextComponent field) {
        if (field == null || !(field.getDocument() instanceof AbstractDocument)) {
            return;
        }
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new NonNegativeNumberFilter());
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (string == null) {
            return;
        }
        if (allowed(nextText(fb, offset, 0, string))) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        String incoming = text == null ? "" : text;
        if (allowed(nextText(fb, offset, length, incoming))) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private static String nextText(FilterBypass fb, int offset, int length, String incoming)
            throws BadLocationException {
        String current = fb.getDocument().getText(0, fb.getDocument().getLength());
        StringBuilder next = new StringBuilder(current);
        next.replace(offset, offset + length, incoming);
        return next.toString();
    }

    /** True when the text does not contain a minus sign. */
    public static boolean allowed(String value) {
        return value != null && value.indexOf('-') < 0;
    }

    /** True for a plain non-negative number such as {@code 0}, {@code 12}, or {@code 3.5}. */
    public static boolean isNonNegativeNumber(String value) {
        if (value == null) {
            return false;
        }
        String trimmed = value.trim();
        return !trimmed.isEmpty()
                && !".".equals(trimmed)
                && trimmed.matches("\\d+(\\.\\d*)?|\\.\\d+");
    }
}
