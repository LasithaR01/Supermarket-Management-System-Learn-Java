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

    private SettingKeys() {
    }
}
