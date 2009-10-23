package org.anachronos.clojure.core.parser;

import org.anachronos.clojure.core.parser.antlr.ASTNodeConverter;
import org.anachronos.clojure.core.parser.antlr.ClojureLexer;
import org.anachronos.clojure.core.parser.antlr.ClojureParser;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.AbstractSourceParser;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

public class ClojureSourceParser extends AbstractSourceParser {

    @Override
    public ModuleDeclaration parse(char[] fileName, char[] content,
	    IProblemReporter reporter) {
	ModuleDeclaration file = null;
	try {
	    final Lexer lexer = new ClojureLexer(new ANTLRStringStream(
		    new String(content)));
	    final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
	    final ClojureParser parser = new ClojureParser(tokenStream);
	    final CommonTree ast = (CommonTree) parser.file().getTree();
	    final CommonTreeNodeStream treeNodeStream = new CommonTreeNodeStream(
		    ast);
	    treeNodeStream.setTokenStream(tokenStream);
	    final ASTNodeConverter astNodeConverter = new ASTNodeConverter(
		    treeNodeStream);
	    file = astNodeConverter.file();
	} catch (RecognitionException e) {
	}
	return file;
    }
}
