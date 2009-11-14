package org.anachronos.clojure.ui.internal.text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

public class ClojureAutoEditStrategy implements IAutoEditStrategy {
    private final static Set<String> AUTO_CLOSE_TRIGGERS = new HashSet<String>(
	    Arrays.asList("(", "[", "{", "\""));
    private final static Map<String, String> TRIGGER_AUTO_CLOSE = new HashMap<String, String>();

    static {
	TRIGGER_AUTO_CLOSE.put("(", ")");
	TRIGGER_AUTO_CLOSE.put("{", "}");
	TRIGGER_AUTO_CLOSE.put("[", "]");
	TRIGGER_AUTO_CLOSE.put("\"", "\"");
    };

    @Override
    public void customizeDocumentCommand(final IDocument document,
	    final DocumentCommand command) {
	if (triggersAutoClose(document, command)) {
	    final String autoClose = TRIGGER_AUTO_CLOSE.get(command.text);
	    command.text = command.text + autoClose;
	    command.shiftsCaret = false;
	    command.caretOffset = command.offset + autoClose.length();
	}
    }

    public boolean triggersAutoClose(final IDocument document,
	    final DocumentCommand command) {
	boolean triggersAutoClosing = false;
	try {
	    triggersAutoClosing = command.text.length() == 1
		    && document.getContentType(command.offset).equals(
			    IDocument.DEFAULT_CONTENT_TYPE)
		    && AUTO_CLOSE_TRIGGERS.contains(command.text);
	} catch (BadLocationException e) {
	    // should never happen
	    e.printStackTrace();
	}
	return triggersAutoClosing;
    }
}
