package p

val greeting = "Dear"

case class Person(firstName: String, lastName:String)

def name(p:Person) : String = s"$greeting ${p.firstName} ${p.lastName}"