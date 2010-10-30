package org.maschinenstuermer.clojure.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.junit.AbstractXtextTests;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.maschinenstuermer.clojure.ClojureStandaloneSetup;
import org.maschinenstuermer.clojure.clojure.Body;
import org.maschinenstuermer.clojure.clojure.ClojureFactory;
import org.maschinenstuermer.clojure.clojure.ClojurePackage;
import org.maschinenstuermer.clojure.clojure.Def;
import org.maschinenstuermer.clojure.clojure.File;
import org.maschinenstuermer.clojure.clojure.Fn;
import org.maschinenstuermer.clojure.clojure.Lambda;
import org.maschinenstuermer.clojure.clojure.NameBinding;
import org.maschinenstuermer.clojure.clojure.SimpleBinding;
import org.maschinenstuermer.clojure.clojure.SymbolDef;

public class ClojureScopeProviderTest extends AbstractXtextTests {
	private ClojureScopeProvider scopeProvider;
	
	private File file;
	private Fn fn;
	private Body body;
	
	public void setUp() throws Exception {
		super.setUp();
		with(ClojureStandaloneSetup.class);
		scopeProvider = get(ClojureScopeProvider.class);
		file = ClojureFactory.eINSTANCE.createFile();
	}
	
	public void testFnWithOneLambda() {
		initFnWithBody();
		final Lambda lambda = addLambdaToBody();
		final SimpleBinding simpleBinding = addSimpleBinding(lambda);
		final NameBinding nameBinding = addNameBinding(simpleBinding, "param1");
		
		thenContextContains(lambda, nameBinding);
	}

	public void testFnWithTwoLambda() {
		initFnWithBody();
		final Lambda lambda1 = addLambdaToBody();
		final SimpleBinding simpleBinding1 = addSimpleBinding(lambda1);
		final NameBinding nameBinding1 = addNameBinding(simpleBinding1, "param1");

		final Lambda lambda2 = addLambdaToBody();
		final SimpleBinding simpleBinding2 = addSimpleBinding(lambda2);
		final NameBinding nameBinding2 = addNameBinding(simpleBinding2, "param2");
		final NameBinding nameBinding3 = addNameBinding(simpleBinding2, "param3");

		thenContextNotContains(lambda2, nameBinding1);
		thenContextContains(lambda1, nameBinding1);		
		thenContextContains(lambda2, nameBinding2);
		thenContextContains(lambda2, nameBinding3);
	}

	public void testDef() {
		final Def def1 = addDef("def1");
		initFnWithBody();		
		thenContextContains(fn, def1);
		
		final Def def2 = addDef("def2");		
		thenContextContains(def1, def1);
		thenContextContains(def1, def2);
		thenContextContains(fn, def1);
		thenContextContains(fn, def2);
	}

	private void thenContextNotContains(final EObject context, final SymbolDef symbolDef) {
		final IScope scope = scopeProvider.getScope(context, ClojurePackage.eINSTANCE.getLiteral_Symbol());
		final IEObjectDescription contentByName = scope.getContentByName(symbolDef.getName());
		assertNull(contentByName);
	}

	private void thenContextContains(final EObject context, final SymbolDef symbolDef) {
		final IScope scope = scopeProvider.getScope(context, ClojurePackage.eINSTANCE.getLiteral_Symbol());
		final IEObjectDescription contentByName = scope.getContentByName(symbolDef.getName());
		assertNotNull(contentByName);
		assertSame(symbolDef, contentByName.getEObjectOrProxy());
	}
	
	private Def addDef(final String name) {
		final Def def = ClojureFactory.eINSTANCE.createDef();
		file.getForms().add(def);
		def.setName(name);
		return def;
	}
	
	private SimpleBinding addSimpleBinding(final Lambda lambda) {
		final SimpleBinding simpleBinding = ClojureFactory.eINSTANCE.createSimpleBinding();
		lambda.getBindings().add(simpleBinding);
		return simpleBinding;
	}

	private NameBinding addNameBinding(final SimpleBinding simpleBinding,
			final String name) {
		NameBinding nameBinding = ClojureFactory.eINSTANCE.createNameBinding();
		nameBinding.setName(name);
		simpleBinding.getBindings().add(nameBinding);
		return nameBinding;
	}

	private Lambda addLambdaToBody() {
		final Lambda lambda = ClojureFactory.eINSTANCE.createLambda();
		body.getLambdas().add(lambda);
		return lambda;
	}

	private void initFnWithBody() {
		fn = ClojureFactory.eINSTANCE.createFn();
		file.getForms().add(fn);
		body = ClojureFactory.eINSTANCE.createBody();
		fn.setBody(body);
	}
}
