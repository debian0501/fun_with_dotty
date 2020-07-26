
def main(args: Array[String]): Unit = println(s"Hello World")

@main def hello(): Unit = println(s"Hello World")

@main def sayHello(name: String, age: Int) = 
    println(s"Hello $name, you are $age years old")


@main def monoidTest(): Unit = 
    import Monoids.{given _, _}
    println(reduce(List(1,2,3,4)))
    println(reduce(List("Hello", " World")))