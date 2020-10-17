
object Algorithm:

    def calc(a: Int, b: Int) : Int =
        var res = a * b
        var c = a
        while c <= b do 
            res * b
            c = c + 1
        res
    
    @main def testCalc(a: Int, b: Int) =
        val result = calc(a, b)

        if result > 1000 then 
            println("result is greater than 1000")
        else
            println("result is smaller than 1000")