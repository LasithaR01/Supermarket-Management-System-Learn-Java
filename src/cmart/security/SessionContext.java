package cmart.security;

import cmart.model.AppUser;

/**
 * In-memory session for the signed-in desktop user (one POS workstation = one session).
 */
public final class SessionContext {

    private static volatile AppUser currentUser;

    private SessionContext() {
    }

    public static void signIn(AppUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User required");
        }
        currentUser = user;
    }

    public static void signOut() {
        currentUser = null;
    }

    public static AppUser currentUser() {
        return currentUser;
    }

    public static boolean isSignedIn() {
        return currentUser != null;
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
