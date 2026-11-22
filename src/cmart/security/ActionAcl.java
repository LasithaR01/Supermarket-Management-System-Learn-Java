package cmart.security;

import cmart.config.SettingKeys;
import cmart.dao.SettingDao;
import cmart.model.AppUser;
import java.sql.SQLException;

/**
 * Fine-grained action gates on top of role permissions.
 * Store Owner and Super Admin always pass; staff follow {@code acl.allow.*} settings.
 */
public final class ActionAcl {

    private ActionAcl() {
    }

    public static void requireDiscountAllowed() {
        requireFlag(SettingKeys.ACL_ALLOW_DISCOUNT, "Discounts are disabled for staff. Ask Store Owner / Super Admin.");
    }

    public static void requireProductDeleteAllowed() {
        requireFlag(SettingKeys.ACL_ALLOW_PRODUCT_DELETE,
                "Product delete is disabled for staff. Ask Store Owner / Super Admin.");
    }

    public static void requireRefundAllowed() {
        requireFlag(SettingKeys.ACL_ALLOW_REFUND, "Refunds are disabled for staff. Ask Store Owner / Super Admin.");
    }

    public static boolean isAllowed(String settingKey) {
        AppUser user = SessionContext.currentUser();
        if (user == null || user.getRole() == null) {
            return false;
        }
        if (user.getRole() == Role.SUPER_ADMIN || user.getRole() == Role.STORE_OWNER) {
            return true;
        }
        return readFlag(settingKey, true);
    }

    private static void requireFlag(String settingKey, String message) {
        if (!isAllowed(settingKey)) {
            throw new SecurityException(message);
        }
    }

    private static boolean readFlag(String key, boolean defaultValue) {
        try {
            String value = new SettingDao().findValue(key);
            if (value == null || value.trim().isEmpty()) {
                return defaultValue;
            }
            return Boolean.parseBoolean(value.trim());
        } catch (SQLException e) {
            return defaultValue;
        }
    }
}
