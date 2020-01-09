# Neue Features in Scala 3

Voraussichtlich im Herbst 2020 wird mit Scala 3 das nächste große Release einer Programmiersprache kommen, die sich die Verschmelzung von typisierter objektorientierter und funktionaler Programmierung auf die Fahnen geschrieben hat. Die Arbeit an Scala 3 hat bereits vor sieben Jahren im Rahmen des Projekts [Dotty](https://dotty.epfl.ch/) begonnen. Ziel von Dotty war herauszufinden, wie ein neues Scala aussehen könnte. Mittlerweile ist klar: Dotty wird das neue Scala 3. Bei der Entwicklung standen diese Hauptziele im Fokus:
* Scala mit dem [DOT-Kalkül](https://www.scala-lang.org/blog/2016/02/03/essence-of-scala.html)  auf eine neue theoretische Basis stellen.
* Den Einstieg in die Sprache erleichtern, durch Zügeln mächtiger Sprachkonstrukte, etwa Implicits.
* Die Konsistenz und Aussagekraft der Sprachkonstrukte weiter verbessern. 

Im Folgenden gebe ich einen Einblick in die neuen Features, die aus meiner Sicht für die tägliche Arbeit besonders relevant sind. Die vollständige Liste aller Features und detaillierte Informationen dazu findet man in der [Dokumentation](https://dotty.epfl.ch/docs/index.html) zu Dotty 

## Top-Level Definitionen
In Scala 3 wird es möglich sein, Typ-Definitionen, Typ-Aliase, Methoden und ähnliches auf Top-Level zu definieren. Das heißt, man benötigt keine Klassen, Traits oder Objekte mehr, um solche Definitionen unterzubringen. Die in Scala 2.8 eingeführten Package Objects erfüllen einen ähnlichen Zweck; sie werden somit hinfällig. Im Gegensatz zu den Package Objects kann es beliebig viele Quelldateien in einem Package geben, die solche Top-Level-Definitionen enthalten.

```scala
package p

val greeting = "Dear"

case class Person(firstName: String, lastName:String)

def greet(p:Person) : String = 
    s"$greeting ${p.firstName} ${p.lastName}"
```

## Enumerations
Scala bietet bisher zwei Möglichkeiten, um Enums zu modellieren, *sealed case objects* oder Erweiterungen von `scala.Enumeration`. Beides hat seine Tücken. Bei der Modellierung mit sealed case objects haben die Werte keine definierte Ordnung, und es fehlt die Möglichkeit, ein Set mit allen Enum-Werten zu erhalten oder ein Enum über den Name zu finden. `scala.Enumeration` wurde dagegen vielfach kritisiert, da es nicht typsicher ist und nicht mit Java Enums zusammenarbeitet.

Scala 3 kommt nun mit einem eigenen Sprach-Konstrukt zur Definition von Enums.

```scala
enum State {
    case Solid, Liquid, Gas, Plasma
}
```

### Enum Methoden
Jeder Enum-Wert entspricht einem eindeutigen Integer-Wert und hat somit eine definierte Ordnung. Die einem Enum-Wert zugeordnete Zahl wird mit der Methode `ordinal` zurückgegeben:

```scala
scala> State.Solid.ordinal
val res2: Int = 0
```

Das Companion-Object einer Enum definiert zwei Hilfsmethoden. Die Methode `valueOf` gibt einen Enum-Wert anhand des übergebenen Namens zurück. Die `values`-Methode gibt alle Werte eines Enums als `Array` zurück.

```scala
scala> State.values
val res0: Array[State] = Array(Solid, Liquid, Gas, Plasma)

scala> State.valueOf("Gas")
val res1: State = Gas
```

### Java Interoperabilität
Um eine in Scala definierte Enumeration auch in Java verwenden zu können, muss diese nur von `java.lang.Enum` ableiten:

```scala
enum State extends java.lang.Enum[State] {
    case Solid, Liquid, Gas, Plasma
}
```
Danach kann die Enum wie eine Java-Enum verwendet werden: 
```scala
scala> State.Solid.compareTo(State.Liquid)
val res2: Int = -1
```

## Union Types
Ein Union Type aus den Typen A und B ist zu einem definierten Zeitpunkt entweder vom Typ A oder vom Typ B. Definiert wird dieser durch die Notation `A | B`

So lässt sich wie im folgenden Beispiel eine Methode `area` schreiben, die einen Union Type vom Typ `Rectangle` | `Square` | `Circle` akzeptiert. Innerhalb der Methode wird dann in der Regel mittels *pattern matching* auf dem konkreten Typ gearbeitet.

```scala
case class Rectangle(length: Double, width: Double)
case class Square(length: Double)
case class Circle(radius: Double)

def area(shape: Rectangle | Square | Circle): Double = 
    shape match { 
        case Square(l) => l * l
        case Rectangle(l, w) => l * w
        case Circle(r) => Math.pow(r, 2.0) * Math.PI
    }
```

Union Types stammen aus der funktionalen Programmierung. In der objektorientierten Programmierung kann gleiches durch Vererbung erreicht werden. Im Vergleich dazu sind Union Types jedoch viel leichtgewichtiger. 

Folgendes Beispiel verdeutlicht das nochmal:

```scala
def size(number: String | Int) : Int = 
    number match {
        case s: String => s.size
        case n: Int => n
    }
```

Würde man das Gleiche durch Vererbung machen, müsste man eine eigene Klassen-Hierarchie erstellen: dafür zuerst ein Trait `StringOrInt` definieren und anschließend `String` und `Int` in einen eigens davon abgeleiteten Typ wrappen. Was umständlich und nicht intuitiv ist.

## Intersection Types
Das Gegenstück zu Union Types sind Intersection Types. Ein Intersection Type aus den Typen A und B (definiert als `A & B`) ist zu einem definierten Zeitpunkt sowohl vom Typ A als auch vom Typ B. Genau wie bei Union Types gibt es in Scala aktuell schon ein ähnliches Konstrukt: Wir können mittels `with` sogenannte Compound-Types `A with B` definieren.

Im Beispiel definieren wir die Methode `reverseUppercase` mit dem Intersection-Type `Capitalizable & Reversable` und können somit die Methode `reverse` und `uppercase` aus den zusammengesetzten Typen verwenden.

```scala
trait A {
    def message : String
}
trait Capitalizable extends A {
  def uppercase(s: String) : String = message.toUpperCase

}
trait Reversable extends A {
  def reverse : String = message.reverse
}

class B extends A with Reversable with Capitalizable {
    override def message = "Hello"
}

def reversUppercase(m: Capitalizable & Reversable) : String = 
    s"${m.reverse} ${m.uppercase}"

```

Der Hauptunterschied zwischen den aktuell existierenden Compound-Types und den neuen Intersection-Types ist die Kommutativität. Aus der Sicht des Typensystems ist `A with B` nicht dasselbe wie `B with A`, während `A & B` und `B & A` dasselbe sind, und damit austauschbar verwendet werden können. Aufgrund dieses Vorteils werden Intersection-Types die Compound-Types ablösen. Das bedeutet, dass die Syntax `A with B` in Typ-Definitionen zukünftig veraltet sein wird. Das Keyword `with` kann jedoch weiterhin bei Mixins verwendet werden.

## Given
Das wahrscheinlich bekannteste und auch gefürchtetste Feature von Scala sind Implicits. Implicits sind ein mächtiges Paradigma für eine Vielzahl von Anwendungsfällen, etwa die Implementierung von Typ-Klassen, Dependency-Injection, Erstellung von Kontexten und vieles mehr. Diese Vielzahl von Einsatzmöglichkeiten und die einfache Deklaration - man benötigt nur einen Modifier - bergen die Gefahr, dieses mächtige Konstrukt im falschen Kontext, zu häufig oder zu leichtsinnig einzusetzen. Scala 3 will diese Gefahr durch eine Vielzahl von Verbesserungen in den Griff bekommen. Die auffälligste Veränderung ist, dass das Keyword `implicit` durch `given` ersetzt wird, um die Intention hinter dem Konstrukt greifbarer zu machen.

Ich zeige die Verwendung von `given` hier am Beispiel einer Monoid-Typklasse. Ein Monoid kann durch folgenden Trait definiert werden:

```scala
trait Monoid[T] {
    def combine(a: T, b: T) : T
    def unit : T
}
```

### Given Instances
Als nächstes definieren wir zwei Monoide für `Int` und `String` durch die Verwendung des Keywords `given`. Hierdurch werden zwei Instanzen der Typklasse Monoid für die Datentypen `Int` und `String` erstellt. Diese können wir anschließend in Funktionen verwenden.

```scala
object Monoids {
    given sumMonoid : Monoid[Int] {
        def combine(a: Int, b: Int) : Int =
            a + b
        def unit : Int = 0
    }

    given strMonoid : Monoid[String] {
        def combine(a: String, b: String) : String =
            a + b
        def unit : String = ""
    }
}

```

In Scala 2 würde man das mit `implicit val` erreichen.

### Given Parameter
Als nächstes verwenden wir unseren Monoid in einer `reduce`-Funktion. Dafür machen wir ihn zunächst verfügbar, indem wir ihn als Funktionsparameter angeben. Durch das Keyword `given` kennzeichnen wir an dieser Stelle, dass der Compiler eine passende Instanz dieses Typs finden und einsetzen soll.

```scala
def reduce[T](x: List[T])(given m: Monoid[T]) : T = 
    x.foldLeft(m.unit)(m.combine)
```

Das entspricht einem Implicit Parameter in Scala 2. Im Vergleich entfällt allerdings die Restriktion, nur eine Implicit Parameter-Liste verwenden zu können; in Scala 3 kann man beliebig viele Listen mit givens an beliebiger Stelle einsetzen.

### Given Imports
Um die `reduce`-Funktion anzuwenden benötigen wir die definierten Monodie im Sichtbarkeitsbereich dieser Funktion. Im Vergleich zu Scala 2 ist Scala 3 hier sehr viel restriktiver und und verlangt eine neue Notation zum Import von Given Instances. Diese müssen mit einem Import der Form `import A.given` explizit verfügbar gemacht werden. 

```scala
scala> import Monoids.given

scala> reduce(List(1,2,3,4,5))
val res1: Int = 15
```

### Implicit Conversion
Implicit Conversions werden in Scala 3 durch Given Instances der Klasse `scala.Conversion` definiert. Diese müssen somit explizit durch einen Given Import importieren werden. Das mindert die Gefahr, durch Wildcard Imports versehentlich eine Typ-Konvertierungen zu aktivieren. 

## Extension Methods
Implicit Classes dienten bisher in Scala dazu, bestehende Klassen um eine neue Funktionalität zu erweitern, ohne Vererbung oder das Decorator-Pattern verwenden zu müssen. 

Im folgenden Beispiel wird die Klasse `String` um eine Methode `toCamelCase` erweitert, welche einen gegebenen String in Camel-Case Schreibweise zurückgibt.

```scala
object StringImplicits {

    implicit class AdditionalStringMethods(str: String) { 
 
      def toCamelCase = {
        str.toLowerCase.split("\\s").
          foldLeft("")((acc, elem) => 
            s"$acc${elem.substring(0,1).toUpperCase}${elem.substring(1)}")
      }
    
    }
}
```

Ist die Implicit Class im Sichtbarkeitsbereich verfügbar, kann die Methode so angewendet werden als wäre sie Teil der Klasse `String`:

```scala
object StringExtensionsDemo {
    import StringImplicits._
    val camelCaseString = "The quick brown fox jumps over the lazy dog".toCamelCase

}
```

Wie man sieht, ist die bisherige Definition von Extension Methods etwas umständlich, daher gibt es dafür
in Scala 3 eine einfachere und klarere Syntax: Man schreibt die zu erweiternde Klasse in Klammern vor dem Namen der Methode.

Als Beispiel nehmen wir wieder unsere `toCamelCase`-Methode und definieren sie zum Beispiel innerhalb eines Objekts.


```scala
object StringExtensions {
    def (str: String) toCamelCase: String = {
        str.toLowerCase.split("\\s").foldLeft("")((acc, elem) => 
            s"$acc${elem.head.toUpperCase}${elem.substring(1)}")
    }
}

```

### Sichtbarkeitsbereich einer Extension Method
Die oben definierte Methode ist zunächst nur im Sichtbarkeitsbereich verfügbar, in dem sie definiert ist. Um sie auch an anderer Stelle verwenden zu können, muss sie durch einen Import verfügbar gemacht werden.

```scala
object StringExtensionsDemo {
    import StringExtensions._

    val camelCaseString = "The quick brown fox jumps over the lazy dog".toCamelCase

}

```

Alternativ zum Import kann man die Methode auch per Vererbung oder als eine Given-Instance in den Sichtbarkeitsbereich bringen. 
Unterm Strich sind Extension Methods in Scala 3 einfacher und intuitiver zu erstellen, da man den Umweg über Implicit-Classes spart.

## Und sonst?

Das waren einige der größeren konzeptuellen Änderungen in Scala 3. Daneben sind eine Reihe kleinerer Änderungen angekündigt, die die tägliche Arbeit einfacherer machen.

### Creator Applications
in Scala 3 kann man Klassen ohne das Keyword `new` instanzieren, auch wenn sie keine _apply_-Methode haben. 

```scala
val sb = StringBuilder()
```

### Neue Control Syntax
In Scala 3 können die die Klammern um die Bedingungen in Kontroll-Ausdrücken weggelassen werden:

```scala
def abs(a: Int) : Int = {
    if a < 0 then
        -a
    else 
        a       
}

def abs(l: List[Int]) : List[Int] = {
    for x <- l
    yield abs(x)
}

```

### Trait Parameter
In Scala 3 können Traits Parameter haben, vergleichbar zu Klassen:

```scala
trait Greet(val name: String) {
    def greet() : String =
        name
}

class HelloWorld extends Greet("Hello") {
    def helloWorld() : String = 
        s"${greet()} World"
}
```

## Zusammenfassung
Scala 3 bringt große Veränderungen. Als Ziel war gesetzt: eine klarere und verständlichere Sprache zu entwickeln. Das ist sicher erreicht. Damit wird die Sprache besonders für Einsteiger attraktiver, und auch der Konkurrenzkampf mit Kotlin könnte weiter angefacht werden. 

Als erfahrene_r Scala-Entwickler_in wird man sich an die neuen Konstrukte und Syntax gewöhnen müssen. Scala 3 wirkt an manchen Stellen wie eine ganz neue Sprache. Die Änderungen sind so weitreichend das die Standardwerke zu Scala fast komplett neu geschrieben werden müssen. Es wird daher nach der Veröffentlichung noch eine Weile dauern bis die neuen Features und Konstrukte allgemein bekannt, verstanden und angewendet werden. Eine baldige Migration von bestehenden Scala 2 Code zu Scala 3 lohnt sich jedoch aus meiner Sicht auf jeden Fall. Um diese so schmerzfrei wie möglich zu gestalten, sind Werkzeuge angekündigt, die große Teile des Codes direkt portieren. Der Aufwand sollte dadurch überschaubar sein und macht sich durch klareren und einfacheren Code und weniger Fallstricke schnell bezahlt. 

Persönlich denke ich: Scala 3 ist ein guter Schritt, um eine Sprache zu rehabilitieren, die leider in den Ruf von hoher Komplexität und vielen Tücken geraten war.

