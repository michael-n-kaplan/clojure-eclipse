package org.anachronos.clojure.ui;

import org.anachronos.clojure.ui.editor.ClojureSourceViewerConfiguration;
import org.anachronos.clojure.ui.internal.text.IClojurePartitions;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.ui.texteditor.ITextEditor;

public class ClojureTextTools extends ScriptTextTools {
    public static final String[] LEGAL_CONTENT_TYPES = new String[] {
	    IDocument.DEFAULT_CONTENT_TYPE, IClojurePartitions.COMMENT,
	    IClojurePartitions.STRING };

    private final IPartitionTokenScanner partitionScanner;

    protected ClojureTextTools(final boolean autoDisposeOnDisplayDispose) {
	super(IDocument.DEFAULT_CONTENT_TYPE, LEGAL_CONTENT_TYPES,
		autoDisposeOnDisplayDispose);
	partitionScanner = new ClojurePartitionScanner();
    }

    @Override
    public ScriptSourceViewerConfiguration createSourceViewerConfiguraton(
	    IPreferenceStore preferenceStore, ITextEditor editor,
	    String partitioning) {
	return new ClojureSourceViewerConfiguration(getColorManager(),
		preferenceStore, editor);
    }

    @Override
    public IPartitionTokenScanner getPartitionScanner() {
	return partitionScanner;
    }
}
