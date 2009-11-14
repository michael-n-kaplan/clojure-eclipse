package org.anachronos.clojure.ui.console;

import org.anachronos.clojure.core.ClojureNature;
import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.anachronos.clojure.ui.launch.ClojureScriptInterpreter;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.dltk.console.IScriptInterpreter;
import org.eclipse.dltk.console.ScriptConsoleServer;
import org.eclipse.dltk.console.ui.IScriptConsoleFactory;
import org.eclipse.dltk.console.ui.ScriptConsole;
import org.eclipse.dltk.console.ui.ScriptConsoleFactoryBase;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.debug.core.DLTKDebugPlugin;
import org.eclipse.dltk.internal.launching.execution.DeploymentManager;
import org.eclipse.dltk.launching.ScriptLaunchUtil;

public class ClojureScriptConsoleFactory extends ScriptConsoleFactoryBase
	implements IScriptConsoleFactory {

    @Override
    protected ScriptConsole createConsoleInstance() {
	return createConsoleInstance(null, null);
    }

    @Override
    public void openConsole(final IScriptInterpreter interpreter,
	    final String id, final ILaunch launch) {
	final ScriptConsole console = createConsoleInstance(interpreter, id);
	console.setLaunch(launch);
	registerAndOpenConsole(console);
    }

    private ScriptConsole createConsoleInstance(IScriptInterpreter interpreter,
	    String id) {
	ILaunch launch = null;
	if (interpreter == null) {
	    try {
		id = "default";
		interpreter = new ClojureScriptInterpreter();
		launch = createLaunch((ClojureScriptInterpreter) interpreter);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	final ScriptConsole console = buildConsole(interpreter, id);
	if (launch != null) {
	    final IProcess[] processes = launch.getProcesses();
	    for (int i = 0; i < processes.length; ++i) {
		final IStreamsProxy proxy = processes[i].getStreamsProxy();
		if (proxy != null) {
		    console.connect(proxy);
		}
	    }
	}
	return console;
    }

    private ILaunch createLaunch(final ClojureScriptInterpreter interpreter)
	    throws Exception {
	final ScriptConsoleServer server = ScriptConsoleServer.getInstance();

	server.register("123", interpreter);
	final String port = Integer.toString(server.getPort());

	final String[] args = new String[] {
		DLTKDebugPlugin.getDefault().getBindAddress(), port, "123" };

	final IExecutionEnvironment exeEnv = (IExecutionEnvironment) EnvironmentManager
		.getLocalEnvironment().getAdapter(IExecutionEnvironment.class);

	final IDeployment deployment = exeEnv.createDeployment();
	final IPath path = deployment.add(ClojureUIPlugin.getDefault().getBundle(), "scripts/client.clj");
	final IFileHandle scriptFile = deployment.getFile(path);

	final ILaunch launch = ScriptLaunchUtil.runScript(ClojureNature.ID,
		scriptFile, null, null, args, null);
	DeploymentManager.getInstance().addDeployment(launch, deployment);
	return launch;
    }

    private ClojureScriptConsole buildConsole(IScriptInterpreter interpreter,
	    String id) {
	return new ClojureScriptConsole(interpreter, id);
    }
}
