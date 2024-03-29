package org.maschinenstuermer.clojure.ui.handler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class EvaluateSelectionInReplHandler extends AbstractCopyTextToReplHandler {

	public String getTextToCopy(final ExecutionEvent event) {
		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		final ITextSelection textSelection = (ITextSelection) selection;
		final String selectedText = textSelection.getText().concat("\n");
		return selectedText;
	}
}
