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
			ClojureColorPrefConstants.CHARACTER_COLOR_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Comment",
			ClojureColorPrefConstants.COMMENT_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_comments },

		new String[] {
			"Number",
			ClojureColorPrefConstants.NUMBER_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"String",
			ClojureColorPrefConstants.STRING_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },

		new String[] {
			"List",
			ClojureColorPrefConstants.LIST_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Map",
			ClojureColorPrefConstants.MAP_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Vector",
			ClojureColorPrefConstants.VECTOR_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },

		new String[] {
			"Keyword",
			ClojureColorPrefConstants.KEYWORD_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Symbol",
			ClojureColorPrefConstants.SYMBOL_PREF_KEY,
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Predefined Symbol",
			ClojureColorPrefConstants.PREDEFINED_SYMBOL_PREF_KEY,
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
