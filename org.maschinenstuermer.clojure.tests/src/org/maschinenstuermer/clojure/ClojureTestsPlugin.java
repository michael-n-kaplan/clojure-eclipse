package org.maschinenstuermer.clojure;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ClojureTestsPlugin extends Plugin implements BundleActivator {
	private static ClojureTestsPlugin plugin;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}
	
	public static ClojureTestsPlugin getDefault() {
		return plugin;
	}
}
