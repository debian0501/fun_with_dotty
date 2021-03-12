
object ExplicitNulls {

    def reverse(string: String|Null ): String = 
        string match
            case s: String => s.reverse
            case null => ""

    def upperReverse(string: String): String = 
        val res: String|Null = string.toUpperCase
        if res != null then res.reverse else ""

        
    @main def testExplicitNull():Unit =
        val r1 = reverse(null)
        val r2 = reverse("racecar")
        val r3 = upperReverse("test") // Found: Null Required: String
        println(r3)
}
