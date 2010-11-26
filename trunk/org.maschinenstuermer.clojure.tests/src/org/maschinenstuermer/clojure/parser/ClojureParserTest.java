package org.maschinenstuermer.clojure.parser;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.junit.AbstractXtextTests;
import org.maschinenstuermer.clojure.ClojureStandaloneSetup;
import org.maschinenstuermer.clojure.clojure.Def;
import org.maschinenstuermer.clojure.clojure.Dot;
import org.maschinenstuermer.clojure.clojure.File;
import org.maschinenstuermer.clojure.clojure.Form;
import org.maschinenstuermer.clojure.clojure.Lambda;
import org.maschinenstuermer.clojure.clojure.Namespace;
import org.maschinenstuermer.clojure.clojure.New;
import org.maschinenstuermer.clojure.clojure.Quote;
import org.maschinenstuermer.clojure.clojure.QuotedSymbol;
import org.maschinenstuermer.clojure.clojure.ReaderQuote;
import org.maschinenstuermer.clojure.clojure.SimpleLiteral;
import org.maschinenstuermer.clojure.clojure.SymbolRef;
import org.maschinenstuermer.clojure.clojure.Throw;
import org.maschinenstuermer.clojure.clojure.Vector;

public class ClojureParserTest extends AbstractXtextTests {
	private File file;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		with(ClojureStandaloneSetup.class);
	}

	public void testNumber() throws Exception {
		file = (File) getModel("-1");

		thenEqualsSimpleLiteral("-1", file);
		
		file = (File) getModel("+1");
		thenEqualsSimpleLiteral("+1", file);	
	}
	
	public void testDef_WithSpecialName() throws Exception {
		file = (File) getModel("(def +)");
		thenEqualsDef("+", file);
		
		file = (File) getModel("(def -)");
		thenEqualsDef("-", file);
		
		file= (File) getModel("(def /)");
		thenEqualsDef("/", file);
		
		file= (File) getModel("(def ..)");
		thenEqualsDef("..", file);
	}
	
	public void testBrokenSymbolRef() throws Exception {
		getModelAndExpect("java.math. BigDecimal", 3);
	}
	
	public void testReaderLambda() throws Exception {
		file = (File) getModel("#()");
		
		EList<Form> exprs = file.getExprs();
		assertEquals(1, exprs.size());
		assertTrue(exprs.get(0) instanceof Lambda);
		
		file = (File) getModel("#(% %1)");
		
		exprs = file.getExprs();
		assertEquals(1, exprs.size());
		assertTrue(exprs.get(0) instanceof Lambda);
		final Lambda lambda = (Lambda) exprs.get(0);
		
		EList<Form> lambdaExprs = lambda.getExprs();
		assertEquals(2, lambdaExprs.size());
		
		assertTrue(lambdaExprs.get(0) instanceof SimpleLiteral);
		final SimpleLiteral param1 = (SimpleLiteral) lambdaExprs.get(0);
		assertEquals("%", param1.getParam());

		assertTrue(lambdaExprs.get(1) instanceof SimpleLiteral);
		final SimpleLiteral param2 = (SimpleLiteral) lambdaExprs.get(1);
		assertEquals("%1", param2.getParam());
	}

	public void testStaticMember() throws Exception {
		file = (File) getModel("(in-ns 'test) System/out");

		final List<Form> exprs = 
			thenHasOneNamespaceWithExprs(1, file);
		
		assertTrue(exprs.get(0) instanceof SymbolRef);
		final SymbolRef symbolRef = (SymbolRef) exprs.get(0);
		assertNotNull(symbolRef.getType());
		assertEquals(System.class.getCanonicalName(), 
				symbolRef.getType().getCanonicalName());
		assertNotNull(symbolRef.getMember());
		assertEquals("java.lang.System.out", symbolRef.getMember().getCanonicalName());
	}
	
	public void testInnerClass() throws Exception {
		file = (File) getModel("(in-ns 'test) java.util.Map$Entry");
		
		final List<Form> exprs = 
			thenHasOneNamespaceWithExprs(1, file);
		
		assertTrue(exprs.get(0) instanceof SymbolRef);
		final SymbolRef symbolRef = (SymbolRef) exprs.get(0);
		assertEquals("java.util.Map$Entry",	
				symbolRef.getType().getCanonicalName());
	}

	public void testThrow() throws Exception {
		file = (File) getModel("(in-ns 'test) (throw (IllegalArgumentException.))");
		
		final List<Form> exprs = 
			thenHasOneNamespaceWithExprs(1, file);
		
		assertTrue(exprs.get(0) instanceof Throw);
		final Throw t = (Throw) exprs.get(0);
		assertTrue(t.getExpr() instanceof New);
		final New newCall = (New) t.getExpr();
		assertEquals(IllegalArgumentException.class.getCanonicalName(), 
				newCall.getType().getCanonicalName());
	}
	
	public void testReaderQuoted() throws Exception {
		file = (File) getModel("'.");
		thenQuotedSymbolEquals(".", file);
		
		file = (File) getModel("'&");
		thenQuotedSymbolEquals("&", file);
		
		file = (File) getModel("'[coll items]");
		final Quote quote = thenExpectQuote(file);
		assertTrue(quote.getQuoted() instanceof Vector);
		final Vector vector = (Vector) quote.getQuoted();
		final EList<Form> values = vector.getValues();
		assertEquals(2, values.size());
		assertTrue(values.get(0) instanceof QuotedSymbol);
		assertTrue(values.get(1) instanceof QuotedSymbol);
	}

	private static void thenQuotedSymbolEquals(final String expectedSymbol, final File file) {
		final Quote readerQuote = thenExpectQuote(file);
		assertTrue(readerQuote.getQuoted() instanceof QuotedSymbol);
		final QuotedSymbol quotedSymbol = (QuotedSymbol) readerQuote.getQuoted();
		assertEquals(expectedSymbol, quotedSymbol.getSymbol());		
	}

	private static Quote thenExpectQuote(final File file) {
		final EList<Form> exprs = file.getExprs();
		assertEquals(1, exprs.size());
		assertTrue(exprs.get(0) instanceof ReaderQuote);
		final Quote readerQuote = (Quote) exprs.get(0);
		return readerQuote;
	}
	
	public void testDot() throws Exception {
		file = (File) getModel("(. java.math.BigDecimal valueOf)");
		
		final EList<Form> exprs = file.getExprs();
		assertEquals(1, exprs.size());
		assertTrue(exprs.get(0) instanceof Dot);
		
		file = (File) getModel("(def anObject java.lang.Object.)(.hashCode anObject)");
	}
	
	public void testConstructorCall() throws Exception {
		file = (File) getModel("(java.math.BigDecimal.)");
		
		final EList<Form> exprs = file.getExprs();
		assertEquals(1, exprs.size());
		assertTrue(exprs.get(0) instanceof New);
		final New newCall = (New) exprs.get(0);
		assertEquals(BigDecimal.class.getCanonicalName(), 
				newCall.getType().getCanonicalName());
	}

	public void testImport() throws Exception {
		file= (File) 
		getModel("(import '(java.math BigDecimal))" 
				+ "(def z BigDecimal/ZERO)");

		final EList<Form> exprs = file.getExprs();
		assertEquals(2, exprs.size());
		assertTrue(exprs.get(1) instanceof Def);
		final Def defZ = (Def) exprs.get(1);
		assertEquals("z", defZ.getName());
		assertTrue(defZ.getInit() instanceof SymbolRef);
		final SymbolRef symbolRef = (SymbolRef) defZ.getInit();
		assertEquals("java.math.BigDecimal", symbolRef.getType().getCanonicalName());
		assertEquals("java.math.BigDecimal.ZERO", symbolRef.getMember().getCanonicalName());
	}

	public void testNsWithImport() throws Exception {
		file= (File) 
			getModel("(ns test (:import (java.math BigDecimal)))" 
				+ "(def z BigDecimal/ZERO)");

		final List<Form> exprs = 
			thenHasOneNamespaceWithExprs(1, file);

		assertTrue(exprs.get(0) instanceof Def);
		final Def defZ = (Def) exprs.get(0);
		assertEquals("z", defZ.getName());
		assertTrue(defZ.getInit() instanceof SymbolRef);
		final SymbolRef symbolRef = (SymbolRef) defZ.getInit();
		assertEquals("java.math.BigDecimal", symbolRef.getType().getCanonicalName());
		assertEquals("java.math.BigDecimal.ZERO", symbolRef.getMember().getCanonicalName());
	}

	public void testNsWithUse() throws Exception {
		file = (File) 
			getModel("(ns test (:use clojure.contrib.import-static))");
	}
	
	public void testDefstruct() throws Exception {
		file = (File) 
			getModel("(defstruct test :key1 :key2)");
	}
		
	private static void thenEqualsDef(final String expectedName, final File file) {
		final EList<Form> exprs = file.getExprs();
		assertEquals(1, exprs.size());
		assertTrue(exprs.get(0) instanceof Def);
		final Def def = (Def) exprs.get(0);
		assertEquals(expectedName, def.getName());
	}

	private static void thenEqualsSimpleLiteral(final String expectedValue, final File file) {
		final EList<Form> exprs = file.getExprs();
		assertEquals(1, exprs.size());
		assertTrue(exprs.get(0) instanceof SimpleLiteral);
		final SimpleLiteral number = (SimpleLiteral) exprs.get(0);
		assertEquals(expectedValue, number.getValue());
	}
	
	private static List<Form> thenHasOneNamespaceWithExprs(final int numOfExpectedExprs, final File file) {
		final EList<Namespace> namespaces = file.getNamespaces();
		assertEquals(1, namespaces.size());
		final Namespace namespace = namespaces.get(0);
		final EList<Form> exprs = namespace.getExprs();
		assertEquals(numOfExpectedExprs, exprs.size());
		return exprs;
	}
}
