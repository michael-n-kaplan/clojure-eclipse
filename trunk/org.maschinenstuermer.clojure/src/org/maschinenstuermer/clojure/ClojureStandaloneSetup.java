
package org.maschinenstuermer.clojure;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class ClojureStandaloneSetup extends ClojureStandaloneSetupGenerated{

	public static void doSetup() {
		new ClojureStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

