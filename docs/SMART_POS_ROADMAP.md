# CMart Smart POS Roadmap

Goal: turn the current desktop POS into a **fast, easy system** a shop owner can run day to day — smart checkout, clear stock control, and simple business reports.

## How to use this roadmap

1. Finish **Phase 1** before big new features (safety and stock first).
2. Build one phase at a time; mark items Done in [MODULES.md](MODULES.md).
3. Log finished work in [CHANGELOG.md](CHANGELOG.md).

---

## Already have (keep and improve)

| Module | What it does now | Needs update |
|--------|------------------|--------------|
| Home / navigation | Menu + welcome cards | Keyboard shortcuts, role-based menus |
| Customers | Add / search / reports | Validation, duplicate check, credit balance |
| Suppliers | Add / search / reports | Purchase link, outstanding payables |
| Employees | Add / search / reports | Login roles, attendance later |
| Products | CRUD + search | Stock alerts, categories, barcode focus |
| Sales | Cart, pay & print | Faster checkout, stock deduct, discounts |
| Invoices | Search sales by ID / name / status | Reprint, refund, payment history |
| Reports | Jasper sales reports | Dashboard + daily/weekly/profit |
| Database (`cmartdb`) | MySQL tables | Indexes, safe SQL, config outside code |

---

## Phase 1 — Make current modules solid (do first)

1. ~~Move DB password out of `db.java` into a local config file (not committed).~~ Done (`config/db.properties`).
2. Replace string SQL with `PreparedStatement` everywhere (ProductDao started).
3. Deduct product stock when a sale is paid.
4. Block sale if qty is 0 or stock is too low.
5. Clear validation messages (empty name, bad phone, negative price).
6. Confirm before Delete / Remove All.
7. Fix invoice number generation (no race / lost numbers).
8. Add keyboard shortcuts on Sales (Enter add, F2 pay, Esc clear).
9. Speed: load only needed columns; search with indexes.
10. One shared “people form” pattern for Customer / Supplier / Employee (less copy-paste).

**Outcome:** Safer, faster checkout with the screens you already have.

---

## Phase 2 — Smart Sales (core POS)

1. Barcode scan box as the main add-to-cart action.
2. Product quick search by name / barcode while typing.
3. Discounts: % or fixed amount per bill / line.
4. Tax / VAT optional per product or bill.
5. Multiple pay methods: Cash, Card, Mobile money, Mixed.
6. Hold bill / recall bill (park a cart, serve next customer).
7. Customer credit / due with payment later.
8. Receipt reprint from Invoices.
9. Return / refund module linked to original invoice.
10. Cashier session: open float, close shift, cash difference.

**Outcome:** Counter feels like a real supermarket till.

---

## Phase 3 — Inventory & purchasing

1. Categories / brands for products.
2. Low-stock alerts on Home and Products.
3. Purchase orders from Suppliers.
4. Goods received → stock increase.
5. Stock adjustment (damage, expire, recount).
6. Expiry date for perishable items (optional).
7. Multi-unit (piece / pack / box) if needed.
8. Supplier price vs selling price (margin view).

**Outcome:** Owner knows what to reorder before stock runs out.

---

## Phase 4 — Business management for the owner

1. Login + roles: Admin, Cashier, Store keeper.
2. Dashboard: today sales, top products, low stock, dues.
3. Daily / weekly / monthly sales reports.
4. Profit report (sales − cost − discounts).
5. Expense module (rent, utilities, wages).
6. Customer outstanding report.
7. Supplier payable report.
8. Employee permissions (who can delete / discount / refund).
9. Audit log (who changed price / deleted record).
10. Backup / restore MySQL with one click.

**Outcome:** Owner manages the business without opening Workbench.

---

## Phase 5 — Extremely fast & friendly polish

1. Offline-first feel: no lag on add-to-cart (local cache of products).
2. Touch-friendly big buttons for tablet / touch POS.
3. Dark / light theme toggle.
4. Sinhala / English language toggle (if you sell in Sri Lanka).
5. Sound / beep on barcode ok / error.
6. Second display / customer-facing total (optional).
7. Thermal printer support (80mm receipts).
8. Export Excel / CSV for reports.
9. Empty states with clear next action (“No products — Add one”).
10. Onboarding wizard: first shop name, tax, currency.

**Outcome:** Staff can learn it in minutes; checkout stays under a few seconds.

---

## Phase 6 — Smart / modern extras (later)

1. Mobile app for owner (view sales & stock only).
2. WhatsApp / SMS bill share.
3. Multi-branch / multi-store.
4. Cloud sync or hosted DB.
5. Loyalty points for customers.
6. Smart suggestions: “often bought together”.
7. Demand forecast for reordering.
8. Web admin panel (optional, after desktop is stable).

**Outcome:** Competitive smart POS, not only a student project.

---

## Suggested build order

1. Phase 1 — security + stock + SQL  
2. Phase 2 — Sales speed  
3. Phase 3 — Inventory  
4. Phase 4 — Owner dashboard  
5. Phase 5 — UX polish  
6. Phase 6 — smart extras  

---

## Related docs

| File | Purpose |
|------|---------|
| [MODULES.md](MODULES.md) | Current modules + status (Done / In progress / Planned) |
| [CHANGELOG.md](CHANGELOG.md) | What was finished over time |
| [../README.md](../README.md) | Setup and how to run |
