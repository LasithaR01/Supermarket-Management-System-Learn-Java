# Changelog

All notable changes to CMart are listed here (newest first).

Format: date · short summary · related docs if useful.

---

## 2026-11-22

- Still-later pack: returns/refunds, cashier shift open/close, purchase orders + goods received, owner dashboard + mysqldump backup, live tax on till, product typeahead search; header ops wired by permission.
- Phase 2 till upgrades: barcode scan add, bill discount (Rs/%), pay method, hold/recall (F4), Esc clear, invoice reprint, Home low-stock hint.
- Invoice panel moved off string SQL onto `InvoiceService` + `SaleDao` filter queries.
- Customer / Supplier / Employee / Product panels moved off string SQL onto DAO + Service (PreparedStatement).
- Users admin screen: create accounts, change passwords, update roles — no need to keep demo passwords.
- Schema migrator expands legacy people tables with missing address/contact columns.
- Professional security + options layer: login (`LoginFrame`), PBKDF2 passwords, role permissions, DB-backed Settings (`app_setting`), schema auto-migrate.
- Sales checkout moved to `SaleService.completeSale` + `SaleDao` (PreparedStatement; no string SQL in `SalePanel` pay path).
- Added team **Activity Log** (`docs/ACTIVITY.md`) with seed history of all prior work + GitHub Action that regenerates it on every push to `main`.
- Added `docs/COMMIT_GUIDE.md` and `scripts/generate_activity.py`.
- Synced NetBeans Design and Run for all screens (Home + Products, Sales, Invoices, Reports, Customers, Suppliers, Employees) — forms hold the layout; runtime only applies theme.
- Added project docs: Smart POS roadmap, module status board, architecture, and this changelog (`docs/`).
- Restructured code into flattened layered package `src/cmart/` (config, db, model, dao, service, util, ui).
- Moved DB password to `config/db.properties` (gitignored); main class is now `cmart.App`.
- Stopped tracking generated NetBeans build files (`build/`, `nbproject/private`, generated Ant files); tightened `.gitignore`.
- Shared green theme and welcome shell for CMart.

## 2026-11-21 (approx.)

- UI polish pass: clearer labels, button colors, table headers, empty search fields.
- Earlier: project setup, MySQL POS schema (`pos.sql`), NetBeans Swing modules for supermarket POS.

---

## Template for new entries

```text
## YYYY-MM-DD

- What you finished (feature, fix, docs).
- Optional: Phase X item from SMART_POS_ROADMAP.md
```
