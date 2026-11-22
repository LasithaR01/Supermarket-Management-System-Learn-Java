# CMart Modules

Status key: **Done** · **In progress** · **Planned** · **Needs update**

Track progress here while you build. Full feature list lives in [SMART_POS_ROADMAP.md](SMART_POS_ROADMAP.md).

---

## Current desktop modules

| Module | Package / files | Status | Notes |
|--------|-----------------|--------|-------|
| Home shell | `ui/shell/HomeFrame`, `PanelLoader`, `ui/theme/*` | Done | Role-filtered nav + Settings / Sign out |
| Login & roles | `ui/auth/LoginFrame`, `security/*`, `AuthService` | Done | PBKDF2 login; ADMIN / CASHIER / STORE_KEEPER |
| Settings | `ui/settings/SettingsPanel`, `SettingService` | Done | Shop name, tax, currency, stock policy in DB |
| Customers | `ui/customer/CustomerPanel` + `CustomerDao` / `CustomerService` | Done | Panel uses service only (PreparedStatement in DAO) |
| Suppliers | `ui/supplier/SupplierPanel` + `SupplierDao` / `SupplierService` | Done | Same layered pattern as customers |
| Employees | `ui/employee/EmployeePanel` + `EmployeeDao` / `EmployeeService` | Done | People CRUD; login users are separate (`app_user`) |
| Products | `ui/product/ProductPanel` + `ProductDao` / `ProductService` | Done | Panel uses service; validation on save/update |
| Users admin | `ui/admin/UsersPanel` + `UserAdminService` | Done | Create users, change passwords, roles (ADMIN only) |
| Sales | `ui/sale/SalePanel` + `SaleService` / `SaleDao` | Done | Barcode, discount, live tax, typeahead search, pay method, hold/recall, stock |
| Invoices | `ui/invoice/InvoicePanel` + `InvoiceService` / `SaleDao` | Done | Filter + reprint receipt (double-click / button) |
| Returns | `ui/refund/RefundPanel` + `ReturnService` | Done | Load invoice lines → refund qty → restock |
| Cashier shift | `ui/shift/ShiftPanel` + `ShiftService` | Done | Open float / close with expected vs counted cash |
| Purchase | `ui/purchase/PurchasePanel` + `PurchaseService` | Done | Create PO + receive goods (stock +) |
| Dashboard | `ui/dashboard/DashboardPanel` + `DashboardService` / `BackupService` | Done | Sales / dues / low stock / top products + mysqldump backup |
| Reports | `ui/report/*`, `resources/reports/*` | Needs update | Jasper all-sales / by invoice (still works) |
| Database | `config` + `SchemaMigrator`, `pos.sql` | Done | Auto-migrate users, settings, held_sale, returns, shifts, PO, tax |

---

## Phase status (roadmap)

| Phase | Focus | Status |
|-------|--------|--------|
| Phase 1 | Harden existing modules (SQL, stock, validation, shortcuts) | Done (core) |
| Phase 2 | Smart Sales (barcode, discount, pay methods, hold bill, refund) | Done (core) |
| Phase 3 | Inventory & purchasing | In progress (PO/GRN done) |
| Phase 4 | Owner dashboard, login, profit, backup | In progress (login + dashboard + backup done) |
| Phase 5 | Speed & UX polish | Planned |
| Phase 6 | Smart extras (mobile, multi-store, loyalty) | Planned |

---

## Planned new modules (not in app yet)

| Module | Phase | Status | Short description |
|--------|-------|--------|-------------------|
| Login & roles | 4 | Done | Admin / Cashier / Store keeper (hashed passwords) |
| Settings | 4 | Done | DB-backed shop options |
| Dashboard | 4 | Done | Recent sales, low stock, dues, top products |
| Purchase orders | 3 | Done | Order from suppliers → receive stock |
| Stock adjustment | 3 | Planned | Damage, expire, recount |
| Returns / refunds | 2 | Done | Linked to original invoice |
| Cashier shift | 2 | Done | Open float / close day |
| Expenses | 4 | Planned | Rent, utilities, wages |
| Audit log | 4 | Planned | Who changed what |
| Backup / restore | 4 | Done | mysqldump to chosen folder (PATH required) |

---

## Tech / project health

| Item | Status | Notes |
|------|--------|-------|
| `.gitignore` for `build/`, NetBeans private files | Done | Generated output not on GitHub |
| Shared UI theme (`UiTheme`) | Done | Applied on Run |
| Run layouts (`RunView`) | Done | Screens rebuilt after NetBeans `initComponents` |
| Layered package `src/cmart/` | Done | config / db / model / dao / service / ui |
| Config outside source for DB password | Done | `config/db.properties` (gitignored) |
| Team activity log (auto on push) | Done | `docs/ACTIVITY.md` + GitHub Action |
| Prepared statements | In progress | ProductDao done; other modules next |
| Automated tests | Planned | Later |

---

## Update checklist (when you finish something)

1. Change status in the tables above.
2. Add a short line under [CHANGELOG.md](CHANGELOG.md).
3. If it was a roadmap item, mark it done in [SMART_POS_ROADMAP.md](SMART_POS_ROADMAP.md) (strike-through or `[x]`).
