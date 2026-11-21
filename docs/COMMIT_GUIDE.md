# Commit message guide (CMart)

Clear commits keep [ACTIVITY.md](ACTIVITY.md) useful for developers and QA after every GitHub push.

## Format

```text
type(module): short note in plain English
```

Examples:

```text
feat(sales): add barcode-first add-to-cart
fix(product): block save when qty is empty
docs(docs): add QA notes for invoice search
refactor(ui): flatten HomeFrame sidebar layout
chore(ci): refresh activity log workflow
test(db): cover ProductDao.findById
```

## Types

| Type | Use when |
|------|----------|
| `feat` | New behavior users can see |
| `fix` | Bug fix |
| `docs` | Docs only |
| `refactor` | Cleanup / structure, same behavior |
| `style` | Formatting / labels / look (no logic) |
| `perf` | Faster path |
| `test` | Tests only |
| `chore` | Tooling, gitignore, build, deps |
| `ci` | GitHub Actions / pipelines |
| `build` | Build scripts / Ant / packaging |

## Modules (pick one)

`sales` · `product` · `customer` · `supplier` · `employee` · `invoice` · `report` · `ui` · `db` · `docs` · `repo` · `ci`

## Who shows up on ACTIVITY?

Git author name from your machine:

```bash
git config user.name
git config user.email
```

Use your real name so the team log shows **you**, not a bot or `cursoragent`.

## What happens on push

1. You push to GitHub  
2. Workflow `.github/workflows/update-activity.yml` runs  
3. `scripts/generate_activity.py` rebuilds `docs/ACTIVITY.md` from:
   - `docs/activity-seed.json` (curated history)
   - `git log` (all commits)
4. If the file changed, the Action commits it back  

## Adding older work by hand

Edit `docs/activity-seed.json` (one JSON object per change), then run:

```bash
python scripts/generate_activity.py
```

Or push — CI will regenerate.
