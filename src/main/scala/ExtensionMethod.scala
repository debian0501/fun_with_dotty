package extension

object StringExtensions {
    def (str: String) toCamelCase: String = {
        str.toLowerCase.split("\\s").foldLeft("")((acc, elem) => 
            s"$acc${elem.substring(0,1).toUpperCase}${elem.substring(1)}")
    }
}

object StringExtensionsDemo {
    import StringExtensions._

    val camelCaseString = "The quick brown fox jumps over the lazy dog".toCamelCase

}







