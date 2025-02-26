module SortedVect

import Data.Vect
import Decidable.Equality
import Data.Vect.Elem
import Data.String
import Utils.Hashable

%default total

data Sorted : (xs: Vect n Nat) -> Type where 
  SortedEmpty : Sorted []
  SortedOne : (x: Nat) -> Sorted [x]
  SortedMany : (x : Nat) ->
               (y : Nat) -> 
               Sorted (y :: ys) -> 
               x `LTE` y -> 
               Sorted (x :: y :: ys)

%name Sorted sp1, sp2, sp3

sortedVect12 : Sorted [1, 2]
sortedVect12 = SortedMany 1 2 (SortedOne 2) (LTESucc LTEZero)

head_not_sorted : (LTE x y -> Void) -> Sorted (x :: (y :: xs)) -> Void
head_not_sorted f (SortedMany x y sp1 z) = f z

tail_not_sorted : LTE x y -> (Sorted (y :: xs) -> Void) -> Sorted (x :: (y :: xs)) -> Void
tail_not_sorted z f (SortedMany x y sp1 w) = f sp1

public export
isSorted : (xs : Vect n Nat) -> Dec (Sorted xs)
isSorted [] = Yes SortedEmpty
isSorted (x :: []) = Yes (SortedOne x)
isSorted (x :: (y :: xs)) = case isLTE x y of
                                 (Yes prf) => case isSorted (y :: xs) of
                                                   (Yes prf_tail) => Yes (SortedMany x y prf_tail prf)
                                                   (No contra) => No (tail_not_sorted prf contra)
                                 (No contra) => No (head_not_sorted contra)

data SortedElem : (x: Nat) -> Vect size Nat -> Type where 
  Here : SortedElem x (x :: xs)
  There : (later: SortedElem x xs) -> SortedElem x (y :: xs)

has1 : SortedElem 1 [3, 2, 1]
has1 = There (There Here)

emptyVectDontHaveElement : SortedElem v [] -> Void
emptyVectDontHaveElement Here impossible
emptyVectDontHaveElement (There later) impossible

oneLengthVectDontHaveElem : (v = x -> Void) -> SortedElem v [x] -> Void
oneLengthVectDontHaveElem f Here = f Refl
oneLengthVectDontHaveElem _ (There Here) impossible
oneLengthVectDontHaveElem _ (There (There later)) impossible

notLater : LTE x y -> Sorted (y :: ys) -> (v = x -> Void) -> LTE v x -> (SortedElem v (y :: ys) -> Void) -> SortedElem v (x :: (y :: ys)) -> Void
notLater z sp1 f w g Here = f Refl
notLater z sp1 f w g (There later) = g later

greaterSoNotFound : LTE x y -> Sorted (y :: ys) -> (v = x -> Void) -> (LTE x v -> Void) -> SortedElem v (x :: (y :: ys)) -> Void
greaterSoNotFound z sp1 f g Here = f Refl
greaterSoNotFound LTEZero (SortedOne y) f g (There later) = g LTEZero
greaterSoNotFound (LTESucc z) (SortedOne (S right)) f g (There later) = ?dfkj
greaterSoNotFound z (SortedMany y j sp1 w) f g (There later) = ?greaterSoNotFound_rhs_3

hasElemInSortedVect : (v: Nat) -> (xs : Vect size Nat) -> {auto prf: Sorted xs} -> Dec (SortedElem v xs) 
hasElemInSortedVect v [] {prf = SortedEmpty} = No emptyVectDontHaveElement
hasElemInSortedVect v [x] {prf = (SortedOne x)} = case decEq v x of
                                                       (Yes Refl) => Yes Here
                                                       (No contra) => No (oneLengthVectDontHaveElem contra)
hasElemInSortedVect v (x :: (y :: ys)) {prf = (SortedMany x y sp1 z)} = 
  case decEq v x of
       (Yes Refl) => Yes Here
       (No contra) => case isLTE x v of
                           (Yes prf) => ?dc
                           (No f) => No (greaterSoNotFound z sp1 contra f)
