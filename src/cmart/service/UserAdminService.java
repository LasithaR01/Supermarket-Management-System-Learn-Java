package cmart.service;

import cmart.dao.UserDao;
import cmart.model.AppUser;
import cmart.security.PasswordHasher;
import cmart.security.PasswordPolicy;
import cmart.security.Permission;
import cmart.security.Role;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User management for Store Owner / Super Admin. Never returns password hashes to the UI.
 */
public class UserAdminService {

    private final UserDao userDao = new UserDao();
    private final AuditService auditService = new AuditService();

    public List<AppUser> listUsers() throws SQLException {
        SessionContext.require(Permission.USER_ADMIN);
        List<AppUser> raw = userDao.findAll();
        List<AppUser> safe = new ArrayList<AppUser>();
        AppUser current = SessionContext.currentUser();
        boolean isSuper = current != null && current.getRole() == Role.SUPER_ADMIN;
        for (AppUser user : raw) {
            if (!isSuper && user.getRole() == Role.SUPER_ADMIN) {
                continue;
            }
            safe.add(publicView(user));
        }
        return safe;
    }

    public Role[] assignableRoles() {
        SessionContext.require(Permission.USER_ADMIN);
        AppUser current = SessionContext.currentUser();
        if (current != null && current.getRole() == Role.SUPER_ADMIN) {
            return Role.allAssignableRoles();
        }
        return Role.staffAssignableRoles();
    }

    public void createUser(String username, String displayName, Role role, char[] password)
            throws SQLException {
        SessionContext.require(Permission.USER_ADMIN);
        validateAssignable(role);
        if (Validation.isBlank(username)) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (Validation.isBlank(displayName)) {
            throw new IllegalArgumentException("Display name is required.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role is required.");
        }
        PasswordPolicy.validate(username.trim(), password);
        if (userDao.findByUsername(username.trim()) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }

        AppUser user = new AppUser();
        user.setUsername(username.trim());
        user.setDisplayName(displayName.trim());
        user.setRole(role);
        user.setActive(true);
        user.setMustChangePassword(true);
        PasswordHasher.HashResult hash = PasswordHasher.hash(password);
        user.setPasswordHash(hash.getHashHex());
        user.setPasswordSalt(hash.getSaltHex());
        userDao.insert(user);
        auditService.record(AuditService.USER_CREATE, user.getUsername(), "Created role " + role.name());
    }

    public void changePassword(int userId, char[] newPassword) throws SQLException {
        SessionContext.require(Permission.USER_ADMIN);
        AppUser existing = requireManagedUser(userId);
        PasswordPolicy.validate(existing.getUsername(), newPassword);
        PasswordHasher.HashResult hash = PasswordHasher.hash(newPassword);
        userDao.updatePassword(userId, hash.getHashHex(), hash.getSaltHex(), true);
        auditService.record(AuditService.PASSWORD_RESET, existing.getUsername(), "Admin reset password");
    }

    public void updateUser(int userId, String displayName, Role role, boolean active) throws SQLException {
        SessionContext.require(Permission.USER_ADMIN);
        if (Validation.isBlank(displayName)) {
            throw new IllegalArgumentException("Display name is required.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role is required.");
        }
        validateAssignable(role);
        AppUser existing = requireManagedUser(userId);
        AppUser current = SessionContext.currentUser();
        if (current != null && current.getId() == userId && !active) {
            throw new IllegalArgumentException("You cannot deactivate your own account while signed in.");
        }
        if (current != null && current.getId() == userId
                && role != current.getRole()
                && (current.getRole() == Role.STORE_OWNER || current.getRole() == Role.SUPER_ADMIN)) {
            throw new IllegalArgumentException("You cannot remove your own elevated role while signed in.");
        }
        if (!active && existing.getRole() == Role.STORE_OWNER
                && userDao.countByRole(Role.STORE_OWNER) <= 1) {
            throw new IllegalArgumentException("Cannot deactivate the last active Store Owner.");
        }
        if (existing.getRole() == Role.STORE_OWNER && role != Role.STORE_OWNER
                && userDao.countByRole(Role.STORE_OWNER) <= 1) {
            throw new IllegalArgumentException("Cannot demote the last active Store Owner.");
        }
        String detail = "Profile update role=" + role.name() + " active=" + active;
        if (existing.getRole() != role) {
            detail = "Role " + existing.getRole().name() + " → " + role.name() + "; active=" + active;
        }
        userDao.updateProfile(userId, displayName.trim(), role, active);
        auditService.record(AuditService.USER_UPDATE, existing.getUsername(), detail);
    }

    private void validateAssignable(Role role) {
        Role[] allowed = assignableRoles();
        for (int i = 0; i < allowed.length; i++) {
            if (allowed[i] == role) {
                return;
            }
        }
        throw new IllegalArgumentException("You are not allowed to assign role: " + role.displayLabel());
    }

    private AppUser requireManagedUser(int userId) throws SQLException {
        AppUser existing = userDao.findById(userId);
        if (existing == null) {
            throw new IllegalArgumentException("User not found.");
        }
        AppUser current = SessionContext.currentUser();
        if (current != null && current.getRole() != Role.SUPER_ADMIN
                && existing.getRole() == Role.SUPER_ADMIN) {
            throw new SecurityException("Store Owner cannot manage Super Admin accounts.");
        }
        return existing;
    }

    private static AppUser publicView(AppUser source) {
        AppUser view = new AppUser();
        view.setId(source.getId());
        view.setUsername(source.getUsername());
        view.setDisplayName(source.getDisplayName());
        view.setRole(source.getRole());
        view.setActive(source.isActive());
        view.setMustChangePassword(source.isMustChangePassword());
        view.setPasswordHash(null);
        view.setPasswordSalt(null);
        return view;
    }
}
