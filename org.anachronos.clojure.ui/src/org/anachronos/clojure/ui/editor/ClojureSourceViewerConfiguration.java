package org.anachronos.clojure.ui.editor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.anachronos.clojure.core.parser.ClojureLexer;
import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.anachronos.clojure.ui.completion.ClojureContentAssistPreference;
import org.anachronos.clojure.ui.preferences.ClojureColorConstants;
import org.anachronos.clojure.ui.syntaxcoloring.AntlrTokenScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Configures source viewer for clojure script editing.
 * 
 * @author km
 */
public class ClojureSourceViewerConfiguration extends
	ScriptSourceViewerConfiguration {

    private AntlrTokenScanner scanner;

    public ClojureSourceViewerConfiguration(IColorManager colorManager,
	    IPreferenceStore preferenceStore, ITextEditor editor) {
	super(colorManager, preferenceStore, editor,
		IDocument.DEFAULT_CONTENT_TYPE);
    }

    @Override
    public String[] getConfiguredContentTypes(final ISourceViewer sourceViewer) {
	return new String[] { IDocument.DEFAULT_CONTENT_TYPE };
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(
	    final ISourceViewer sourceViewer) {
	final PresentationReconciler reconciler = new PresentationReconciler();

	scanner = initTokenScanner();
	final DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
	reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
	reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

	return reconciler;
    }

    @Override
    public void handlePropertyChangeEvent(PropertyChangeEvent event) {
	if (scanner.affectsBehavior(event))
	    scanner.adaptToPreferenceChange(event);
    }

    @Override
    public boolean affectsTextPresentation(PropertyChangeEvent event) {
	return scanner.affectsBehavior(event)
		|| super.affectsTextPresentation(event);
    }

    private AntlrTokenScanner initTokenScanner() {
	final AntlrTokenScanner scanner = AntlrTokenScanner.createTokenScanner(
		createLexer(), getColorManager(), ClojureUIPlugin.getDefault()
			.getPreferenceStore());

	scanner.addToken(ClojureLexer.CHARACTER,
		ClojureColorConstants.CHARACTER_COLOR_PREF_KEY);
	scanner.addToken(ClojureLexer.COMMENT,
		ClojureColorConstants.COMMENT_PREF_KEY);
	scanner.addToken(ClojureLexer.KEYWORD,
		ClojureColorConstants.KEYWORD_PREF_KEY);
	scanner.addToken(ClojureLexer.NUMBER,
		ClojureColorConstants.NUMBER_PREF_KEY);
	scanner.addToken(ClojureLexer.STRING,
		ClojureColorConstants.STRING_PREF_KEY);

	scanner.addToken(ClojureLexer.LPAREN,
		ClojureColorConstants.LIST_PREF_KEY);
	scanner.addToken(ClojureLexer.RPAREN,
		ClojureColorConstants.LIST_PREF_KEY);

	scanner.addToken(ClojureLexer.LBRACKET,
		ClojureColorConstants.VECTOR_PREF_KEY);
	scanner.addToken(ClojureLexer.RBRACKET,
		ClojureColorConstants.VECTOR_PREF_KEY);

	scanner.addToken(ClojureLexer.SET,
 ClojureColorConstants.MAP_PREF_KEY);
	scanner.addToken(ClojureLexer.LCURLY,
		ClojureColorConstants.MAP_PREF_KEY);
	scanner.addToken(ClojureLexer.RCURLY,
		ClojureColorConstants.MAP_PREF_KEY);

	scanner.addToken(ClojureLexer.SYMBOL,
		ClojureColorConstants.SYMBOL_PREF_KEY);
	scanner.addToken(ClojureLexer.PREDEFINED_SYMBOL,
		ClojureColorConstants.PREDEFINED_SYMBOL_PREF_KEY);

	scanner.initialize();
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

    @Override
    protected IInformationControlCreator getOutlinePresenterControlCreator(
	    final ISourceViewer sourceViewer, final String commandId) {
	return new IInformationControlCreator() {
	    public IInformationControl createInformationControl(
		    final Shell parent) {
		final int shellStyle = SWT.RESIZE;
		final int treeStyle = SWT.V_SCROLL | SWT.H_SCROLL;
		return new ClojureOutlineInformationControl(parent, shellStyle,
			treeStyle, commandId);
	    }
	};
    }

    @Override
    protected ContentAssistPreference getContentAssistPreference() {
	return ClojureContentAssistPreference.getDefault();
    }
}