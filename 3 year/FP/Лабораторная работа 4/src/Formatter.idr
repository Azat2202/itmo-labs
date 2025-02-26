module Formatter
import Parser
import Utils
import Data.String

%default total

public export
data Format : Type where
    Doubl : Maybe Nat -> Format
    Number : Format
    Percent : Format
    Str : Format
    Lit : String -> Format

public export
Show Format where 
    show (Doubl l) = "double (" ++ show l ++ ")"
    show Number    = "num"
    show Percent   = "%"
    show Str       = "str" 
    show (Lit s)   = s

partial
public export
ToFormat : Parser Format
ToFormat = fdouble <|> fnumber <|> fstring <|> fcharacter <|> fpercent

        where
        rightBound : Parser (Maybe Nat)
        rightBound = maybe $ char '.' ~> nat 

        fdouble : Parser Format
        fdouble = map (Doubl) $ string "%f" ~> rightBound

        fnumber : Parser Format
        fnumber = string "%d" ~> pure Number

        fstring : Parser Format
        fstring = string "%s" ~> pure Str

        fpercent : Parser Format 
        fpercent = string "%%" >>= \_ => pure Percent 

        fcharacter : Parser Format
        fcharacter = do s <- some $ sat (/= '%')
                        pure $ Lit $ pack s


public export
Format2Type : List Format -> Type
Format2Type [] = String
Format2Type ((Doubl a) :: fmt)   = (i : Double) -> Format2Type fmt
Format2Type (Number    :: fmt)   = (i : Int)    -> Format2Type fmt
Format2Type (Str       :: fmt)   = (i : String) -> Format2Type fmt
Format2Type (Percent   :: fmt)   = Format2Type fmt
Format2Type ((Lit s)   :: fmt)   = Format2Type fmt

public export
formatBuild : (fmt: List Format) -> (acc : String) -> Format2Type fmt
formatBuild [] acc                     = acc
formatBuild (Number :: xs) acc         = \i => formatBuild xs (acc ++ show i)
formatBuild (Str    :: xs) acc         = \i => formatBuild xs (acc ++ i)
formatBuild ((Doubl len) :: xs) acc    = \i => case len of
                                               Nothing => formatBuild xs (acc ++ show i)
                                               (Just l) => formatBuild xs (acc ++ show (round l i))
formatBuild (Percent :: xs) acc        = formatBuild xs (acc ++ "%")
formatBuild ((Lit s) :: xs) acc        = formatBuild xs (acc ++ s)

public export
partial
format : (fmt : String) -> Format2Type (ParseFull ToFormat fmt)
format fmt = formatBuild _ ""

partial
main : IO ()
main = do putStrLn $ format "_%s_%f.1" "hello" 1.23