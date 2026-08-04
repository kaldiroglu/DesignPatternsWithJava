<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `composite` — the Composite pattern

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

**Source:** *Design Patterns: Elements of Reusable Object-Oriented Software* — Composite,
pp. 163–174.

Five example packages, and the one design decision that separates them.

| Package | What it is |
|---|---|
| `gof` | The book's own two examples — graphics, and equipment where the roll-up is money |
| `bom` | The realistic problem: a bicycle's bill of materials, with `problem` and `solution` side by side |
| `graphic` | Shapes on a canvas |
| `fileSystem` | Files, directories, links, and a depth-first iterator |
| `hw` | Worked solutions to the homework: org chart, expression tree, survey form |

## The decision that runs through all of them

GoF's implementation issue 4 (p. 167): does child management belong on the **Component**, or
on the Composite alone?

| | Packages | Gain | Bill |
|---|---|---|---|
| **Transparent** | `gof`, `bom`, `hw.surveyform` | Every element alike; no client tests a type | `add` on a leaf compiles, then throws |
| **Safe** | `graphic`, `fileSystem` | Adding to a leaf will not compile | Tree-building code must know it holds a composite |

GoF favor transparency and say so. They also say it is a trade. This repository contains both
answers on purpose, so the trade can be shown rather than described.

## The liability worth knowing

The pattern says **part-whole hierarchies**, and a hierarchy is a tree. The moment a child is
shared — one wheel object used twice in `bom`, one person reporting to two managers in
`hw.orgchart` — the structure is a graph, and any operation that counts nodes over-counts.
Nothing in the type system warns you. `bom` accepts this deliberately and multiplies by a
quantity held on the edge; `hw.orgchart` demonstrates what happens when you do not.

## Run it with

```bash
mvn -o test -Dtest='NaiveBomTest,RollUpTest,StructureTest,CacheInvalidationTest,ExtensibilityTest,DesignComparisonTest,GraphicsCompositeTest,EquipmentCompositeTest,GraphicCompositeTest,FileSystemCompositeTest,OrgChartTest,ExpressionTest,SurveyFormTest'

python3 tools/make_package_diagrams.py     # re-render the class diagrams
```
