package org.maschinenstuermer.clojure.install;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class ClojureInstalls {
	private final List<ClojureInstall> clojureInstalls = 
		new ArrayList<ClojureInstall>();
	
	public void add(final ClojureInstall clojureInstall) {
		clojureInstalls.add(clojureInstall);
	}
	
	public void initFromPreferences() throws BackingStoreException {
		final Preferences node = getOrCreateClojureInstallsNode();
		for (final String name : node.keys()) {
			final ClojureInstall clojureInstall = 
				new ClojureInstall(name, node.get(name, ""));
			clojureInstalls.add(clojureInstall);
		}
	}
	
	public void storeToPreferences() throws BackingStoreException {
		final Preferences clojureInstallsNode = getOrCreateClojureInstallsNode();
		for (final ClojureInstall clojureInstall : clojureInstalls) {
			clojureInstallsNode.put(clojureInstall.getName(), clojureInstall.getLocation());
		}
		clojureInstallsNode.flush();
	}
	
	private Preferences getOrCreateClojureInstallsNode() {
		final IPreferencesService preferencesService = Platform.getPreferencesService();
		final Preferences node = preferencesService.getRootNode().
			node(ClojureClasspathContainer.ID);
		return node;
	}

	public ClojureInstall getDefault() {
		final ClojureInstall defaultInstall = Iterables.find(clojureInstalls, new Predicate<ClojureInstall>() {
			@Override
			public boolean apply(ClojureInstall input) {
				return input.isDefault();
			}
		});
		return defaultInstall;
	}
	
	public Object[] getElements() {
		return clojureInstalls.toArray();
	}
}
