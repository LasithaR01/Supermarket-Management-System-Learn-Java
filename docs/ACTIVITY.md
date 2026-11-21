# CMart Activity Log

Living timeline for **developers**, **QA**, and the team. Updated automatically on GitHub push from `git` commits + curated seed history.

## How to read this

| Column | Meaning |
|--------|---------|
| When | Date and time (local commit / seed time) |
| Who | Git author name |
| Type | `feat` feature · `fix` bugfix · `docs` docs · `refactor` cleanup · `chore` tooling · `test` tests · `perf` performance |
| Module | Area of the app (`sales`, `product`, `ui`, `docs`, …) |
| Note | What changed |
| Source | `git` = from commit · `seed` = curated history |

## Commit message format (required for clean auto rows)

```text
feat(sales): add barcode-first add-to-cart
fix(product): block save when qty is empty
docs(docs): update ACTIVITY seed for Nov sprint
```

Full guide: [COMMIT_GUIDE.md](COMMIT_GUIDE.md)

---

## Timeline (newest first)

| When | Who | Type | Module | Note | Source |
|------|-----|------|--------|------|--------|
| 2026-11-22 02:05 | Lasitha Ransara | `docs` | `general` | Update activity log and commit guide with GitHub Actions cleanup feature | git `d26f1bb` |
| 2026-11-22 02:03 | Lasitha Ransara | `ci` | `ci` | Auto-delete GitHub Actions runs older than 2 days (keep Actions tab clean) | seed |
| 2026-11-22 02:00 | Lasitha Ransara | `docs` | `general` | Add team activity log and commit guide to improve project documentation | git `cde9f84` |
| 2026-11-22 01:54 | Lasitha Ransara | `docs` | `docs` | Add team ACTIVITY log + GitHub Action auto-update on push (dev/QA timeline) | seed |
| 2026-11-22 01:12 | Lasitha Ransara | `refactor` | `ui` | Unify UI layout and enhance styling across customer, employee, and invoice panels | seed |
| 2026-11-22 01:00 | Lasitha Ransara | `fix` | `ui` | Sync Design = Run for all screens (Products, Sales, Invoices, Reports, Customers, Suppliers, Employees) | seed |
| 2026-11-22 00:56 | Lasitha Ransara | `fix` | `ui` | Sync HomeFrame Design = Run (green shell + welcome cards in .form) | seed |
| 2026-11-22 00:39 | Lasitha Ransara | `refactor` | `repo` | Clean up project structure and remove unused legacy files | seed |
| 2026-11-22 00:39 | Lasitha Ransara | `refactor` | `general` | Clean up project structure and remove unused files | git `58e88f7` |
| 2026-11-22 00:34 | Lasitha Ransara | `docs` | `docs` | Recreate docs + layered cmart restructure after chat reset | seed |
| 2026-11-22 00:26 | Lasitha Ransara | `docs` | `docs` | Discuss public repo copyright / All Rights Reserved vs private repo | seed |
| 2026-11-22 00:24 | Lasitha Ransara | `refactor` | `repo` | Flatten package to src/cmart/ (remove deep com/cmart/pos folders) | seed |
| 2026-11-22 00:16 | Lasitha Ransara | `refactor` | `repo` | World-class layered package structure (config/db/model/dao/service/ui) | seed |
| 2026-11-22 00:10 | Lasitha Ransara | `docs` | `docs` | Create docs/SMART_POS_ROADMAP.md, MODULES.md, CHANGELOG.md | seed |
| 2026-11-22 00:08 | Lasitha Ransara | `docs` | `docs` | Smart POS vision: grow app into fast real-world POS; list modules to update step by step | seed |
| 2026-11-21 23:58 | Lasitha Ransara | `chore` | `ui` | Stop tracking generated NetBeans build files. | git `815f3b1` |
| 2026-11-21 23:55 | Lasitha Ransara | `chore` | `repo` | Stop tracking generated NetBeans build/dist/private files; tighten .gitignore | seed |
| 2026-11-21 23:53 | Lasitha Ransara | `chore` | `repo` | Merge pull request #13 from LasithaR01/feature | git `8675dfd` |
| 2026-11-21 23:52 | Lasitha Ransara | `feat` | `ui` | Enhance UI functionality and layout across multiple components | seed |
| 2026-11-21 23:42 | Lasitha Ransara | `feat` | `ui` | RunView layouts applied so Run screens use friendly layout before Design forms | seed |
| 2026-11-21 23:39 | Lasitha Ransara | `fix` | `ui` | Align Home Design path toward NetBeans Design view (run vs designer mismatch) | seed |
| 2026-11-21 23:34 | Lasitha Ransara | `docs` | `docs` | Create / update README with CMart setup instructions | seed |
| 2026-11-21 23:34 | Lasitha Ransara | `docs` | `docs` | Update README.md | git `56281ef` |
| 2026-11-21 23:34 | Lasitha Ransara | `feat` | `docs` | Create README.md | git `65e95d4` |
| 2026-11-21 23:29 | Lasitha Ransara | `chore` | `repo` | Merge pull request #12 from LasithaR01/feature | git `a56601f` |
| 2026-11-21 23:27 | Lasitha Ransara | `feat` | `ui` | Refactor UI components and improve UX (feature branch) | seed |
| 2026-11-21 23:27 | Lasitha Ransara | `feat` | `general` | Refactor UI components and improve user experience | git `37a3a83` |
| 2026-11-21 23:20 | Lasitha Ransara | `feat` | `ui` | Shared UiTheme + Home shell with welcome cards; clearer labels and button colors | seed |
| 2026-11-21 22:55 | Lasitha Ransara | `chore` | `repo` | Update .gitignore | seed |
| 2026-11-21 22:51 | Lasitha Ransara | `chore` | `repo` | NetBeans / CMart POS project setup on current tree | seed |
| 2026-11-21 22:51 | Lasitha Ransara | `build` | `general` | Project Setup | git `9793baf` |
| 2024-12-24 00:00 | Lasitha Ransara | `chore` | `repo` | Merge pull request #11 from LasithaR01/brand_model | git `f1648b6` |
| 2024-12-23 23:59 | Lasitha Ransara | `feat` | `product` | Brand model coding | seed |
| 2024-12-23 23:59 | Lasitha Ransara | `chore` | `product` | Coded Brand Model | git `a2f47f5` |
| 2024-12-21 13:07 | Lasitha Ransara | `chore` | `repo` | Merge pull request #10 from LasithaR01/category_model | git `55dd82a` |
| 2024-12-21 13:06 | Lasitha Ransara | `feat` | `product` | Category Delete button | seed |
| 2024-12-21 12:48 | Lasitha Ransara | `chore` | `repo` | Merge pull request #9 from LasithaR01/category_model | git `dcb9661` |
| 2024-12-21 12:47 | Lasitha Ransara | `feat` | `product` | Category Edit button | seed |
| 2024-12-21 01:25 | Lasitha Ransara | `chore` | `repo` | Merge pull request #8 from LasithaR01/category_model | git `441de84` |
| 2024-12-21 01:24 | Lasitha Ransara | `feat` | `product` | Category table coding | seed |
| 2024-12-21 01:24 | Lasitha Ransara | `chore` | `product` | Coded Category Table | git `9155032` |
| 2024-12-20 19:26 | Lasitha Ransara | `feat` | `product` | Add Category table UI | seed |
| 2024-12-20 19:26 | Lasitha Ransara | `feat` | `product` | Add Table for Category | git `3fecca2` |
| 2024-12-20 17:04 | Lasitha Ransara | `chore` | `repo` | Merge pull request #7 from LasithaR01/category_model | git `bb6ef3c` |
| 2024-12-20 17:03 | Lasitha Ransara | `feat` | `product` | Create Category interface (JFrame) | seed |
| 2024-12-20 17:03 | Lasitha Ransara | `feat` | `product` | Create Category Interface Using JFrame | git `0fec64a` |
| 2024-12-16 20:21 | Lasitha Ransara | `chore` | `repo` | Project build again | seed |
| 2024-12-16 20:21 | Lasitha Ransara | `chore` | `ui` | Project Build | git `a4acc83` |
| 2024-12-16 19:08 | Lasitha Ransara | `chore` | `repo` | Delete / rebuild project structure | seed |
| 2024-12-16 19:08 | Lasitha Ransara | `chore` | `general` | Delete Project | git `4b0fe91` |
| 2024-12-15 16:17 | Lasitha Ransara | `feat` | `db` | Connect MySQL database | seed |
| 2024-12-15 16:17 | Lasitha Ransara | `chore` | `db` | Connect Database | git `bcf74f6` |
| 2024-12-15 14:05 | Lasitha Ransara | `feat` | `sales` | Create SalesFacts model | seed |
| 2024-12-15 14:05 | Lasitha Ransara | `chore` | `repo` | Merge pull request #6 from LasithaR01/salesfacts_model | git `93d9bb5` |
| 2024-12-15 14:05 | Lasitha Ransara | `feat` | `sales` | Create SalesFacts.java | git `b48a360` |
| 2024-12-15 14:03 | Lasitha Ransara | `chore` | `repo` | Merge pull request #5 from LasithaR01/promotion_model | git `8afc339` |
| 2024-12-15 14:02 | Lasitha Ransara | `feat` | `sales` | Create Promotion model | seed |
| 2024-12-15 14:02 | Lasitha Ransara | `feat` | `general` | Create Promotion.java | git `5713d98` |
| 2024-12-15 13:59 | Lasitha Ransara | `feat` | `customer` | Create Customer model | seed |
| 2024-12-15 13:59 | Lasitha Ransara | `chore` | `repo` | Merge pull request #4 from LasithaR01/customer_model | git `5c85ae1` |
| 2024-12-15 13:59 | Lasitha Ransara | `feat` | `customer` | Create Customer.java | git `b64a6fc` |
| 2024-12-15 13:55 | Lasitha Ransara | `chore` | `repo` | Merge pull request #3 from LasithaR01/employee_model | git `ead7868` |
| 2024-12-15 13:54 | Lasitha Ransara | `feat` | `employee` | Create Employee model | seed |
| 2024-12-15 13:54 | Lasitha Ransara | `feat` | `employee` | Create Employee.java | git `3c27725` |
| 2024-12-15 13:40 | Lasitha Ransara | `chore` | `repo` | Merge pull request #2 from LasithaR01/department_model | git `18d1890` |
| 2024-12-15 13:39 | Lasitha Ransara | `feat` | `employee` | Create Department model | seed |
| 2024-12-15 13:39 | Lasitha Ransara | `feat` | `general` | Create Department.java | git `d3bb62e` |
| 2024-12-15 13:30 | Lasitha Ransara | `chore` | `repo` | Merge pull request #1 from LasithaR01/product_model | git `f01d9f8` |
| 2024-12-15 13:28 | Lasitha Ransara | `feat` | `product` | Create Product model | seed |
| 2024-12-15 13:28 | Lasitha Ransara | `feat` | `product` | Create Product.java | git `a6ddf14` |
| 2024-12-15 11:55 | Lasitha Ransara | `docs` | `docs` | Added ER diagram (ER-Supermarket.png) | seed |
| 2024-12-15 11:55 | Lasitha Ransara | `feat` | `general` | Create ER-Supermarket.png | git `120e052` |
| 2024-12-13 09:58 | Lasitha Ransara | `chore` | `repo` | Project setup for supermarket management learning project | seed |
| 2024-12-13 09:58 | Lasitha Ransara | `chore` | `general` | Project Setup | git `8bbfb2d` |
| 2024-12-12 19:38 | Lasitha Ransara | `chore` | `repo` | Initial commit — repository created | seed |
| 2024-12-12 19:38 | Lasitha Ransara | `chore` | `general` | Initial commit | git `08d1ec0` |

---

*Generated by `scripts/generate_activity.py`. Do not hand-edit the table; update `activity-seed.json` or commit with a conventional message.*
