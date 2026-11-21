#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Build docs/ACTIVITY.md for developers and QA.

Sources (merged, newest first):
  1. docs/activity-seed.json  — curated / session history
  2. git log                  — every commit (author, date, message)

Commit tip: feat(sales): short note
"""
from __future__ import print_function

import json
import os
import re
import subprocess
import sys
from datetime import datetime

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
SEED_PATH = os.path.join(ROOT, "docs", "activity-seed.json")
OUT_PATH = os.path.join(ROOT, "docs", "ACTIVITY.md")

CONV = re.compile(
    r"^(?P<type>feat|fix|docs|refactor|test|chore|style|perf|build|ci|revert)"
    r"(?:\((?P<module>[^)]+)\))?!?:\s*(?P<note>.+)$",
    re.I,
)

MODULE_HINTS = [
    ("customer", "customer"),
    ("supplier", "supplier"),
    ("employee", "employee"),
    ("product", "product"),
    ("category", "product"),
    ("brand", "product"),
    ("sale", "sales"),
    ("invoice", "invoice"),
    ("report", "report"),
    ("ui", "ui"),
    ("theme", "ui"),
    ("home", "ui"),
    ("readme", "docs"),
    ("docs", "docs"),
    ("gitignore", "repo"),
    ("database", "db"),
    ("mysql", "db"),
    ("jdbc", "db"),
    ("cmart", "repo"),
    ("structure", "repo"),
    ("package", "repo"),
]


def run_git(args):
    try:
        out = subprocess.check_output(
            ["git"] + args,
            cwd=ROOT,
            stderr=subprocess.STDOUT,
            universal_newlines=True,
        )
        return out.strip()
    except (subprocess.CalledProcessError, OSError) as e:
        print("git warning:", e, file=sys.stderr)
        return ""


def guess_module(subject):
    low = subject.lower()
    for needle, module in MODULE_HINTS:
        if needle in low:
            return module
    return "general"


def parse_subject(subject):
    m = CONV.match(subject.strip())
    if m:
        return (
            m.group("type").lower(),
            (m.group("module") or "general").lower(),
            m.group("note").strip(),
        )
    # Merge / PR noise
    if subject.lower().startswith("merge "):
        return "chore", "repo", subject.strip()
    # Guess type from keywords
    low = subject.lower()
    if low.startswith("create ") or low.startswith("add "):
        t = "feat"
    elif "fix" in low or "bug" in low:
        t = "fix"
    elif "readme" in low or "doc" in low:
        t = "docs"
    elif "refactor" in low or "clean" in low or "restructure" in low:
        t = "refactor"
    elif "gitignore" in low or "build" in low or "setup" in low:
        t = "chore"
    else:
        t = "chore"
    return t, guess_module(subject), subject.strip()


def load_seed():
    if not os.path.isfile(SEED_PATH):
        return []
    with open(SEED_PATH, "r", encoding="utf-8") as f:
        data = json.load(f)
    rows = []
    for item in data:
        rows.append(
            {
                "when": item.get("when", ""),
                "who": item.get("who", "unknown"),
                "type": item.get("type", "chore"),
                "module": item.get("module", "general"),
                "note": item.get("note", ""),
                "source": "seed",
                "sha": "",
            }
        )
    return rows


def load_commits(limit=500):
    fmt = "%H%x09%an%x09%ad%x09%s"
    raw = run_git(
        ["log", "--pretty=format:" + fmt, "--date=format:%Y-%m-%d %H:%M", "-n", str(limit)]
    )
    if not raw:
        return []
    rows = []
    for line in raw.splitlines():
        parts = line.split("\t")
        if len(parts) < 4:
            continue
        sha, who, when, subject = parts[0], parts[1], parts[2], parts[3]
        # Skip activity-bot loops
        if subject.startswith("docs(activity):") or subject.startswith("chore(activity):"):
            continue
        if "auto-update activity log" in subject.lower():
            continue
        typ, module, note = parse_subject(subject)
        rows.append(
            {
                "when": when,
                "who": who,
                "type": typ,
                "module": module,
                "note": note,
                "source": "git",
                "sha": sha[:7],
            }
        )
    return rows


def note_fingerprint(note):
    n = re.sub(r"[^a-z0-9\s]", "", note.lower())
    n = re.sub(r"\s+", " ", n).strip()
    return n[:70]


def dedupe(rows):
    """Prefer seed rows; drop git rows that repeat the same day+note."""
    # Seed first so they win when notes overlap
    rows = sorted(rows, key=lambda r: 0 if r["source"] == "seed" else 1)
    seen = set()
    out = []
    for r in rows:
        key = (r["when"][:10], r["who"].lower(), note_fingerprint(r["note"]))
        if key in seen:
            continue
        seen.add(key)
        out.append(r)
    return out


def sort_key(r):
    try:
        return datetime.strptime(r["when"][:16], "%Y-%m-%d %H:%M")
    except ValueError:
        try:
            return datetime.strptime(r["when"][:10], "%Y-%m-%d")
        except ValueError:
            return datetime.min


def render(rows):
    lines = []
    lines.append("# CMart Activity Log")
    lines.append("")
    lines.append(
        "Living timeline for **developers**, **QA**, and the team. "
        "Updated automatically on GitHub push from `git` commits + curated seed history."
    )
    lines.append("")
    lines.append("## How to read this")
    lines.append("")
    lines.append("| Column | Meaning |")
    lines.append("|--------|---------|")
    lines.append("| When | Date and time (local commit / seed time) |")
    lines.append("| Who | Git author name |")
    lines.append("| Type | `feat` feature · `fix` bugfix · `docs` docs · `refactor` cleanup · `chore` tooling · `test` tests · `perf` performance |")
    lines.append("| Module | Area of the app (`sales`, `product`, `ui`, `docs`, …) |")
    lines.append("| Note | What changed |")
    lines.append("| Source | `git` = from commit · `seed` = curated history |")
    lines.append("")
    lines.append("## Commit message format (required for clean auto rows)")
    lines.append("")
    lines.append("```text")
    lines.append("feat(sales): add barcode-first add-to-cart")
    lines.append("fix(product): block save when qty is empty")
    lines.append("docs(docs): update ACTIVITY seed for Nov sprint")
    lines.append("```")
    lines.append("")
    lines.append("Full guide: [COMMIT_GUIDE.md](COMMIT_GUIDE.md)")
    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("## Timeline (newest first)")
    lines.append("")
    lines.append("| When | Who | Type | Module | Note | Source |")
    lines.append("|------|-----|------|--------|------|--------|")

    for r in rows:
        when = r["when"].replace("|", "/")
        who = r["who"].replace("|", "/")
        note = r["note"].replace("|", "/").replace("\n", " ")
        src = r["source"]
        if r.get("sha"):
            src = "%s `%s`" % (src, r["sha"])
        lines.append(
            "| %s | %s | `%s` | `%s` | %s | %s |"
            % (when, who, r["type"], r["module"], note, src)
        )

    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("*Generated by `scripts/generate_activity.py`. Do not hand-edit the table; update `activity-seed.json` or commit with a conventional message.*")
    lines.append("")
    return "\n".join(lines)


def main():
    rows = load_seed() + load_commits()
    rows = dedupe(rows)
    rows.sort(key=sort_key, reverse=True)
    text = render(rows)
    os.makedirs(os.path.dirname(OUT_PATH), exist_ok=True)
    with open(OUT_PATH, "w", encoding="utf-8", newline="\n") as f:
        f.write(text)
    print("Wrote %s (%d rows)" % (OUT_PATH, len(rows)))
    return 0


if __name__ == "__main__":
    sys.exit(main())
