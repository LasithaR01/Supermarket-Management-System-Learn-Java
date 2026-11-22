package cmart.config;

/**
 * Canonical keys for {@code app_setting}. UI and services use these constants — never raw literals.
 */
public final class SettingKeys {

    public static final String SHOP_NAME = "shop.name";
    public static final String SHOP_TAGLINE = "shop.tagline";
    public static final String RECEIPT_FOOTER = "receipt.footer";
    public static final String TAX_ENABLED = "tax.enabled";
    public static final String TAX_RATE_PERCENT = "tax.rate.percent";
    public static final String CURRENCY_SYMBOL = "currency.symbol";
    public static final String ALLOW_NEGATIVE_STOCK = "stock.allow.negative";
    public static final String STOCK_LOW_THRESHOLD = "stock.low.threshold";

    /** Hashed shop recovery PIN (forgot-password for owner). Managed by AuthService. */
    public static final String RECOVERY_PIN_HASH = "security.recovery.pin.hash";
    public static final String RECOVERY_PIN_SALT = "security.recovery.pin.salt";

    /**
     * One-time seed marker for role/demo login accounts (super/owner/cashier/store).
     * When bumped, AuthService re-applies those four accounts once.
     */
    public static final String BOOTSTRAP_USERS_VERSION = "security.bootstrap.users.version";

    /** When true, only SUPER_ADMIN may sign in (restore / migrate window). */
    public static final String MAINTENANCE_MODE = "system.maintenance.mode";

    /** Last SchemaMigrator run timestamp (ISO-ish string). */
    public static final String SCHEMA_LAST_MIGRATE = "schema.migrate.last";
    public static final String SCHEMA_VERSION = "schema.version";

    /** Fine-grained staff ACL (Owner/Super always allowed). Default true. */
    public static final String ACL_ALLOW_DISCOUNT = "acl.allow.discount";
    public static final String ACL_ALLOW_PRODUCT_DELETE = "acl.allow.product.delete";
    public static final String ACL_ALLOW_REFUND = "acl.allow.refund";

    private SettingKeys() {
    }
}
