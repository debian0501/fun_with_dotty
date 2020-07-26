
object ExplicitNulls {

    def reverse(string: String|Null ): String = 
        string match
            case s: String => s.reverse
            case _ => ""

    def upperReverse(string: String): Int|Null = 
        string.toUpperCase.length

        

    @main def test():Unit =
        val r1 = reverse(null)
        val r2 = reverse("racecar")
        //val r3 = upperReverse(null) // Found: Null Required: String

}
