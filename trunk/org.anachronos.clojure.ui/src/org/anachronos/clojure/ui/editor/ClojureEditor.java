package org.anachronos.clojure.ui.editor;

import org.anachronos.clojure.ui.ClojureLanguageToolkit;
import org.anachronos.clojure.ui.ClojureUILanguageToolkit;
import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.anachronos.clojure.ui.preferences.ClojurePreferenceConstants;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

/**
 * Editor for clojure files with syntax highlighting and parens matching.
 * 
 * @author km
 */
public class ClojureEditor extends ScriptEditor {
    private static final String ID = "org.anachronos.clojure.ui.editor";

    public ClojureEditor() {
	super();
	final IColorManager colorManager = ClojureUIPlugin.getDefault()
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
    }

    @Override
    protected void initializeEditor() {
	super.initializeEditor();
	setPreferenceStore(ClojureUIPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected boolean affectsTextPresentation(PropertyChangeEvent event) {
	return getScriptSourceViewerConfiguration().affectsTextPresentation(
		event)
		|| super.affectsTextPresentation(event);
    }

    private ScriptSourceViewerConfiguration getScriptSourceViewerConfiguration() {
	return (ScriptSourceViewerConfiguration) getSourceViewerConfiguration();
    }

    @Override
    protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
	final ScriptSourceViewerConfiguration sourceViewerConfig = getScriptSourceViewerConfiguration();
	sourceViewerConfig.handlePropertyChangeEvent(event);
	super.handlePreferenceStoreChanged(event);
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

    @Override
    public String getEditorId() {
	return ID;
    }

    @Override
    public IDLTKLanguageToolkit getLanguageToolkit() {
	return ClojureLanguageToolkit.getDefault();
    }

    @Override
    protected IPreferenceStore getScriptPreferenceStore() {
	return ClojureUIPlugin.getDefault().getPreferenceStore();
    }
}
