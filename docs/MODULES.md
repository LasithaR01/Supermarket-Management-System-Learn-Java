# CMart Modules

Status key: **Done** · **In progress** · **Planned** · **Needs update**

Track progress here while you build. Full feature list lives in [SMART_POS_ROADMAP.md](SMART_POS_ROADMAP.md).

---

## Current desktop modules

| Module | Package / files | Status | Notes |
|--------|-----------------|--------|-------|
| Home shell | `ui/shell/HomeFrame`, `PanelLoader`, `ui/theme/*` | Done | Design = Run (green shell + welcome cards) |
| Customers | `ui/customer/CustomerPanel` | Needs update | CRUD, search, Jasper reports; SQL still string-built |
| Suppliers | `ui/supplier/SupplierPanel` | Needs update | Same pattern as customers |
| Employees | `ui/employee/EmployeePanel` | Needs update | Same pattern; no login yet |
| Products | `ui/product/ProductPanel` + `dao/ProductDao` | Needs update | Dao/service exist; panel still uses older SQL path |
| Sales | `ui/sale/SalePanel` + `service/SaleService` | Needs update | Cart, pay & print; no stock deduct / barcode-first flow yet |
| Invoices | `ui/invoice/InvoicePanel` | Needs update | Filter by invoice / customer / status |
| Reports | `ui/report/*`, `resources/reports/*` | Needs update | Invoice / all-sales Jasper views |
| Database | `config` + `db/ConnectionFactory`, `pos.sql` | Needs update | Password in `config/db.properties`; more prepared statements next |

---

## Phase status (roadmap)

| Phase | Focus | Status |
|-------|--------|--------|
| Phase 1 | Harden existing modules (SQL, stock, validation, shortcuts) | Planned |
| Phase 2 | Smart Sales (barcode, discount, pay methods, hold bill, refund) | Planned |
| Phase 3 | Inventory & purchasing | Planned |
| Phase 4 | Owner dashboard, login, profit, backup | Planned |
| Phase 5 | Speed & UX polish | Planned |
| Phase 6 | Smart extras (mobile, multi-store, loyalty) | Planned |

---

## Planned new modules (not in app yet)

| Module | Phase | Status | Short description |
|--------|-------|--------|-------------------|
| Login & roles | 4 | Planned | Admin / Cashier / Store keeper |
| Dashboard | 4 | Planned | Today sales, low stock, dues |
| Purchase orders | 3 | Planned | Order from suppliers → receive stock |
| Stock adjustment | 3 | Planned | Damage, expire, recount |
| Returns / refunds | 2 | Planned | Linked to original invoice |
| Cashier shift | 2 | Planned | Open float / close day |
| Expenses | 4 | Planned | Rent, utilities, wages |
| Audit log | 4 | Planned | Who changed what |
| Backup / restore | 4 | Planned | One-click MySQL backup |

---

## Tech / project health

| Item | Status | Notes |
|------|--------|-------|
| `.gitignore` for `build/`, NetBeans private files | Done | Generated output not on GitHub |
| Shared UI theme (`UiTheme`) | Done | Applied on Run |
| Run layouts (`RunView`) | Done | Screens rebuilt after NetBeans `initComponents` |
| Layered package `src/cmart/` | Done | config / db / model / dao / service / ui |
| Config outside source for DB password | Done | `config/db.properties` (gitignored) |
| Prepared statements | In progress | ProductDao done; other modules next |
| Automated tests | Planned | Later |

---

## Update checklist (when you finish something)

1. Change status in the tables above.
2. Add a short line under [CHANGELOG.md](CHANGELOG.md).
3. If it was a roadmap item, mark it done in [SMART_POS_ROADMAP.md](SMART_POS_ROADMAP.md) (strike-through or `[x]`).
