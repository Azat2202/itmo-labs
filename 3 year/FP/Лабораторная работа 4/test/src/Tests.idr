module Tests
import Formatter
import Parser
import Utils
import Data.List
import System

partial
tests : List (String, String)
tests = [
    ((format "_%d_"      1),         "_1_"),
    ((format "_%f_"      2),         "_2.0_"),
    ((format "_%f.2_"    1.445),     "_1.45_"),
    ((format "_%f.2_"    1.495),     "_1.5_"),
    ((format "_%d_%f.2_" 1 1.495),     "_1_1.5_"),
    ((format "_%d%d_"    2 3), "_23_"),
    ((format "_%d%f%d_"  2 3.1 4), "_23.14_"),
    ((format "_%d%%_"    2), "_2%_"),
    ((format "%s, %s!"    "Hello" "world"), "Hello, world!")
]

partial
main : IO ()
main = do let run = map (\(a, b) => a == b) tests
          let failed = findIndices (not) run
          putStrLn (if (not $ isNil failed) 
                then "Failed tests: " ++ show failed
                else "All tests succeeded!")
          if (not $ isNil failed) then exitFailure else exitSuccess
