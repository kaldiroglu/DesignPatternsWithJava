*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-31*

# Tests

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The tests are JUnit, under `src/test/java`, in the same package as the code they test.
All of them are unit tests and run in one JVM.

## SlideRunsTest

`dev.kaldiroglu.dp.structural.SlideRunsTest` runs every class named by a `· run X` label in
the Structural deck part files, 60 classes, by calling its `main` in this JVM. Each must
return normally and print the lines the slide quotes. A slide that shows code and no figure
gets the line that shows the slide's claim. Timestamps are not asserted.

It is a table written by hand, so a new `· run X` label needs a new row. Nothing here can
see the labels, because the decks are in another repository. Each Structural deck's
`build_deck.py` reads this table and fails if a label has no row; it skips the check when this
repository is absent.

## Creational

`dev.kaldiroglu.dp.creational.SlideRunsTest` works the same way for the Creational decks.
`creational.introduction.ChapterFiguresTest` checks the figures and behaviors that deck states:
the field counts, the loan rules, and that the easy creation calls compile and return objects.

## Run it with

```bash
mvn -o test -Dtest=SlideRunsTest   # the runs named on the Structural slides
mvn -o test -Dtest='dev.kaldiroglu.dp.creational.**'   # the Creational tests
mvn -o test                        # everything
```
