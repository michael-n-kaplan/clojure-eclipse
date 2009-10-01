package org.anchronos.clojure.ui.editor;

import org.anchronos.clojure.ui.Activator;
import org.anchronos.clojure.ui.preferences.ClojurePreferenceConstants;
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

    private final ColorManager colorManager;

    public ClojureEditor() {
	super();
	colorManager = new ColorManager();
	setSourceViewerConfiguration(new ClojureConfiguration(colorManager));
	setDocumentProvider(new FileDocumentProvider());
	setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    public void dispose() {
	colorManager.dispose();
	super.dispose();
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
