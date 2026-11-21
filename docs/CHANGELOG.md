# Changelog

All notable changes to CMart are listed here (newest first).

Format: date · short summary · related docs if useful.

---

## 2026-11-22

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
