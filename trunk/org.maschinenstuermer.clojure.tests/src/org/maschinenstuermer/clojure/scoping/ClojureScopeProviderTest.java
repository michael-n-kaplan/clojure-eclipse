package org.maschinenstuermer.clojure.scoping;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.junit.Before;
import org.junit.Test;
import org.maschinenstuermer.clojure.clojure.Body;
import org.maschinenstuermer.clojure.clojure.ClojureFactory;
import org.maschinenstuermer.clojure.clojure.ClojurePackage;
import org.maschinenstuermer.clojure.clojure.Fn;
import org.maschinenstuermer.clojure.clojure.Lambda;
import org.maschinenstuermer.clojure.clojure.NameBinding;
import org.maschinenstuermer.clojure.clojure.SimpleBinding;

public class ClojureScopeProviderTest {
	private ClojureScopeProvider scopeProvider;
	private Fn fn;
	private Body body;
	
	@Before
	public void setup() {
		scopeProvider = new ClojureScopeProvider();
	}
	
	@Test
	public void fnWithOneLambda() {
		initFnWithBody();
		final Lambda lambda = addLambdaToBody();
		final SimpleBinding simpleBinding = addSimpleBinding(lambda);
		final NameBinding nameBinding = addNameBinding(simpleBinding, "param1");
		IScope scope = scopeProvider.getScope(lambda, ClojurePackage.eINSTANCE.getLiteral_Symbol());
		final IEObjectDescription contentByName = scope.getContentByName("param1");
		
		assertNotNull(contentByName);
		assertSame(nameBinding, contentByName.getEObjectOrProxy());
	}

	@Test
	public void fnWithTwoLambda() {
		initFnWithBody();
		final Lambda lambda1 = addLambdaToBody();
		final SimpleBinding simpleBinding1 = addSimpleBinding(lambda1);
		final NameBinding nameBinding1 = addNameBinding(simpleBinding1, "param1");

		final Lambda lambda2 = addLambdaToBody();
		final SimpleBinding simpleBinding2 = addSimpleBinding(lambda2);
		final NameBinding nameBinding2 = addNameBinding(simpleBinding2, "param2");
		final NameBinding nameBinding3 = addNameBinding(simpleBinding2, "param3");

		IScope scope = scopeProvider.getScope(lambda1, ClojurePackage.eINSTANCE.getLiteral_Symbol());
		IEObjectDescription contentByName = scope.getContentByName("param1");
		
		assertNotNull(contentByName);
		assertSame(nameBinding1, contentByName.getEObjectOrProxy());
		
		scope = scopeProvider.getScope(lambda2, ClojurePackage.eINSTANCE.getLiteral_Symbol());
		
		contentByName = scope.getContentByName("param1");
		assertNull(contentByName);
	
		contentByName = scope.getContentByName("param2");
		assertNotNull(contentByName);
		assertSame(nameBinding2, contentByName.getEObjectOrProxy());
		
		contentByName = scope.getContentByName("param3");
		assertNotNull(contentByName);
		assertSame(nameBinding3, contentByName.getEObjectOrProxy());
	}

	private SimpleBinding addSimpleBinding(final Lambda lambda) {
		final SimpleBinding simpleBinding = ClojureFactory.eINSTANCE.createSimpleBinding();
		lambda.getBindings().add(simpleBinding);
		return simpleBinding;
	}

	private NameBinding addNameBinding(final SimpleBinding simpleBinding,
			String name) {
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
		body = ClojureFactory.eINSTANCE.createBody();
		fn.setBody(body);
	}
}
