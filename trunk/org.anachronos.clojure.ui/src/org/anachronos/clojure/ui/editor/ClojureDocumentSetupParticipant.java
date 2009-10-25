package org.anachronos.clojure.ui.editor;

import org.anachronos.clojure.ui.ClojureTextTools;
import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;

public class ClojureDocumentSetupParticipant implements
	IDocumentSetupParticipant {
    public final static String CLOJURE_PARTIONING = "__clojure_partioning";

    @Override
    public void setup(IDocument document) {
	final ClojureTextTools textTools = ClojureUIPlugin.getDefault()
		.getTextTools();
	textTools.setupDocumentPartitioner(document, CLOJURE_PARTIONING);
    }
}
