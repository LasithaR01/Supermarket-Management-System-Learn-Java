# CMart Architecture

Package root: `cmart` (flat — no `com.company` nesting)

## Project layout

```text
project/
├── config/                  # db.properties (+ example); gitignored secrets
├── docs/                    # architecture, modules, roadmap, activity
├── lib/                     # MySQL 8 connector + Jasper deps
├── scripts/                 # activity log generator
├── sql/
│   ├── pos.sql              # base schema (import into cmartdb)
│   └── migrate_*.sql        # optional / historical SQL scripts
├── src/cmart/
│   ├── App.java             # migrate → defaults → login → home
│   ├── config/              # DatabaseConfig, SettingKeys
│   ├── db/                  # ConnectionFactory, SchemaMigrator
│   ├── security/            # Role, Permission, PasswordHasher, SessionContext
│   ├── model/               # entities, filters, SaleCheckoutRequest
│   ├── dao/                 # PreparedStatement access
│   ├── service/             # *Service business rules only
│   ├── util/                # ReportPaths, Validation, CartCodec
│   ├── resources/
│   │   ├── img/             # UI icons (classpath /cmart/resources/img/…)
│   │   └── reports/         # Jasper templates (single reports home)
│   └── ui/
│       ├── auth/            # LoginFrame
│       ├── admin/           # UsersPanel, SuperAdminMonitorPanel
│       ├── settings/        # SettingsPanel
│       ├── theme/
│       ├── shell/           # HomeFrame, PanelLoader
│       ├── customer/ supplier/ employee/ product/
│       ├── sale/ invoice/ refund/ purchase/ shift/
│       ├── dashboard/ report/
└── test/                    # reserved for automated tests
```

## Layers

| Layer | Role |
|-------|------|
| **ui** | Swing only — no SQL, no password logic |
| **service** | Business rules, checkout, auth, settings |
| **dao** | PreparedStatement queries |
| **security** | Roles / permissions / PBKDF2 hashing / session |
| **model** | Data objects + checkout/filter DTOs |
| **config / db** | JDBC + idempotent schema migrate |

## Config

1. Copy `config/db.properties.example` → `config/db.properties`
2. Set your MySQL URL, user, and password
3. `db.properties` is gitignored
4. Shop options live in `app_setting` (keys in `SettingKeys`) — not hardcoded in screens
5. Base schema: import `sql/pos.sql` into `cmartdb` before first run

## Security

- Login required before `HomeFrame`
- Passwords: PBKDF2-HMAC-SHA256 + per-user salt (`PasswordHasher`)
- Self-service **Change password** (Home header) + forced change when `must_change_password`
- **Forgot password**: staff → Owner resets in Users; Owner/Super Admin → recovery PIN on login
- Password policy: min 8 chars, letter + number (`PasswordPolicy`)
- Login lockout: 5 failed attempts → 15 minute lock
- Idle auto sign-out: 15 minutes with no keyboard/mouse activity
- Security audit table `security_audit` (login, lock, password, user changes)
- Menus gated by `Permission` via `SessionContext.can(...)`
- Staff action ACL (`ActionAcl` + `acl.allow.*` settings) for discount / product delete / refund; Owner/Super always allowed
- Maintenance mode (`system.maintenance.mode`) blocks non–Super Admin login
- Super Admin Monitor: health, audit/error export, unlock, emergency users, ACL, schema, backup/restore
- Roles:
  - `SUPER_ADMIN` — dev / support team (`SYSTEM` permission; can create another Super Admin in Users)
  - `STORE_OWNER` — shop owner (users, settings, dashboard, backup; no `SYSTEM`)
  - `CASHIER` — till, invoices, refunds, shifts
  - `STORE_KEEPER` — products, suppliers, purchase
- Legacy DB role `ADMIN` migrates to `STORE_OWNER`
- First-run accounts (created only when `app_user` is empty; must change password):
  - `super` / `super123` (SUPER_ADMIN)
  - `owner` / `owner123` (STORE_OWNER)
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
- Legacy duplicate `src/reports/` removed; Jasper files live only under `src/cmart/resources/reports/`.
- Product is the reference module for dao + service; other screens follow the same pattern.
