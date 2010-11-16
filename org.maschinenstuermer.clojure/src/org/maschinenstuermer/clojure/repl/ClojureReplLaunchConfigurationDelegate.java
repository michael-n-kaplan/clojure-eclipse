package org.maschinenstuermer.clojure.repl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.JavaLaunchDelegate;

public class ClojureReplLaunchConfigurationDelegate extends JavaLaunchDelegate {
	
	@Override
	public void launch(final ILaunchConfiguration configuration, final String mode,
			final ILaunch launch, final IProgressMonitor monitor) throws CoreException {
		final ILaunchConfigurationWorkingCopy workingCopy = 
			configuration.getWorkingCopy();
		workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, 
				"clojure.lang.Repl");
		final ILaunchConfiguration savedConfiguration = workingCopy.doSave();
		super.launch(savedConfiguration, mode, launch, monitor);
	}
}
