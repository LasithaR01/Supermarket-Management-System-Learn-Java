package cmart.security;

/**
 * Fine-grained capabilities. Menus and actions check these — not raw role strings.
 */
public enum Permission {
    HOME,
    SALE,
    INVOICE,
    PRODUCT,
    CUSTOMER,
    SUPPLIER,
    EMPLOYEE,
    REPORT,
    SETTINGS,
    USER_ADMIN,
    REFUND,
    SHIFT,
    PURCHASE,
    DASHBOARD,
    BACKUP
}
