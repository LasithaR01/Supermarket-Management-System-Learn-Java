# Changelog

All notable changes to CMart are listed here (newest first).

Format: date · short summary · related docs if useful.

---

## 2026-11-22

- Added project docs: Smart POS roadmap, module status board, architecture, and this changelog (`docs/`).
- Restructured code into flattened layered package `src/cmart/` (config, db, model, dao, service, util, ui).
- Moved DB password to `config/db.properties` (gitignored); main class is now `cmart.App`.
- Stopped tracking generated NetBeans build files (`build/`, `nbproject/private`, generated Ant files); tightened `.gitignore`.
- Run UI: shared theme, Home shell with welcome cards, and run-time layouts for Sales, Products, Customers, Suppliers, Employees, Invoices, and Reports (Design view still shows old forms).

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
