# Lida

An Android app for remembering where you stopped in a movie or a series. Keep a list of what you're watching, jump to the next episode with one tap, and note the exact timestamp you paused at.

Everything is stored locally on the device. There is no account, server or network access.

## Features

- A list of movies and series with season, episode and timestamp
- Add and edit entries; the title is required, and the timestamp is optional (`23:45` or `1:23:45`)
- **+1 episode** on series moves the progress forward and clears the timestamp
- Soft delete: deleted entries disappear from the list but are never removed from the database
- English, Ukrainian and Polish UI

## Tech stack

Kotlin 2.2.10, Jetpack Compose with Material 3, Navigation Compose, Room with KSP, and Lifecycle ViewModel. Built with AGP 9.5.1 and Gradle 9.6.0. The app targets SDK 37 and needs Android 10 (API 29) or newer.

Architecture: MVVM with one `StateFlow<UiState>` per screen and manual DI through `AppContainer`. Details are in [AGENTS.md](AGENTS.md).

## Getting started

Requirements: a recent Android Studio or the Android SDK with JDK 17+. `local.properties` with `sdk.dir` is created by Android Studio and is not committed.

```sh
./gradlew :app:installDebug          # build and install on a connected device
./gradlew testDebugUnitTest          # unit tests
./gradlew connectedDebugAndroidTest  # instrumented tests (device unlocked)
./gradlew lintDebug                  # lint
```

To run one instrumented test class, use `-Pandroid.testInstrumentationRunnerArguments.class=<FQCN>`, not `--tests`. `connectedDebugAndroidTest` uninstalls the app when it finishes, so run `:app:installDebug` again afterwards.

## Project layout

```
app/src/main/java/com/animarkode/lida/
  data/          models, Room (local/), WatchRepository
  feature/list/  list screen and ViewModel
  feature/edit/  add and edit screen, timestamp parsing
  ui/            theme, shared components, navigation
app/schemas/     exported Room schemas
```

## Localization

Every user-facing string lives in `app/src/main/res/values/`, `values-uk/` and `values-pl/`. Add any new string to all three. To check a language on the device:

```sh
adb shell cmd locale set-app-locales com.animarkode.lida --locales uk
```

## Working with coding agents

[AGENTS.md](AGENTS.md) holds the project rules for Claude Code, Codex, Gemini and other agents. `CLAUDE.md` is a symlink to it.

- Skills live in `.agents/skills/`, with sources and licenses listed in [SOURCES.md](.agents/skills/SOURCES.md). `.claude/skills/` contains only symlinks to them.
- `/implement-issue <N> [--tier N] [--worktree]` takes a GitHub issue through planning, implementation and review. It sizes the work from the issue's `type:*` and `area:*` labels.

## Contributing

Work on a branch named `{issue-number}-{short-description}`, use test-driven development, and write commit messages in the [Conventional Commits](https://www.conventionalcommits.org/) format (`feat:`, `fix:`, `chore:`, `docs:` and so on).

## License

[Apache License 2.0](LICENSE)
