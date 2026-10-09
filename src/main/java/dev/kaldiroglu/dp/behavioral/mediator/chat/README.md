# Mediator — a team chat and its private messages

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The main worked example of the Mediator deck. A team chat has messages to everyone and
private messages. The promise: a private message is seen only by the person it is sent to.

## Three attempts — `problem`

| Stage | Package | What it gets right | What it costs |
|---|---|---|---|
| one | `problem.direct` | Messages go straight to their receiver | Every member holds every other: 4 members, 12 references |
| two | `problem.directory` | One shared list; a new member is added once | Every sender applies the rules itself |
| three | `problem.bus` | No client knows another | The bus delivers everything to everyone; each client decides what to show |

**Where stage three fails.** Elif sends Mert a private message. The bus delivers it to
Burak, Mert and Can. The team's own clients hide it unless it is for them, but the guest
client, written later by another team, shows it: Can reads Elif's private message to Mert.

## The pattern — `solution`

| Class | Role | What it does |
|---|---|---|
| `ChatRoom` | Mediator | Decides who receives each message: everyone, one person, nobody who blocked the sender |
| `Participant` | Colleague | `name()` and `receive(from, text)` |
| `Member`, `Guest` | ConcreteColleague | Talk only to the room |
| `Main` | — | Runs stage one, stage three and the room |

In the room, the same private message is delivered to Mert only. The guest checks nothing,
and still shows nothing it should not, because it receives only what the room sends.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.mediator.chat.solution.Main
```
