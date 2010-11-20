package org.maschinenstuermer.clojure.ui.console;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.console.IConsoleDocumentPartitioner;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.console.TextConsoleViewer;

public class ClojureConsoleViewer extends TextConsoleViewer {
	private IDocumentListener documentListener;
    
    public ClojureConsoleViewer(final Composite parent, final TextConsole console) {
        super(parent, console);
    }
        
    protected void handleVerifyEvent(VerifyEvent e) {
        IDocument doc = getDocument();
        String[] legalLineDelimiters = doc.getLegalLineDelimiters();
        String eventString = e.text;
        try {
            IConsoleDocumentPartitioner partitioner = (IConsoleDocumentPartitioner) doc.getDocumentPartitioner();
            if (!partitioner.isReadOnly(e.start)) {
                boolean isCarriageReturn = false;
                for (int i = 0; i < legalLineDelimiters.length; i++) {
                    if (e.text.equals(legalLineDelimiters[i])) {
                        isCarriageReturn = true;
                        break;
                    }
                }

                if (!isCarriageReturn) {
                    super.handleVerifyEvent(e);
                    return;
                }
            }

            int length = doc.getLength();
            if (e.start == length) {
                super.handleVerifyEvent(e);
            } else {
                try {
                    doc.replace(length, 0, eventString);
                } catch (BadLocationException e1) {
                }
                e.doit = false;
            }
        } finally {
            StyledText text = (StyledText) e.widget;
            text.setCaretOffset(text.getCharCount());
        }
    }
   
    private IDocumentListener getDocumentListener() {
    	if (documentListener == null) {
            documentListener = new IDocumentListener() {
                public void documentAboutToBeChanged(DocumentEvent event) {
                }

                public void documentChanged(DocumentEvent event) {
                	revealEndOfDocument();
                }
            };
    	}
    	return documentListener;
    }
    
    public void setDocument(final IDocument document) {
    	final IDocument oldDocument= getDocument();
        
        super.setDocument(document);
        
        if (oldDocument != null) {
            oldDocument.removeDocumentListener(getDocumentListener());
        }
        if (document != null) {
            document.addDocumentListener(getDocumentListener());
        }
    }
}

