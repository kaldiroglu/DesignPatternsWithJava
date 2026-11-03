*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-11-01*

# Factory Method

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The examples for the Factory Method deck. Date of this package: 2026-11-01.

**Expected benefit.** The student sees what creating objects with `new` costs, what a factory
method fixes, and what it does not fix. Each stage changes one thing.

**Functional properties.** A company hires employees, managers and directors, and pays them.
Every `Main` prints the output of one slide, and at most six lines. The same lines are printed
on every run.

**Architectural approach.** One package per stage. The GoF creator and product roles are in `gof`,
and each stage in `company.solution` is a variation of them.

| Package | What it shows | Run |
|---|---|---|
| `company.problem.stageOne` | `HR` calls `new` with no arguments, then setters; `addDirector` forgets `setDepartment` | `Main` |
| `company.problem.stageTwo` | `HR` calls a constructor on each class | `Main` |
| `company.problem.stageThree` | `HR` passes a type name to one method with a `switch`; every manager gets a team of 8 | `Main`, `TeamSizeMain` |
| `company.solution.fm1` | One `Factory` interface; each factory creates one product and has no parameters | `Main` |
| `company.solution.fm2` | `create(String name)`: one parameter for every type | `Main` |
| `company.solution.fm3` | Three interfaces, because each type needs different data | `Main` |
| `company.solution.fm4` | One interface with three methods and one implementation | `Main` |
| `company.solution.fm5` | One static method with a `switch`, from a string or from an enum | `Main` |
| `company.solution.fm6` | One static method per type | `Main` |
| `report` | A service that receives a report factory | `Main` |
| `gof.app` | GoF's `Application` and `Document`, with `MyApplication` and `MyDocument` | `Main` |
| `gof.figure` | GoF's parallel hierarchies: `Figure` and `Manipulator` | `Main` |
| `modern` | `Supplier<Employee>` with a constructor reference; a sealed interface with a pattern `switch` | `ConstructorReferenceMain`, `SealedSwitchMain` |
| `company.solution.variety1`, `variety2` | Earlier variations; no slide uses them | `Test` |

## Fixed values

- **The random values have a fixed seed.** `EmployeeRandomizer` starts from `new Random(SEED)`, and
  each `Main` injects a new generator. The tests run every `Main` in one JVM, and they would
  depend on their order without this. `variety1` and `variety2` still use an unseeded generator.
- **The report date is a parameter of the factory.** `report.Main` passes 2026-11-01.
- **A director's bonus is 5,000 in `fm1` to `fm6`.**

## What the design does not solve

- `HR` in `fm1` has one field and one `addX` method per type, so a new type still changes `HR`.
- Each new product class needs one new factory class.
- `fm4` has one implementation of its interface, so nothing can be swapped.
- An enum `switch` expression and a sealed `switch` are both checked by the compiler. The sealed
  version adds one thing: each record holds the data its kind needs.

Each package with classes has its diagram in `uml/`. Run `python3 tools/make_creational_diagrams.py` after any change; it applies the one style in `tools/creational-uml-style.iuml`.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o test -Dtest='dev.kaldiroglu.dp.creational.**'
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm1.Main
java -cp target/classes dev.kaldiroglu.dp.creational.factoryMethod.modern.SealedSwitchMain
```
