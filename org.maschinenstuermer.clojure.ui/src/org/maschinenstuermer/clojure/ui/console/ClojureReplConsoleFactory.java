package org.maschinenstuermer.clojure.ui.console;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleListener;
import org.eclipse.ui.console.IConsoleManager;
import org.maschinenstuermer.clojure.ui.internal.ClojureActivator;

public class ClojureReplConsoleFactory implements IConsoleFactory {
	private final IConsoleManager consoleManager;
	private ClojureConsole clojureConsole;

	public ClojureReplConsoleFactory() {
		consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		consoleManager.addConsoleListener(new IConsoleListener() {
			public void consolesAdded(IConsole[] consoles) {
			}

			public void consolesRemoved(IConsole[] consoles) {
				for (int i = 0; i < consoles.length; i++) {
					if(consoles[i] == clojureConsole) {
						clojureConsole = null;
					}
				}
			}
		});
	}

	@Override
	public void openConsole() {
		if (clojureConsole == null) {
			final String symbolicName = ClojureActivator.getInstance().getBundle().getSymbolicName();
			final ImageDescriptor imageDescriptor = ClojureActivator.imageDescriptorFromPlugin(symbolicName, 
			"icons/clojure-repl.png");
			clojureConsole = new ClojureConsole("Clojure REPL", imageDescriptor);
			consoleManager.addConsoles(new IConsole[] { clojureConsole });
		}
		try {
			launch();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		consoleManager.showConsoleView(clojureConsole);
	}

	private void launch() throws CoreException {
		final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		final IEditorInput editorInput = activeEditor.getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			final IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
			final IProject project = fileEditorInput.getFile().getParent().getProject();
			final IJavaProject javaProject = JavaCore.create(project);
			launchRepl(javaProject);
		}
	}

	private void launchRepl(final IJavaProject javaProject)
			throws CoreException {
		IVMInstall vmInstall = JavaRuntime.getVMInstall(javaProject);
		if (vmInstall == null)
			vmInstall = JavaRuntime.getDefaultVMInstall();
		if (vmInstall != null) {
			IVMRunner vmRunner = vmInstall.getVMRunner(ILaunchManager.RUN_MODE);
			if (vmRunner != null) {
				String[] classPath = null;
				try {
					classPath = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);
				} catch (CoreException e) { }
				if (classPath != null) {
					VMRunnerConfiguration vmConfig = 
						new VMRunnerConfiguration("clojure.main", classPath);
					final IProject project = javaProject.getProject();
					final IPath location = project.getLocation();
					final String workingDir = location.toOSString();
					vmConfig.setWorkingDirectory(workingDir);
					vmConfig.setProgramArguments(new String[] { "--repl" });
					ILaunch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
					vmRunner.run(vmConfig, launch, null);
					clojureConsole.connect(launch);
				}
			}
		}
	}
}
