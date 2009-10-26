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
      d=form { if (d != null) moduleDeclaration.addStatement(d); }
    )*;
  
form returns [Declaration def]
@init { def = null; }: 
    literal |
    ( 
      d=def | d=defn | d=fn |
      d=list
    ) { def = d; }; 
    // | var | let |
  
//  require | refer | use | in_ns | import__ | ns |
  
//  list | map | set | vector |
  
//  reader_macro;

def returns [Declaration def]:
  ^(d=DEF name=SYMBOL initial=form)
  { 
    if (initial instanceof MethodDeclaration) {
      initial.setName(name.getText());
      def = initial; 
    } else
      def = factory.createDef(d, name); 
  };
  
defn returns [Declaration def]
@init { List<Declaration> nestedDefs = new ArrayList<Declaration>(); }:
  ^(d=DEFN 
      name=SYMBOL doc=STRING? 
      args=params 
      (nestedDef=form { if (nestedDef != null) nestedDefs.add(nestedDef); })*
   ) 
  { 
    MethodDeclaration defn = factory.createDefn(d, name, doc); 
    defn.acceptArguments(args);
    defn.acceptBody(factory.createBody(d, nestedDefs));
    def = defn; 
  };

fn returns [Declaration def]
@init { List<Declaration> nestedDefs = new ArrayList<Declaration>(); }:
  ^(f=FN 
      args=params 
      (nestedDef=form { if (nestedDef != null) nestedDefs.add(nestedDef); })*
   ) 
  { 
    MethodDeclaration fn = factory.createFn(f); 
    fn.acceptArguments(args); 
    fn.acceptBody(factory.createBody(f, nestedDefs));
    def = fn;
  };

params returns [List<Argument> args]
@init{ args = new ArrayList<Argument>(); }:
  ^(PARAMS 
      (name=SYMBOL { args.add(factory.createArgument(name)); })* 
   );
   
list returns [Declaration def]:
  ^(LIST (d=form)*) { def = d; };
  
literal:
  NUMBER | SYMBOL;

