module HashMap

import Data.List
import Data.Vect
import Data.Fin
import Utils.Hashable

%default total

public export
data Entry a = Empty 
             | Just a Nat 

entry_eq : Eq a => Entry a -> Entry a -> Bool
entry_eq Empty Empty = True
entry_eq Empty (Just x k) = False
entry_eq (Just x k) Empty = False
entry_eq (Just x k) (Just y j) = x == y && k == j

public export
Eq a => Eq (Entry a) where 
  (==) = entry_eq

(Hashable a) => Hashable (Entry a) where
  hash Empty = 0
  hash (Just x k) = hash x

public export
record HashMap a where
  constructor MkHashMap
  size: Nat
  slots: Vect size (Entry a)

%name Entry en
%name HashMap hm

public export
emptyHashMap : (Hashable a) => Nat -> HashMap a
emptyHashMap k = MkHashMap k (replicate k Empty)

get_idx : (Hashable a) => a -> Nat -> Nat
get_idx x Z     = Z
get_idx x (S k) = (hash x) `mod` (S k) 

get_iteration_indexes : (size: Nat) -> Nat -> List (Fin size)
get_iteration_indexes size idx = let (before, after) = splitAt idx (allFins size) in after ++ before

replaceAt : Fin len -> elem -> (list : List elem) -> List elem
replaceAt _      y []      = []
replaceAt FZ     y (x::xs) = y :: xs
replaceAt (FS k) y (x::xs) = x :: replaceAt k y xs

mutual
  public export
  insert : (Hashable a) => a -> HashMap a -> HashMap a
  insert item hm@(MkHashMap size slots) = 
    let idx = get_idx item size 
       in go (get_iteration_indexes size idx) where
          go : List (Fin size) -> HashMap a
          -- We can assert_total because if we expand hashmap there will be one more Empty slot
          go [] = assert_total $ insert item (expand hm)
          go (x :: xs) = case index x slots of
                              Empty => MkHashMap size (replaceAt x (Just item 1) slots)
                              (Just entry count) => case entry == item of
                                                      False => go xs 
                                                      True  => MkHashMap size (replaceAt x (Just item (S count)) slots)

  public export
  insert_all : (Hashable a) => Vect size (Entry a) -> HashMap a -> HashMap a
  insert_all [] hm = hm
  insert_all (Empty :: xs) hm = insert_all xs hm
  insert_all ((Just x 0) :: xs) hm = insert_all xs hm
  insert_all prev_slots@((Just x (S k)) :: xs) hm = 
    insert_all (assert_smaller prev_slots (((Just x k)) :: xs)) (insert x hm)

  public export
  insert_all_values : (Hashable a) => Vect size a -> HashMap a -> HashMap a
  insert_all_values xs hm = insert_all (map (\e => Just e 1) xs) hm

  public export
  expand : (Hashable a) => HashMap a -> HashMap a
  expand {a} hm@(MkHashMap size slots) = let new_hm = emptyHashMap (size * 2) {a=a} in 
                                         insert_all slots new_hm

public export
delete : (Hashable a) => a -> HashMap a -> Maybe (HashMap a)
delete _    (MkHashMap Z    _    ) = Nothing
delete item (MkHashMap size slots) = 
  let idx = get_idx item size in 
      go (get_iteration_indexes size idx) where
        go : List (Fin size) -> Maybe (HashMap a)
        go [] = Nothing
        go (x :: xs) = case index x slots of
                            Empty => Nothing
                            (Just entry Z) => Just $ MkHashMap size (replaceAt x Empty slots)
                            (Just entry (S count)) => if entry /= item then go xs else
                                                      if count == Z 
                                                         then Just $ MkHashMap size (replaceAt x Empty slots)
                                                         else Just $ MkHashMap size (replaceAt x (Just entry count) slots)

public export 
find : (Hashable a) => a -> HashMap a -> Maybe (Entry a)
find item hm@(MkHashMap size slots) = 
  let idx = get_idx item size in 
      go (get_iteration_indexes size idx) where 
        go : (List (Fin size)) -> Maybe (Entry a)
        go [] = Nothing
        go (x :: xs) = case index x slots of
                            Empty => Nothing
                            en@(Just entry count) => if entry == item 
                                                        then Just $ en 
                                                        else go xs

public export
filter : (Hashable a) => (a -> Bool) -> HashMap a -> HashMap a
filter f (MkHashMap size slots) = MkHashMap size (map apply_filter slots) where 
                                              apply_filter : Entry a -> Entry a
                                              apply_filter Empty = Empty
                                              apply_filter v@(Just x k) = if (f x) then v else Empty

public export
merge : (Hashable a) => HashMap a -> HashMap a -> HashMap a
merge (MkHashMap size1 slots1) (MkHashMap size2 slots2) = 
  let new_hm = emptyHashMap (size1 + size2) in 
      insert_all slots2 (insert_all slots1 new_hm)

public export
(Hashable a) => Semigroup (HashMap a) where 
  (<+>) = merge 

public export
(Hashable a) => Monoid (HashMap a) where 
  neutral = MkHashMap 0 []

public export
hashmap_equals : (Hashable a) => HashMap a -> HashMap a -> Bool
hashmap_equals hm1@(MkHashMap size1 slots1) hm2@(MkHashMap size2 slots2) = 
  cmp_each hm1 where 
       cmp_each : (HashMap a) -> Bool
       cmp_each (MkHashMap 0 []) = True
       cmp_each (MkHashMap (S len) (Empty :: xs)) = assert_total $ cmp_each (MkHashMap len xs)
       cmp_each (MkHashMap (S len) (el@(Just x k) :: xs)) = if (find x hm2) == Just el 
                                                               then assert_total $ cmp_each (MkHashMap len xs)
                                                               else False


public export
(Hashable a) => (Eq a) => Eq (HashMap a) where 
  (==) = hashmap_equals


||| WARNING IT WILL DESTROY HASHING
-- public export 
-- Functor HashMap where 
--  map f (MkHashMap size slots) = MkHashMap size (map f_entry slots)
--                                  where 
--                                    f_entry : (Entry a -> Entry b)
--                                    f_entry Empty = Empty
--                                    f_entry (Just x k) = Just (f x) k

map' : (Hashable a) => (Hashable b) => (f: a -> b) -> Vect size (Entry a) -> Vect size (Entry b)
map' f [] = []
map' f (Empty :: xs) = Empty :: (map' f xs)
map' f ((Just x k) :: xs) = (Just (f x) k) :: (map' f xs)

public export
map_hash : (Hashable a) => (Hashable b) => (f: a -> b) -> HashMap a -> HashMap b
map_hash f (MkHashMap size slots) = insert_all (map' f slots) (emptyHashMap size)

namespace Foldr
  repeat : (t -> acc -> acc) -> (acc -> acc) -> Entry t -> (acc -> acc)
  repeat f go Empty          = go
  repeat f go (Just x 0    ) = go
  repeat f go prev@(Just x (S k)) = repeat f (go . (f x)) (assert_smaller prev (Just x k))
  
  export
  foldrImpl : (t -> acc -> acc) -> acc -> (acc -> acc) -> HashMap t -> acc
  foldrImpl f e go (MkHashMap 0       []     ) = go e
  foldrImpl f e go (MkHashMap (S len) (x::xs)) = assert_total foldrImpl f e (go . (repeat f id x)) (MkHashMap len xs)

namespace Foldl
  repeat : (a -> acc -> acc) -> acc -> Entry a -> acc
  repeat f acc Empty          = acc
  repeat f acc (Just y 0    ) = acc
  repeat f acc prev@(Just y (S k)) = repeat f (f y acc) (assert_smaller prev (Just y k))

  export
  foldlImpl : (a -> acc -> acc) -> acc -> HashMap a -> acc
  foldlImpl f x (MkHashMap 0       []       ) = x
  foldlImpl f x (MkHashMap (S len) (y :: xs)) = assert_total $ foldlImpl f (repeat f x y) (MkHashMap len xs)

public export
Foldable HashMap where 
  foldr f acc hm = Foldr.foldrImpl f acc id hm
  foldr f acc hm = Foldl.foldlImpl f acc hm 

public export
Show a => Show (Entry a) where
  show Empty = "Empty"
  show (Just x k) = "(" ++ (show x) ++ ")x" ++ (show k) 

public export
Show a => Show (HashMap a) where
  show (MkHashMap size slots) = "HashMap, size: " ++ (show size) ++ "elements: " ++ (show slots)
