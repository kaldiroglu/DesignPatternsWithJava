# Visitor — GoF's compiler example

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Design Patterns, pp. 331–344. A compiler keeps a program as a tree of nodes, and runs
several jobs over it: type checking, code generation and pretty-printing.

The program used here:

```
y = 2
x = y + 1
z = w + x
```

## Before the pattern — `problem`

Every node class (`AssignmentNode`, `VariableRefNode`, `ConstantNode`, `AddNode`) has
`typeCheck`, `generateCode` and `prettyPrint`. Each job is spread over four classes.

## After it — `solution`

| Class | GoF participant | What it does |
|---|---|---|
| `NodeVisitor` | Visitor | `visitAssignment`, `visitVariableRef`, `visitConstant`, `visitAdd` |
| `TypeCheckingVisitor` | ConcreteVisitor | Reports `w is used before it is assigned` |
| `CodeGeneratingVisitor` | ConcreteVisitor | Stack-machine code: `PUSH`, `LOAD`, `ADD`, `STORE` |
| `PrettyPrintingVisitor` | ConcreteVisitor | Prints the program back |
| `Node` and the four records | Element, ConcreteElement | `accept` only |

The visitors walk the tree themselves, by calling `accept` on the children.

The older package `visitor.pattern.problem` holds the first sketch of the same nodes.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.visitor.gof.Main
```
