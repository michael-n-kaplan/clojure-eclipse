package org.anachronos.clojure.ui.editor;

import org.anachronos.clojure.ui.Activator;
import org.anachronos.clojure.ui.ClojureUILanguageToolkit;
import org.anachronos.clojure.ui.preferences.ClojurePreferenceConstants;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

/**
 * Editor for clojure files with syntax highlighting and parens matching.
 * 
 * @author km
 */
public class ClojureEditor extends TextEditor {

    public ClojureEditor() {
	super();
	final IColorManager colorManager = Activator.getDefault()
		.getTextTools()
		.getColorManager();
	final IPreferenceStore preferenceStore = ClojureUILanguageToolkit
		.getDefault().getPreferenceStore();
	final ClojureSourceViewerConfiguration configuration = new ClojureSourceViewerConfiguration(
		colorManager,
		preferenceStore,
		this);
	setSourceViewerConfiguration(configuration);
	setDocumentProvider(new FileDocumentProvider());
	setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected void configureSourceViewerDecorationSupport(
	    SourceViewerDecorationSupport support) {
	super.configureSourceViewerDecorationSupport(support);
	support.setCharacterPairMatcher(new DefaultCharacterPairMatcher(
		new char[] { '(', ')', '[', ']', '{', '}' }));
	support.setMatchingCharacterPainterPreferenceKeys(
		ClojurePreferenceConstants.HIGHLIGHT_MATCHING_BRACKETS,
		ClojurePreferenceConstants.MATCHING_BRACKETS_COLOR);
	support.install(getPreferenceStore());
    }
}
