module PropertyTests

import Data.Vect 
import HashMap
import Utils.Hashable
import Hedgehog

gen_l : (l: Nat) -> Gen (Vect l Int)
gen_l a = vect a (int (linear 0 30))

public export
associativity_property : Property
associativity_property = property $ do 
  length <- forAll $ nat $ constant 0 100
  l_1 <- forAll $ gen_l length
  l_2 <- forAll $ gen_l length
  l_3 <- forAll $ gen_l length
  hm1 <- pure $ insert_all_values l_1 (emptyHashMap length)
  hm2 <- pure $ insert_all_values l_2 (emptyHashMap length)
  hm3 <- pure $ insert_all_values l_3 (emptyHashMap length)
  (hm1 <+> hm2) <+> hm3 === hm1 <+> (hm2 <+> hm3)

public export
neutrality_property : Property
neutrality_property = property $ do 
  length <- forAll $ nat $ constant 0 100 
  l_1 <- forAll $ gen_l length
  hm1 <- pure $ insert_all_values l_1 (emptyHashMap length)
  neutral <+> hm1 === hm1
  hm1 === hm1 <+> neutral

public export
commutativity_property : Property
commutativity_property = property $ do 
  length <- forAll $ nat $ constant 0 100
  l_1 <- forAll $ gen_l length
  l_2 <- forAll $ gen_l length
  hm1 <- pure $ insert_all_values l_1 (emptyHashMap length)
  hm2 <- pure $ insert_all_values l_2 (emptyHashMap length)
  hm1 <+> hm2 === hm2 <+> hm1


public export
property_tests : IO ()
property_tests = test . pure $ MkGroup "Property tests" 
  [ ( "(hm1 <+> hm2) <+> hm3 == hm1 <+> (hm2 <+> hm3)", associativity_property)
  , ("e <+> hm1 == hm1 == hm1 <+> e", neutrality_property)
  , ("hm1 <+> hm2 === hm2 <+> hm1", commutativity_property)
  ]
