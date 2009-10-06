package org.anachronos.clojure.ui;

import org.anachronos.clojure.ui.editor.ClojureSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.ITextEditor;

public class ClojureTextTools extends ScriptTextTools {

    protected ClojureTextTools(final boolean autoDisposeOnDisplayDispose) {
	super(IDocument.DEFAULT_CONTENT_TYPE,
		new String[] { IDocument.DEFAULT_CONTENT_TYPE },
		autoDisposeOnDisplayDispose);
    }

    @Override
    public ScriptSourceViewerConfiguration createSourceViewerConfiguraton(
	    IPreferenceStore preferenceStore, ITextEditor editor,
	    String partitioning) {
	return new ClojureSourceViewerConfiguration(getColorManager(),
		preferenceStore, editor);
    }
}
