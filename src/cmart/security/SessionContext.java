package cmart.security;

import cmart.model.AppUser;

/**
 * In-memory session for the signed-in desktop user (one POS workstation = one session).
 */
public final class SessionContext {

    /** Idle timeout before auto sign-out (15 minutes). */
    public static final long IDLE_TIMEOUT_MS = 15L * 60L * 1000L;

    private static volatile AppUser currentUser;
    private static volatile long lastActivityMs;

    private SessionContext() {
    }

    public static void signIn(AppUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User required");
        }
        currentUser = user;
        touch();
    }

    public static void signOut() {
        currentUser = null;
        lastActivityMs = 0L;
    }

    public static AppUser currentUser() {
        return currentUser;
    }

    public static boolean isSignedIn() {
        return currentUser != null;
    }

    public static void touch() {
        lastActivityMs = System.currentTimeMillis();
    }

    public static long lastActivityMs() {
        return lastActivityMs;
    }

    public static boolean isIdleTimedOut() {
        if (currentUser == null || lastActivityMs <= 0L) {
            return false;
        }
        return System.currentTimeMillis() - lastActivityMs >= IDLE_TIMEOUT_MS;
    }

    public static boolean can(Permission permission) {
        AppUser user = currentUser;
        return user != null && user.getRole() != null && user.getRole().can(permission);
    }

    public static void require(Permission permission) {
        if (!can(permission)) {
            throw new SecurityException("Missing permission: " + permission);
        }
    }
}
