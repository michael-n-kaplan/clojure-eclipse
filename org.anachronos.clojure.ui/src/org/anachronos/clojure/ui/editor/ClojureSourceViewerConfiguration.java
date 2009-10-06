package org.anachronos.clojure.ui.editor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.anachronos.clojure.core.parser.ClojureLexer;
import org.anachronos.clojure.ui.preferences.ClojureColorConstants;
import org.anachronos.clojure.ui.preferences.ColorManager;
import org.anachronos.clojure.ui.syntaxcoloring.AntlrTokenScanner;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

/**
 * Configures source viewer for clojure script editing.
 * 
 * @author km
 */
public class ClojureSourceViewerConfiguration extends
	TextSourceViewerConfiguration {
    private final ColorManager colorManager;

    public ClojureSourceViewerConfiguration(final ColorManager colorManager) {
	this.colorManager = colorManager;
    }

    @Override
    public String[] getConfiguredContentTypes(final ISourceViewer sourceViewer) {
	return new String[] { IDocument.DEFAULT_CONTENT_TYPE };
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(
	    final ISourceViewer sourceViewer) {
	final PresentationReconciler reconciler = new PresentationReconciler();

	final AntlrTokenScanner scanner = initTokenScanner();
	final DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
	reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
	reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

	return reconciler;
    }

    private AntlrTokenScanner initTokenScanner() {
	final AntlrTokenScanner scanner = AntlrTokenScanner
		.createTokenScanner(createLexer());

	scanner.addToken(ClojureLexer.CHARACTER,
		createToken(ClojureColorConstants.CHARACTER));
	scanner.addToken(ClojureLexer.COMMENT,
		createToken(ClojureColorConstants.COMMENT));
	scanner.addToken(ClojureLexer.KEYWORD,
		createToken(ClojureColorConstants.KEYWORD));
	scanner.addToken(ClojureLexer.NUMBER,
		createToken(ClojureColorConstants.NUMBER));
	scanner.addToken(ClojureLexer.STRING,
		createToken(ClojureColorConstants.STRING));

	scanner.addToken(ClojureLexer.LPAREN,
		createToken(ClojureColorConstants.LIST));
	scanner.addToken(ClojureLexer.RPAREN,
		createToken(ClojureColorConstants.LIST));

	scanner.addToken(ClojureLexer.LBRACKET,
		createToken(ClojureColorConstants.VECTOR));
	scanner.addToken(ClojureLexer.RBRACKET,
		createToken(ClojureColorConstants.VECTOR));

	scanner.addToken(ClojureLexer.LCURLY,
		createToken(ClojureColorConstants.MAP));
	scanner.addToken(ClojureLexer.RCURLY,
		createToken(ClojureColorConstants.MAP));

	scanner.addToken(ClojureLexer.SYMBOL,
		createToken(ClojureColorConstants.SYMBOL));
	scanner.addToken(ClojureLexer.PREDEFINED_SYMBOL, createToken(
		ClojureColorConstants.PREDEFINED_SYMBOL, SWT.BOLD));

	return scanner;
    }

    private ClojureLexer createLexer() {
	final ClojureLexer lexer = new ClojureLexer();
	final Set<String> predefinedSymbols = new HashSet<String>(Arrays
		.asList(new String[] { "catch", "def", "do", "defmethod",
			"defmulti", "defmacro", "defstruct", "defn", "finally",
			"fn", "fn*", "if", "identical?", "in-ns", "let",
			"let*", "loop", "loop*", "new", "monitor-enter",
			"monitor-exit", "quote", "recur", "the-var", "throw",
			"try", "set!" }));

	lexer.setPredefinedSymbols(predefinedSymbols);
	return lexer;
    }

    private IToken createToken(final RGB foreground) {
	return createToken(foreground, SWT.NONE);
    }

    private IToken createToken(final RGB foreground, final int style) {
	final Color fgColor = colorManager.getColor(foreground);
	return new Token(new TextAttribute(fgColor, null, style));
    }
}