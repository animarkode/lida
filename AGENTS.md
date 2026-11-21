# Lida Agent Guide

Single instruction file for Claude Code, Codex, Gemini and other agents. `CLAUDE.md` is a symlink to this file.

## Project

Lida is an Android app (Kotlin, Jetpack Compose) for tracking watched items. Package `com.animarkode.lida`.

## Stack

- Kotlin 2.2.10, AGP 9.5.1 (built-in Kotlin), Gradle 9.6.0 (wrapper).
- compileSdk 37, targetSdk 37, minSdk 29.
- Jetpack Compose (BOM 2026.02.01), Material 3, Navigation Compose 2.10.2.
- Room 2.8.5 with KSP; schemas are exported to `app/schemas/`.
- Versions live in `gradle/libs.versions.toml`. Do not add a dependency or bump a version without a task that says so.

## Architecture

Code is under `app/src/main/java/com/animarkode/lida/`: `data/` (model, `local/` Room, repository), `feature/<name>/` (list, edit), `ui/` (theme, components, navigation).

- MVVM. One `StateFlow<UiState>` per screen, owned by the `ViewModel`.
- Each screen is a stateful `XScreen` (gets the ViewModel, collects state) plus a stateless `XContent` (state and callbacks in, UI out).
- Collect Flow in Compose with `collectAsStateWithLifecycle`.
- Manual DI through `AppContainer`. No Hilt yet; Hilt arrives together with the TMDB network layer.
- Ids are UUID strings. Deletes are soft (`deleted` flag); never hard-delete rows.
- Room: DAOs use `suspend` / `Flow`, never touch the main thread. Changing the schema means a new `version` in `AppDatabase`, a migration with a test, and the new JSON committed in `app/schemas/`.

## Commands

**Run every Gradle command through the `gradle-run` skill (`.agents/skills/gradle-run`, needs `python3`). Never call `./gradlew` directly.** The tasks to pass to it:

| Task | Command |
|---|---|
| Unit tests | `./gradlew testDebugUnitTest` |
| Instrumented tests | `./gradlew connectedDebugAndroidTest` |
| One instrumented class | `./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=<FQCN>` (NOT `--tests`) |
| Lint (run when resources or strings change) | `./gradlew lintDebug` |
| Install on device | `./gradlew :app:installDebug` |

## Device notes

- The phone must be unlocked for UI tests.
- `connectedDebugAndroidTest` uninstalls the app when it finishes; run `:app:installDebug` afterwards.
- There is one device: never run instrumented tests or installs in parallel. `testDebugUnitTest` and `lintDebug` may run in parallel.

## Localization

- Every new string goes into `values/`, `values-uk/` and `values-pl/` (`app/src/main/res/`).
- No hardcoded UI text. Tests read text from string resources, never literals.
- Icon-only controls get a `contentDescription`.
- Check a locale on the device: `adb shell cmd locale set-app-locales com.animarkode.lida --locales en|uk|pl`.

## Process

- TDD, red then green: use `superpowers:test-driven-development` for every feature and fix.
- Conventional Commits: `feat:`, `fix:`, `chore:`, `build:`, `test:`, `docs:`, `refactor:`.
- **Commits made by an agent end with a `Co-Authored-By:` trailer naming the model.** This is the single source of truth; skills and agents reference this rule.
- Work on a branch named `{issue-number}-{kebab-description}`, never on `main`.
- No push and no PR without explicit user confirmation.
- Plans go to `.claude/plans/{YYYY-MM-DD}-{issue}-{slug}.md` (gitignored). Plans live only locally: never commit a plan, and never write one to `docs/` (this overrides any skill default such as `docs/superpowers/plans/`).
- Keep changes scoped to the request; preserve unrelated working-tree changes; never commit secrets.

## Skills

Skills live only in `.agents/skills/`; `.claude/skills/` holds relative symlinks to them. Source and licenses: `.agents/skills/SOURCES.md`. Never invent a skill name; check the list.

| Skill | Use when |
|---|---|
| `gradle-run` | Any Gradle execution or build diagnosis (mandatory, see Commands) |
| `compose-state-and-effects` | State ownership, hoisting, `LaunchedEffect`/`DisposableEffect`, Flow collection, screen state holders |
| `compose-component-design` | Designing reusable composable APIs (modifier, slots, content parameters) |
| `compose-animations` | Enter/exit, animate-to-target, content switching, transitions |
| `compose-performance` | Recomposition cost, stability reports, skippability |
| `jetpack-compose-performance` | Google's guide for Compose jank and recomposition; stability, shared elements |
| `compose-ui-testing-patterns` | Compose UI tests, semantics assertions, previews, screenshot evidence |
| `compose-focus-navigation` | Focus, keyboard/D-pad, accessibility focus (dependency of other skills) |
| `kotlin-concurrency-and-flow` | Coroutine scopes, cancellation, `StateFlow`/`SharedFlow`, `stateIn` |
| `kotlin-api-design` | Function ownership, extensions, factories, value classes, data classes |
| `kotlin-control-flow` | `when`, sealed exhaustiveness, smart casts, early returns |
| `material-3` | Theme, tokens, M3 components, Expressive, screen audits |
| `edge-to-edge` | System bar and keyboard insets |
| `testing-setup` | Test strategy and screenshot tests (`screenshotTests`, AGP 9.5+). Ignore its "install Hilt if no DI" step for now |
| `/implement-issue <N> [--tier N] [--worktree]` | Issue to plan to implementation pipeline |

`implement-issue` triage reads GitHub labels. `type:*`: `type:feat`, `type:fix`, `type:refactor`, `type:chore`, `type:docs`, `type:test`. `area:*`: `area:list`, `area:edit`, `area:data` (Room, repository), `area:ui` (theme, components, animations), `area:i18n`, `area:build` (Gradle, versions), `area:tests` (test infrastructure).
