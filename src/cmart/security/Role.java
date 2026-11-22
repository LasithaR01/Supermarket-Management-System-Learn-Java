package cmart.security;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

/**
 * Application roles with explicit permission sets (no scattered if-role hardcoding).
 */
public enum Role {
    ADMIN(EnumSet.allOf(Permission.class)),
    CASHIER(EnumSet.of(
            Permission.HOME,
            Permission.SALE,
            Permission.INVOICE,
            Permission.CUSTOMER,
            Permission.PRODUCT,
            Permission.REPORT,
            Permission.REFUND,
            Permission.SHIFT
    )),
    STORE_KEEPER(EnumSet.of(
            Permission.HOME,
            Permission.PRODUCT,
            Permission.SUPPLIER,
            Permission.REPORT,
            Permission.PURCHASE
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

    public static Role fromStorage(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }
        return Role.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}
