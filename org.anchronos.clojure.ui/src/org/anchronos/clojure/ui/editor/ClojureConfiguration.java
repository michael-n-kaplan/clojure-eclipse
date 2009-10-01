package org.anchronos.clojure.ui.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 * Configures source viewer for clojure script editing.
 * 
 * @author km
 */
public class ClojureConfiguration extends SourceViewerConfiguration {
    private final ColorManager colorManager;

    public ClojureConfiguration(final ColorManager colorManager) {
	this.colorManager = colorManager;
    }

    @Override
    public String[] getConfiguredContentTypes(final ISourceViewer sourceViewer) {
	return new String[] { IDocument.DEFAULT_CONTENT_TYPE };
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(
	    final ISourceViewer sourceViewer) {
	final PresentationReconciler reconciler = new PresentationReconciler();

	final ClojureScanner scanner = new ClojureScanner(colorManager);
	final DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
		scanner);
	reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
	reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

	return reconciler;
    }
}