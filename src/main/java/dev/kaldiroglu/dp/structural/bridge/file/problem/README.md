<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-08-17
-->

# `bridge.file.problem` — the three things a team writes first

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Two departments keep documents under different retention rules. Three vendors store them.
This package is what the code looks like **before** anybody reaches for Bridge — three
honest designs, each a real improvement on the one before, each failing in a new way.

The answer is one package up, in `bridge.file`.

## The two axes

| What we keep — the department decides | Where it is kept — procurement decides |
|---|---|
| **Finance** — seven years of audit trail, here the last **5** versions | **Evernote** — a note in a notebook, addressed by title; opaque version GUIDs |
| **Insurance** — data minimization, no more than **2** versions | **SharePoint** — bytes at a site-relative URL; versions numbered from 1 |
| *next year: legal, with its own hold rules* | **FileNet** — a document in an object store; version series identifiers |

Neither list is derived from the other. Finance did not choose FileNet and FileNet has never
heard of a retention policy. That is the shape Bridge is for.

`VendorStores` is the fixed point: three SDKs that agree on nothing — not on what an address
is, not on what a version is, not even on what "store this" is called. Every design in this
package ends at those calls, which is the same role `notifications.domain.Transports` plays
for the notification example.

## The three designs

**1 · A switch on each axis** — `SwitchingFileManager`

One method, six branches, written by hand. Adding a store means editing every department's
branch; adding a department means editing every store's. No edit touches one axis alone.

The retention rule is not owned by anything, so it is repeated wherever somebody remembered
it. **Six branches store a version; five of them then trim.** The insurance branch for
FileNet does not, so insurance keeps every version for ever — a retention breach that throws
nothing, logs nothing, and is visible only to an auditor.

**2 · A class per pair** — `FinanceEvernoteManager`, `InsuranceEvernoteManager`,
`FinanceSharepointManager`

Three of the six are written out; the rest are the same idea. The name has to state both
axes, because the class *is* both. Compare the first two: the Evernote calls are identical
and only the retention number differs. Compare the first and third: the retention number is
identical and only the vendor calls differ.

**3 · Inherit the store** — `EvernoteStore` + `EvernoteBoundFinanceManager`

A real improvement, and worth saying so: the Evernote calls are written once instead of once
per department. But the manager now **is an** `EvernoteStore` rather than having one, so the
store was chosen when the code was compiled. When the Evernote contract ends, this object
cannot follow the documents anywhere. There is no `setStore` and there cannot be.

## Counted

Every figure below is asserted by `FileProblemTest`, which reads the source and counts rather
than doing arithmetic on literals.

| | switch | class per pair | inherit | bridge |
|---|---|---|---|---|
| 2 departments × 3 stores | 6 branches | 6 classes | 6 classes | **5 classes** |
| The retention rule is written | in 5 of 6 branches | once per pair | once per store | **once, per department** |
| A fourth store | edit all 6 branches | +2 classes | +2 classes | **+1 class** |
| Move a live manager to another store | no | no | no | **`setProvider`** |

## Run it with

```bash
mvn -o test -Dtest=FileProblemTest
java -cp target/classes dev.kaldiroglu.dp.structural.bridge.file.problem.Main
```
