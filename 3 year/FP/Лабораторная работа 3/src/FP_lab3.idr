module FP_lab3

import Data.String
import Data.Fuel
import Data.Vect
import System

%default total

record Point where
  constructor MkPoint
  x : Double 
  y : Double

zPoint : Point
zPoint = MkPoint 0.0 0.0 

Show Point where 
  show (MkPoint x y) = (show x) ++ "\t" ++ (show y)

has : String -> List String -> Bool
has str strs = case find (== str) strs of
                    Nothing => False
                    (Just x) => True

partial
parseDiscretisation : List String -> Double
parseDiscretisation strs = case find (isPrefixOf "-d") strs of
                                Nothing => 1.0
                                (Just arg) => let (d, discretisation) = span (/= '=') arg in 
                                                  case parseDouble (strTail discretisation) of
                                                       Nothing => 1.0
                                                       (Just x) => x

getPoint : IO Point
getPoint = do putStrLn "Введите точку в формате (x y) (q чтобы выйти)"
              line <- getLine 
              _ <- if (line == "q") then exitSuccess else pure ()
              let (xStr, yStr) = span (/= ' ') line
              let x = fromMaybe 0.0 $ parseDouble xStr
              let y = fromMaybe 0.0 $ parseDouble yStr
              pure $ MkPoint x y

unzipPoint : Point -> (Double, Double)
unzipPoint (MkPoint x y) = (x, y)

lagrange_l : {len: Nat} -> (n: Fin (S (S len))) -> Double -> Vect (S (S len)) Double -> Double
lagrange_l i x xs = numerator / denominator where
                      numerator : Double
                      numerator = foldl1 (*) $ deleteAt i $ zipWith (-) (replicate (S (S len)) x) xs

                      denominator : Double
                      denominator = let x_i = index i xs in
                        foldl1 (*) $ deleteAt i $ zipWith (-) (replicate (S (S len)) x_i) xs

lagrange_L : {len : Nat} -> Double -> Vect (S (S len)) Point -> Point
lagrange_L x points = let (xs, ys) = unzip $ map unzipPoint points
                          indexes = Data.Vect.Fin.range {len=S(S len)}
                          l_i = map (\i => lagrange_l i x xs) indexes 
                          y = foldl1 (+) $ zipWith (*) ys l_i in 
                          MkPoint x y


doubleRange : (start: Double) -> (end: Double) -> (discretisation: Double) -> List Double
doubleRange start end discretisation = case start < end of
                                            False => [start]
                                            True => assert_total $ start :: (doubleRange (start + discretisation) end discretisation)

lagrange : (discretisation : Double) -> Vect 4 Point -> List Point
lagrange discretisation xs@(p0@(MkPoint x0 y0) :: p1 :: p2 :: p3@(MkPoint x3 y3) :: Nil) = 
  do let range = doubleRange x0 x3 discretisation
     map (\x => lagrange_L x xs) range

linearFunc : Vect 2 Point -> Double -> Point
linearFunc xs@((MkPoint x0 y0) :: ((MkPoint x1 y1) :: [])) x = 
  MkPoint x $ y0 + (x - x0) * (y1 - y0) / (x1 - x0)

linear : (discretisation : Double) -> Vect 2 Point -> List Point
linear discretisation xs@((MkPoint x0 y0) :: ((MkPoint x1 y1) :: [])) = 
  do let range = doubleRange x0 x1 discretisation
     map (linearFunc xs) range 

record Interpolator where 
  constructor MkInterpolator
  name : String
  window : Nat
  discretisation : Double
  func : (Vect window Point -> List Point)


record InterpolationState where
  constructor MkState
  points : List Point
  interpolators : List Interpolator

tryToTake : (l: Nat) -> List Point -> Maybe (Vect l Point)
tryToTake 0 xs = Just []
tryToTake (S k) [] = Nothing
tryToTake (S k) (x :: xs) = case tryToTake k xs of
                                 Nothing => Nothing
                                 (Just ys) => Just $ x :: ys

interpolatorTryAndGo : (points : List Point) -> Interpolator -> List Point
interpolatorTryAndGo points (MkInterpolator name window discretisation func) = 
  case tryToTake window points of
       Nothing => []
       (Just pointsWindow) => func $ reverse pointsWindow

printResults : List Interpolator -> List (List Point) -> IO ()
printResults [] _ = pure ()
printResults _ [] = pure ()
printResults ((MkInterpolator name window discretisation func) :: xs) (points :: ys) = if (length points == Z) then pure () else 
  do putStrLn name
     putStrLn $ foldl1 (++) $ [""] ++ map ((++ "\n") . show) points 
     printResults xs ys

interpolationGo : Fuel -> InterpolationState -> Nat -> IO ()
interpolationGo Dry x y = pure ()
interpolationGo (More fuel) (MkState points interpolators) maxWindowSize = 
  do let pointsLen = length points 
     let interpolationResults = map (interpolatorTryAndGo points) interpolators
     printResults interpolators interpolationResults
     newPoint <- getPoint
     let newPointList = if ((length points) >= maxWindowSize) then (init (newPoint :: points)) else (newPoint :: points)
     interpolationGo fuel (MkState newPointList interpolators) maxWindowSize


partial
main : IO()
main = do putStrLn ""
          args <- getArgs
          let discretisation = parseDiscretisation args 
          let linearInterpolator = if (not $ has "linear" args) then Prelude.Nil else [
            MkInterpolator "Линейная интерполяция" 2 discretisation (linear discretisation)
          ]
          let lagrangeInterpolator = if (not $ has "lagrange" args) then Prelude.Nil else [
            MkInterpolator "Интерполяция Лагранжа" 4 discretisation (lagrange discretisation)
          ]
          let interpolationState = MkState [] (linearInterpolator ++ lagrangeInterpolator)
          let maxWindowSize = 4
          interpolationGo forever interpolationState maxWindowSize
