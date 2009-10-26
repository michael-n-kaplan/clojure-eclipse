package org.anachronos.clojure.core.parser.antlr;

import java.util.List;

import org.antlr.runtime.tree.CommonTree;
import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.statements.Block;

public class ASTNodeFactory {
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
	    final List<Declaration> nestedDefs) {
	return new Block(body.getTokenStartIndex(), body.getTokenStopIndex(),
		nestedDefs);
    }

    public MethodDeclaration createDefn(final CommonTree defn,
	    final CommonTree name, final CommonTree doc) {
	final int declStart = defn.getTokenStartIndex();
	final int declEnd = defn.getTokenStopIndex();
	final MethodDeclaration methodDeclaration = new MethodDeclaration(name.getText(), name.getTokenStartIndex(),
		name.getTokenStopIndex(), declStart, declEnd);
	methodDeclaration.setComments(doc == null ? null : doc.getText()
		.substring(1, doc.getText().length() - 1));
	return methodDeclaration;
    }

    public Argument createArgument(final CommonTree name) {
	return new Argument(new SimpleReference(0, 0, name.getText()), name
		.getTokenStartIndex(), name.getTokenStopIndex(), null, 0);
    }

    public FieldDeclaration createDef(final CommonTree def,
	    final CommonTree name) {
	return new FieldDeclaration(name.getText(), name.getTokenStartIndex(),
		name.getTokenStopIndex(), def.getTokenStartIndex(), def
			.getTokenStopIndex());
    }
}
