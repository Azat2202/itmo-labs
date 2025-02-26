module Utils

import Data.String

public export
round : (len: Nat) -> (num: Double) -> Double 
round n x = if fracPart < 0.5 
                then f / factor 
                else c / factor
    where
    factor : Double
    factor = pow 10.0 (cast n)

    scaled : Double
    scaled = x * cast factor

    c : Double 
    c = ceiling scaled

    f : Double 
    f = floor scaled

    fracPart : Double 
    fracPart = scaled - f
