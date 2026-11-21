package cmart.util;

/**
 * Shared input checks for forms.
 */
public final class Validation {

    private Validation() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isPositiveNumber(String value) {
        if (isBlank(value)) {
            return false;
        }
        try {
            return Double.parseDouble(value.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
