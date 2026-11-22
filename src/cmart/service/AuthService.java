package cmart.service;

import cmart.dao.UserDao;
import cmart.model.AppUser;
import cmart.security.PasswordHasher;
import cmart.security.Role;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;

/**
 * Authentication and bootstrap admin. Passwords are never stored in plain text.
 */
public class AuthService {

    /** First-run only; change immediately after install. */
    public static final String BOOTSTRAP_USERNAME = "admin";
    public static final String BOOTSTRAP_PASSWORD = "admin123";

    private final UserDao userDao = new UserDao();

    public void ensureBootstrapAdmin() throws SQLException {
        if (userDao.countUsers() > 0) {
            return;
        }
        // First-run demo accounts only — passwords are hashed; change after install.
        insertUser(BOOTSTRAP_USERNAME, "Administrator", Role.ADMIN, BOOTSTRAP_PASSWORD);
        insertUser("cashier", "Front Cashier", Role.CASHIER, "cashier123");
        insertUser("store", "Store Keeper", Role.STORE_KEEPER, "store123");
    }

    private void insertUser(String username, String displayName, Role role, String plainPassword)
            throws SQLException {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setDisplayName(displayName);
        user.setRole(role);
        user.setActive(true);
        PasswordHasher.HashResult hash = PasswordHasher.hash(plainPassword.toCharArray());
        user.setPasswordSalt(hash.getSaltHex());
        user.setPasswordHash(hash.getHashHex());
        userDao.insert(user);
    }

    /**
     * @return signed-in user, or null if credentials are invalid
     */
    public AppUser login(String username, char[] password) throws SQLException {
        if (Validation.isBlank(username) || password == null || password.length == 0) {
            return null;
        }
        AppUser user = userDao.findByUsername(username.trim());
        if (user == null || !user.isActive()) {
            return null;
        }
        boolean ok = PasswordHasher.matches(password, user.getPasswordSalt(), user.getPasswordHash());
        if (!ok) {
            return null;
        }
        // Do not keep hash material in the session object beyond verification
        user.setPasswordHash(null);
        user.setPasswordSalt(null);
        SessionContext.signIn(user);
        return user;
    }

    public void logout() {
        SessionContext.signOut();
    }
}
