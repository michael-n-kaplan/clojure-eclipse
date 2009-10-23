grammar Clojure;

options {
  language = Java;
  output = AST;
}

tokens {
  DEREF = '@';
  DEF = 'def';
  DEFN = 'defn';
  
  FN = 'fn';
  
  IMPORT = 'import';
  IN_NS = 'in-ns';
  
  LAMBDA = '#(';
  LET = 'let';
  LIST;
  
  MAP;
  META = '^';
  META_DATA;
  
  NS = 'ns';
  
  QUOTE = '\'';
  
  PARAMS;
  
  REGEX;
  REQUIRE = 'require';
  REFER = 'refer';
  
  SET;
  SYNTAX_QUOTE = '`';
  
  USE = 'use';
  UNQUOTE = '~';
  UNQUOTE_SPLICING = '~@';
  
  VAR = 'var';
  VAR_QUOTE;
  VAR_ARG = '&';
  VECTOR;
    
  LPAREN = '(';
  RPAREN = ')';
  
  LBRACKET = '[';
  RBRACKET = ']';
  
  LCURLY = '{';
  RCURLY = '}';  
}

@header {
package org.anachronos.clojure.core.parser;
}
  
@lexer::header {
package org.anachronos.clojure.core.parser;

import java.util.HashSet;
import java.util.Set;  
}

file: 
  form*;
  
form: 
  literal |
  
  def | defn | fn | var | let |
  
  require | refer | use | in_ns | import__ | ns |
  
  list | map | set | vector |
  
  reader_macro;
  
// Definitions

def:
  '(' DEF name=SYMBOL initialValue=form? ')'
  -> ^(DEF $name $initialValue);
  
defn:
  '(' DEFN SYMBOL STRING? map? params body+=form+ ')' 
  -> ^(DEFN SYMBOL STRING? map? params $body);
   
params:
  '[' args+=SYMBOL* ']'
  -> ^(PARAMS $args);
   
fn:
  '(' FN params body+=form+ ')'
  -> ^(FN params $body);

lambda:
  LAMBDA body+=form+ ')' 
  -> ^(LAMBDA $body);
  
let:
  '(' LET bindings=vector body+=form* ')'
  -> ^(LET $bindings $body);

// References 
  
var:
  '(' VAR symbol=SYMBOL ')' |
  '#\'' symbol=SYMBOL
  -> ^(VAR $symbol);
  

// Module\Namespace handling

require:
  '(' REQUIRE namespace=quoted_namespace_symbol ')'
  -> ^(REQUIRE $namespace);

refer:
  '(' REFER namespace=quoted_namespace_symbol ')'
  -> ^(REFER $namespace);
  
use:
  '(' USE ':reload-all'? namespace=quoted_namespace_symbol ')'
  -> ^(USE $namespace);
  
in_ns:
  '(' IN_NS namespace=quoted_namespace_symbol ')'
  -> ^(IN_NS $namespace);
  
import__:
  '(' IMPORT '(' pkg=SYMBOL classes+=SYMBOL ')' ')'
  -> ^(IMPORT $pkg $classes);
  
ns:
  '(' NS name=SYMBOL namespace_references+=namespace_reference+ ')'
  -> ^(NS $name $namespace_references);
  
namespace_reference:
  '(' ':use' packages+=SYMBOL+ ')' 
  |
  '(' ':import' '(' pkg=SYMBOL cls+=SYMBOL+ ')';
  
quoted_namespace_symbol:
  QUOTE namespace=SYMBOL
  -> ^($namespace);
  
// Data types
  
list:
  '(' form* ')' 
  -> ^(LIST form*);

set:
  '#{' form* '}' 
  -> ^(SET form*);
    
map:
  '{' (form form)* '}' 
  -> ^(MAP (form form)*);
  
vector:
  '[' form* ('&' form)? ']' 
  -> ^(VECTOR form*);

special_form:
  ('\'' | '`' | '~' | '~@' | '^' | '@') form;
  
  
meta_data:
  '#^' map form 
  -> ^(META_DATA map form);
  
var_quote:
  '\'#' SYMBOL 
  -> ^(VAR_QUOTE SYMBOL);
  
regex:
  '#' STRING 
  -> ^(REGEX STRING);
  
reader_macro:
  lambda |
  meta_data |
  special_form |
  regex |
  var_quote; 
    
literal:
  STRING |
  NUMBER |   
  CHARACTER |
  NIL |
  BOOLEAN |
  KEYWORD |
  SYMBOL |
  PARAM_NAME;   
  
STRING: 
  '"' ( ~'"' | ('\\' '"') )* '"';
  
NUMBER: 
  '-'? '0'..'9'+ ('.' '0'..'9'+)? (('e'|'E') '-'? '0'..'9'+)?;

CHARACTER:
  '\\' .;

NIL:
  'nil';
  
BOOLEAN:
  'true' | 'false';

KEYWORD:
  ':' SYMBOL;

SYMBOL: 
  '.' | 
  '/' |   
  NAME ('/' NAME)?;

PARAM_NAME:
  '%' (('1'..'9')('0'..'9')*)?;
  
fragment
NAME:   
  SYMBOL_HEAD SYMBOL_REST* (':' SYMBOL_REST+)*;

fragment
SYMBOL_HEAD:   
  'a'..'z' | 'A'..'Z' | '*' | '+' | '!' | '-' | '_' | '?' | '>' | '<' | '=' | '$';
    
fragment
SYMBOL_REST:
  SYMBOL_HEAD |   
  '0'..'9' | 
  '.';  
  
WS:
  (' '|'\n'|'\t'|'\r'|',') { $channel = HIDDEN; };

COMMENT:
  ';' ~('\n')* ('\n'|EOF) { $channel = HIDDEN; };
