case class Rectangle(length:Double, width:Double)
case class Square(length: Double)
case class Circle(radius: Double)

def area(s: Rectangle | Square | Circle) :Double = 
    s match { 
        case Square(l) => l * l
        case Rectangle(l, w) => l * w
        case Circle(r) => Math.pow(r, 2.0) * Math.PI
    }

def size(number:String | Int): Int = 
    number match {
        case s: String => s.size
        case n: Int => n
    }

val numberSize  = size(1234)   //1234
val stringSize  = size("1234") //4


    