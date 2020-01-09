trait Monoid[T] {
    def combine(a: T, b: T) : T
    def unit : T
}

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

def reduce[T](x: List[T])(given m: Monoid[T]) : T = 
    x.foldLeft(m.unit)(m.combine)








