<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `bridge.file` — departments and document stores

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Two departments keep documents under different retention rules. Three vendors store them.
Neither list has anything to do with the other, which is the shape Bridge is for.

## Two things this package is careful about

**1. Nothing here is called an adapter.** The classes were once `FileProviderAdaptor`,
`EvernoteAdaptor` and so on, and in a course that also teaches Adapter that naming does real
damage. An adapter makes an **existing, incompatible** interface fit one you already have,
after the fact. `FileProvider` was designed up front, alongside `FileManager`, so the two
hierarchies could vary independently. That is Bridge, and a test asserts no type in this
package has "adapt" in its name.

**2. The provider does not mirror the manager.** The old interface was
`readFile`/`writeFile`/`updateFile` — the same interface written twice, where a new manager
operation would force every vendor to grow. The provider now offers storage primitives:

```java
String open(String path);
byte[] read(String handle);
int    write(String handle, byte[] content);
List<Integer> versions(String handle);
void   deleteVersion(String handle, int version);
```

and the managers compose them. `FileManager.save` writes a version and then applies the
department's retention rule — a rule written once and correct on every store, present and
future. Finance keeps five versions; insurance keeps two.

## Counted

| | |
|---|---|
| 2 departments × 3 stores | **5 classes**, 6 combinations |
| A fourth store | **+1 class**, no retention rule touched |
| A third department | **+1 class**, no store touched |
| Move a manager to another store at run time | `manager.setProvider(other)` |

The vendors are simulated in memory so the example runs anywhere and a test can assert what
was stored — the same reason `notifications.domain.Transports` exists.

## Known gap

There is no `problem` package here, so this example shows the answer without the pain that
motivates it. Teach it after `bridge.shape` or `bridge.notifications`, both of which carry
their naive counterpart.

## Run it with

```bash
mvn -o test -Dtest=FileBridgeTest
java -cp target/classes dev.kaldiroglu.dp.structural.bridge.file.Main
```
