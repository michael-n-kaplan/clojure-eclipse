package org.anachronos.clojure.core.parser.antlr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.junit.Test;

public class ClojureParserTest {

    @Test
    public void callValidArity() throws Exception {
	ClojureParser parser = buildParser("(test 1)");
	parser.getGlobalScope().addFunctionDef("test", 1, true);
	CommonTree tree = (CommonTree) parser.file().getTree();
	assertEquals(ClojureLexer.CALL, tree.getType());
    }

    @Test
    public void callUnboundValidArity() throws Exception {
	ClojureParser parser = buildParser("(test 1 2)");
	parser.getGlobalScope().addFunctionDef("test", 1, false);
	CommonTree tree = (CommonTree) parser.file().getTree();
	assertEquals(ClojureLexer.CALL, tree.getType());
    }

    @Test
    public void callInvalidArity() throws Exception {
	ClojureParser parser = buildParser("(test 1 2)");
	parser.getGlobalScope().addFunctionDef("test", 1, true);
	parser.file();
	assertTrue(parser.hasErrors());
    }

    @Test
    public void defnWithVarArg() throws Exception {
	ClojureParser parser = buildParser("(defn test [a & b] 1)");
	parser.file();
	assertTrue(parser.getGlobalScope().isDefinedArity("test", 1));
	assertTrue(parser.getGlobalScope().isDefinedArity("test", 3));
    }

    @Test
    public void defnWithAnonParams() throws Exception {
	ClojureParser parser = buildParser("(defn test [] %)");
	parser.file();
	final Set<RecognitionException> errors = parser.getErrorToMessage()
		.keySet();
	assertFalse(errors.isEmpty());
    }

    @Test
    public void lambdaWithParams() throws Exception {
	CommonTree tree = buildAst("#(% %2 %3 %44)");

	assertEquals(ClojureLexer.LAMBDA, tree.getType());
	assertEquals(4, tree.getChildCount());
    }

    @Test
    public void defnWithVarArgs() throws Exception {
	CommonTree tree = buildAst("(defn test [x & ys] 1)");

	assertEquals(3, tree.getChildCount());

	final Tree params = tree.getChild(1);
	assertEquals(ClojureLexer.PARAMS, params.getType());
	assertEquals(2, params.getChildCount());
	assertEquals("x", params.getChild(0).getText());

	final Tree varArg = params.getChild(1);
	assertEquals(ClojureLexer.VAR_ARG, varArg.getType());
	assertEquals(1, varArg.getChildCount());
	assertEquals("ys", varArg.getChild(0).getText());
    }

    @Test
    public void defnWithParams() throws Exception {
	final Tree tree = buildAst("(defn test [a b c] 1)");
	assertEquals("defn", tree.getText());
	assertEquals(3, tree.getChildCount());
	final Tree name = tree.getChild(0);
	assertEquals("test", name.getText());
	final Tree params = tree.getChild(1);
	assertEquals(ClojureLexer.PARAMS, params.getType());
	assertEquals(3, params.getChildCount());
	char c = 'a';
	for (int i = 0; i < 3; i++, c++) {
	    final Tree param = params.getChild(i);
	    assertEquals(String.valueOf(c), param.getText());
	}
    }

    @Test
    public void defnNestedDefns() throws Exception {
	final CommonTree tree = buildAst("(defn test []\n"
		+ "(defn nest1 [] (1 2))" + "(defn nest2 [] (1 3)))");
	assertEquals(4, tree.getChildCount());

	final Tree defn1 = tree.getChild(2);
	assertEquals(3, defn1.getChildCount());
	assertEquals("nest1", defn1.getChild(0).getText());

	final Tree defn2 = tree.getChild(3);
	assertEquals(3, defn2.getChildCount());
	assertEquals("nest2", defn2.getChild(0).getText());
    }

    private CommonTree buildAst(String script) throws RecognitionException {
	final ClojureParser parser = buildParser(script);
	final CommonTree ast = (CommonTree) parser.file().getTree();
	assertFalse("Expected no errors during parse!", parser.hasErrors());
	return ast;
    }

    private ClojureParser buildParser(String script) {
	final Lexer lexer = new ClojureLexer(new ANTLRStringStream(script));
	final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
	final ClojureParser parser = new ClojureParser(tokenStream);
	return parser;
    }
}
