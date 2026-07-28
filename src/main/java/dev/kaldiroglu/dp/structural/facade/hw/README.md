<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `facade.hw` — worked solutions to the homework

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Three problems, each about a different thing a facade can absorb — and one of them about a
facade that absorbs nothing. **Try them before you read them.**

| Package | What the facade owns | What the code settles |
|---|---|---|
| `checkout` | failure handling | a declined payment must release the stock already reserved |
| `reporting` | a protocol, or nothing at all | a facade is judged by what its clients must **name** |
| `transcode` | the **order** of the calls | six classes that only work in one sequence |

## 1 · `checkout`

Reserve stock, take payment, book shipping, send a receipt. One call across four subsystems,
which is the easy half.

The exercise is **partial failure**. Stock is reserved and then the payment is declined —
somebody has to put it back. If the facade does not, every caller needs to know how to undo a
reservation, which is precisely the subsystem knowledge the facade exists to remove. So the
compensation lives in the facade, and that is a real cost of the pattern worth naming.

`shippingFailureCompensatesTwice` asserts both undos happen, in order, and that no receipt
goes out.

## 2 · `reporting`

Two facades over one subsystem, producing byte-identical reports. Only one of them helped.

`LeakyReportFacade` wraps every subsystem call, which looks like progress — until you read
the signatures. Callers still build a `QueryPlan`, still hold a `ResultSetCursor`, and still
have to know that `advance()` comes before `current()`.

`onlyOneIsAFacade` reflects over both classes and fails if a subsystem type appears in any
signature. **A facade is judged by what its clients have to name, not by how many classes it
wraps.**

## 3 · `transcode`

Demux, decode audio, decode video, scale, encode, mux. Six subsystem classes, each simple.

The difficulty is that they work in exactly one order and **nothing in their interfaces says
so** — get it wrong and there is no error, just a file that is quietly wrong. That is the most
valuable thing a facade can absorb: knowledge a client could not have discovered from the
subsystem itself.

## Run it with

```bash
mvn -o test -Dtest='CheckoutFacadeTest,ReportFacadeTest,VideoConverterTest'

java -cp target/classes dev.kaldiroglu.dp.structural.facade.hw.checkout.Main
java -cp target/classes dev.kaldiroglu.dp.structural.facade.hw.reporting.Main
java -cp target/classes dev.kaldiroglu.dp.structural.facade.hw.transcode.Main
```
