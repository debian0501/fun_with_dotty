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

   
def main() = {
    println(reversUppercase(new B))
}