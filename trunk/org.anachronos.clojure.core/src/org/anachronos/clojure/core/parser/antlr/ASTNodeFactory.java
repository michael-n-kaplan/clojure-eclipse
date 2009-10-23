package org.anachronos.clojure.core.parser.antlr;

import org.antlr.runtime.tree.CommonTree;
import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.references.SimpleReference;

public class ASTNodeFactory {
    private static final int ILLEGAL_INDEX = -1;

    public ModuleDeclaration createFile() {
	return new ModuleDeclaration(ILLEGAL_INDEX);
    }

    public MethodDeclaration createFn(CommonTree fn) {
	final int startIndex = fn.getTokenStartIndex();
	return new MethodDeclaration("", startIndex, startIndex, startIndex, fn
		.getTokenStopIndex());
    }

    public MethodDeclaration createDefn(CommonTree defn, CommonTree name,
	    CommonTree doc) {
	final MethodDeclaration methodDeclaration = new MethodDeclaration(name.getText(), name.getTokenStartIndex(),
			name.getTokenStopIndex(), defn.getTokenStartIndex(),
			defn.getTokenStopIndex());
	methodDeclaration.setComments(doc == null ? null : doc.getText()
		.substring(1, doc.getText().length() - 1));
	return methodDeclaration;
    }

    public Argument createArgument(CommonTree name) {
	return new Argument(new SimpleReference(0, 0, name.getText()), name
		.getTokenStartIndex(), name.getTokenStopIndex(), null, 0);
    }

    public FieldDeclaration createDef(CommonTree def, CommonTree name) {
	return new FieldDeclaration(name.getText(), name.getTokenStartIndex(),
		name.getTokenStopIndex(), def.getTokenStartIndex(), def
			.getTokenStopIndex());
    }
}
