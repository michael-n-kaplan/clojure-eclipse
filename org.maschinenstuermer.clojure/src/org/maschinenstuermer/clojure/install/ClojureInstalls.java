package org.maschinenstuermer.clojure.install;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.maschinenstuermer.clojure.install.ClojureInstall.Version;
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
			final String location = node.get(name, "");
			final ClojureInstall clojureInstall = createClojureInstall(name, location);
			clojureInstalls.add(clojureInstall);
		}
	}

	public static ClojureInstall createClojureInstall(final String name, final String location) {
		final ClojureInstall clojureInstall = 
			new ClojureInstall(name, getVersion(location), location);
		return clojureInstall;
	}
	
	public void storeToPreferences() throws BackingStoreException {
		final Preferences clojureInstallsNode = getOrCreateClojureInstallsNode();
		for (final ClojureInstall clojureInstall : clojureInstalls) {
			clojureInstallsNode.put(clojureInstall.getName(), clojureInstall.getLocation());
		}
		clojureInstallsNode.flush();
	}
	
	private static Version getVersion(final String jarFilePath) {
		int major = 0, minor = 0, incremental = 0;
		final List<Closeable> closeables = new ArrayList<Closeable>(); 
		try {
			final JarFile jarFile = new JarFile(jarFilePath);
			closeables.add(new Closeable() {
				@Override
				public void close() throws IOException {
					jarFile.close();
				}
			});
			final ZipEntry entry = jarFile.getEntry("clojure/version.properties");
			if (entry != null) {
				final Properties versionProperties = new Properties();
				final InputStream inputStream = jarFile.getInputStream(entry);
				closeables.add(inputStream);
				versionProperties.load(inputStream);
				major = Integer.valueOf(versionProperties.
						getProperty("clojure.version.major", "0"));
				minor = Integer.valueOf(versionProperties.
						getProperty("clojure.version.minor", "0"));
				incremental = Integer.valueOf(versionProperties.
						getProperty("clojure.version.incremental", "0"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			for (final Closeable closeable : closeables) {
				try {
					closeable.close();				
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new Version(major, minor, incremental);
	}
	
	private Preferences getOrCreateClojureInstallsNode() {
		final IEclipsePreferences node = new InstanceScope().getNode(ClojureClasspathContainer.PREF_ID);
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
