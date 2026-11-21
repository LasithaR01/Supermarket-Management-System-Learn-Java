# CMart Architecture

Package root: `cmart` (flat — no `com.company` nesting)

## Layout

```text
src/cmart/
├── App.java                 # main entry
├── config/                  # DatabaseConfig (reads config/db.properties)
├── db/                      # ConnectionFactory
├── model/                   # Product, Customer, Supplier, Employee, Sale, CartItem
├── dao/                     # SQL access (ProductDao uses PreparedStatement)
├── service/                 # ProductService, SaleService
├── util/                    # ReportPaths, Validation
├── resources/
│   ├── img/                 # UI icons
│   └── reports/             # Jasper .jrxml / .jasper
└── ui/
    ├── theme/               # UiTheme, RunView
    ├── shell/               # HomeFrame, PanelLoader
    ├── customer/            # CustomerPanel
    ├── supplier/            # SupplierPanel
    ├── employee/            # EmployeePanel
    ├── product/             # ProductPanel
    ├── sale/                # SalePanel
    ├── invoice/             # InvoicePanel
    └── report/              # ReportsPanel, ReportView
```

## Layers

| Layer | Role |
|-------|------|
| **ui** | Swing screens only |
| **service** | Business rules |
| **dao** | Database queries |
| **model** | Data objects |
| **config / db** | Connection settings |

## Config

1. Copy `config/db.properties.example` → `config/db.properties`
2. Set your MySQL URL, user, and password
3. `db.properties` is gitignored

## Run

- Main class: `cmart.App` (also set in NetBeans `project.properties`)
- **All screens**: Design and Run use the same form layout (Home shell + Products, Sales, Invoices, Reports, Customers, Suppliers, Employees). Runtime only applies `UiTheme` styling — it no longer rebuilds the UI with `RunView.install`.

## Team activity log

- `docs/ACTIVITY.md` — who / when / type / module / note (dev + QA)
- `docs/activity-seed.json` — curated history of past work
- `scripts/generate_activity.py` — merges seed + `git log`
- `.github/workflows/update-activity.yml` — regenerates on every push to `main`

## Migration notes

- Legacy package `pos.pro` was removed.
- Product is the reference module for dao + service; other screens still call SQL in the panel and should move to dao/service next (see roadmap Phase 1).
