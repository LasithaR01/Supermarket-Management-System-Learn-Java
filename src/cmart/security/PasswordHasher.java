package cmart.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * PBKDF2 password hashing. Never store or compare plain-text passwords.
 */
public final class PasswordHasher {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int SALT_BYTES = 16;

    private PasswordHasher() {
    }

    public static final class HashResult {
        private final String saltHex;
        private final String hashHex;

        public HashResult(String saltHex, String hashHex) {
            this.saltHex = saltHex;
            this.hashHex = hashHex;
        }

        public String getSaltHex() {
            return saltHex;
        }

        public String getHashHex() {
            return hashHex;
        }
    }

    public static HashResult hash(char[] password) {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] hash = pbkdf2(password, salt);
        return new HashResult(toHex(salt), toHex(hash));
    }

    public static boolean matches(char[] password, String saltHex, String expectedHashHex) {
        if (password == null || saltHex == null || expectedHashHex == null) {
            return false;
        }
        byte[] salt = fromHex(saltHex);
        byte[] actual = pbkdf2(password, salt);
        return constantTimeEquals(toHex(actual), expectedHashHex);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH_BITS);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Password hashing unavailable", e);
        }
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= Character.toLowerCase(a.charAt(i)) ^ Character.toLowerCase(b.charAt(i));
        }
        return result == 0;
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            sb.append(String.format(Locale.ROOT, "%02x", value));
        }
        return sb.toString();
    }

    private static byte[] fromHex(String hex) {
        int len = hex.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Invalid hex length");
        }
        byte[] out = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            out[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }
        return out;
    }
}
