package org.maschinenstuermer.clojure.install;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.service.prefs.BackingStoreException;

public class ClojureClasspathContainerInitializer extends
		ClasspathContainerInitializer {

	public ClojureClasspathContainerInitializer() {
	}

	@Override
	public boolean canUpdateClasspathContainer(IPath containerPath,
			IJavaProject project) {
		return true;
	}
	
	@Override
	public void requestClasspathContainerUpdate(IPath containerPath,
			IJavaProject project, IClasspathContainer containerSuggestion)
			throws CoreException {
		JavaCore.setClasspathContainer(containerPath, 
				new IJavaProject[] { project }, 
				new IClasspathContainer[] { containerSuggestion }, 
				null); 
	}
	
	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {
		final ClojureInstalls clojureInstalls = new ClojureInstalls();
		try {
			clojureInstalls.initFromPreferences();
		} catch (BackingStoreException e) {
			throw new RuntimeException(e);
		}
		
		final ClojureClasspathContainer clojureClasspathContainer = 
			new ClojureClasspathContainer(containerPath, clojureInstalls.getDefault());
		requestClasspathContainerUpdate(containerPath, project, 
				clojureClasspathContainer);
	}
}
