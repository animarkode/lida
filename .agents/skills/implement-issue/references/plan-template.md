# Plan template — Tier 2 and 3

Save to `.claude/plans/{YYYY-MM-DD}-{issue-number}-{kebab-slug}.md`. The issue number is mandatory and always sits between the date and the slug — `.claude/plans/` is gitignored, so the filename is the only link back to the issue.

**The plan is a pointer sheet, not the implementation.** It names *where* work happens and *how it is verified*; the implementer reads the actual code. No inline code blocks except the exact shell commands for verify and commit. Never paste a file's future contents into the plan.

A Tier 2 plan stays under ~6 KB. If it grows past that, the issue is a Tier 3.

Fill every placeholder — one left as-is is a plan failure.

---

```markdown
# Issue #{N} — {issue title, verbatim from GitHub}

**Goal:** [one sentence]

**Approach:** [2–3 sentences — the shape of the change, not its code]

## Global constraints

- Branch: `{issue-number}-{kebab-case-description}`
- Verify every task through the `gradle-run` skill, never `./gradlew` directly: [exact Gradle task(s) and flags, per AGENTS.md "Commands"]
- [AGENTS.md rules this issue actually touches: MVVM / one `StateFlow<UiState>` per screen, localization in all three `values*` dirs, TDD red then green, Room schema rules]
- Commits: Conventional Commits and the trailer rule from `AGENTS.md`.

## Checkpoints

- [ ] Task 1: [name]
- [ ] Task 2: [name]
- [ ] Manual QA on the phone (below, if the issue has a UI surface)

---

### Task 1: [name]

[One or two sentences: which problem from the issue this task closes.]

**Files**
- Create: `exact/path`
- Modify: `exact/path` — [what changes, in words]
- Delete: `exact/path`
- Test: `exact/path`

**Required skills:** [names that genuinely exist and apply, or `None`]

**Interfaces**
- Consumes: [exact names/signatures produced by earlier tasks]
- Produces: [exact names/signatures later tasks rely on — an implementer sees only its own brief]

**Verify:** via `gradle-run`: `[exact Gradle task and flags]` → expects `[exact expected output]`

**Commit:** `type(scope): description` (plus the trailer per AGENTS.md)

---

## Manual QA

- [ ] [what to look at on the phone] — [exact screen/interaction]
- [ ] Check in all three locales: `adb shell cmd locale set-app-locales com.animarkode.lida --locales en`, then `uk`, then `pl`; reset afterwards with `adb shell cmd locale set-app-locales com.animarkode.lida --locales ""`

[Write "not applicable — [reason]" when the issue has no UI surface.]
```

---

## Bookkeeping during execution

Tick a task's checkbox only after its diff is reviewed clean, and append the commit hash on the same line:

```
- [x] Task 1: rename the token — `a1b2c3d`, review clean
```

The checkboxes are the source of truth for what is done — the session's memory of the run is not.
