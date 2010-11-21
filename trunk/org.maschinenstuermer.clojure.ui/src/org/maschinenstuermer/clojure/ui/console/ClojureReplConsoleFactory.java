package org.maschinenstuermer.clojure.ui.console;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

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
import org.eclipse.ui.console.IConsoleManager;
import org.maschinenstuermer.clojure.ui.internal.ClojureActivator;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class ClojureReplConsoleFactory implements IConsoleFactory {

	private final IConsoleManager consoleManager;

	private static ClojureReplConsoleFactory defaultFactory;
	
	public ClojureReplConsoleFactory() {
		consoleManager = ConsolePlugin.getDefault().getConsoleManager();
	}

	public static synchronized ClojureReplConsoleFactory getDefault() {
		if (defaultFactory == null)
			defaultFactory = new ClojureReplConsoleFactory();
		return defaultFactory;
	}
	
	public ClojureConsole openAndShowConsole() {
		final IJavaProject activeProject = getActiveProject();
		final String consoleName = activeProject == null ? 
				"Clojure REPL" : activeProject.getElementName();
		ClojureConsole clojureConsole = (ClojureConsole) findClojureConsole(consoleName);
		if (clojureConsole == null) {
			final ImageDescriptor imageDescriptor = getImageDescriptor();
			clojureConsole = new ClojureConsole(consoleName, imageDescriptor);
			consoleManager.addConsoles(new IConsole[] { clojureConsole });
			try {
				launchRepl(clojureConsole, activeProject);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		consoleManager.showConsoleView(clojureConsole);
		return clojureConsole;
	}
	
	@Override
	public void openConsole() {
		openAndShowConsole();
	}

	private ImageDescriptor getImageDescriptor() {
		final String symbolicName = ClojureActivator.getInstance().getBundle().getSymbolicName();
		final ImageDescriptor imageDescriptor = ClojureActivator.imageDescriptorFromPlugin(symbolicName, 
			"icons/clojure-repl.png");
		return imageDescriptor;
	}

	private IConsole findClojureConsole(final String name) {
		final List<IConsole> consoles = Arrays.asList(consoleManager.getConsoles());
		IConsole clojureConsole = null;
		try {
			clojureConsole = Iterables.find(consoles, new Predicate<IConsole>() {

				@Override
				public boolean apply(final IConsole input) {
					final String consoleName = input.getName();
					return ClojureConsole.TYPE.equals(input.getType()) 
					&& consoleName != null
					&& consoleName.equals(name);
				}
			});
		} catch (NoSuchElementException e) {
		}
		return clojureConsole;
	}
	
	private IJavaProject getActiveProject() {
		IJavaProject activeProject = null;
		final IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
			getActivePage().getActiveEditor();
		if (activeEditor != null) {
			final IEditorInput editorInput = activeEditor.getEditorInput();
			if (editorInput instanceof IFileEditorInput) {
				final IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
				final IProject project = fileEditorInput.getFile().getParent().getProject();
				activeProject = JavaCore.create(project);
			}
		}
		return activeProject;
	}
	
	private void launchRepl(final ClojureConsole clojureConsole, final IJavaProject javaProject)
			throws CoreException {
		IVMInstall vmInstall = null;
		if (javaProject != null)
			vmInstall = JavaRuntime.getVMInstall(javaProject);
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
					final ILaunch launch = new Launch(null, ILaunchManager.RUN_MODE, null);
					vmRunner.run(vmConfig, launch, null);
					clojureConsole.connect(launch);
				}
			}
		}
	}
}
