<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `decorator.io` — the pattern as the Java library itself uses it

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

The GoF name `java.io` as a known use, and it is the clearest one a Java developer already
has on disk.

| GoF role | `java.io` |
|---|---|
| Component | `InputStream`, `OutputStream` |
| ConcreteComponent | `FileInputStream`, `FileOutputStream` |
| Decorator | `FilterInputStream`, `FilterOutputStream` |
| ConcreteDecorator | `DataInputStream`, `BufferedInputStream`, `PrintStream` |

**A caution worth keeping:** not every wrapper in the package is a decorator.
`ObjectOutputStream` extends `OutputStream` directly, **not** `FilterOutputStream`, so it
is a component in its own right. A test asserts this, so a slide cannot drift from the
library.

## What the demo shows

`writeInvoice` and `readInvoice` are written once and run over two **different** stacks of
decorators. Neither knows which stack it is in, and neither was recompiled:

```
plain    total: $646.82  file: 274 bytes
gzipped  total: $646.82  file: 186 bytes

Compression saved 88 bytes, and writeInvoice() never learned about it.
```

Same answer, different work. That gap is the benefit of the pattern, as a number.

Two points the code makes explicitly:

- **Closing the outermost decorator closes everything beneath it.** `close()` is forwarded
  down the chain, so the `FileOutputStream` at the bottom is released although nobody kept
  a reference to it.
- **Every layer added on the way out needs its counterpart on the way in, in reverse
  order.** That is the one real obligation the pattern places on the caller.

## Run it with

```bash
mvn -o test -Dtest=DataInputOutputStreamDemoTest        # 3 tests
java -cp target/classes dev.kaldiroglu.dp.structural.decorator.io.DataInputOutputStreamDemo
```
