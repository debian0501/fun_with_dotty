
object OpaqueType:

    opaque type Nat = Int

    object Nat: 
        def apply(d: Int): Nat = 
            if(d >= 0) then d 
                else throw Exception("Nat must be positiv")
    

@main def testOpaque() = 
    import OpaqueType._
    val n = Nat(1)
