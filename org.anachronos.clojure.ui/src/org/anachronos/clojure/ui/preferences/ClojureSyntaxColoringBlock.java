package org.anachronos.clojure.ui.preferences;

import java.io.InputStream;

import org.anachronos.clojure.ui.editor.ClojureSourceViewerConfiguration;
import org.eclipse.dltk.internal.ui.editor.ScriptSourceViewer;
import org.eclipse.dltk.ui.preferences.AbstractScriptEditorColoringConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.preferences.PreferencesMessages;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.texteditor.ITextEditor;

public class ClojureSyntaxColoringBlock extends
	AbstractScriptEditorColoringConfigurationBlock {

    public ClojureSyntaxColoringBlock(final OverlayPreferenceStore store) {
	super(store);
    }

    @Override
    protected ProjectionViewer createPreviewViewer(Composite parent,
	    IVerticalRuler verticalRuler, IOverviewRuler overviewRuler,
	    boolean showAnnotationsOverview, int styles, IPreferenceStore store) {
	return new ScriptSourceViewer(parent, verticalRuler, overviewRuler,
		showAnnotationsOverview, styles, store);
    }

    @Override
    protected ScriptSourceViewerConfiguration createSimpleSourceViewerConfiguration(
	    IColorManager colorManager, IPreferenceStore preferenceStore,
	    ITextEditor editor, boolean configureFormatter) {
	return new ClojureSourceViewerConfiguration(colorManager,
		preferenceStore, editor);
    }

    @Override
    protected String[][] getSyntaxColorListModel() {
	return new String[][] {
		new String[] {
			"Character",
			ClojureColorConstants.CHARACTER_COLOR_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Comment",
			ClojureColorConstants.COMMENT_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTKdoc },

		new String[] {
			"Number",
			ClojureColorConstants.NUMBER_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"String",
			ClojureColorConstants.STRING_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },

		new String[] {
			"List",
			ClojureColorConstants.LIST_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Map",
			ClojureColorConstants.MAP_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Vector",
			ClojureColorConstants.VECTOR_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },

		new String[] {
			"Keyword",
			ClojureColorConstants.KEYWORD_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Symbol",
			ClojureColorConstants.SYMBOL_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Predefined Symbol",
			ClojureColorConstants.PREDEFINED_SYMBOL_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK } };
    }

    @Override
    protected void setDocumentPartitioning(IDocument document) {
    }

    @Override
    protected InputStream getPreviewContentReader() {
	return getClass().getResourceAsStream("Preview.clj");
    }
}
