# CMart Modules — full build checklist

Status key: **Done** · **Partial** · **Todo**

Use this as the master tick list while building. Phase order and outcomes live in [SMART_POS_ROADMAP.md](SMART_POS_ROADMAP.md). Log finishes in [CHANGELOG.md](CHANGELOG.md).

---

## Phase status (roadmap)

| Phase | Focus | Status |
|-------|--------|--------|
| Phase 1 | Harden existing modules (SQL, stock, validation, shortcuts) | Done (core) |
| Phase 2 | Smart Sales (barcode, discount, pay methods, hold, refund, tax) | Done (core) |
| Phase 3 | Inventory & purchasing | Partial (PO/GRN done) |
| Phase 4 | Owner dashboard, login, profit, backup | Partial (login + dashboard + backup done) |
| Phase 5 | Speed & UX polish | Todo |
| Phase 6 | Smart extras (mobile, multi-store, loyalty) | Todo |

---

## Current desktop modules (quick map)

| Module | Package / files | Status | Notes |
|--------|-----------------|--------|-------|
| Home shell | `ui/shell/HomeFrame`, `PanelLoader`, `ui/theme/*` | Done | Role-filtered nav + header ops + Settings / Sign out |
| Login & roles | `ui/auth/LoginFrame`, `security/*`, `AuthService` | Done | PBKDF2; SUPER_ADMIN / STORE_OWNER / CASHIER / STORE_KEEPER |
| Settings | `ui/settings/SettingsPanel`, `SettingService` | Done | Shop options + recovery PIN |
| Customers | `ui/customer/CustomerPanel` + Dao/Service | Done | Panel uses service only (PreparedStatement in DAO) |
| Suppliers | `ui/supplier/SupplierPanel` + Dao/Service | Done | Same layered pattern as customers |
| Employees | `ui/employee/EmployeePanel` + Dao/Service | Done | People CRUD; login users are separate (`app_user`) |
| Products | `ui/product/ProductPanel` + Dao/Service | Done | Panel uses service; validation on save/update |
| Users admin | `ui/admin/UsersPanel` + `UserAdminService` | Done | Owner creates staff + reset password (no support call) |
| Sales | `ui/sale/SalePanel` + `SaleService` / `SaleDao` | Done | Barcode, discount, live tax, typeahead, pay method, hold/recall, stock |
| Invoices | `ui/invoice/InvoicePanel` + `InvoiceService` | Done | Filter + reprint receipt (double-click / button) |
| Returns | `ui/refund/RefundPanel` + `ReturnService` | Done | Load invoice lines → refund qty → restock |
| Cashier shift | `ui/shift/ShiftPanel` + `ShiftService` | Done | Open float / close with expected vs counted cash |
| Purchase | `ui/purchase/PurchasePanel` + `PurchaseService` | Done | Create PO + receive goods (stock +) |
| Dashboard | `ui/dashboard/DashboardPanel` + Backup | Done | Sales / dues / low stock / top products + mysqldump backup |
| Reports | `ui/report/*`, `resources/reports/*` | Partial | Jasper all-sales / by invoice (still works) |
| Database | `config` + `SchemaMigrator`, `sql/pos.sql` | Done | Auto-migrate users, settings, held_sale, returns, shifts, PO, tax |

---

## 1. Shell / Home

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Home welcome screen | Done | `ui/shell/HomeFrame` |
| Left nav menus | Done | Role-filtered toggles |
| Welcome cards | Done | Sale, Invoice, Product, People, Reports |
| Header ops (Returns, Shift, Purchase, Dashboard) | Done | Role-gated header buttons |
| Role-filtered menus | Done | `Permission` + `SessionContext` |
| Branding from Settings | Done | Shop name / tagline |
| Low-stock hint on Home | Done | Uses `STOCK_LOW_THRESHOLD` |
| Global keyboard shortcuts | Todo | Beyond Sales till shortcuts |

---

## 2. Auth & security

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Login screen | Done | Split brand + form `LoginFrame` (UiTheme) |
| PBKDF2 password hash | Done | `security/PasswordHasher` |
| Roles: SUPER_ADMIN / STORE_OWNER / CASHIER / STORE_KEEPER | Done | `security/Role` (+ legacy ADMIN→STORE_OWNER) |
| Permissions per action | Done | `security/Permission` (+ `SYSTEM`) |
| Change own password | Done | Home → Password; forced when `must_change_password` |
| Forgot password | Done | Recovery PIN (owner/super); staff → Owner Users reset |
| Password policy | Done | Min 8 + letter + number (`PasswordPolicy`) |
| Login lockout | Done | 5 fails → 15 min lock (`failed_login_count` / `locked_until`) |
| Idle auto sign-out | Done | 15 min inactivity on Home |
| Sign out | Done | Clears session → Login |
| Users admin (create / password / role) | Done | Owner self-service; cannot manage Super Admin |
| Role dashboards | Done | Super / Owner / Cashier / Store Keeper views |
| Security audit log | Done | `security_audit` + filterable Super Admin Monitor |
| Super Admin system monitor | Done | Health / audit / errors / unlock / backup (`SuperAdminMonitorPanel`) |
| App error / fault log | Done | `app_error_log` |
| Backup history | Done | `backup_log` + Monitor tab |
| Fine-grained action ACL (discount / delete / refund beyond role) | Partial | Role-level only today |

---

## 3. Settings

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Shop name / tagline / receipt footer | Done | `SettingKeys` + `SettingsPanel` |
| Currency symbol | Done | |
| Tax enabled flag | Done | `tax.enabled` |
| Tax rate % | Done | `tax.rate.percent` |
| Allow negative stock | Done | |
| Low-stock threshold | Done | |
| Onboarding wizard (first-run) | Todo | |

---

## 4. Customers

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Add / update / delete | Done | `CustomerPanel` + Service/Dao |
| Search / filter | Done | |
| Jasper report | Partial | Still works; needs refresh |
| Validation (phone, empty name) | Partial | |
| Duplicate check | Todo | |
| Credit balance / outstanding dues | Todo | |
| Customer outstanding report | Todo | |

---

## 5. Suppliers

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Add / update / delete | Done | `SupplierPanel` + Service/Dao |
| Search / filter | Done | |
| Jasper report | Partial | |
| Link to purchase orders | Done | Via Purchase module |
| Supplier payables report | Todo | |
| Cost vs sell margin view | Todo | |

---

## 6. Employees

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Add / update / delete | Done | `EmployeePanel` + Service/Dao |
| Search / filter | Done | |
| Linked to login users | Partial | People ≠ `app_user` (separate) |
| Attendance | Todo | |

---

## 7. Products

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| CRUD + search | Done | `ProductPanel` + Service/Dao |
| Barcode field | Done | |
| Stock qty | Done | |
| Stock deduct on sale | Done | Via `SaleService` |
| Block sale if stock low | Done | Settings-aware |
| Low-stock list / alerts | Done | Home + Dashboard |
| Categories / brands | Todo | |
| Expiry date | Todo | |
| Multi-unit (piece / pack / box) | Todo | |
| Stock adjustment (damage / expire / recount) | Todo | |

---

## 8. Sales (Till)

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Cart add / remove | Done | `SalePanel` |
| Pay & print receipt | Done | |
| Safe invoice number (INID) | Done | Allocated on pay |
| Barcode scan add | Done | |
| Typeahead product search | Done | Find product field |
| Bill discount (Rs / %) | Done | |
| Line discount | Todo | |
| Live tax on bill | Done | From Settings |
| Per-product tax | Todo | |
| Pay methods (Cash / Card / Mobile / Mixed) | Done | `PayMethod` |
| Hold / recall bill | Done | F4 / held_sale |
| Shortcuts (Enter / F2 / Esc / F4) | Done | |
| Customer credit / pay later | Partial | Status/balance; full due flow weak |
| Confirm before clear / remove | Done | |

---

## 9. Invoices

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Search by invoice / customer / status | Done | `InvoicePanel` + `InvoiceService` |
| Reprint receipt | Done | |
| Payment history on invoice | Todo | |
| Link to returns | Done | Via Returns screen |

---

## 10. Returns / refunds

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Load invoice lines | Done | `RefundPanel` + `ReturnService` |
| Refund qty + reason | Done | |
| Restock product | Done | |
| Refund history view | Partial | Service exists; UI thin |

---

## 11. Cashier shift

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Open shift + float | Done | `ShiftPanel` + `ShiftService` |
| Close shift + counted cash | Done | |
| Expected vs difference | Done | |
| Sales since open (accurate by datetime) | Partial | Approx; sales date filter weak |

---

## 12. Purchase / inventory in

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Create purchase order | Done | `PurchasePanel` + `PurchaseService` |
| PO lines | Done | |
| Goods received → stock + | Done | |
| List open POs | Done | |
| Supplier price on PO | Partial | Uses product price today |
| Partial receive | Todo | |

---

## 13. Dashboard (owner)

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Recent / today sales total | Done | `DashboardPanel` |
| Unpaid / dues count | Done | |
| Low stock summary | Done | |
| Top products | Done | |
| Charts / graphs | Todo | |

---

## 14. Reports

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| All sales Jasper | Done | `ui/report/*` |
| By-invoice Jasper | Done | |
| Daily / weekly / monthly sales | Todo | |
| Profit report (sales − cost − discount) | Todo | |
| Export Excel / CSV | Todo | |

---

## 15. Expenses

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Rent / utilities / wages module | Todo | |

---

## 16. Backup

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| MySQL dump backup | Done | `BackupService` (mysqldump on PATH) |
| Restore from backup | Todo | |

---

## 17. UX polish (Phase 5)

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Product local cache (faster till) | Todo | |
| Touch / big buttons | Todo | |
| Dark / light theme | Todo | |
| Sinhala / English | Todo | |
| Barcode beep | Todo | |
| Customer-facing second display | Todo | |
| 80mm thermal printer | Todo | |
| Empty-state helpers | Todo | |

---

## 18. Smart extras (Phase 6)

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Owner mobile app | Todo | |
| WhatsApp / SMS bill | Todo | |
| Multi-store | Todo | |
| Cloud sync | Todo | |
| Loyalty points | Todo | |
| Bought-together suggestions | Todo | |
| Demand forecast | Todo | |
| Web admin | Todo | |

---

## 19. Platform / tech

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Layered packages (`ui → service → dao`) | Done | `src/cmart/` |
| Schema migrator | Done | `db/SchemaMigrator` |
| Config outside source (`db.properties`) | Done | gitignored |
| PreparedStatement everywhere | Partial | Most DAOs done; keep hardening |
| Shared people-form pattern | Todo | Less Customer/Supplier/Employee copy-paste |
| Shared UI theme (`UiTheme`) | Done | |
| Team activity log | Done | `docs/ACTIVITY.md` + Action |
| Automated tests | Todo | |
| `.gitignore` for build / private | Done | |

---

## Suggested next build order (remaining)

1. Finish **Partial** items: credit/due, shift dating, refund history, people↔user link  
2. **Stock adjustment** + categories / brands  
3. **Reports** (daily / profit) + **Expenses**  
4. **Audit log** + backup **restore**  
5. Phase 5 polish  
6. Phase 6 extras last  

---

## Update checklist (when you finish something)

1. Change **Done / Partial / Todo** in the tables above.  
2. Add a short line under [CHANGELOG.md](CHANGELOG.md).  
3. If it was a roadmap item, mark it in [SMART_POS_ROADMAP.md](SMART_POS_ROADMAP.md).
