
val greeting = "Dear"

case class Person(firstName: String, lastName:String)

def name(p:Person) : String = s"$greeting ${p.firstName} ${p.lastName}"

trait Greet(val name: String) {
    def greet() : String =
        name
}

class HelloWorld extends Greet("Hello") {
    def helloWorld() : String = 
        s"${greet()} World"
}