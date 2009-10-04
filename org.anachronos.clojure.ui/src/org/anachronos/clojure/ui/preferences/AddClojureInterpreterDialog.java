package org.anachronos.clojure.ui.preferences;

import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterLibraryBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.IAddInterpreterDialogRequestor;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.swt.widgets.Shell;

public class AddClojureInterpreterDialog extends AddScriptInterpreterDialog {

    public AddClojureInterpreterDialog(
	    final IAddInterpreterDialogRequestor requestor, final Shell shell,
	    final IInterpreterInstallType[] interpreterInstallTypes,
	    final IInterpreterInstall editedInterpreter) {
	super(requestor, shell, interpreterInstallTypes, editedInterpreter);
    }

    @Override
    protected AbstractInterpreterLibraryBlock createLibraryBlock(
	    AddScriptInterpreterDialog dialog) {
	return new ClojureLibraryBlock(dialog);
    }

}
