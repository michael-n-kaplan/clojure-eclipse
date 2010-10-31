package org.maschinenstuermer.clojure.naming;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.junit.AbstractXtextTests;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.maschinenstuermer.clojure.ClojureStandaloneSetup;
import org.maschinenstuermer.clojure.clojure.Body;
import org.maschinenstuermer.clojure.clojure.ClojureFactory;
import org.maschinenstuermer.clojure.clojure.Def;
import org.maschinenstuermer.clojure.clojure.Fn;
import org.maschinenstuermer.clojure.clojure.Lambda;
import org.maschinenstuermer.clojure.clojure.NameBinding;
import org.maschinenstuermer.clojure.clojure.Ns;
import org.maschinenstuermer.clojure.clojure.SimpleBinding;

public class ClojureQualifiedNameProviderTest extends AbstractXtextTests {

	private Ns ns;
	private IQualifiedNameProvider qualifiedNameProvider;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		with(ClojureStandaloneSetup.class);
		ns = ClojureFactory.eINSTANCE.createNs();
		ns.setName("test");
		qualifiedNameProvider = get(IQualifiedNameProvider.class);
	}
	
	public void testWithNs() {
		final Def printfDef = ClojureFactory.eINSTANCE.createDef();
		printfDef.setName("printf");
		ns.getElements().add(printfDef);
		
		final Fn fn = ClojureFactory.eINSTANCE.createFn();
		final Body body = ClojureFactory.eINSTANCE.createBody();
		fn.setBody(body);
		
		printfDef.setInit(fn);
		fn.setName("local");
		
		final Lambda lambda = ClojureFactory.eINSTANCE.createLambda();
		body.getLambdas().add(lambda);
		final SimpleBinding simpleBinding = ClojureFactory.eINSTANCE.createSimpleBinding();
		lambda.getBindings().add(simpleBinding);
		final NameBinding nameBinding = ClojureFactory.eINSTANCE.createNameBinding();
		simpleBinding.getBindings().add(nameBinding);
		nameBinding.setName("local");
		
		thenHasQName("test.printf", printfDef);
		
		thenHasNoQName(ns);
		thenHasNoQName(fn);
		thenHasNoQName(body);
		thenHasNoQName(lambda);
		thenHasNoQName(simpleBinding);
		thenHasNoQName(nameBinding);
	}
	
	private void thenHasQName(final String qName, final EObject eObject) {
		assertEquals(qName, qualifiedNameProvider.getQualifiedName(eObject));
	}
	
	private void thenHasNoQName(final EObject eObject) {
		assertNull("Expected that object has no qualifiedName!", 
				qualifiedNameProvider.getQualifiedName(eObject));
	}
}
