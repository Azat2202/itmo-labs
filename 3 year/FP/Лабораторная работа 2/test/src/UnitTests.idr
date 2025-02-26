module UnitTests 
import Control.App
import Utils.Unit
import Data.Vect
import HashMap
import Utils.Hashable

gen_empty_hm_size_0 : HashMap Int
gen_empty_hm_size_0 = MkHashMap Z []

gen_empty_hm_size_2 : HashMap Int
gen_empty_hm_size_2 = MkHashMap 2 [Empty, Empty]

public export
empty_hm_k : Test es
empty_hm_k = MkTest "creation of hashMap size k" $ do 
  assert ((emptyHashMap 2 {a=Int}) == gen_empty_hm_size_2) "should be equal"

public export 
empty_hm_Z : Test es 
empty_hm_Z = MkTest "creation of hashmap size Z" $ do 
  assert (emptyHashMap Z {a=Int} == gen_empty_hm_size_0) "should be equal"

public export 
insert_no_shrink  : Test es 
insert_no_shrink = MkTest "inserting value without shrinking" $ do 
  assert (insert 1 gen_empty_hm_size_2 == MkHashMap _ [Empty, (Just 1 1)]) "should be equal"


public export 
insert_repeat_no_shrink : Test es 
insert_repeat_no_shrink = MkTest "inserting value with repetition without shrinking" $ do 
  assert (insert 1 (insert 1 gen_empty_hm_size_2) == 
         (MkHashMap 2 [Empty, (Just 1 2)])) "should be equal"

public export 
check_hashing : Test es
check_hashing = MkTest "check hashing" $ do 
  assert (insert 6 (insert 1 (insert 2 (emptyHashMap 3))) == 
    MkHashMap _ [(Just 6 1), (Just 1 1), (Just 2 1)]) "should be equal"

public export 
insert_all_test : Test es 
insert_all_test = MkTest "insert all from vect test" $ do 
  assert ((insert_all_values [1, 2, 3, 4] (emptyHashMap 5)) == 
    MkHashMap _ [Empty, (Just 1 1), (Just 2 1), (Just 3 1), (Just 4 1)]) "should be equal"

public export
insert_shrink : Test es
insert_shrink = MkTest "insert with shrinking" $ do 
   assert ((insert_all_values [1, 2, 3, 4] (emptyHashMap 3)) == 
    MkHashMap _ [Empty, (Just 1 1), (Just 2 1), (Just 3 1), (Just 4 1), Empty]) 
    "should be equal"


gen_hm : HashMap Int 
gen_hm = insert_all_values [1, 1, 2, 3] (emptyHashMap 4)

public export 
delete_found : Test es
delete_found = MkTest "delete found repeated value" $ do 
  assert ((delete 1 gen_hm) == 
    Just (MkHashMap _ [Empty, (Just 1 1), (Just 2 1), (Just 3 1)])) "should be equal"

public export 
delete_found_not_repeated : Test es
delete_found_not_repeated = MkTest "delete found not repeated value" $ do 
  assert ((delete 2 gen_hm) == 
    Just (MkHashMap _ [Empty, (Just 1 2), Empty, (Just 3 1)])) "should be equal"

public export 
delete_not_found : Test es
delete_not_found = MkTest "delete not found value" $ do 
  assert ((delete 0 gen_hm) == Nothing) "should be equal"

public export 
find_found_at_first_idx : Test es 
find_found_at_first_idx = MkTest "find found at first hash" $ do 
  assert ((find 1 gen_hm) == (Just $ Just 1 2)) "should be equal"

public export 
find_found_at_second_idx : Test es 
find_found_at_second_idx = MkTest "find found at second hash" $ do 
  assert ((find 5 (insert_all_values [1, 2, 3, 5] (emptyHashMap 4))) == 
    (Just $ Just 5 1)) "should be equal"

public export
find_not_found : Test es 
find_not_found = MkTest "find not found" $ do 
  assert ((find 0 gen_hm) == Nothing) "should be equal"

public export 
filter_test : Test es
filter_test = MkTest "filter test" $ do 
  assert ((filter (\e => e >= 2) gen_hm) == 
    MkHashMap _ [Empty, Empty, (Just 2 1), (Just 3 1)]) "should be equal"

public export
map_with_hash_test : Test es 
map_with_hash_test = MkTest "map with hash test" $ do 
  assert ((map_hash (\e => e * 2) gen_hm) == 
    MkHashMap _ [(Just 4 1), Empty, (Just 2 2), (Just 6 1)]) "should be equal"

public export
foldr_impl_test: Test es 
foldr_impl_test = MkTest "foldr test" $ do 
  assert ((foldr (+) 0 gen_hm) == 7) "should be equal"

public export
foldl_impl_test: Test es 
foldl_impl_test = MkTest "foldl test" $ do 
  assert ((foldl (+) 0 gen_hm) == 7) "should be equal"




