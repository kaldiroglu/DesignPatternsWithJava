# Visitor — tax, shipping and receipt lines at a checkout

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The main worked example of the Visitor deck. A shop sells books, food and electronics, and
every item on the receipt must be taxed at its own rate: books 5%, food 1%, electronics
20%. Later the catalog team adds gift cards, which carry no tax and are not shipped.

## Three attempts — `problem`

| Stage | Class | What it gets right | What it costs |
|---|---|---|---|
| one | `problem.methods.Item` | Each item computes its own tax | Every new operation edits every item class |
| two | `OverloadedTax` | Tax rules in one class, items stay data | Overloads are chosen at compile time: every item is taxed at the standard rate |
| three | `TypeTestTax`, `TypeTestShipping` | Correct for every item kind it knows | A new item kind falls into the `else` branch, with no error |

**Where stage three fails.** A cart of a book for 40, food for 100 and electronics for 500
has tax 103. Stage two charges 128. Stage three charges 103, until a gift card for 100 is
added: then it charges tax 123 and shipping 60, where the correct figures are 103 and 50.

## The pattern — `solution`

| Class | Role | What it does |
|---|---|---|
| `Item` | Element | `accept(ItemVisitor<R>)` |
| `Book`, `Food`, `Electronics`, `GiftCard` | ConcreteElement | Data, and `visitor.visit(this)` |
| `ItemVisitor<R>` | Visitor | One `visit` per kind of item |
| `TaxVisitor`, `ShippingVisitor`, `ReceiptLineVisitor` | ConcreteVisitor | One operation each |
| `Checkout` | ObjectStructure | Walks the cart and sends each item the visitor |
| `Main` | — | Runs the same carts through every design |

When `GiftCard` is added, `ItemVisitor` gets `visit(GiftCard)`, and every visitor that does
not handle it stops compiling.

## The same check without the pattern — `modern`

`Item` is a `sealed` interface with four records, and `Tax.of` is a `switch` with no
`default`. A switch that misses one permitted class does not compile. This gives the same
check as the visitor's interface, without `accept` and `visit`.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.visitor.checkout.solution.Main
```
