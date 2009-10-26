package org.anachronos.clojure.core.parser.antlr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.junit.Test;

public class ASTNodeConverterTest {

    @Test
    public void def() throws Exception {
	final String script = "(def test 1)";
	final ModuleDeclaration file = convertScript(script);

	final FieldDeclaration testVar = getVariable(file, 0);
	assertEquals("test", testVar.getName());
    }

    private FieldDeclaration getVariable(ModuleDeclaration file, int index) {
	final FieldDeclaration[] variables = file.getVariables();
	assertTrue("Expected correct number of variables!",
		index + 1 <= variables.length);
	return variables[index];
    }

    @Test
    @SuppressWarnings("unchecked")
    public void simpleDefn() throws Exception {
	final String script = "(defn test [] 1)";
	final ModuleDeclaration file = convertScript(script);

	final MethodDeclaration testFunction = getFunction(file, 0);
	assertEquals("test", testFunction.getName());

	final List arguments = testFunction.getArguments();
	assertEquals(0, arguments.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void defn() throws Exception {
	final String script = "(defn test \"doc\" [a] 1)";
	final ModuleDeclaration file = convertScript(script);
	
	final MethodDeclaration testFunction = getFunction(file, 0);
	assertEquals("test", testFunction.getName());
	assertEquals("doc", testFunction.getComments());

	final List arguments = testFunction.getArguments();
	assertEquals(1, arguments.size());

	final Argument arg = (Argument) arguments.get(0);
	assertEquals("a", arg.getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void defnNestedFn() throws Exception {
	final String script = "(defn test [] (fn [a] 1))";
	final ModuleDeclaration file = convertScript(script);

	final MethodDeclaration testFunction = getFunction(file, 0);
	assertEquals("test", testFunction.getName());

	final MethodDeclaration anonFunction = getNestedFunction(testFunction,
		0);
	assertEquals("", anonFunction.getName());

	final List arguments = anonFunction.getArguments();
	assertEquals(1, arguments.size());
	final Argument arg = (Argument) arguments.get(0);
	assertEquals("a", arg.getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void fn() throws Exception {
	final String script = "(fn [a] 1)";
	final ModuleDeclaration file = convertScript(script);

	final MethodDeclaration anonFunction = getFunction(file, 0);
	assertEquals("", anonFunction.getName());

	final List arguments = anonFunction.getArguments();
	assertEquals(1, arguments.size());

	final Argument arg = (Argument) arguments.get(0);
	assertEquals("a", arg.getName());
    }

    private MethodDeclaration getFunction(ModuleDeclaration file, int index) {
	MethodDeclaration[] functions = file.getFunctions();
	assertTrue("Expected correct number of functions!",
		index + 1 <= functions.length);
	return functions[index];
    }

    @SuppressWarnings("unchecked")
    private MethodDeclaration getNestedFunction(MethodDeclaration defn,
	    int index) {
	final List nestedFunctions = defn.getStatements();
	assertTrue("Expected correct number of nested functions!",
		index + 1 <= nestedFunctions.size());
	final MethodDeclaration nestedFunction = (MethodDeclaration) nestedFunctions
		.get(index);
	return nestedFunction;
    }

    private ModuleDeclaration convertScript(String script)
	    throws RecognitionException {
	final Lexer lexer = new ClojureLexer(new ANTLRStringStream(script));
	final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
	final ClojureParser parser = new ClojureParser(tokenStream);
	final CommonTree ast = (CommonTree) parser.file().getTree();
	final CommonTreeNodeStream treeNodeStream = new CommonTreeNodeStream(
		ast);
	treeNodeStream.setTokenStream(tokenStream);
	final ASTNodeConverter astNodeConverter = new ASTNodeConverter(
		treeNodeStream);
	ModuleDeclaration file = astNodeConverter.file();
	return file;
    }
}