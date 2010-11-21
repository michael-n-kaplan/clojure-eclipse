package org.maschinenstuermer.clojure.ui.handler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;


public class LoadFileInReplHandler extends AbstractCopyTextToReplHandler {

	@Override
	public String getTextToCopy(final ExecutionEvent event) {
		final IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
		activeEditor.doSave(null);
		final IEditorInput editorInput = activeEditor.getEditorInput();
		if (editorInput instanceof IFileEditorInput) {
			final IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
			final String fullPath = fileEditorInput.getFile().getProjectRelativePath().toPortableString();
			return String.format("(load-file \"%s\")\n", fullPath);
		}
		return null;
	}
}
