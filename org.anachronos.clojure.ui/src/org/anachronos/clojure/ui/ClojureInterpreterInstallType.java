package org.anachronos.clojure.ui;

import java.io.IOException;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.internal.launching.AbstractInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterInstall;

public class ClojureInterpreterInstallType extends
	AbstractInterpreterInstallType {

    @Override
    protected IPath createPathFile(IDeployment deployment) throws IOException {
	return null;
    }

    @Override
    protected IInterpreterInstall doCreateInterpreterInstall(String id) {
	return new ClojureInterpreterInstall(this, id);
    }

    @Override
    protected ILog getLog() {
	return Activator.getDefault().getLog();
    }

    @Override
    protected String getPluginId() {
	return Activator.PLUGIN_ID;
    }

    @Override
    protected String[] getPossibleInterpreterNames() {
	return new String[] { "clojure" };
    }

    @Override
    public String getName() {
	return "Clojure Interpreter";
    }

    @Override
    public String getNatureId() {
	return ClojureNature.CLOJURE_NATURE;
    }
}
