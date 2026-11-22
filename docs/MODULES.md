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
| Home shell | ui/shell/HomeFrame, PanelLoader, ui/theme/* | Done | Role-filtered nav + header ops |
| Login & roles | ui/auth/LoginFrame, security/*, AuthService | Done | PBKDF2; ADMIN / CASHIER / STORE_KEEPER |
| Settings | ui/settings/SettingsPanel, SettingService | Done | Shop name, tax, currency, stock policy |
| Customers | ui/customer/CustomerPanel + Dao/Service | Done | PreparedStatement DAO |
| Suppliers | ui/supplier/SupplierPanel + Dao/Service | Done | Same layered pattern |
| Employees | ui/employee/EmployeePanel + Dao/Service | Done | People CRUD; login is pp_user |
| Products | ui/product/ProductPanel + Dao/Service | Done | Validation on save/update |
| Users admin | ui/admin/UsersPanel + UserAdminService | Done | ADMIN only |
| Sales | ui/sale/SalePanel + SaleService / SaleDao | Done | Barcode, discount, tax, typeahead, hold |
| Invoices | ui/invoice/InvoicePanel + InvoiceService | Done | Filter + reprint |
| Returns | ui/refund/RefundPanel + ReturnService | Done | Refund + restock |
| Cashier shift | ui/shift/ShiftPanel + ShiftService | Done | Open / close float |
| Purchase | ui/purchase/PurchasePanel + PurchaseService | Done | PO + goods received |
| Dashboard | ui/dashboard/DashboardPanel + Backup | Done | Totals + mysqldump |
| Reports | ui/report/*, 
esources/reports/* | Partial | Jasper all-sales / by invoice |
| Database | config + SchemaMigrator, pos.sql | Done | Auto-migrate tables/columns |

---

## 1. Shell / Home

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Home welcome screen | Done | ui/shell/HomeFrame |
| Left nav menus | Done | Role-filtered toggles |
| Welcome cards | Done | Sale, Invoice, Product, People, Reports |
| Header ops (Returns, Shift, Purchase, Dashboard) | Done | Role-gated header buttons |
| Role-filtered menus | Done | Permission + SessionContext |
| Branding from Settings | Done | Shop name / tagline |
| Low-stock hint on Home | Done | Uses STOCK_LOW_THRESHOLD |
| Global keyboard shortcuts | Todo | Beyond Sales till shortcuts |

---

## 2. Auth & security

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Login screen | Done | ui/auth/LoginFrame |
| PBKDF2 password hash | Done | security/PasswordHasher |
| Roles: ADMIN / CASHIER / STORE_KEEPER | Done | security/Role |
| Permissions per action | Done | security/Permission |
| Sign out | Done | Clears session → Login |
| Users admin (create / password / role) | Done | ui/admin/UsersPanel |
| Fine-grained action ACL (discount / delete / refund beyond role) | Partial | Role-level only today |
| Audit log (who changed what) | Todo | —

---

## 3. Settings

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Shop name / tagline / receipt footer | Done | SettingKeys + SettingsPanel |
| Currency symbol | Done | |
| Tax enabled flag | Done | 	ax.enabled |
| Tax rate % | Done | 	ax.rate.percent |
| Allow negative stock | Done | |
| Low-stock threshold | Done | |
| Onboarding wizard (first-run) | Todo | |

---

## 4. Customers

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Add / update / delete | Done | CustomerPanel + Service/Dao |
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
| Add / update / delete | Done | SupplierPanel + Service/Dao |
| Search / filter | Done | |
| Jasper report | Partial | |
| Link to purchase orders | Done | Via Purchase module |
| Supplier payables report | Todo | |
| Cost vs sell margin view | Todo | |

---

## 6. Employees

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Add / update / delete | Done | EmployeePanel + Service/Dao |
| Search / filter | Done | |
| Linked to login users | Partial | People != pp_user (separate) |
| Attendance | Todo | |

---

## 7. Products

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| CRUD + search | Done | ProductPanel + Service/Dao |
| Barcode field | Done | |
| Stock qty | Done | |
| Stock deduct on sale | Done | Via SaleService |
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
| Cart add / remove | Done | SalePanel |
| Pay & print receipt | Done | |
| Safe invoice number (INID) | Done | Allocated on pay |
| Barcode scan add | Done | |
| Typeahead product search | Done | Find product field |
| Bill discount (Rs / %) | Done | |
| Line discount | Todo | |
| Live tax on bill | Done | From Settings |
| Per-product tax | Todo | |
| Pay methods (Cash / Card / Mobile / Mixed) | Done | PayMethod |
| Hold / recall bill | Done | F4 / held_sale |
| Shortcuts (Enter / F2 / Esc / F4) | Done | |
| Customer credit / pay later | Partial | Status/balance; full due flow weak |
| Confirm before clear / remove | Done | |

---

## 9. Invoices

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Search by invoice / customer / status | Done | InvoicePanel + InvoiceService |
| Reprint receipt | Done | |
| Payment history on invoice | Todo | |
| Link to returns | Done | Via Returns screen |

---

## 10. Returns / refunds

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Load invoice lines | Done | RefundPanel + ReturnService |
| Refund qty + reason | Done | |
| Restock product | Done | |
| Refund history view | Partial | Service exists; UI thin |

---

## 11. Cashier shift

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Open shift + float | Done | ShiftPanel + ShiftService |
| Close shift + counted cash | Done | |
| Expected vs difference | Done | |
| Sales since open (accurate by datetime) | Partial | Approx; sales date filter weak |

---

## 12. Purchase / inventory in

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Create purchase order | Done | PurchasePanel + PurchaseService |
| PO lines | Done | |
| Goods received → stock + | Done | |
| List open POs | Done | |
| Supplier price on PO | Partial | Uses product price today |
| Partial receive | Todo | |

---

## 13. Dashboard (owner)

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| Recent / today sales total | Done | DashboardPanel |
| Unpaid / dues count | Done | |
| Low stock summary | Done | |
| Top products | Done | |
| Charts / graphs | Todo | |

---

## 14. Reports

| Sub-module | Status | Package / notes |
|------------|--------|-----------------|
| All sales Jasper | Done | ui/report/* |
| By-invoice Jasper | Done | |
| Daily / weekly / monthly sales | Todo | |
| Profit report (sales - cost - discount) | Todo | |
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
| MySQL dump backup | Done | BackupService (mysqldump on PATH) |
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
| Layered packages (ui -> service -> dao) | Done | src/cmart/ |
| Schema migrator | Done | db/SchemaMigrator |
| Config outside source (db.properties) | Done | gitignored |
| PreparedStatement everywhere | Partial | Most DAOs done; keep hardening |
| Shared people-form pattern | Todo | Less Customer/Supplier/Employee copy-paste |
| Shared UI theme (UiTheme) | Done | |
| Team activity log | Done | docs/ACTIVITY.md + Action |
| Automated tests | Todo | |
| .gitignore for build / private | Done | |

---

## Suggested next build order (remaining)

1. Finish **Partial** items: credit/due, shift dating, refund history, people-user link
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
