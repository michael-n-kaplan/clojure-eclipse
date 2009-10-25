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
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
}

@members {
  private final ASTNodeFactory factory = new ASTNodeFactory();
}

file returns [ModuleDeclaration moduleDeclaration] 
@init { moduleDeclaration = factory.createFile(); }:  
  form[moduleDeclaration]*;
  
form[ModuleDeclaration moduleDeclaration]: 
//  literal |
  
    v=def { moduleDeclaration.getVariablesList().add(v); }
  |
    (d=defn | d=fn) { moduleDeclaration.getFunctionList().add(d); moduleDeclaration.addStatement(d); }; 
    // | var | let |
  
//  require | refer | use | in_ns | import__ | ns |
  
//  list | map | set | vector |
  
//  reader_macro;

def returns [FieldDeclaration def]:
  ^(d=DEF name=SYMBOL initial_value=literal)
  { def = factory.createDef(d, name); };
  
defn returns [MethodDeclaration defn]:
  ^(d=DEFN name=SYMBOL doc=STRING? args=params body=literal) 
  { defn = factory.createDefn(d, name, doc); defn.acceptArguments(args); };

fn returns [MethodDeclaration fn]:
  ^(f=FN args=params body=literal) 
  { fn = factory.createFn(f); fn.acceptArguments(args); };

params returns [List<Argument> args]
@init{ args = new ArrayList<Argument>(); }:
  ^(PARAMS 
      (name=SYMBOL { args.add(factory.createArgument(name)); })* 
   );
   
literal:
  NUMBER;

