package org.anachronos.clojure.ui.launch;

import java.io.IOException;

import org.anachronos.clojure.core.ClojureNature;
import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.launching.AbstractInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterInstall;

public class ClojureInterpreterInstallType extends
	AbstractInterpreterInstallType {

    @Override
    protected IPath createPathFile(IDeployment deployment) throws IOException {
	return deployment.add(ClojureUIPlugin.getDefault().getBundle(),
		"scripts/classpath.clj");
    }

    @Override
    protected IInterpreterInstall doCreateInterpreterInstall(String id) {
	return new ClojureInterpreterInstall(this, id);
    }


    @Override
    protected ILog getLog() {
	return ClojureUIPlugin.getDefault().getLog();
    }

    @Override
    protected String getPluginId() {
	return ClojureUIPlugin.PLUGIN_ID;
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
	return ClojureNature.ID;
    }

    @Override
    protected String[] buildCommandLine(IFileHandle installLocation,
	    IFileHandle pathFile) {
	return new String[] { installLocation.getCanonicalPath(),
		pathFile.getCanonicalPath() };
    }
}
