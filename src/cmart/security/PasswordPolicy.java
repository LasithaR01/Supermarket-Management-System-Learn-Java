package cmart.security;

/**
 * Strong password rules for create / change / reset flows.
 * Demo bootstrap accounts are seeded separately and already meet these rules.
 */
public final class PasswordPolicy {

    public static final int MIN_LENGTH = 8;

    private PasswordPolicy() {
    }

    public static void validate(String username, char[] password) {
        if (password == null || password.length < MIN_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_LENGTH + " characters.");
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (int i = 0; i < password.length; i++) {
            char c = password[i];
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        if (!hasLetter || !hasDigit) {
            throw new IllegalArgumentException("Password must include at least one letter and one number.");
        }
        if (username != null && username.trim().length() > 0) {
            String user = username.trim();
            String pass = new String(password);
            if (pass.equalsIgnoreCase(user)) {
                throw new IllegalArgumentException("Password cannot be the same as the username.");
            }
        }
    }

    public static void validateRecoveryPin(char[] pin) {
        if (pin == null || pin.length < 6) {
            throw new IllegalArgumentException("Recovery PIN must be at least 6 characters.");
        }
        boolean hasDigit = false;
        for (int i = 0; i < pin.length; i++) {
            if (Character.isDigit(pin[i])) {
                hasDigit = true;
                break;
            }
        }
        if (!hasDigit) {
            throw new IllegalArgumentException("Recovery PIN must include at least one number.");
        }
    }
}
