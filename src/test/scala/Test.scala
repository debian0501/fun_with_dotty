import intent.{Stateless, TestSuite}



class ToEqualTest extends TestSuite with Stateless with
  "toEqual" :
    "for Boolean" :
     "true should equal true" in expect(true).toEqual(true)
      "true should *not* equal false" in expect(true).not.toEqual(false)

