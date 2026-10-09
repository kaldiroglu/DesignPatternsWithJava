# Memento — a game checkpoint

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The main worked example of the Memento deck. A player has health, a position and an
inventory. The game saves a checkpoint and loads it when the player dies. The promise:
loading gives back exactly what the player had at the checkpoint.

The play used everywhere: pick up a sword and a shield, reach the bridge, save. Drop the
sword, pick up a potion, walk into the cave, take 100 damage. Load.

## Three attempts — `problem`

| Stage | Package | What it gets right | What it costs |
|---|---|---|---|
| one | `problem.setters` | Loading is correct | Public setters for every field: anyone can set health to 999 |
| two | `problem.history` | Nothing outside sees the fields | The player keeps its own save slots |
| three | `problem.copy` | The game keeps the copies; no public fields | The copy shares the inventory list with the player |

**Where stage three fails.** After loading, the player has health 100 and is at the bridge,
but carries `[shield, potion]` — what it had when it died, not `[sword, shield]`.

## The pattern — `solution`

| Class | Role | What it does |
|---|---|---|
| `Player` | Originator | `save()` makes a checkpoint; `load(Checkpoint)` takes it back |
| `Player.Checkpoint` | Memento | Private fields, no getters, a copy of the inventory |
| `Game` | Caretaker | Keeps the checkpoints and decides which one to load |
| `Main` | — | Runs the same play through stage one, stage three and the memento |

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.memento.game.solution.Main
```
