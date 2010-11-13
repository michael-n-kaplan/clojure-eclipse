package org.maschinenstuermer.clojure.parser;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.junit.AbstractXtextTests;
import org.maschinenstuermer.clojure.ClojureStandaloneSetup;
import org.maschinenstuermer.clojure.clojure.Def;
import org.maschinenstuermer.clojure.clojure.Defn;
import org.maschinenstuermer.clojure.clojure.File;
import org.maschinenstuermer.clojure.clojure.Form;
import org.maschinenstuermer.clojure.clojure.Literal;
import org.maschinenstuermer.clojure.clojure.Namespace;

public class ClojureParserTest extends AbstractXtextTests {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		with(ClojureStandaloneSetup.class);
	}

	public void testStaticMember() throws Exception {
		final File file = (File) getModel("(in-ns 'test) System/out");

		assertNotNull(file);
		final EList<Namespace> namespaces = file.getNamespaces();
		assertEquals(1, namespaces.size());
		final Namespace namespace = namespaces.get(0);
		final EList<Form> exprs = namespace.getExprs();
		assertEquals(1, exprs.size());
		assertTrue(exprs.get(0) instanceof Literal);
		final Literal literal = (Literal) exprs.get(0);

		assertNotNull(literal.getType());
		assertEquals("java.lang.System", literal.getType().getCanonicalName());

		assertNotNull(literal.getMember());
		assertEquals("java.lang.System.out", literal.getMember().getCanonicalName());
	}

	public void testImport() throws Exception {
		final File file= (File) 
		getModel("(import '(java.math BigDecimal))" 
				+ "(def z BigDecimal/ZERO)");

		final EList<Form> exprs = file.getExprs();
		assertEquals(2, exprs.size());
		assertTrue(exprs.get(1) instanceof Def);
		final Def defZ = (Def) exprs.get(1);
		assertEquals("z", defZ.getName());
		assertTrue(defZ.getInit() instanceof Literal);
		final Literal literal = (Literal) defZ.getInit();
		assertEquals("java.math.BigDecimal", literal.getType().getCanonicalName());
		assertEquals("java.math.BigDecimal.ZERO", literal.getMember().getCanonicalName());
	}

	public void testNsWithImport() throws Exception {
		final File file= (File) 
		getModel("(ns test (:import (java.math BigDecimal)))" 
				+ "(def z BigDecimal/ZERO)");

		final EList<Namespace> namespaces = file.getNamespaces();
		assertEquals(1, namespaces.size());
		final Namespace ns = namespaces.get(0);
		final EList<Form> elements = ns.getExprs();
		assertEquals(1, elements.size());
		assertTrue(elements.get(0) instanceof Def);
		final Def defZ = (Def) elements.get(0);
		assertEquals("z", defZ.getName());
		assertTrue(defZ.getInit() instanceof Literal);
		final Literal literal = (Literal) defZ.getInit();
		assertEquals("java.math.BigDecimal", literal.getType().getCanonicalName());
		assertEquals("java.math.BigDecimal.ZERO", literal.getMember().getCanonicalName());
	}

	public void testDefn_SpecialCases() throws Exception {
		File file= (File) 
		getModel("(defn / [] \"div\") (/ 12 1)");

		assertEquals(2, file.getExprs().size());
		assertTrue(file.getExprs().get(0) instanceof Defn);
		Defn defn = (Defn) file.getExprs().get(0);
		assertEquals("/", defn.getName());

		file= (File) 
			getModel("(defn .. [] 1)");

		assertEquals(1, file.getExprs().size());
		assertTrue(file.getExprs().get(0) instanceof Defn);
		defn = (Defn) file.getExprs().get(0);
		assertEquals("..", defn.getName());
	}
}
