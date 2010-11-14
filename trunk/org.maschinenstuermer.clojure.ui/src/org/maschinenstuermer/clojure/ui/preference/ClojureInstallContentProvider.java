package org.maschinenstuermer.clojure.ui.preference;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.maschinenstuermer.clojure.install.ClojureInstalls;

class ClojureInstallContentProvider implements IStructuredContentProvider {
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		final ClojureInstalls clojureInstalls = 
			(ClojureInstalls) inputElement;
		return clojureInstalls.getElements();
	}
}
