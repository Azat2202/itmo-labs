module Prob1

import Data.List.Lazy

sum_of_k : Int -> Int
sum_of_k n = (n * (n + 1)) `div` 2

prob1 : Int -> Int
prob1 k = 3 * sum_of_k (k `div` 3)
          + 5 * sum_of_k (k `div` 5)
          - 15 * sum_of_k (k `div` 15)


prob1_cond : Int -> Bool
prob1_cond i = (i `mod` 3 == 0) || (i `mod` 5 == 0)

export
prob1_rec : Int -> Int
prob1_rec 0 = 0
prob1_rec i = 
  if prob1_cond i then
      i + prob1_rec (i - 1)
  else 
      prob1_rec (i - 1)


export
prob1_tail_rec : Int -> Int
prob1_tail_rec i = go 0 i where
  go: Int -> Int -> Int
  go sum 0 = sum
  go sum i = if prob1_cond i 
                then go (sum + i) (i - 1) 
                else go sum (i - 1)


export
prob1_filter : Int -> Int
prob1_filter i = sumlist $ filterlist $ generatelist i where
  generatelist : Int -> List Int
  generatelist i = [0,1..i]

  filterlist : List Int -> List Int
  filterlist xs = filter prob1_cond xs

  sumlist : List Int -> Int
  sumlist xs = foldr (+) 0 xs

export
prob1_map : Int -> Int
prob1_map i = sumlist $ maplist $ generatelist i where
  generatelist : Int -> List Int
  generatelist i = [0,1..i]

  maplist : List Int -> List Int
  maplist xs = map (\x => if prob1_cond x then x else 0) xs

  sumlist : List Int -> Int
  sumlist xs = foldr (+) 0 xs

export
prob1_lazy : Int -> Int
prob1_lazy i = sumlist $ takeFilter i $ maplist $ generatelist 0 where
  generatelist : (start : Int) -> LazyList Int
  generatelist start = start :: generatelist (start + 1)

  maplist : LazyList Int -> LazyList Int
  maplist x = filter prob1_cond x

  takeFilter : Int -> LazyList Int -> LazyList Int
  takeFilter i xs = takeWhile (<= i) xs

  sumlist : LazyList Int -> Int
  sumlist x = foldr (+) 0 x 
  




main: IO()
main = do 
  let limit = 999
  let result = prob1_lazy limit
  print result
 


