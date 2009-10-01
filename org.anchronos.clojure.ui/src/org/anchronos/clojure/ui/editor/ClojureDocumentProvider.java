package org.anchronos.clojure.ui.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

/**
 * Document provider for clojure documents.
 * 
 * @author km
 */
public class ClojureDocumentProvider extends FileDocumentProvider {
    @Override
    protected IDocument createDocument(final Object element)
	    throws CoreException {
	final IDocument document = super.createDocument(element);
	final IDocumentPartitioner partitioner = new FastPartitioner(
		new RuleBasedPartitionScanner(), new String[] {});
	if (document instanceof IDocumentExtension3) {
	    final IDocumentExtension3 document3 = (IDocumentExtension3) document;
	    document3.setDocumentPartitioner(
		    IDocumentExtension3.DEFAULT_PARTITIONING, partitioner);

	} else if (document != null) {
	    document.setDocumentPartitioner(partitioner);
	}
	partitioner.connect(document);
	return document;
    }
}
