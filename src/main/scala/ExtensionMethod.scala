package extension

object StringExtensions: 
    extension (str: String) 
      def toCamelCase: String = 
        str.toLowerCase.split("\\s").foldLeft("")((acc, elem) => 
            s"$acc${elem.substring(0,1).toUpperCase}${elem.substring(1)}")

@main def toCamelCase(str: String) : Unit = 
    import StringExtensions.*
    println(str.toCamelCase)








