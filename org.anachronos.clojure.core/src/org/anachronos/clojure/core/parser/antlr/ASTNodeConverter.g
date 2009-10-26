tree grammar ASTNodeConverter;

options {
  language = Java;
  tokenVocab = Clojure;
  ASTLabelType = CommonTree;
}

@header {
package org.anachronos.clojure.core.parser.antlr;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
}

@members {
  private final ASTNodeFactory factory = new ASTNodeFactory();
  private final ModuleDeclaration moduleDeclaration = factory.createFile();
}

file returns [ModuleDeclaration file] 
@init { file = moduleDeclaration; }:  
    (
      literal |
      (d=def | d=defn | d=fn) { moduleDeclaration.addStatement(d); }
    )*;
  
body returns [Declaration def]
@init { def = null; }: 
    literal |
    (d=def | d=defn | d=fn) { def = d; }; 
    // | var | let |
  
//  require | refer | use | in_ns | import__ | ns |
  
//  list | map | set | vector |
  
//  reader_macro;

def returns [Declaration def]:
  ^(d=DEF name=SYMBOL initial_value=body)
  { 
    def = factory.createDef(d, name); 
  };
  
defn returns [Declaration def]
@init { List<Declaration> nestedDefs = new ArrayList<Declaration>(); }:
  ^(d=DEFN name=SYMBOL doc=STRING? 
    args=params 
    (nestedDef=body { if (nestedDef != null) nestedDefs.add(nestedDef); })+) 
  { 
    MethodDeclaration defn = factory.createDefn(d, name, doc); 
    defn.acceptArguments(args);
    defn.acceptBody(factory.createBody(d, nestedDefs));
    def = defn; 
  };

fn returns [Declaration def]:
  ^(f=FN args=params body+) 
  { 
    MethodDeclaration fn = factory.createFn(f); 
    fn.acceptArguments(args); 
    def = fn;
  };

params returns [List<Argument> args]
@init{ args = new ArrayList<Argument>(); }:
  ^(PARAMS 
      (name=SYMBOL { args.add(factory.createArgument(name)); })* 
   );
   
literal:
  NUMBER;

