# Template Method — exporting a report

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The main worked example of the Template Method deck. A reporting tool exports the same
report as CSV, HTML or Markdown. Every export must check the user's permission first and
write an audit record last; the auditors depend on it.

## Three attempts — `problem`

| Stage | Classes | What it gets right | What it costs |
|---|---|---|---|
| one | `StandaloneCsvExport`, `StandaloneHtmlExport` | Simple | Each format copies the whole algorithm |
| two | `SwitchingExporter`, `Format` | The algorithm is written once | Every format is a branch in this one class |
| three | `ExportSupport`, `CsvExport`, `MarkdownExport` | Shared steps, and any team can add a format | Each subclass writes the order of the steps |

**Where stage three fails.** `MarkdownExport` was written later and never calls
`recordAudit`. It compiles and makes a correct file, and its exports are missing from the
audit log. The steps were shared; the algorithm was not.

## The pattern — `solution`

| Class | Role | What it does |
|---|---|---|
| `ReportExporter` | AbstractClass | `export()` is the template method, and `final`. `header`, `row` and `extension` are abstract; `footer` is a hook. |
| `CsvExporter`, `HtmlExporter`, `MarkdownExporter` | ConcreteClass | Only the text of each format. `HtmlExporter` also uses the hook. |
| `Main` | — | Runs stage three and the solution, and prints both audit logs |

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.templateMethod.export.solution.Main
```
