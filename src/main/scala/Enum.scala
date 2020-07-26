enum State extends java.lang.Enum[State] 
    case Solid, Liquid, Gas, Plasma


enum Emoticons(icon: String) 
    case Smile extends Emoticons(":)")
    case Angry extends Emoticons(":(")
    case Kiss extends Emoticons(":*")


enum Maybe[+A]
    case Just(a: A) extends Maybe[A]
    case Nothing extends Maybe[Nothing]