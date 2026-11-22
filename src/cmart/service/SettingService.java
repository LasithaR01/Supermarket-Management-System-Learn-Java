package cmart.service;

import cmart.config.SettingKeys;
import cmart.dao.SettingDao;
import cmart.security.Permission;
import cmart.security.SessionContext;
import cmart.util.Validation;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shop options loaded from DB. Defaults are applied once — not hardcoded in UI labels forever.
 */
public class SettingService {

    private final SettingDao settingDao = new SettingDao();

    private static final Map<String, String> DEFAULTS;

    static {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put(SettingKeys.SHOP_NAME, "CMart");
        map.put(SettingKeys.SHOP_TAGLINE, "Supermarket desk");
        map.put(SettingKeys.RECEIPT_FOOTER, "Thank you for shopping with us");
        map.put(SettingKeys.TAX_ENABLED, "false");
        map.put(SettingKeys.TAX_RATE_PERCENT, "0");
        map.put(SettingKeys.CURRENCY_SYMBOL, "Rs");
        map.put(SettingKeys.ALLOW_NEGATIVE_STOCK, "false");
        map.put(SettingKeys.STOCK_LOW_THRESHOLD, "5");
        DEFAULTS = Collections.unmodifiableMap(map);
    }

    public void ensureDefaults() throws SQLException {
        for (Map.Entry<String, String> entry : DEFAULTS.entrySet()) {
            if (settingDao.findValue(entry.getKey()) == null) {
                settingDao.upsert(entry.getKey(), entry.getValue());
            }
        }
    }

    public Map<String, String> loadAll() throws SQLException {
        Map<String, String> stored = settingDao.findAll();
        Map<String, String> merged = new LinkedHashMap<String, String>(DEFAULTS);
        merged.putAll(stored);
        return merged;
    }

    public String get(String key) throws SQLException {
        String value = settingDao.findValue(key);
        if (value != null) {
            return value;
        }
        String fallback = DEFAULTS.get(key);
        return fallback == null ? "" : fallback;
    }

    public boolean getBoolean(String key) throws SQLException {
        return Boolean.parseBoolean(get(key).trim());
    }

    public double getDouble(String key, double fallback) throws SQLException {
        try {
            return Double.parseDouble(get(key).trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public void save(Map<String, String> values) throws SQLException {
        SessionContext.require(Permission.SETTINGS);
        if (values == null) {
            return;
        }
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            if (!DEFAULTS.containsKey(key)) {
                throw new IllegalArgumentException("Unknown setting key: " + key);
            }
            String value = entry.getValue() == null ? "" : entry.getValue().trim();
            validate(key, value);
            settingDao.upsert(key, value);
        }
    }

    private void validate(String key, String value) {
        if (SettingKeys.SHOP_NAME.equals(key) && Validation.isBlank(value)) {
            throw new IllegalArgumentException("Shop name cannot be empty");
        }
        if (SettingKeys.TAX_RATE_PERCENT.equals(key) || SettingKeys.STOCK_LOW_THRESHOLD.equals(key)) {
            try {
                double rate = Double.parseDouble(value);
                if (rate < 0) {
                    throw new IllegalArgumentException(key + " must be >= 0");
                }
                if (SettingKeys.TAX_RATE_PERCENT.equals(key) && rate > 100) {
                    throw new IllegalArgumentException("Tax rate must be between 0 and 100");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(key + " must be a number");
            }
        }
        if (SettingKeys.TAX_ENABLED.equals(key) || SettingKeys.ALLOW_NEGATIVE_STOCK.equals(key)) {
            if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
                throw new IllegalArgumentException(key + " must be true or false");
            }
        }
    }
}
