grammar Clojure;

options {
  language = Java;
  output = AST;
}

tokens {
  LAMBDA;
  LIST;
  MAP;
  META_DATA;
  REGEX;
  VAR_QUOTE;
  VECTOR;
  
  PREDEFINED_SYMBOL;
  
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

@lexer::members {
  private Set<String> predefinedSymbols = new HashSet<String>();
  
  public void setPredefinedSymbols(final Set<String> predefinedSymbols) {
    this.predefinedSymbols = predefinedSymbols;
  }
  
  private boolean isPredefinedSymbol(final String symbol) {
    return predefinedSymbols.contains(symbol);
  }
}

file: 
  list*;
  
form: 
  literal |
  
  list |
  vector |
  map |
  
  reader_macro;
  
list:
  '(' form* ')' -> ^(LIST form*);
  
vector:
  '[' form* ']' -> ^(VECTOR form*);
  
map:
  '{' (form form)* '}' -> ^(MAP (form form)*);

special_form:
  ('\'' | '`' | '~' | '~@' | '^' | '@') form;
  
lambda:
  '#(' form* ')' -> ^(LAMBDA form*);
  
meta_data:
  '#^' map form -> ^(META_DATA map form);
  
var_quote:
  '\'#' SYMBOL -> ^(VAR_QUOTE SYMBOL);
  
regex:
  '#' STRING -> ^(REGEX STRING);
  
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
  NAME ('/' NAME)? { $type = isPredefinedSymbol($text) ? PREDEFINED_SYMBOL : SYMBOL; };

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
