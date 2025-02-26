module DictElem 

import Data.Vect
import Data.String
import Decidable.Equality
import System.File.ReadWrite

%default total 

public export
infixl 0 :>

public export
data DictEntry : a -> Type where 
  (:>) : (k : Nat) -> (v : t) -> DictEntry t 


public export
sampleDict : Vect 4 (DictEntry String)
sampleDict = [ 1 :> "one"
             , 2 :> "two"
             , 3 :> "three"
             , 4 :> "four"
             ]


public export
data DictElem : (k : Nat) -> (vType : Type) -> (xs : Vect size (DictEntry vType)) -> Type where
  Here : (v : a) -> DictElem k a ((k :> v) :: xs)
  There : (later : DictElem k a xs) -> DictElem k a (y :: xs)


elemSampleDictFirst : DictElem 1 String [ 1 :> "one", 2 :> "two", 3 :> "three", 4 :> "five"]
elemSampleDictFirst = Here "one"


elemSampleDictThird : DictElem 3 String [ 1 :> "one", 2 :> "two", 3 :> "three", 4 :> "five"]
elemSampleDictThird = There (There (Here "three"))

noEntryOfEmptyVect : DictElem k a [] -> Void
noEntryOfEmptyVect (Here v) impossible
noEntryOfEmptyVect (There later) impossible

notFoundInLater : (k = j -> Void) -> (DictElem k a xs -> Void) -> DictElem k a ((j :> v) :: xs) -> Void
notFoundInLater f g (Here v) = f Refl
notFoundInLater f g (There later) = g later

public export
isDictElem : (k : Nat) -> (xs : Vect size (DictEntry a)) -> Dec (DictElem k a xs)
isDictElem k [] = No noEntryOfEmptyVect
isDictElem k ((j :> v) :: xs) = case decEq k j of
                                     (Yes Refl) => Yes (Here v)
                                     (No contra) => case isDictElem k xs of
                                                         (Yes prf) => Yes (There prf)
                                                         (No f) => No (notFoundInLater contra f)


public export
getValue : (k : Nat) -> (xs : Vect size (DictEntry a)) -> {auto prf : DictElem k a xs} -> a
getValue k ((k :> v) :: xs) {prf = (Here v)} = v
getValue k (y :: xs) {prf = (There later)} = getValue k xs


public export 
readValue : (line : String) -> (xs : Vect size (DictEntry String)) -> String
readValue line xs = case parsePositive line {a=Nat} of  
                              Nothing => "Please write positive number!"
                              (Just k) => case isDictElem k xs of
                                               (Yes prf) => getValue k xs 
                                               (No contra) => "Element with that key dont exists!"

public export 
partial
main : IO ()
main = do file <- readFile "../test.txt"
          case file of
               (Left x) => printLn x
               (Right x) => printLn $ "Found element is: " ++ readValue (trim x) sampleDict
