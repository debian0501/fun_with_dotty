trait UpperCaseable 
  def uppercase(s: String) : String =  s.toUpperCase

trait Reversable 
  def reverse(s: String) : String = s.reverse
    
def convert(in: String, c: UpperCaseable & Reversable) : String = 
    c.uppercase(c.reverse(in))

class Converter 
  extends UpperCaseable with Reversable

@main def testIntersection() = 
    println(convert("racecar", Converter()))