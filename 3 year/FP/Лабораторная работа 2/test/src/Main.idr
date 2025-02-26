module Main

import System
import Control.App
import Utils.Unit
import UnitTests
import PropertyTests
import DictElemUnitTests
import Data.Vect

unit_tests : List (Utils.Unit.Test es)
unit_tests = [ empty_hm_k
        , empty_hm_Z
        , insert_no_shrink
        , insert_repeat_no_shrink
        , insert_all_test
        , insert_shrink
        , check_hashing
        , delete_found 
        , delete_found_not_repeated
        , delete_not_found
        , find_found_at_first_idx 
        , find_found_at_second_idx
        , find_not_found
        , filter_test
        , map_with_hash_test
        , foldr_impl_test
        , foldl_impl_test
        , numberIsNotValid
        , numberFoundAtFirstPos
        , numberFound 
        , numberNotFound
        ]


main : IO ()
main = runTests unit_tests *> property_tests


