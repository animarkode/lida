# Skill routing — `area:*` → project skills

Read when composing a task brief. Pick from the rows the task actually touches, then **verify every name against the currently available skills list**. Never invent a skill name; write `None` when nothing applies. A brief that lists a skill the session cannot load wastes the implementer's first tool call.

| Label | Skills |
|---|---|
| `area:list`, `area:edit` | `compose-state-and-effects`, `kotlin-concurrency-and-flow`, `compose-ui-testing-patterns` |
| `area:ui` | `material-3`, `compose-component-design`, `compose-animations`, `edge-to-edge` |
| `area:data` | `kotlin-concurrency-and-flow`, `kotlin-api-design` |
| `area:i18n` | `None` — the rule "string in three `values*` dirs" lives in `AGENTS.md` |
| `area:build` | `gradle-run` |
| `area:tests` | `testing-setup`, `compose-ui-testing-patterns` |

## Cross-cutting

- Any feature or fix → `superpowers:test-driven-development`, ahead of the domain skills.
- Performance or jank → `jetpack-compose-performance`, `compose-performance`.
- Any task that runs Gradle → `gradle-run`.
- A task that only edits markdown, config, or CI → `None`.

Keep the list short. Three skills an implementer actually reads beat six it skims.
