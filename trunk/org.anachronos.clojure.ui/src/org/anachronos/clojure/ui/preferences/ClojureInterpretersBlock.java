package org.anachronos.clojure.ui.preferences;

import org.anachronos.clojure.core.ClojureNature;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.internal.debug.ui.interpreters.IScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersBlock;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;

public class ClojureInterpretersBlock extends InterpretersBlock {

    @Override
    protected IScriptInterpreterDialog createInterpreterDialog(
	    final IEnvironment environment, final IInterpreterInstall standin) {
	final AddClojureInterpreterDialog interpreterDialog = new AddClojureInterpreterDialog(
		this, getShell(), ScriptRuntime
			.getInterpreterInstallTypes(getCurrentNature()), standin);
	interpreterDialog.setEnvironment(environment);
	return interpreterDialog;
    }

    @Override
    protected String getCurrentNature() {
	return ClojureNature.CLOJURE_NATURE;
    }
}
