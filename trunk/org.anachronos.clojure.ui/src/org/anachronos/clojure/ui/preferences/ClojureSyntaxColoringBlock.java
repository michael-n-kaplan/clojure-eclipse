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
			"Symbol",
			"Symbol",
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Predefined Symbol",
			"Predefined Symbol",
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Number",
			"Number",
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"String",
			"String",
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Character",
			"Character",
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"List",
			"List",
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Vector",
			"Vector",
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Map",
			"Map",
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTK },
		new String[] {
			"Comment",
			"Comment",
			PreferencesMessages.DLTKEditorPreferencePage_coloring_category_DLTKdoc } };
    }

    @Override
    protected void setDocumentPartitioning(IDocument document) {
    }

    @Override
    protected InputStream getPreviewContentReader() {
	return getClass().getResourceAsStream("Preview.clj");
    }
}
