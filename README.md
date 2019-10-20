# Scala 3 Gefühl und Härte

## Top-level Definitions
In Scala 3 wird es möglich sein Typdefinitionen, Typ-Aliase, Methoden, implicit Classes, etc. auf Top-level zu definieren. Man benötigt somit keine umgebende Klassen, Traits oder Objekte mehr um solche Definitionen unterzubringen. Die in Scala 2.8 eingeführten Package Objects erfüllen einen ähnnlichen Zweck und werden somit hinfällig. Sie werden in Scala 3.0 erstmal deprecated und dann in einer Folgeversion komplett entfernt. Im Gegensatz zu den Package Objects kann es beliebig viele Quelldateien in einem Package geben, die solche Toplevel-Definitionen enthalten.

```scala
package p

val greeting = "Dear"

case class Person(firstName: String, lastName:String)

def name(p:Person) : String = 
    s"$greeting ${p.firstName} ${p.lastName}"
```

## Enumerations
Scala bietet im Moment zwei Möglichkeiten um Enums zu Modelieren. Entweder man verwendet dafür sealed case objects oder eine Erweiterung von `scala.Enumeration`. Beides ist nicht optimal. Bei der Modelierung mit Sealed case objects haben die Werte keine definierte Ordnung. Zudem fehlt eine API um ein Set mit allen Enum-Werten zu erhalten oder ein Enum über den Name zu finden. `scala.Enumeration` hingegen wurde vielfach kritisiert, da es nicht typsicher ist und nicht mit Java `enum` zusammenarbeitet.

Scala 3 bietet nun ein eigenes Sprach-Konstrukt zur Definition von Enums

```scala
enum State {
    case Solid, Liquid, Gas, Plasma
}
```
Dieser Code definiert eine `sealed` class `State` mit den Wert `State.Solid`, `State.Liquid`, `State.Gas`, `State.Plasma`. 

### Enum Methoden
Jeder Enum-Wert entspricht einem eindeutigen Integer-Wert. Die einem Enum-Wert zugeordnete Zahl wird mit ihrer Ordinalmethode zurückgegeben:

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
Um eine in Scala definierte Enumeration auch in Java verwenden zu können muss man `java.lang.Enum` erweitern:

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