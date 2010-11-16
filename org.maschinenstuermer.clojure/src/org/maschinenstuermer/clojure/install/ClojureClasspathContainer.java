package org.maschinenstuermer.clojure.install;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

public class ClojureClasspathContainer implements IClasspathContainer {
	private final static String CONTAINER_ID = "install.CLOJURE_CONTAINER";
	
	public final static String PLUGIN_ID = "org.maschinenstuermer.clojure";
	public final static String PREF_ID = PLUGIN_ID + "/" + CONTAINER_ID;
	public final static String ID = PLUGIN_ID + "." + CONTAINER_ID;
	
	private final IPath path;
	private final IClasspathEntry[] classpathEntries;

	private ClojureInstall clojureInstall;
	
	public ClojureClasspathContainer(final IPath path, final ClojureInstall clojureInstall) {
		this.path = path;
		this.clojureInstall = clojureInstall;
		classpathEntries =
			new IClasspathEntry[] {
				JavaCore.newLibraryEntry(new Path(clojureInstall.getLocation()), null, null)
		};
	}

	@Override
	public IClasspathEntry[] getClasspathEntries() {
		return classpathEntries;
	}

	@Override
	public String getDescription() {		
		return  String.format("Clojure Runtime [%s]", clojureInstall.getName());
	}

	@Override
	public int getKind() {
		return K_SYSTEM;
	}

	@Override
	public IPath getPath() {
		return path;
	}		
}