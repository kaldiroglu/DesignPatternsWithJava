#!/usr/bin/env python3
"""
Regenerates the package class diagrams for the structural patterns.

Each diagram is written into its own package's `uml/` folder as .puml, then rendered to
SVG and PNG. Editing a diagram means editing this file and re-running it, so the pictures
and the notes on them stay in one place rather than drifting apart across 13 folders.

Naming follows whatever each pattern's existing diagrams already use, so a folder looks
internally consistent:

  bridge/*     "Bridge - <Topic> (<Part>) - Class Diagram.puml"
  decorator/*  "decorator-<topic>-class-diagram.puml"

The diagrams under bridge/gof, bridge/notifications, decorator/gof and decorator/middleware
are NOT generated here — they came with those examples and are maintained by hand.

Requires PlantUML on the PATH:  brew install plantuml

Run it with:  python3 tools/make_package_diagrams.py
"""

import subprocess
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parent.parent
SRC = REPO / "src/main/java/dev/kaldiroglu/dp/structural"

STYLE = "skinparam classAttributeIconSize 0\nhide empty members\n"

# (package path, file stem, title, body)
DIAGRAMS = []


def add(pkg, stem, title, body):
    DIAGRAMS.append((pkg, stem, title, body))


# ───────────────────────────────────────────────────────────── decorator / io
add("decorator/io", "decorator-io-class-diagram",
    "Decorator — java.io, and where the roles sit", """
abstract class OutputStream <<Component>>
class FileOutputStream <<ConcreteComponent>>
abstract class FilterOutputStream <<Decorator>> {
  # out : OutputStream
}
class BufferedOutputStream <<ConcreteDecorator>>
class DataOutputStream <<ConcreteDecorator>>
class PrintStream <<ConcreteDecorator>>
class GZIPOutputStream <<ConcreteDecorator>>
class ObjectOutputStream

OutputStream <|-- FileOutputStream
OutputStream <|-- FilterOutputStream
OutputStream <|-- ObjectOutputStream
FilterOutputStream o--> "1" OutputStream : out
FilterOutputStream <|-- BufferedOutputStream
FilterOutputStream <|-- DataOutputStream
FilterOutputStream <|-- PrintStream
FilterOutputStream <|-- GZIPOutputStream

class DataInputOutputStreamDemo {
  + {static} writeInvoice(out : DataOutputStream)
  + {static} readInvoice(in : DataInputStream) : double
}
DataInputOutputStreamDemo ..> DataOutputStream : uses

note right of ObjectOutputStream
  <b>Not a decorator.</b>
  It extends OutputStream directly,
  so it is a component in its own
  right. A test asserts this, so the
  slide cannot drift from the library.
end note

note bottom of DataInputOutputStreamDemo
  writeInvoice() is written once and run over
  two different stacks:
    File -> Buffered -> Data          274 bytes
    File -> GZIP -> Buffered -> Data  186 bytes
  Same answer, different work.
end note
""")

# ──────────────────────────────────────────── decorator / hw / invoicepipeline
add("decorator/hw/invoicepipeline", "decorator-hw-invoicepipeline-class-diagram",
    "Decorator — homework 1, the invoice pipeline", """
interface Pipeline <<Component>> {
  + process(input : byte[]) : byte[]
  + undo(input : byte[]) : byte[]
}

class PlainInvoice <<ConcreteComponent>>

abstract class PipelineStage <<Decorator>> {
  # inner : Pipeline
  # {abstract} forward(bytes : byte[]) : byte[]
  # {abstract} backward(bytes : byte[]) : byte[]
}

class Compressed <<ConcreteDecorator>>
class Encrypted <<ConcreteDecorator>> {
  - key : long
}
class WithHeader <<ConcreteDecorator>> {
  - header : byte[]
}
class Checksummed <<ConcreteDecorator>>

Pipeline <|.. PlainInvoice
Pipeline <|.. PipelineStage
PipelineStage o--> "1" Pipeline : inner
PipelineStage <|-- Compressed
PipelineStage <|-- Encrypted
PipelineStage <|-- WithHeader
PipelineStage <|-- Checksummed

note right of PipelineStage
  process() = forward(inner.process(x))
  undo()    = inner.undo(backward(x))

  The asymmetry is deliberate: on the way
  out a stage runs <b>after</b> the inner one,
  on the way back <b>before</b> it. That makes
  the read chain the mirror of the write
  chain structurally, not by convention.
end note

note bottom of Checksummed
  Belongs <b>outermost</b>: a checksum is worth
  having only over the bytes that travel.
end note

note bottom of Encrypted
  Encrypted bytes do not compress.
  compress-then-encrypt  225 bytes
  encrypt-then-compress 2033 bytes
end note
""")

# ─────────────────────────────────────────────── decorator / hw / feeengine
add("decorator/hw/feeengine", "decorator-hw-feeengine-class-diagram",
    "Decorator — homework 2, the fee engine", """
interface Charge <<Component>> {
  + amount() : BigDecimal
}

class BasketTotal <<ConcreteComponent>>

abstract class Adjustment <<Decorator>> {
  # component : Charge
  # {abstract} adjust(base : BigDecimal) : BigDecimal
}

class PlatformFee <<ConcreteDecorator>>
class TransactionFee <<ConcreteDecorator>>
class ValueAddedTax <<ConcreteDecorator>>
class PromotionalDiscount <<ConcreteDecorator>>
class Voucher <<ConcreteDecorator>>

Charge <|.. BasketTotal
Charge <|.. Adjustment
Adjustment o--> "1" Charge : component
Adjustment <|-- PlatformFee
Adjustment <|-- TransactionFee
Adjustment <|-- ValueAddedTax
Adjustment <|-- PromotionalDiscount
Adjustment <|-- Voucher

note right of ValueAddedTax
  A discount reduces the consideration
  the customer pays, so VAT goes
  <b>outside</b> the discount.
  Tax law, not taste.
end note

note bottom of Voucher
  <b>Subtraction</b>, which does not commute
  with multiplying:
    VAT outside  120.60   (lawful)
    VAT inside   122.60
  The 2.00 gap is the VAT on the voucher.
end note

note bottom of PromotionalDiscount
  <b>Multiplication</b>, which does commute.
  Both orders give 119.34, so here the
  order genuinely does not matter.
end note
""")

# ───────────────────────────────────────────────── decorator / hw / powerups
add("decorator/hw/powerups", "decorator-hw-powerups-class-diagram",
    "Decorator — homework 3, the power-up", """
interface Combatant <<Component>> {
  + damage() : int
}

class Fighter <<ConcreteComponent>>

abstract class PowerUp <<Decorator>> {
  # component : Combatant
}

class DoubleDamage <<ConcreteDecorator>>
class Poison <<ConcreteDecorator>>
class Berserk <<ConcreteDecorator>>

class EffectStack {
  - base : Combatant
  - active : List<Effect>
  + grant(e : Effect)
  + revoke(name : String) : boolean
  + advance(ticks : int)
  + chain() : Combatant
}

class Effect <<record>> {
  + name : String
  + expiresAtTick : int
  + apply : UnaryOperator<Combatant>
}

Combatant <|.. Fighter
Combatant <|.. PowerUp
Combatant <|.. EffectStack
PowerUp o--> "1" Combatant : component
PowerUp <|-- DoubleDamage
PowerUp <|-- Poison
PowerUp <|-- Berserk
EffectStack o--> "*" Effect : active
Effect ..> PowerUp : builds

note right of EffectStack
  <b>You cannot remove a link from a chain.</b>
  Every decorator holds the one beneath it and
  nothing holds the one above, so this class
  keeps the <b>recipe</b> for each effect and
  rebuilds the chain whenever the list changes.

  Attaching is simple. Detaching costs you
  this class.
end note

note bottom of Berserk
  Forwards <b>zero</b> times, which GoF allow —
  and silently discards every effect below it.
end note
""")

# ────────────────────────────────────────────────────── bridge / basic
add("bridge/basic/problem", "Bridge - Basic (Problem) - Class Diagram",
    "Bridge — the implementation as a subclass", """
interface AnAbstraction {
  + doIt() : void
}

class ASubAbstraction
class AnotherSubAbstraction
class AConcreteImplementation1
class AConcreteImplementation2
class AnotherConcreteImplementation1
class AnotherConcreteImplementation2
class Client {
  - anAbstraction : AnAbstraction
  + start() : void
}

AnAbstraction <|.. ASubAbstraction
AnAbstraction <|.. AnotherSubAbstraction
ASubAbstraction <|-- AConcreteImplementation1
ASubAbstraction <|-- AConcreteImplementation2
AnotherSubAbstraction <|-- AnotherConcreteImplementation1
AnotherSubAbstraction <|-- AnotherConcreteImplementation2
Client o--> "1" AnAbstraction

note bottom of AConcreteImplementation1
  Read the <b>extends</b> as the claim it makes:
  this implementation <b>is a</b> refinement.
  Change the implementation and you have
  changed the object's type — which is why
  nothing here can switch once it exists.
end note

note as N1
  2 refinements x 2 implementations
  = <b>4 leaf classes</b>, plus the 2
  refinements themselves = <b>6</b>.

  A third implementation costs <b>+2</b>.
end note
""")

add("bridge/basic/pattern", "Bridge - Basic (Pattern) - Class Diagram",
    "Bridge — the implementation behind a reference", """
interface AnAbstraction <<Abstraction>> {
  + doIt() : void
}

class ASubAbstraction <<RefinedAbstraction>> {
  - implementation : AnAbstractionImplementation
}
class AnotherSubAbstraction <<RefinedAbstraction>> {
  - implementation : AnAbstractionImplementation
}

interface AnAbstractionImplementation <<Implementor>> {
  + doingIt() : void
}

class AConcreteImplementation1 <<ConcreteImplementor>>
class AConcreteImplementation2 <<ConcreteImplementor>>

class Client {
  - anAbstraction : AnAbstraction
  + start() : void
}

AnAbstraction <|.. ASubAbstraction
AnAbstraction <|.. AnotherSubAbstraction
ASubAbstraction o--> "1" AnAbstractionImplementation
AnotherSubAbstraction o--> "1" AnAbstractionImplementation
AnAbstractionImplementation <|.. AConcreteImplementation1
AnAbstractionImplementation <|.. AConcreteImplementation2
Client o--> "1" AnAbstraction

note right of AnAbstractionImplementation
  The bridge: a <b>reference</b>, not a
  superclass. Both hierarchies can be
  extended without touching the other.
end note

note as N1
  2 refinements + 2 implementations
  = <b>4 classes</b>, 4 combinations.

  A third implementation costs <b>+1</b>.
end note
""")

# ────────────────────────────────────────────────────── bridge / shape
add("bridge/shape/problem", "Bridge - Shape (Problem) - Class Diagram",
    "Bridge — the device as a superclass", """
interface Shape {
  + draw() : void
  + erase() : void
}

abstract class AbstractShape {
  - name : String
}

abstract class Circle
abstract class Rectangle
abstract class Triangle

class CircleMacOS
class CircleXWindows
class RectangleMacOS
class RectangleXWindows
class TriangleMacOS
class TriangleXWindows

Shape <|.. AbstractShape
AbstractShape <|-- Circle
AbstractShape <|-- Rectangle
AbstractShape <|-- Triangle
Circle <|-- CircleMacOS
Circle <|-- CircleXWindows
Rectangle <|-- RectangleMacOS
Rectangle <|-- RectangleXWindows
Triangle <|-- TriangleMacOS
Triangle <|-- TriangleXWindows

note as N1
  <b>The device is the class.</b>
  An object cannot change its class, so a
  CircleMacOS is a MacOS circle for life —
  there is no setDrawer to write.

  3 shapes x 2 devices = <b>6 leaf classes</b>,
  plus 4 abstract ones.
  A third device: <b>+3</b>. A fourth shape: <b>+2</b>.
end note
""")

add("bridge/shape/pattern", "Bridge - Shape (Pattern) - Class Diagram",
    "Bridge — primitives on one side, composition on the other", """
interface Shape <<Abstraction>> {
  + draw() : void
  + erase() : void
  + setDrawer(d : ShapeDrawer) : void
}

abstract class AbstractShape {
  - name : String
  # drawer : ShapeDrawer
}

class Circle <<RefinedAbstraction>>
class Rectangle <<RefinedAbstraction>>
class Triangle <<RefinedAbstraction>>

interface ShapeDrawer <<Implementor>> {
  + drawLine(x1, y1, x2, y2) : void
  + drawArc(cx, cy, r, start, sweep) : void
  + clear(x, y, w, h) : void
}

abstract class AbstractShapeDrawer {
  - name : String
  - calls : List<String>
  + calls() : List<String>
}

class MacOSDrawer <<ConcreteImplementor>>
class XWindowsDrawer <<ConcreteImplementor>>

Shape <|.. AbstractShape
AbstractShape <|-- Circle
AbstractShape <|-- Rectangle
AbstractShape <|-- Triangle
AbstractShape o--> "1" ShapeDrawer : drawer
ShapeDrawer <|.. AbstractShapeDrawer
AbstractShapeDrawer <|-- MacOSDrawer
AbstractShapeDrawer <|-- XWindowsDrawer

note right of ShapeDrawer
  <b>Not one method per shape.</b>
  drawCircle() here would force every
  drawer to grow when a shape is added,
  and the two hierarchies would stop
  being independent — the only thing
  Bridge exists to buy.

  GoF p. 154: "the Implementor interface
  provides only primitive operations, and
  Abstraction defines higher-level
  operations based on these primitives."
end note

note bottom of XWindowsDrawer
  Has <b>no arc call</b>, so drawArc builds one
  from 16 line segments — GoF's Presentation
  Manager detail (p. 157).
  The same Circle: 1 call on MacOS, 16 here.
end note

note bottom of Triangle
  Added <b>after</b> both drawers were written.
  Neither drawer changed.
end note
""")

# ────────────────────────────────────────────────────── bridge / file
add("bridge/file", "Bridge - File (Solution) - Class Diagram",
    "Bridge — departments and document stores", """
abstract class FileManager <<Abstraction>> {
  # provider : FileProvider
  + read(path) : String
  + save(path, content) : int
  + versions(path) : List<Integer>
  + setProvider(p : FileProvider) : void
  # {abstract} applyRetention(handle) : void
}

class FinanceFileManager <<RefinedAbstraction>> {
  - KEEP = 5
}
class InsuranceFileManager <<RefinedAbstraction>> {
  - KEEP = 2
}

interface FileProvider <<Implementor>> {
  + open(path) : String
  + read(handle) : byte[]
  + write(handle, content) : int
  + versions(handle) : List<Integer>
  + deleteVersion(handle, version) : void
}

abstract class InMemoryProvider
class EvernoteProvider <<ConcreteImplementor>>
class SharepointProvider <<ConcreteImplementor>>
class FileNetProvider <<ConcreteImplementor>>

FileManager <|-- FinanceFileManager
FileManager <|-- InsuranceFileManager
FileManager o--> "1" FileProvider : provider
FileProvider <|.. InMemoryProvider
InMemoryProvider <|-- EvernoteProvider
InMemoryProvider <|-- SharepointProvider
InMemoryProvider <|-- FileNetProvider

note right of FileProvider
  Storage <b>primitives</b>, not the manager's
  operations. readFile/writeFile/updateFile
  would be the same interface written twice.

  And nothing here is an <b>Adapter</b>: this was
  designed alongside FileManager, not fitted
  to an incompatible interface after the fact.
end note

note bottom of FileManager
  save() writes a version and then applies the
  department's retention rule — written once,
  correct on every store, present and future.
end note

note as N1
  2 departments + 3 stores = <b>5 classes</b>,
  6 combinations.
  A fourth store: <b>+1</b>, no rule touched.
end note
""")

# ────────────────────────────────────────────────────── bridge / violation
add("bridge/violation", "Bridge - Violation - Class Diagram",
    "Why the implementation does not belong in a subclass", """
class AType {
  # anIntVariable : int
  # aBoolVariable : boolean
  + doIt() : void
}

class ASubType {
  - aStringVariable : String
  + doIt() : void
  + writeIt() : void
}

AType <|-- ASubType

note right of AType
  <b>The contract:</b> calling doIt() prints.
  Every caller holding an AType is
  entitled to rely on that.
end note

note bottom of ASubType
  <b>The violation.</b> doIt() stores instead of
  printing, so a caller holding an AType gets
  silence — no exception, no log line, no way
  to tell except by testing the type.

  A breach of the <b>Liskov Substitution
  Principle</b>, and it compiles perfectly.
end note

note as N1
  <b>This is not a Bridge.</b> It is the argument
  for one.

  Subclassing was used to change <b>how</b>
  something is done, which is what an
  implementation is for — and overriding to do
  that can break a promise the supertype made.

  Delegation cannot: a refinement in
  bridge.basic.pattern runs its own body and
  <b>then</b> calls implementation.doingIt(), so its
  own contract survives whichever
  implementation it holds.
end note
""")

# ───────────────────────────────────────────── bridge / hw / statementrun
add("bridge/hw/statementrun", "Bridge - Statement Run (Solution) - Class Diagram",
    "Bridge — homework 1, three documents over three media", """
abstract class Document <<Abstraction>> {
  # medium : Medium
  + render() : String
  # {abstract} body() : void
}

class Invoice <<RefinedAbstraction>>
class AccountStatement <<RefinedAbstraction>>
class DunningLetter <<RefinedAbstraction>>

interface Medium <<Implementor>> {
  + heading(level, text) : void
  + field(label, value) : void
  + row(cells...) : void
  + total(label, amount) : void
  + output() : String
}

class HtmlMedium <<ConcreteImplementor>>
class PlainTextMedium <<ConcreteImplementor>>
class SpokenMedium <<ConcreteImplementor>>

Document <|-- Invoice
Document <|-- AccountStatement
Document <|-- DunningLetter
Document o--> "1" Medium : medium
Medium <|.. HtmlMedium
Medium <|.. PlainTextMedium
Medium <|.. SpokenMedium

note right of Medium
  Every primitive describes <b>meaning</b>,
  not ink. drawBox, setFont, newPage and
  margin are all questions about <b>paper</b>,
  and a voice can answer none of them.
end note

note bottom of SpokenMedium
  The medium that decided the interface.
  A test asserts its output contains no
  &lt; , no = and no newline.
end note

note as N1
  3 documents + 3 media = <b>6 classes</b>, not 9.
end note
""")

# ───────────────────────────────────────────── bridge / hw / paymentdesk
add("bridge/hw/paymentdesk", "Bridge - Payment Desk (Solution) - Class Diagram",
    "Bridge — homework 2, payment kinds over providers", """
abstract class Payment <<Abstraction>> {
  # provider : PaymentProvider
  + {abstract} collect(amount) : List<Receipt>
}

class OneOffPayment <<RefinedAbstraction>>
class InstallmentPlan <<RefinedAbstraction>> {
  - installments : int
}
class Refund <<RefinedAbstraction>>

interface PaymentProvider <<Implementor>> {
  + name() : String
  + authorize(amount) : Authorization
  + capture(auth) : Receipt
  + refund(amount, reference) : Receipt
}

class BankGateway <<ConcreteImplementor>>
class Wallet <<ConcreteImplementor>>
class CashDrawer <<ConcreteImplementor>>

class Authorization <<record>> {
  + reference : String
  + amount : BigDecimal
  + settled : boolean
}
class Receipt <<record>>

Payment <|-- OneOffPayment
Payment <|-- InstallmentPlan
Payment <|-- Refund
Payment o--> "1" PaymentProvider : provider
PaymentProvider <|.. BankGateway
PaymentProvider <|.. Wallet
PaymentProvider <|.. CashDrawer
PaymentProvider ..> Authorization
PaymentProvider ..> Receipt

note bottom of CashDrawer
  <b>Cash cannot authorize then capture.</b>
  authorize() takes the money and returns an
  Authorization already marked settled;
  capture() then has nothing to do.

  Rejected alternatives: a supportsTwoPhase()
  boolean, or splitting the interface — both
  are "which implementation are you?" in
  disguise, and every branch on one is a piece
  of the abstraction that knows about providers.
end note

note as N1
  The cost of the choice, stated: there is no
  <b>void</b> primitive, because only two of the
  three providers could implement it.
end note
""")

# ───────────────────────────────────────────── bridge / hw / routeplanner
add("bridge/hw/routeplanner", "Bridge - Route Planner (Solution) - Class Diagram",
    "Bridge — homework 3, route kinds over map providers", """
abstract class RoutePlanner <<Abstraction>> {
  # maps : MapProvider
  + plan(from, to, hubs) : Route
  # {abstract} score(route) : long
}

class FastestRoute <<RefinedAbstraction>>
class CheapestRoute <<RefinedAbstraction>>
class StepFreeRoute <<RefinedAbstraction>>

interface MapProvider <<Implementor>> {
  + name() : String
  + travelSeconds(from, to) : int
  + tollMinor(from, to) : int
  + stepFree(from, to) : boolean
}

class InHouseMaps <<ConcreteImplementor>>
class VendorMaps <<ConcreteImplementor>>

class Route <<record>> {
  + stops : List<String>
  + seconds : int
  + tollMinor : int
  + stepFree : boolean
}

RoutePlanner <|-- FastestRoute
RoutePlanner <|-- CheapestRoute
RoutePlanner <|-- StepFreeRoute
RoutePlanner o--> "1" MapProvider : maps
RoutePlanner ..> Route
MapProvider <|.. InHouseMaps
MapProvider <|.. VendorMaps

note right of MapProvider
  Measurements of one leg. No routing
  decision, and <b>no vendor name</b>.
end note

note bottom of VendorMaps
  Surveyed Uskudar &gt; Levent and found steps,
  so StepFreeRoute picks a different journey.
  Different answer, identical routing code.
end note

note as N1
  A test reflects over every abstraction class
  and fails if a concrete provider type appears
  in any field or constructor — the closest you
  can get to asserting "the diff was empty".
end note
""")

# ─────────────────────────────────────────────────────────── composite / gof
add("composite/gof/graphics", "Composite - Graphics - Class Diagram",
    "Composite — GoF's graphics example", """
abstract class Graphic <<Component>> {
  + draw() : void
  + add(g : Graphic) : void
  + remove(g : Graphic) : void
  + getChild(i : int) : Graphic
}

class Line <<Leaf>>
class Rectangle <<Leaf>>
class Text <<Leaf>>
class Picture <<Composite>> {
  - children : List<Graphic>
}

Graphic <|-- Line
Graphic <|-- Rectangle
Graphic <|-- Text
Graphic <|-- Picture
Picture o--> "*" Graphic : children

note right of Graphic
  Child management is declared <b>here</b>,
  on the Component — GoF's own choice,
  and the <b>transparent</b> one. Every
  element looks alike to a client.

  The price: add() on a Line has to fail
  at run time, because it compiles.
end note

note bottom of Picture
  draw() draws itself, then asks each
  child to draw. That is the pattern.
end note
"""); 

add("composite/gof/equipment", "Composite - Equipment - Class Diagram",
    "Composite — GoF's equipment example, where the roll-up is money", """
abstract class Equipment <<Component>> {
  + name() : String
  + power() : int
  + netPrice() : Currency
  + discountPrice() : Currency
}

class FloppyDisk <<Leaf>>
class Card <<Leaf>>
abstract class CompositeEquipment <<Composite>> {
  - parts : List<Equipment>
}
class Chassis
class Cabinet
class Bus

Equipment <|-- FloppyDisk
Equipment <|-- Card
Equipment <|-- CompositeEquipment
CompositeEquipment o--> "*" Equipment : parts
CompositeEquipment <|-- Chassis
CompositeEquipment <|-- Cabinet
CompositeEquipment <|-- Bus

note right of CompositeEquipment
  netPrice() adds up the parts.
  A cabinet holding a chassis holding
  cards answers one number, and no
  client writes the loop.
end note
"""); 

# ───────────────────────────────────────────────────────── composite / graphic
add("composite/graphic", "Composite - Graphic - Class Diagram",
    "Composite — shapes on a canvas, the safe variant", """
interface Graphic <<Component>> {
  + draw() : void
  + erase() : void
  + paint() : void
  + shapeCount() : int
}

abstract class GraphicObject {
  # name : String
  # color : String
}

class Circle <<Leaf>>
class Ellipse <<Leaf>>
class Triangle <<Leaf>>
class Rectangle <<Leaf>>

interface CompositeGraphic {
  + addGraphic(g : Graphic) : void
  + removeGraphic(g : Graphic) : void
  + getGraphics() : Collection<Graphic>
  + listGraphic() : void
}

class Canvas <<Composite>>

Graphic <|.. GraphicObject
GraphicObject <|-- Circle
GraphicObject <|-- Ellipse
GraphicObject <|-- Triangle
GraphicObject <|-- Rectangle
GraphicObject <|-- Canvas
CompositeGraphic <|.. Canvas
Canvas o--> "*" Graphic : elements

note right of CompositeGraphic
  Child management is <b>not</b> on the
  Component — the <b>safe</b> side of GoF's
  implementation issue 1.

  A Circle cannot be given children,
  because the method does not exist
  on it. The compiler says so.
end note

note bottom of Canvas
  The price of safety: a client that
  <b>builds</b> a tree must hold a
  CompositeGraphic, not a Graphic.
  Compare composite.hw.surveyform,
  which chose the other side.
end note

note as N1
  shapeCount() is the payoff. A leaf
  answers 1, a canvas sums its children,
  and the client asks one object.
end note
"""); 

# ────────────────────────────────────────────────────── composite / fileSystem
add("composite/fileSystem", "Composite - File System - Class Diagram",
    "Composite — a file system, with a recursive iterator", """
interface Storage <<Component>> {
  + getName() : String
  + rename(name) : void
  + save() : void
  + delete() : void
  + copy() : Storage
  + move(target : Directory) : void
  + size() : long
  + render(indent) : String
}

abstract class StorageElement {
  - name : String
  - parent : Directory
  # attach() : void
  + path() : String
}

class File <<Leaf>> {
  - bytes : long
}
abstract class Link <<Leaf>> {
  - target : Storage
}
class Alias
class ShortCut

interface StorageContainer {
  + add(e : Storage) : void
  + remove(e : Storage) : void
  + elements() : List<Storage>
  + iterator() : StorageIterator
}

class Directory <<Composite>>

interface StorageIterator
class DirectoryIterator

Storage <|.. StorageElement
StorageElement <|-- File
StorageElement <|-- Link
Link <|-- Alias
Link <|-- ShortCut
StorageElement <|-- Directory
StorageContainer <|.. Directory
Directory o--> "*" Storage : elements
Directory ..> DirectoryIterator : creates
StorageIterator <|.. DirectoryIterator

note right of Directory
  size() is the payoff: one call,
  any depth, no loop at the call site.

  render() used to ask isDirectory()
  and branch — a type test in the one
  pattern that exists to remove them.
  Each element now renders itself.
end note

note bottom of Link
  A link is its own 64 bytes, <b>not</b> the
  size of its target. Any other answer
  makes size() on a root double-count.
end note

note bottom of DirectoryIterator
  Depth-first over the whole subtree.
  The first version returned the
  immediate children only, so nested
  directories were never entered.
end note
"""); 

# ──────────────────────────────────────────────────── composite / hw / orgchart
add("composite/hw/orgchart", "Composite - Org Chart (Homework) - Class Diagram",
    "Composite — homework 1, the org chart", """
interface Employee <<Component>> {
  + getName() : String
  + totalCost() : long
  + headcount() : int
  + render(indent) : String
}

class IndividualContributor <<Leaf>> {
  - role : String
  - salary : long
}

class Manager <<Composite>> {
  - salary : long
  - reports : List<Employee>
  + add(e : Employee) : Manager
  + distinctHeadcount() : int
}

Employee <|.. IndividualContributor
Employee <|.. Manager
Manager o--> "*" Employee : reports

note right of Manager
  A manager counts <b>their own</b> salary
  as well as their reports'. Otherwise
  the cost of the company excludes the
  chief executive.
end note

note as N1
  <b>The trap.</b> One person reporting to two
  managers makes the structure a graph,
  not a tree — and every roll-up over it
  silently over-counts:

    headcount()          7
    distinctHeadcount()  6

  Nothing throws. Nothing warns.
  Composite assumes a tree.
end note
"""); 

# ────────────────────────────────────────────────── composite / hw / expression
add("composite/hw/expression", "Composite - Expression (Homework) - Class Diagram",
    "Composite — homework 2, where the tree is the data", """
interface Expression <<Component>> {
  + evaluate() : double
  + toText() : String
  + nodeCount() : int
}

class Number <<Leaf>> {
  - value : double
}

abstract class BinaryOperation <<Composite>> {
  # left : Expression
  # right : Expression
  # symbol() : String
}

class Add
class Subtract
class Multiply
class Divide

Expression <|.. Number
Expression <|.. BinaryOperation
BinaryOperation o--> "1" Expression : left
BinaryOperation o--> "1" Expression : right
BinaryOperation <|-- Add
BinaryOperation <|-- Subtract
BinaryOperation <|-- Multiply
BinaryOperation <|-- Divide

note right of BinaryOperation
  <b>Exactly two children</b>, not a list.
  A Composite holds <b>components</b> — it
  does not have to hold an unbounded
  collection of them.
end note

note bottom of Divide
  A node that cannot answer throws
  rather than inventing a number.
  A wrong total is worse than none.
end note

note as N1
  A leaf and an operation are one type to
  the client, so (3 + 4) * (10 - 2) / 4
  can be replaced by 56 / 4 and nothing
  above it notices: 14.0 either way,
  9 nodes against 3.
end note
"""); 

# ───────────────────────────────────────────────── composite / hw / surveyform
add("composite/hw/surveyform", "Composite - Survey Form (Homework) - Class Diagram",
    "Composite — homework 3, the transparent variant", """
interface FormElement <<Component>> {
  + getTitle() : String
  + validate() : List<String>
  + questionCount() : int
  + answeredCount() : int
  + render(indent) : String
  + add(e : FormElement) : void
}

class Question <<Leaf>> {
  - required : boolean
  - answer : String
  + answer(a : String) : Question
}

class Section <<Composite>> {
  - children : List<FormElement>
  + with(elements...) : Section
}

FormElement <|.. Question
FormElement <|.. Section
Section o--> "*" FormElement : children

note right of FormElement
  add() is declared <b>here</b> — the
  <b>transparent</b> side of GoF's
  implementation issue 1.

  Every element looks alike, and no
  client ever tests a type.
end note

note bottom of Question
  The bill: add() on a Question must
  throw, and the call that does it
  <b>compiles without complaint</b>.

  composite.graphic and
  composite.fileSystem chose safety
  instead. Compare the three.
end note

note as N1
  validate() <b>concatenates</b> rather than
  sums — the shape most real composite
  operations take, and slightly harder
  than adding numbers up.
end note
"""); 

# ─────────────────────────────────────────────────────────────── proxy / pm
add("proxy/pm", "Proxy - Prime Minister - Class Diagram",
    "Proxy — three stages, and only the third is the pattern", """
package "pm1 — no proxy" {
  class PM1 as "PM" {
    + listen(problem)
    + findJob(name)
    - worthMyTime(problem) : boolean
  }
  class Citizen1 as "Citizen"
  Citizen1 --> PM1
}

package "pm2 — a stand-in, but no shared type" {
  class PM2 as "PM"
  class Proxy2 as "Proxy" {
    + listen(problem)
    - worthHisTime(problem)
  }
  class Citizen2 as "Citizen"
  Citizen2 --> Proxy2
  Proxy2 --> PM2
}

package "pm3 — the pattern" {
  interface PM3 as "PM" <<Subject>> {
    + listen(problem)
    + findJob(name)
  }
  class RealPM <<RealSubject>>
  class ProxyPM <<Proxy>>
  class PMSecretary
  class Citizen3 as "Citizen"

  PM3 <|.. RealPM
  PM3 <|.. ProxyPM
  ProxyPM o--> "1" PM3
  PMSecretary ..> ProxyPM : creates
  Citizen3 --> PM3
}

note bottom of PM1
  The screening rule lives <b>inside</b>
  the class it exists to protect.
end note

note bottom of Citizen2
  The field had to change from PM
  to Proxy. <b>Every client was edited</b>
  to accept the stand-in — which is
  what a proxy should make unnecessary.
end note

note bottom of Citizen3
  The field is a <b>PM</b> again, exactly as
  in stage 1. Screening happens and this
  class does not know it does.
end note
""")

# ──────────────────────────────────────────────────────────── proxy / network
add("proxy/network", "Proxy - Network - Class Diagram",
    "Proxy — a protection proxy with no people in it", """
interface Network <<Subject>> {
  + telnet(ip, targetIp)
  + ftp(ip, targetIp)
}

class Gateway <<RealSubject>> {
  + ftpCalls() : int
  + telnetCalls() : int
}

class ProxyServer <<Proxy>> {
  - gateway : Network
  - filter(protocol, ip, targetIp)
}

class NetworkServer
class Logger
class ForbiddenAccessException

Network <|.. Gateway
Network <|.. ProxyServer
ProxyServer o--> "1" Network : gateway
ProxyServer ..> Logger : logs every request
ProxyServer ..> ForbiddenAccessException : refuses with
NetworkServer ..> ProxyServer : hands out

note right of ProxyServer
  Logs, then checks, then forwards.
  A refused request <b>never reaches</b>
  the gateway.
end note

note bottom of Gateway
  Contains <b>no access rule</b>. Called
  directly, it happily does the thing
  the proxy would have refused — which
  is how the tests prove where the rule
  lives.
end note

note bottom of ForbiddenAccessException
  Renamed from YasakKardesimException;
  this repository keeps identifiers
  and output in English.
end note
""")

# ───────────────────────────────────────────────────────── proxy / hw / pricing
add("proxy/hw/pricing", "Proxy - Pricing (Homework) - Class Diagram",
    "Proxy — homework 1, the caching proxy", """
interface PriceService <<Subject>> {
  + priceOf(sku) : Money
}

class SupplierPriceService <<RealSubject>> {
  + calls() : int
}

class CachingPriceProxy <<Proxy>> {
  - cache : Map<String, Entry>
  - ttlMillis : long
  + hits() : int
  + misses() : int
  + invalidate(sku)
}

interface Clock
class ManualClock

PriceService <|.. SupplierPriceService
PriceService <|.. CachingPriceProxy
CachingPriceProxy o--> "1" PriceService : supplier
CachingPriceProxy o--> "1" Clock
Clock <|.. ManualClock

note right of CachingPriceProxy
  Five identical requests cost
  <b>one</b> supplier call.

  A decorator forwards and adds.
  This forwards <b>sometimes</b>, and the
  benefit is in the times it does not.
end note

note bottom of Clock
  Injected, so expiry is tested
  without any test sleeping.
end note

note as N1
  The easy half is the saving.
  The hard half is <b>invalidation</b>: for up
  to one TTL this returns a price that is
  wrong, and confidently. No pattern fixes
  that — somebody decides how stale the
  business can afford to be.
end note
""")

# ────────────────────────────────────────────────────────── proxy / hw / remote
add("proxy/hw/remote", "Proxy - Remote (Homework) - Class Diagram",
    "Proxy — homework 2, the remote proxy and what it hides", """
interface InventoryService <<Subject>> {
  + stockOf(sku) : int
}

class WarehouseInventory <<RealSubject>>

class RemoteInventoryProxy <<Proxy>> {
  - attempts : int
}

class Link {
  - latencyMillis : long
  + roundTrips() : int
  + elapsedMillis() : long
  + failNext(n) : Link
}

class RemoteCallFailedException

InventoryService <|.. WarehouseInventory
InventoryService <|.. RemoteInventoryProxy
RemoteInventoryProxy o--> "1" InventoryService : warehouse
RemoteInventoryProxy o--> "1" Link
RemoteInventoryProxy ..> RemoteCallFailedException

note right of InventoryService
  Says nothing about latency, failure
  or timeouts. <b>That silence is the
  exercise.</b>
end note

note bottom of Link
  Failures are <b>scripted</b>, latency is
  counted rather than slept — so every
  run and every test agrees.
end note

note as N1
  A six-line basket becomes <b>6 round trips
  and 720 ms</b>. The proxy did its job
  perfectly — it made a remote object look
  local — and that is exactly how the
  mistake gets made.
end note
""")

# ─────────────────────────────────────────────────────────── proxy / hw / vault
add("proxy/hw/vault", "Proxy - Vault (Homework) - Class Diagram",
    "Proxy — homework 3, the smart reference", """
interface Document <<Subject>> {
  + read() : String
  + write(text)
  + close()
}

class StoredDocument <<RealSubject>> {
  + opensPerformed() : int
}

class DocumentHandle <<Proxy>> {
  - holders : int
  - writeLockOwner : String
  + acquire() : DocumentHandle
  + lockForWriting(owner)
  + unlock(owner)
}

Document <|.. StoredDocument
Document <|.. DocumentHandle
DocumentHandle o--> "0..1" StoredDocument

note right of DocumentHandle
  GoF's three smart-reference duties,
  all present:

  <b>counting</b> — closed only when the
  last holder lets go
  <b>loading on first access</b> — a virtual
  proxy hiding inside this one
  <b>locking</b> — a second writer is refused
end note

note bottom of StoredDocument
  Never opened until somebody actually
  reads. Two holders, two closes,
  <b>one</b> real close.
end note

note as N1
  close() here does not mean "close it".
  It means <b>"I am finished with it"</b>.
  Get that wrong and you either leak the
  handle or shut it under somebody's feet.
end note
""")


def main() -> int:
    if not SRC.is_dir():
        print(f"cannot find the source tree at {SRC}")
        return 1

    written = []
    for pkg, stem, title, body in DIAGRAMS:
        folder = SRC / pkg / "uml"
        folder.mkdir(parents=True, exist_ok=True)
        path = folder / f"{stem}.puml"
        path.write_text(
            f'@startuml "{stem}"\n{STYLE}title {title}\n\n{body}\n@enduml\n',
            encoding="utf-8")
        written.append(path)

    print(f"wrote {len(written)} .puml files")

    failed = 0
    for fmt in ("svg", "png"):
        result = subprocess.run(["plantuml", f"-t{fmt}", *[str(p) for p in written]],
                                capture_output=True, text=True)
        if result.returncode != 0:
            print(f"FAILED rendering {fmt}:\n{result.stdout}\n{result.stderr}")
            failed = 1
        elif result.stderr.strip():
            print(f"[{fmt}] {result.stderr.strip()[:400]}")
        else:
            print(f"rendered {fmt}: {len(written)} files")
    return failed


if __name__ == "__main__":
    sys.exit(main())
