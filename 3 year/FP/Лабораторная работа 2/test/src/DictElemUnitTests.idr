module DictElemUnitTests 

import DictElem
import Control.App
import Utils.Unit 
import Data.Vect

%default total

public export
testDict : Vect 4 (DictEntry String)
testDict = [ 1 :> "one"
           , 2 :> "two"
           , 3 :> "three"
           , 4 :> "four"
           ]


public export 
numberIsNotValid : Test es
numberIsNotValid = MkTest "providing non valid number" $ do 
  assert ((readValue "foo" testDict) == "Please write positive number!") "should be equal"

public export 
numberFoundAtFirstPos : Test es
numberFoundAtFirstPos = MkTest "finding number at first position" $ do 
  assert ((readValue "1" testDict) == "one") "should be equal"

public export 
numberFound : Test es
numberFound = MkTest "finding any number" $ do 
  assert ((readValue "3" testDict) == "three") "should be equal"

public export 
numberNotFound : Test es
numberNotFound = MkTest "number is not found" $ do 
  assert ((readValue "5" testDict) == "Element with that key dont exists!") "should be equal"


