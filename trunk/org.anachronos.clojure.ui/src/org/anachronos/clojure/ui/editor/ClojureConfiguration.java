package org.anachronos.clojure.ui.editor;

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
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Configures source viewer for clojure script editing.
 * 
 * @author km
 */
public class ClojureConfiguration extends SourceViewerConfiguration {
    private final ColorManager colorManager;

    public ClojureConfiguration(final ColorManager colorManager) {
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
		.createTokenScanner(new ClojureLexer());

	scanner.addToken(ClojureLexer.COMMENT,
		createAttribute(ClojureColorConstants.COMMENT));
	scanner.addToken(ClojureLexer.KEYWORD,
		createAttribute(ClojureColorConstants.LITERAL));
	scanner.addToken(ClojureLexer.STRING,
		createAttribute(ClojureColorConstants.STRING));
	scanner.addToken(ClojureLexer.SYMBOL,
		createAttribute(ClojureColorConstants.SYMBOL));
	return scanner;
    }

    private IToken createAttribute(final RGB foreground) {
	final Color fgColor = colorManager.getColor(foreground);
	return new Token(new TextAttribute(fgColor));
    }
}