package org.maschinenstuermer.clojure.jdt;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

public class ClojureClasspathContainer implements IClasspathContainer {
	public final static String ID = "org.maschinenstuermer.clojure.jdt.CLOJURE_CONTAINER";
	
	private final IPath path;
	private final IClasspathEntry[] classpathEntries;
	
	public ClojureClasspathContainer(final IPath path) {
		this.path = path;
		classpathEntries =
			new IClasspathEntry[] {
				JavaCore.newLibraryEntry(new Path("/home/km/Desktop/Downloads/down/clojure-classes.jar"), null, null)
		};
	}

	@Override
	public IClasspathEntry[] getClasspathEntries() {
		return classpathEntries;
	}

	@Override
	public String getDescription() {
		return "Clojure Runtime";
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