# Flyweight — Problem

We're rendering a forest with millions of trees. Each tree has a position, plus visual data: a name, a color, and a (large) texture. Most trees share the same name/color/texture — there are perhaps a dozen distinct *kinds* of tree across millions of instances.

## Without the pattern

Every `Tree` object stores its own copy of the texture, color, and name. A million trees ≈ a million copies of every texture — memory blows up, even though the texture data is identical across most trees.

See `problem/`.

## With the Flyweight pattern

Split state into:

- **Intrinsic** (shared, immutable): `name`, `color`, `texture` → captured in `TreeType`. A `TreeFactory` interns one `TreeType` per distinct combination.
- **Extrinsic** (per-instance): `x`, `y` → stays on `Tree`, which holds a reference to a shared `TreeType`.

Now a million trees hold a million `(x, y, ref)` records — but only a handful of `TreeType` objects exist in memory.

See `solution/`.
