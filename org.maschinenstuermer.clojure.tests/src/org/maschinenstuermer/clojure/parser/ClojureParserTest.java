package org.maschinenstuermer.clojure.parser;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.junit.AbstractXtextTests;
import org.maschinenstuermer.clojure.ClojureStandaloneSetup;
import org.maschinenstuermer.clojure.clojure.File;
import org.maschinenstuermer.clojure.clojure.Form;
import org.maschinenstuermer.clojure.clojure.Literal;

public class ClojureParserTest extends AbstractXtextTests {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		with(ClojureStandaloneSetup.class);
	}
	
	public void test() throws Exception {
		final File file = (File) getModel("System/out");
		
		assertNotNull(file);
		final EList<Form> exprs = file.getExprs();
		assertEquals(1, exprs.size());
		final Literal literal = (Literal) exprs.get(0);
		
		assertNotNull(literal.getType());
		assertEquals("java.lang.System", literal.getType().getCanonicalName());
		
		assertNotNull(literal.getMember());
		assertEquals("java.lang.System.out", literal.getMember().getCanonicalName());
	}
}
