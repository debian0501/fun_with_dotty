package Monoids

trait Monoid[T]:
    def combine(a: T, b: T) : T
    def unit : T

given sumMonoid: Monoid[Int] with
    def combine(a: Int, b: Int) : Int =
        a + b
    def unit : Int = 0

given strMonoid: Monoid[String] with
    def combine(a: String, b: String) : String =
        a + b
    def unit : String = ""

def reduce[T](x: List[T])(using m: Monoid[T]) : T = 
    x.foldLeft(m.unit)(m.combine)


def mapViaFold[A, B] (l: List[A])(f: A => B) : List[B] =
    l.foldLeft(Nil:List[B])(_:+f(_))

def filterViaFold[A](l: List[A])(f: A => Boolean) : List[A] = 
    l.foldLeft(Nil:List[A])((b, a) => if f(a) then b:+a else b)


@main def testMap() =
    println(mapViaFold(List(1,2,3))(_ + 1))
    println(filterViaFold(List(1,2,3,4,5))(x => x < 3))



