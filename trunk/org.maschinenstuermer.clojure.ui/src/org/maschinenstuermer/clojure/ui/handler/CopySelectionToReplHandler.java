package org.maschinenstuermer.clojure.ui.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class CopySelectionToReplHandler extends AbstractCopyTextToReplHandler {

	@Override
	public String getTextToCopy(ExecutionEvent event) {
		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		final ITextSelection textSelection = (ITextSelection) selection;
		final String text = textSelection.getText();
		return text;
	}
}
