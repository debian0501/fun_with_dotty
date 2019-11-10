# Scala 3 Gefühl und Härte

## Top-level Definitions
In Scala 3 wird es möglich sein Typdefinitionen, Typ-Aliase, Methoden etc. auf Top-level zu definieren. D.h. benötigt keine Klassen, Traits oder Objekte mehr um solche Definitionen unterzubringen. Die in Scala 2.8 eingeführten Package Objects erfüllen einen ähnnlichen Zweck und werden somit hinfällig. Sie werden in Scala 3.0 erstmal deprecated und dann in einer Folgeversion komplett entfernt. Im Gegensatz zu den Package Objects kann es beliebig viele Quelldateien in einem Package geben, die solche Toplevel-Definitionen enthalten.

```scala
package p

val greeting = "Dear"

case class Person(firstName: String, lastName:String)

def name(p:Person) : String = 
    s"$greeting ${p.firstName} ${p.lastName}"
```

## Enumerations
Scala bietet momentan zwei Möglichkeiten um Enums zu modellieren. Entweder man verwendet dafür sealed case objects oder `scala.Enumeration`. Beides hat seine Tücken. Bei der Modellierung mit sealed case objects haben die Werte keine definierte Ordnung. Zudem fehlt die Möglichkeit ein Set mit allen Enum-Werten zu erhalten oder ein Enum über den Name zu finden. `scala.Enumeration` hingegen wurde vielfach kritisiert, da es nicht typsicher ist und nicht mit Java `enum` zusammenarbeitet.

Scala 3 kommt nun mit einem eigenen Sprach-Konstrukt zur Definition von Enums.

```scala
enum State {
    case Solid, Liquid, Gas, Plasma
}
```
Dieser Code definiert eine `sealed` class `State` mit den Wert `State.Solid`, `State.Liquid`, `State.Gas`, `State.Plasma`. 

### Enum Methoden
Jeder Enum-Wert entspricht einem eindeutigen Integer-Wert und hat somit eine definierte Ordnung. Die einem Enum-Wert zugeordnete Zahl wird mit ihrer Ordinalmethode zurückgegeben:

```scala
scala> State.Solid.ordinal
val res2: Int = 0
```

Das Companion-Object einer Enum definiert zwei Hilfsmethoden. Die Methode `valueOf` gibt einen Enum-Wert anhand des übergebenen Namens zurück. Die `values`-Methode gibt alle Werte eines Enums in einem `Array` zurück.

```scala
scala> State.values
val res0: Array[State] = Array(Solid, Liquid, Gas, Plasma)

scala> State.valueOf("Gas")
val res1: State = Gas
```

### Java interoperabilität
Um eine in Scala definierte Enumeration auch in Java verwenden zu können muss man nicht mehr tun als von `java.lang.Enum` abzuleiten:

```scala
enum State extends java.lang.Enum[State] {
    case Solid, Liquid, Gas, Plasma
}
```
Danach kann die enum wie eine Java-Enum verwenden: 
```scala
scala> State.Solid.compareTo(State.Liquid)
val res2: Int = -1
```

Das neue `enum` Sprachkonstrukt behebt die bisher bestehenden Probleme von `sealed` objects und `scala.Enumeration` und wird daher sicher häufig eingesetzt werden. 


## Extension Methods
Implicit Classes dienten bisher dazu bestehende Klassen um eine neue Funktionalität zu erweitern, ohne Vererbung oder das Decorator-Pattern verwenden zu müssen. 

Im folgenden Beispiel wird die Klasse `String` um eine Methode `toCamelCase` erweitert, welche einen gegebenen String in camel-case Schreibweise zurückgibt.

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

Ist die `implicit class` im Sichbarkeitsbereich verfügbar kann die Methode so angewendet werden als wäre Teil der Klasse `String`

```scala
object StringExtensionsDemo {
    import StringImplicits._
    val camelCaseString = "The quick brown fox jumps over the lazy dog".toCamelCase

}
```

Wie man sieht, ist die bisherige Definition von Extension Methods etwas umständlich, daher gibt es dafür
in Scala 3 eine einfachere und klarere Syntax: Man schreibt in Klammern vor dem Namen der Methode die Klasse, die um diese Methode erweitert werden soll.

Als Beispiel nehmen wir wieder unsere `toCamelCase` Methode und definieren sie z.B. innerhalb eines `object`.


```scala
object StringExtensions {
    def (str: String) toCamelCase: String = {
        str.toLowerCase.split("\\s").foldLeft("")((acc, elem) => 
            s"$acc${elem.substring(0,1).toUpperCase}${elem.substring(1)}")
    }
}

```

### Sichtbarkeitsbereich einer Extension Method
Die oben definierte Methode ist erstmal nur in dem Sichtbarkeitsbereich in dem sie definiert ist verfügbar. Um sie auch an anderer Stelle verwenden zu können muss sie durch einen import verfügbar gemacht werden.

```scala
object StringExtensionsDemo {
    import StringExtensions._

    val camelCaseString = "The quick brown fox jumps over the lazy dog".toCamelCase

}

```

Alternativ zum import man die Methode auch per Vererbung oder als eine _given_ Instanz in den Sichtbarkeitsbereich bringen. 
Unterm Strich sind Extension Methods in Scala 3 einfacher zu erstellen, da man sich den Umweg über implicit classes sparen kannn.

## Union Types
Als neuen Typ werden in Scala 3 Union Types unterstützt. Ein Union Type aus den Typen A und B ist zu einem Zeitpunkt entweder Typ A oder Typ B. Union Types stammen aus der funktionalen Programmierung. In der objektorientierten Programmierung wird das gleiche durch Polymorphismus erreicht. Im Vergleich dazu sind Union Types jedoch viel leichtgewichtiger. Beispielsweises definiert man einen Union Type aus A und B einfach mittels `A | B`

So lässt sich z.B. wie im folgenden Beispiel eine Methode `area` definieren, die einen Union Type vom Typ `Rectangel` | `Square` | `Circle` akzeptiert. In der Methode wird dann in der Regel mittels Pattern matching auf dem speziellen Typ gearbeitet.

```scala
case class Rectangle(length:Double, width:Double)
case class Square(length: Double)
case class Circle(radius: Double)

def area(shape: Rectangle | Square | Circle) :Double = 
    shape match { 
        case Square(l) => l * l
        case Rectangle(l, w) => l * w
        case Circle(r) => Math.pow(r, 2.0) * Math.PI
    }
```
Gleiches lässt sich natürlich auch durch Definition eines allgemeineren Types `Shape` und Vererbung erreichen. Deshalb stellt sich die Frage wo Union Types denn in der Praxis angewendet werden: 
Union Types sind sehr hilfreich, wenn man schnell eine Methode definieren will, die für mehrere Typen gelten soll und ein eigene Klassenhierarchie dafür zu schwerfällig ist.

```scala
def size(number:String | Int) : Int = 
    number match {
        case s:String => s.size
        case n:Int => n
    }
```
Würde man das gleiche wie im Beispiel durch eine Klassenhierarchie machen wollen müsste man einen allgemeinneren Type `StringOrInt` definieren und dann die beiden value types `String` und `Int` Wrappen um dafür eine eigene Hierarchie definieren zu können. 

Union Types sind also eine gutes Mittel um ad hoc einen allgemeinen Typen zu definieren und werden in Scala 3 sicher häufig Anwendung finden.

## Intersection Types
Das Gegenstück zu Union Types bilden Intersection Types. Ein Intersection Type aus den Typen A und B (definiert als `A & B`) ist zu einem Zeitpunkt sowohl vom Typ A als auch vom Typ B. Genau wie bei Union Typs gibt es in Scala aktuell schon ein ähnliches Konstrukt. Wir können bereits mittels `with` sogenannte Compound-Types `A with B` definieren.

Der Hauptunterschied zwischen solchen Compound-Types und Intersection-Types, ist die Kommutativität. Aus der Sicht des Typensystems ist `A with B` nicht dasselbe wie `B with A`, während `A & B` und `B & A` dasselbe sind und austauschbar verwendet werden können. Aufgrund dieses Vorteils werden Intersection-Types die Compound-Types ablösen. Das bedeutet, dass die Syntax `A with B` in Typdefinitionen in Zukunft veraltet sein wird. Das Keyword `with` wird jedoch weiterhin bei Mixins verwendet.

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
    val message = "Hello"
}

def reversUppercase(m: Capitalizable & Reversable) : String = 
    s"${m.reverse} ${m.uppercase}"

```
Im Beispiel definieren wir die Methode `reverseUppercase` mit dem Intersection-Type `Capitalizable & Reversable` und können somit die Methode `reverse` und `uppercase` aus den zusammenngesetzen Typen verwenden. 


# References
https://medium.com/@sinisalouc/whats-new-in-scala-3-28d9c11eec30
