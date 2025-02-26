module Parser

import Data.String

%default partial

public export
data Parser : Type -> Type where
    MkParser : (String -> List (a, String)) -> Parser a

public export
parse : Parser a -> String -> List (a, String)
parse (MkParser p) inp = p inp

public export
item : Parser Char 
item = MkParser (\str => case unpack str of
                              [] => []
                              (c :: cs) => [(c, pack cs)])

public export
Functor Parser where
    map f p = MkParser (\inp => case parse p inp of
                                     [] => []
                                     ((v, out) :: xs) => [(f v, out)])

public export
Applicative Parser where 
    pure v = MkParser (\inp => [(v, inp)])
    pf <*> p = MkParser (\inp => case parse pf inp of
                                  [(f, out)] => parse (map f p) out
                                  _ => [])

public export
Monad Parser where
  p >>= f = MkParser (\inp => case parse p inp of
                                  [(v, out)] => parse (f v) out
                                  _ => [])

public export
Alternative Parser where
  empty = MkParser (\_ => [])

  p <|> q = MkParser (\inp => case parse p inp of
                                [(v, out)] => [(v, out)]
                                _ => parse q inp )

public export
infixr 5 ~>

-- if a then parse b
public export
(~>) : Parser a -> Parser b -> Parser b
(~>) pa pb = pa >>= \_ => pb

public export
sat : (Char -> Bool) -> Parser Char 
sat pred = do x <- item 
              case pred x of
                False => empty
                True => pure x

public export
any : Parser Char 
any = sat (\_ => True)

public export
char : Char -> Parser Char 
char c = sat (== c)

public export
digit : Parser Char
digit = sat isDigit

public export
maybe : Parser a -> Parser (Maybe a)
maybe parser = map (Just) parser
               <|> pure Nothing


mutual 
    public export
    some : Parser a -> Parser (List a)
    some p = pure (::) <*> p <*> many p
    
    public export
    many : Parser a -> Parser (List a)
    many p = some p <|> pure []

public export
nat : Parser Nat
nat = do ns <- some digit
         pure (stringToNatOrZ (pack ns))

public export
int : Parser Int
int = do _ <- char '-'
         ns <- nat
         pure (-(cast ns))
      <|> do ns <- nat
             pure (cast ns)

public export
string : String -> Parser String
string "" = pure ""
string cs = let (c :: cs) = unpack cs in
                do _ <- char c
                   _ <- string (pack cs)
                   pure $ pack (c :: cs)

public export
parseFull : Parser a -> String -> List (a)
parseFull parser inp = case parse parser inp of
                            [] => []
                            ((p, unconsumed) :: xs) => [p] ++ (parseFull parser unconsumed)


-- in type we need CamelCase
public export
ParseFull : Parser a -> String -> List (a)
ParseFull = parseFull



main : IO ()
main = do putStrLn $ show $ parse int "123asdf123"
          pure ()
