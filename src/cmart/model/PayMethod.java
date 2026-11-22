package cmart.model;

import java.util.Locale;

/**
 * How the customer paid. Stored on {@code sales.pay_method}.
 */
public enum PayMethod {
    CASH,
    CARD,
    MOBILE,
    MIXED;

    public static PayMethod fromStorage(String value) {
        if (value == null || value.trim().isEmpty()) {
            return CASH;
        }
        return PayMethod.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}
