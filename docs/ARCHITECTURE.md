# CMart Architecture

Package root: `cmart` (flat — no `com.company` nesting)

## Layout

```text
src/cmart/
├── App.java                 # migrate → defaults → login → home
├── config/                  # DatabaseConfig, SettingKeys
├── db/                      # ConnectionFactory, SchemaMigrator
├── security/                # Role, Permission, PasswordHasher, SessionContext
├── model/                   # AppUser, Product, Customer, … Sale, CartItem
├── dao/                     # PreparedStatement access (Product, Sale, User, Setting, Customer)
├── service/                 # Auth, Setting, Sale, Product, Customer
├── util/                    # ReportPaths, Validation
├── resources/
│   ├── img/
│   └── reports/
└── ui/
    ├── auth/                # LoginFrame
    ├── admin/               # UsersPanel (create / password / roles)
    ├── settings/            # SettingsPanel (DB-backed options)
    ├── theme/
    ├── shell/               # HomeFrame (role-filtered menus)
    ├── customer/ supplier/ employee/ product/  # service-backed CRUD
    ├── sale/ invoice/ report/
```

## Layers

| Layer | Role |
|-------|------|
| **ui** | Swing only — no SQL, no password logic |
| **service** | Business rules, checkout, auth, settings |
| **dao** | PreparedStatement queries |
| **security** | Roles / permissions / PBKDF2 hashing / session |
| **model** | Data objects |
| **config / db** | JDBC + idempotent schema migrate |

## Config

1. Copy `config/db.properties.example` → `config/db.properties`
2. Set your MySQL URL, user, and password
3. `db.properties` is gitignored
4. Shop options live in `app_setting` (keys in `SettingKeys`) — not hardcoded in screens

## Security

- Login required before `HomeFrame`
- Passwords: PBKDF2-HMAC-SHA256 + per-user salt (`PasswordHasher`)
- Menus gated by `Permission` via `SessionContext.can(...)`
- First-run accounts (created only when `app_user` is empty):
  - `admin` / `admin123` (ADMIN)
  - `cashier` / `cashier123` (CASHIER)
  - `store` / `store123` (STORE_KEEPER)

## Run

- Main class: `cmart.App` (also set in NetBeans `project.properties`)
- Startup: `SchemaMigrator` → setting defaults → bootstrap users → `LoginFrame`
- **All screens**: Design and Run use the same form layout where forms exist. Runtime applies theme + role visibility.

## Team activity log

- `docs/ACTIVITY.md` — who / when / type / module / note (dev + QA)
- `docs/activity-seed.json` — curated history of past work
- `scripts/generate_activity.py` — merges seed + `git log`
- `.github/workflows/update-activity.yml` — regenerates on every push to `main`

## Migration notes

- Legacy package `pos.pro` was removed.
- Product is the reference module for dao + service; other screens still call SQL in the panel and should move to dao/service next (see roadmap Phase 1).
