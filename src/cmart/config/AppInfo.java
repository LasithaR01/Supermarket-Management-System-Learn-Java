package cmart.config;

/**
 * App identity for Super Admin health cards.
 */
public final class AppInfo {

    public static final String APP_NAME = "CMart Smart POS";
    public static final String APP_VERSION = "1.5.0-ops";
    public static final String SCHEMA_LABEL = "pos + auth/security/audit/monitor/ops";
    /** Bumped when SchemaMigrator adds ops features (restore, ACL, maintenance). */
    public static final String SCHEMA_VERSION = "5";

    private AppInfo() {
    }
}
