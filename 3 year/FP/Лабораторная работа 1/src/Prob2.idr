module Prob2

import Data.List.Lazy

sum_of_5_pows: Int -> Int
sum_of_5_pows = go 0 where
  go: Int -> Int -> Int 
  go sum 0 = sum
  go sum i = go (sum + cast (pow (cast $ i `mod` 10) 5)) (i `div` 10) 


export
prob2_tailrec : Int
prob2_tailrec = go 0 2 where
  go: Int -> Int -> Int
  go sum i with (i > 7*9*9*9*9*9*9)
    go sum i | True = sum
    go sum i | _ with (sum_of_5_pows i == i)
      go sum i | _ | True = go (sum + i) (i + 1)
      go sum i | _ | False = go sum (i + 1)


export
prob2_rec : Int
prob2_rec = go 2 where
  go: Int -> Int
  go i with (i > 7*9*9*9*9*9*9)
    go i | True = 0
    go i | _ with (sum_of_5_pows i == i)
      go i | _ | True = i + go (i + 1)
      go i | _ | False = go (i + 1)


export
prob2_filter : Int
prob2_filter = sumlist $ filterlist $ generatelist where
  generatelist : List Int
  generatelist = 
    let limit = 7*9*9*9*9*9*9 in
    [2..limit]

  filterlist : List Int -> List Int
  filterlist is = filter (\x => sum_of_5_pows x == x) is

  sumlist : List Int -> Int
  sumlist is = foldr (+) 0 is 


export
prob2_map : Int
prob2_map = sumlist $ maplist $ generatelist where
  generatelist : List Int
  generatelist = 
    let limit = 7*9*9*9*9*9*9 in
    [2..limit]

  maplist : List Int -> List Int
  maplist is = map (\x => if sum_of_5_pows x == x then x else 0) is

  sumlist : List Int -> Int
  sumlist is = foldr (+) 0 is 

export
prob2_lazy : Int
prob2_lazy = sumlist $ filterlist $ filterTake $ generatelist 2 where
  generatelist : (start: Int) -> LazyList Int 
  generatelist start = start :: (generatelist (start + 1))

  filterlist : LazyList Int -> LazyList Int
  filterlist is = filter (\x => sum_of_5_pows x == x) is

  filterTake : LazyList Int -> LazyList Int
  filterTake x = let limit = 7*9*9*9*9*9*9 in 
                     takeWhile (<= limit) x

  sumlist : LazyList Int -> Int
  sumlist is = foldr (+) 0 is 



main : IO()
main = print prob2_lazy




