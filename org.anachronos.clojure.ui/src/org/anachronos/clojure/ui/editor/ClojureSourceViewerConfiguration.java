package org.anachronos.clojure.ui.editor;

import org.anachronos.clojure.core.parser.antlr.ClojureLexer;
import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.anachronos.clojure.ui.completion.ClojureContentAssistPreference;
import org.anachronos.clojure.ui.preferences.ClojureColorPrefConstants;
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
import org.eclipse.jface.text.source.Annotation;
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
    protected boolean isShowInVerticalRuler(Annotation annotation) {
	// TODO hack to see error messages in vertical ruler
	return true;
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

	scanner.addTokenPrefKey(ClojureColorPrefConstants.CHARACTER_COLOR_PREF_KEY, ClojureLexer.CHARACTER);
	scanner.addTokenPrefKey(ClojureColorPrefConstants.COMMENT_PREF_KEY, ClojureLexer.COMMENT);
	scanner.addTokenPrefKey(ClojureColorPrefConstants.KEYWORD_PREF_KEY, ClojureLexer.KEYWORD);
	scanner.addTokenPrefKey(ClojureColorPrefConstants.NUMBER_PREF_KEY, ClojureLexer.NUMBER);
	scanner.addTokenPrefKey(ClojureColorPrefConstants.STRING_PREF_KEY, ClojureLexer.STRING);
	scanner.addTokenPrefKey(ClojureColorPrefConstants.LIST_PREF_KEY,
		ClojureLexer.LAMBDA, ClojureLexer.LPAREN, ClojureLexer.RPAREN);
	scanner.addTokenPrefKey(ClojureColorPrefConstants.VECTOR_PREF_KEY,
		ClojureLexer.LBRACKET, ClojureLexer.RBRACKET);
	scanner.addTokenPrefKey(ClojureColorPrefConstants.MAP_PREF_KEY,
		ClojureLexer.SET, ClojureLexer.LCURLY, ClojureLexer.RCURLY);
	scanner.addTokenPrefKey(ClojureColorPrefConstants.SYMBOL_PREF_KEY, ClojureLexer.SYMBOL);
	scanner.addTokenPrefKey(
		ClojureColorPrefConstants.PREDEFINED_SYMBOL_PREF_KEY,
		ClojureLexer.DEF, ClojureLexer.DEFN, 
		ClojureLexer.FN,  
		ClojureLexer.IMPORT, ClojureLexer.IN_NS, ClojureLexer.LET,
		ClojureLexer.NS, ClojureLexer.REFER, ClojureLexer.REQUIRE,
		ClojureLexer.USE, ClojureLexer.VAR);

	scanner.initialize();
	return scanner;
    }

    private ClojureLexer createLexer() {
	final ClojureLexer lexer = new ClojureLexer();
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