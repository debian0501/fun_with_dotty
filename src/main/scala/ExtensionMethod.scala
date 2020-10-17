package extension

object StringExtensions: 
    def (str: String) toCamelCase: String = 
       str.toLowerCase.split("\\s").foldLeft("")((acc, elem) => 
            s"$acc${elem.substring(0,1).toUpperCase}${elem.substring(1)}")

@main def toCamelCase(str: String) : Unit = 
    import StringExtensions._
    println(str.toCamelCase)








