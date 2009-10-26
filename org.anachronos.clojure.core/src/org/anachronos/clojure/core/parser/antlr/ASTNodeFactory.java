package org.anachronos.clojure.core.parser.antlr;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.tree.CommonTree;
import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.ast.statements.Statement;

public class ASTNodeFactory {
    private static final String VAR_ARG_LABEL = "...";
    private static final int ILLEGAL_INDEX = -1;

    public ModuleDeclaration createFile() {
	return new ModuleDeclaration(ILLEGAL_INDEX);
    }

    public MethodDeclaration createFn(final CommonTree fn) {
	final int startIndex = fn.getTokenStartIndex();
	return new MethodDeclaration("", startIndex, startIndex, startIndex, fn
		.getTokenStopIndex());
    }

    public Block createBody(final CommonTree body,
	    final List<Statement> statements) {
	return new Block(body.getTokenStartIndex(), body.getTokenStopIndex(),
		statements);
    }

    public MethodDeclaration createDefn(final CommonTree defn,
	    final CommonTree name, final CommonTree doc) {
	final int declStart = ((CommonToken) defn.getToken()).getStartIndex();
	final int declEnd = ((CommonToken) ((CommonTree) defn.getChild(defn
		.getChildCount() - 1)).getToken()).getStopIndex();
	final MethodDeclaration methodDeclaration = new MethodDeclaration(name
		.getText(), name.getTokenStartIndex(),
		name.getTokenStopIndex(), declStart, declEnd);
	methodDeclaration.setComments(doc == null ? null : doc.getText()
		.substring(1, doc.getText().length() - 1));
	return methodDeclaration;
    }

    public Argument createArgument(final CommonTree name) {
	final String argName = name.getText();
	return createArgument(name, argName);
    }

    public List<Argument> createLambdaArguments(final int paramCount) {
	final List<Argument> args = new ArrayList<Argument>();
	for (int param = 0; param < paramCount; param++) {
	    args.add(createArgument("%" + (param + 1)));
	}
	return args;
    }

    private Argument createArgument(final String name) {
	return new Argument(new SimpleReference(0, 0, name), 0, 0, null, 0);
    }

    private Argument createArgument(final CommonTree name, final String argName) {
	return new Argument(new SimpleReference(0, 0, argName), name
		.getTokenStartIndex(), name.getTokenStopIndex(), null, 0);
    }

    public Argument createVarArg(CommonTree name) {
	return createArgument(name, name.getText() + VAR_ARG_LABEL);
    }

    public FieldDeclaration createDef(final CommonTree def,
	    final CommonTree name) {
	return new FieldDeclaration(name.getText(), name.getTokenStartIndex(),
		name.getTokenStopIndex(), def.getTokenStartIndex(), def
			.getTokenStopIndex());
    }
}
