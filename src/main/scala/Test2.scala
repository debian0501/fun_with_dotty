def (str: String) toCamelCase: String = {
    str.toLowerCase.split("\\s").foldLeft("")((acc, elem) => 
        s"$acc${elem.substring(0,1).toUpperCase}${elem.substring(1)}")
}

val t = "test".toCamelCase