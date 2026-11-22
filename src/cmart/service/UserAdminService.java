package cmart.service;

import cmart.dao.UserDao;
import cmart.model.AppUser;
import cmart.security.PasswordHasher;
import cmart.security.Permission;
import cmart.security.Role;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin-only user management. Never returns password hashes to the UI.
 */
public class UserAdminService {

    private final UserDao userDao = new UserDao();

    public List<AppUser> listUsers() throws SQLException {
        SessionContext.require(Permission.USER_ADMIN);
        List<AppUser> raw = userDao.findAll();
        List<AppUser> safe = new ArrayList<AppUser>();
        for (AppUser user : raw) {
            safe.add(publicView(user));
        }
        return safe;
    }

    public void createUser(String username, String displayName, Role role, char[] password)
            throws SQLException {
        SessionContext.require(Permission.USER_ADMIN);
        if (Validation.isBlank(username)) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (Validation.isBlank(displayName)) {
            throw new IllegalArgumentException("Display name is required.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role is required.");
        }
        if (password == null || password.length < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        if (userDao.findByUsername(username.trim()) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }

        AppUser user = new AppUser();
        user.setUsername(username.trim());
        user.setDisplayName(displayName.trim());
        user.setRole(role);
        user.setActive(true);
        PasswordHasher.HashResult hash = PasswordHasher.hash(password);
        user.setPasswordHash(hash.getHashHex());
        user.setPasswordSalt(hash.getSaltHex());
        userDao.insert(user);
    }

    public void changePassword(int userId, char[] newPassword) throws SQLException {
        SessionContext.require(Permission.USER_ADMIN);
        if (newPassword == null || newPassword.length < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        AppUser existing = userDao.findById(userId);
        if (existing == null) {
            throw new IllegalArgumentException("User not found.");
        }
        PasswordHasher.HashResult hash = PasswordHasher.hash(newPassword);
        userDao.updatePassword(userId, hash.getHashHex(), hash.getSaltHex());
    }

    public void updateUser(int userId, String displayName, Role role, boolean active) throws SQLException {
        SessionContext.require(Permission.USER_ADMIN);
        if (Validation.isBlank(displayName)) {
            throw new IllegalArgumentException("Display name is required.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role is required.");
        }
        AppUser existing = userDao.findById(userId);
        if (existing == null) {
            throw new IllegalArgumentException("User not found.");
        }
        AppUser current = SessionContext.currentUser();
        if (current != null && current.getId() == userId && !active) {
            throw new IllegalArgumentException("You cannot deactivate your own account while signed in.");
        }
        if (current != null && current.getId() == userId && role != Role.ADMIN) {
            throw new IllegalArgumentException("You cannot remove your own ADMIN role while signed in.");
        }
        userDao.updateProfile(userId, displayName.trim(), role, active);
    }

    private static AppUser publicView(AppUser source) {
        AppUser view = new AppUser();
        view.setId(source.getId());
        view.setUsername(source.getUsername());
        view.setDisplayName(source.getDisplayName());
        view.setRole(source.getRole());
        view.setActive(source.isActive());
        view.setPasswordHash(null);
        view.setPasswordSalt(null);
        return view;
    }
}
