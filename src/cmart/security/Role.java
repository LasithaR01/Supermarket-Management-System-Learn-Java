package cmart.security;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

/**
 * Application roles with explicit permission sets (no scattered if-role hardcoding).
 * <ul>
 *   <li>{@link #SUPER_ADMIN} — development / support team</li>
 *   <li>{@link #STORE_OWNER} — shop owner (manages staff, settings, money)</li>
 *   <li>{@link #CASHIER} — counter staff</li>
 *   <li>{@link #STORE_KEEPER} — stock / purchasing staff</li>
 * </ul>
 */
public enum Role {
    SUPER_ADMIN(EnumSet.allOf(Permission.class)),
    STORE_OWNER(EnumSet.complementOf(EnumSet.of(Permission.SYSTEM))),
    CASHIER(EnumSet.of(
            Permission.HOME,
            Permission.SALE,
            Permission.INVOICE,
            Permission.CUSTOMER,
            Permission.PRODUCT,
            Permission.REPORT,
            Permission.REFUND,
            Permission.SHIFT,
            Permission.DASHBOARD
    )),
    STORE_KEEPER(EnumSet.of(
            Permission.HOME,
            Permission.PRODUCT,
            Permission.SUPPLIER,
            Permission.REPORT,
            Permission.PURCHASE,
            Permission.DASHBOARD
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = Collections.unmodifiableSet(permissions);
    }

    public boolean can(Permission permission) {
        return permissions.contains(permission);
    }

    public Set<Permission> permissions() {
        return permissions;
    }

    public String displayLabel() {
        switch (this) {
            case SUPER_ADMIN:
                return "Super Admin";
            case STORE_OWNER:
                return "Store Owner";
            case CASHIER:
                return "Cashier";
            case STORE_KEEPER:
                return "Store Keeper";
            default:
                return name();
        }
    }

    /** Resolve a table / form label or storage code to a Role. */
    public static Role fromLabel(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }
        String trimmed = value.trim();
        Role[] all = Role.values();
        for (int i = 0; i < all.length; i++) {
            if (all[i].displayLabel().equalsIgnoreCase(trimmed)
                    || all[i].name().equalsIgnoreCase(trimmed)) {
                return all[i];
            }
        }
        // Legacy / longer labels
        String lower = trimmed.toLowerCase(Locale.ROOT);
        if (lower.contains("super")) {
            return SUPER_ADMIN;
        }
        if (lower.contains("owner") || "admin".equals(lower)) {
            return STORE_OWNER;
        }
        if (lower.contains("cashier")) {
            return CASHIER;
        }
        if (lower.contains("keeper") || lower.contains("store keep")) {
            return STORE_KEEPER;
        }
        return fromStorage(trimmed);
    }

    public String shortDescription() {
        switch (this) {
            case SUPER_ADMIN:
                return "Install, support, system recovery — not daily shop work";
            case STORE_OWNER:
                return "Users, settings, dashboard, sales & stock oversight";
            case CASHIER:
                return "Till, invoices, customers, refunds, shifts";
            case STORE_KEEPER:
                return "Products, suppliers, purchase orders / goods in";
            default:
                return "";
        }
    }

    /** Roles a Store Owner may assign when creating staff. */
    public static Role[] staffAssignableRoles() {
        return new Role[] {Role.STORE_OWNER, Role.CASHIER, Role.STORE_KEEPER};
    }

    /** Roles Super Admin may assign (includes Super Admin). */
    public static Role[] allAssignableRoles() {
        return Role.values();
    }

    public static Role fromStorage(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT);
        // Legacy single ADMIN role → shop owner
        if ("ADMIN".equals(normalized)) {
            return STORE_OWNER;
        }
        return Role.valueOf(normalized);
    }
}
