package cmart.service;

import cmart.config.SettingKeys;
import cmart.dao.SettingDao;
import cmart.dao.UserDao;
import cmart.model.AppUser;
import cmart.security.PasswordHasher;
import cmart.security.PasswordPolicy;
import cmart.security.Permission;
import cmart.security.Role;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Authentication, bootstrap accounts, lockout, self-service password change, and recovery PIN reset.
 */
public class AuthService {

    public static final String BOOTSTRAP_SUPER = "super";
    public static final String BOOTSTRAP_OWNER = "owner";
    public static final String BOOTSTRAP_CASHIER = "cashier";
    public static final String BOOTSTRAP_STORE = "store";

    private static final String BOOTSTRAP_VERSION = "4";
    private static final int MAX_FAILED_LOGINS = 5;
    private static final long LOCK_MINUTES = 15L;

    private final UserDao userDao = new UserDao();
    private final SettingDao settingDao = new SettingDao();
    private final AuditService auditService = new AuditService();

    /**
     * Ensures the four role demo accounts exist and can sign in:
     * super/super123, owner/owner123, cashier/cashier123, store/store123.
     */
    public void ensureBootstrapAdmin() throws SQLException {
        String installed = settingDao.findValue(SettingKeys.BOOTSTRAP_USERS_VERSION);
        boolean reseeding = !BOOTSTRAP_VERSION.equals(installed);

        ensureAccount(BOOTSTRAP_SUPER, "CMart Support", Role.SUPER_ADMIN, "super123", reseeding);
        ensureAccount(BOOTSTRAP_OWNER, "Store Owner", Role.STORE_OWNER, "owner123", reseeding);
        ensureAccount(BOOTSTRAP_CASHIER, "Front Cashier", Role.CASHIER, "cashier123", reseeding);
        ensureAccount(BOOTSTRAP_STORE, "Store Keeper", Role.STORE_KEEPER, "store123", reseeding);

        AppUser legacyAdmin = userDao.findByUsername("admin");
        if (legacyAdmin != null && legacyAdmin.getRole() != Role.STORE_OWNER
                && legacyAdmin.getRole() != Role.SUPER_ADMIN) {
            userDao.updateProfile(legacyAdmin.getId(), legacyAdmin.getDisplayName(), Role.STORE_OWNER, true);
        }

        if (reseeding) {
            settingDao.upsert(SettingKeys.BOOTSTRAP_USERS_VERSION, BOOTSTRAP_VERSION);
        }
    }

    private void ensureAccount(String username, String displayName, Role role, String plainPassword,
            boolean resetPasswordIfExists) throws SQLException {
        AppUser existing = userDao.findByUsername(username);
        if (existing == null) {
            insertUser(username, displayName, role, plainPassword, false);
            return;
        }
        if (existing.getRole() != role || !existing.isActive()) {
            userDao.updateProfile(existing.getId(), displayName, role, true);
        }
        if (resetPasswordIfExists) {
            PasswordHasher.HashResult hash = PasswordHasher.hash(plainPassword.toCharArray());
            userDao.updatePassword(existing.getId(), hash.getHashHex(), hash.getSaltHex(), false);
        }
    }

    private void insertUser(String username, String displayName, Role role, String plainPassword,
            boolean mustChange) throws SQLException {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setDisplayName(displayName);
        user.setRole(role);
        user.setActive(true);
        user.setMustChangePassword(mustChange);
        PasswordHasher.HashResult hash = PasswordHasher.hash(plainPassword.toCharArray());
        user.setPasswordSalt(hash.getSaltHex());
        user.setPasswordHash(hash.getHashHex());
        userDao.insert(user);
    }

    /**
     * @return signed-in user, or null if credentials are invalid
     * @throws IllegalStateException when the account is temporarily locked
     */
    public AppUser login(String username, char[] password) throws SQLException {
        if (Validation.isBlank(username) || password == null || password.length == 0) {
            return null;
        }
        String userKey = username.trim();
        AppUser user = userDao.findByUsername(userKey);
        if (user == null || !user.isActive()) {
            auditService.record(AuditService.LOGIN_FAIL, userKey, "Unknown user or inactive");
            return null;
        }
        if (user.isLockedNow()) {
            long mins = Math.max(1L, (user.getLockedUntil().getTime() - System.currentTimeMillis() + 59999L) / 60000L);
            auditService.record(AuditService.LOGIN_LOCKED, userKey, "Attempt while locked");
            throw new IllegalStateException("Account locked after failed attempts. Try again in about "
                    + mins + " minute(s).");
        }
        boolean ok = PasswordHasher.matches(password, user.getPasswordSalt(), user.getPasswordHash());
        if (!ok) {
            registerFailedLogin(user);
            return null;
        }
        userDao.clearLoginFailures(user.getId());
        user.setPasswordHash(null);
        user.setPasswordSalt(null);
        user.setFailedLoginCount(0);
        user.setLockedUntil(null);
        SessionContext.signIn(user);
        auditService.record(AuditService.LOGIN_OK, userKey, "Signed in as " + user.getRole().name());
        return user;
    }

    private void registerFailedLogin(AppUser user) throws SQLException {
        int fails = user.getFailedLoginCount() + 1;
        Timestamp lockedUntil = null;
        String detail = "Failed attempt " + fails + "/" + MAX_FAILED_LOGINS;
        if (fails >= MAX_FAILED_LOGINS) {
            lockedUntil = new Timestamp(System.currentTimeMillis() + LOCK_MINUTES * 60L * 1000L);
            fails = 0;
            detail = "Locked for " + LOCK_MINUTES + " minutes after " + MAX_FAILED_LOGINS + " failures";
            auditService.record(AuditService.LOGIN_LOCKED, user.getUsername(), detail);
        } else {
            auditService.record(AuditService.LOGIN_FAIL, user.getUsername(), detail);
        }
        userDao.recordLoginFailure(user.getId(), fails, lockedUntil);
    }

    public void logout() {
        AppUser user = SessionContext.currentUser();
        if (user != null) {
            auditService.record(AuditService.LOGOUT, user.getUsername(), "Signed out");
        }
        SessionContext.signOut();
    }

    public void changeOwnPassword(char[] currentPassword, char[] newPassword) throws SQLException {
        AppUser sessionUser = SessionContext.currentUser();
        if (sessionUser == null) {
            throw new SecurityException("Sign in required.");
        }
        PasswordPolicy.validate(sessionUser.getUsername(), newPassword);
        if (currentPassword == null || currentPassword.length == 0) {
            throw new IllegalArgumentException("Current password is required.");
        }
        AppUser stored = userDao.findById(sessionUser.getId());
        if (stored == null) {
            throw new IllegalArgumentException("User not found.");
        }
        if (!PasswordHasher.matches(currentPassword, stored.getPasswordSalt(), stored.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        if (PasswordHasher.matches(newPassword, stored.getPasswordSalt(), stored.getPasswordHash())) {
            throw new IllegalArgumentException("New password must be different from the current password.");
        }
        PasswordHasher.HashResult hash = PasswordHasher.hash(newPassword);
        userDao.updatePassword(sessionUser.getId(), hash.getHashHex(), hash.getSaltHex(), false);
        sessionUser.setMustChangePassword(false);
        auditService.record(AuditService.PASSWORD_CHANGE, sessionUser.getUsername(), "Self-service password change");
    }

    public void completeForcedPasswordChange(char[] currentPassword, char[] newPassword) throws SQLException {
        changeOwnPassword(currentPassword, newPassword);
    }

    public boolean isRecoveryPinConfigured() throws SQLException {
        String hash = settingDao.findValue(SettingKeys.RECOVERY_PIN_HASH);
        String salt = settingDao.findValue(SettingKeys.RECOVERY_PIN_SALT);
        return !Validation.isBlank(hash) && !Validation.isBlank(salt);
    }

    public void setRecoveryPin(char[] pin) throws SQLException {
        SessionContext.require(Permission.SETTINGS);
        PasswordPolicy.validateRecoveryPin(pin);
        PasswordHasher.HashResult hash = PasswordHasher.hash(pin);
        settingDao.upsert(SettingKeys.RECOVERY_PIN_HASH, hash.getHashHex());
        settingDao.upsert(SettingKeys.RECOVERY_PIN_SALT, hash.getSaltHex());
        AppUser actor = SessionContext.currentUser();
        auditService.record(AuditService.RECOVERY_PIN_SET,
                actor == null ? "" : actor.getUsername(),
                "Recovery PIN updated");
    }

    public void resetPasswordWithRecoveryPin(String username, char[] recoveryPin, char[] newPassword)
            throws SQLException {
        if (Validation.isBlank(username)) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (recoveryPin == null || recoveryPin.length == 0) {
            throw new IllegalArgumentException("Recovery PIN is required.");
        }
        PasswordPolicy.validate(username.trim(), newPassword);
        if (!isRecoveryPinConfigured()) {
            throw new IllegalArgumentException(
                    "Recovery PIN is not set. Ask Super Admin / Owner to set it in Settings, or reset via Users.");
        }
        String salt = settingDao.findValue(SettingKeys.RECOVERY_PIN_SALT);
        String hash = settingDao.findValue(SettingKeys.RECOVERY_PIN_HASH);
        if (!PasswordHasher.matches(recoveryPin, salt, hash)) {
            throw new IllegalArgumentException("Recovery PIN is incorrect.");
        }
        AppUser user = userDao.findByUsername(username.trim());
        if (user == null || !user.isActive()) {
            throw new IllegalArgumentException("User not found or inactive.");
        }
        if (user.getRole() != Role.STORE_OWNER && user.getRole() != Role.SUPER_ADMIN) {
            throw new IllegalArgumentException(
                    "Recovery PIN reset is only for Store Owner / Super Admin. Staff: ask Owner to reset in Users.");
        }
        PasswordHasher.HashResult newHash = PasswordHasher.hash(newPassword);
        userDao.updatePassword(user.getId(), newHash.getHashHex(), newHash.getSaltHex(), false);
        auditService.record(AuditService.PASSWORD_RESET, user.getUsername(), "Reset via recovery PIN");
    }
}
