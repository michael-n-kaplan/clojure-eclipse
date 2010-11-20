package org.maschinenstuermer.clojure.ui.preference;

import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.maschinenstuermer.clojure.install.ClojureInstall;

class ClojureInstallLabelProvider implements ITableLabelProvider, ICheckStateProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		final ClojureInstall clojureInstall = (ClojureInstall) element;			
		switch (columnIndex) {
		case 0: return clojureInstall.getName();
		case 1: return clojureInstall.getVersion().toString();
		case 2: return clojureInstall.getLocation();
		default: return null;
		}
	}

	@Override
	public boolean isChecked(Object element) {
		final ClojureInstall clojureInstall = (ClojureInstall) element;			
		return clojureInstall.isDefault();
	}

	@Override
	public boolean isGrayed(Object element) {
		return false;
	}
}
