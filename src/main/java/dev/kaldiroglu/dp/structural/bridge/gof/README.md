<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `bridge.gof` — the windowing example from the book

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

**Source:** *Design Patterns: Elements of Reusable Object-Oriented Software* — Bridge,
pp. 151–161.

A `Window` abstraction that has to run on more than one window system. Implemented twice —
once with the platform as a **superclass** (`problem`), once with the platform behind an
**implementor** (`solution`) — and a comparison test proves the two draw identical windows.

## Layout

```
gof/
├── uml/                       Bridge - Pattern Structure - Class Diagram
│   ├── problem/               Bridge - Window (Problem) - Class Diagram
│   └── solution/              Bridge - Window (Solution) - Class Diagram
│                              Bridge - Window (Solution) - Object Diagram
└── window/
    ├── Canvas.java            a character grid, shared by both designs
    ├── Display.java           side-by-side printing for the demos
    ├── problem/               Window + XWindow, PMWindow, IconWindow, TransientWindow,
    │                          the four leaf classes their product requires, and Main
    └── solution/              Window (Abstraction), IconWindow, TransientWindow
                               (RefinedAbstractions), WindowImp (Implementor),
                               XWindowImp, PMWindowImp (ConcreteImplementors), and Main
```

## What the two designs look like when run

Both draw onto a character grid, so the platform difference is visible rather than asserted.
The same `IconWindow` class, over two implementors:

```
      X Window System                Presentation Manager

+----------------------+     #======================#
| +-+                  |     ! #=#                  !
| +-+ readme.txt       |     ! #=# readme.txt       !
+----------------------+     #======================#
```

## The two designs, counted

| | `problem` | `solution` |
|---|---|---|
| 3 window kinds × 2 platforms | **6 classes** | **5 classes** |
| A third platform | +3 classes | **+1 class** |
| A fourth window kind | +2 classes | **+1 class** |
| Change platform on a live object | impossible | `window.setImp(pm)` |
| The X drawing code appears | **3 times** | once |

## The detail worth stopping on

`PMWindowImp.deviceRect` does not draw a rectangle — Presentation Manager has no such call,
so it builds one from a polyline, exactly as GoF describe on p. 157. The implementor records
what it was asked to do, and `abstractionUsesPrimitivesOnly` asserts that an `IconWindow`
produces **seven** PM calls and **three** X calls for the same drawing. The window never
learns why.

## What the tests establish — 14 tests

| Test | Count | Point |
|---|---|---|
| `WindowProblemTest` | 5 | The subclass design works — and the platform *is* the class, the X code exists three times, and kinds multiply platforms |
| `WindowSolutionTest` | 6 | One kind over every platform; one platform under every kind; the implementation swaps at run time; the abstraction speaks only in primitives; a new platform costs one class |
| `DesignComparisonTest` | 3 | Both designs draw identical windows; 6 classes against 5, and the gap widens |

## Run it with

```bash
mvn -o test -Dtest='WindowProblemTest,WindowSolutionTest,DesignComparisonTest'

java -cp target/classes dev.kaldiroglu.dp.structural.bridge.gof.window.solution.Main
java -cp target/classes dev.kaldiroglu.dp.structural.bridge.gof.window.problem.Main

find uml -name "*.puml" -exec plantuml -tpng {} \;
```
