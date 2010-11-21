package org.maschinenstuermer.clojure.ui.handler;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.UIJob;
import org.maschinenstuermer.clojure.ui.console.ClojureConsole;
import org.maschinenstuermer.clojure.ui.console.ClojureReplConsoleFactory;
import org.maschinenstuermer.clojure.ui.internal.ClojureActivator;

public class EvaluateSelectionInRepl extends AbstractHandler implements IHandler {

	private static final class ScheduleCopySelection implements IDocumentListener {
		private final IDocument document;
		private final String selectedText;

		private ScheduleCopySelection(final IDocument document, final String selectedText) {
			this.document = document;
			this.selectedText = selectedText;
		}

		@Override
		public void documentChanged(final DocumentEvent _) {
			boolean isWaitingForInput = isWaitingForInput(document);
			
			final IDocumentListener documentListener = this;
			if (isWaitingForInput) {
				final Job copySelectionJob = new UIJob("Copy selection to console!") {
					@Override
					public IStatus runInUIThread(final IProgressMonitor monitor) {
						document.removeDocumentListener(documentListener);
						return copyText(document, selectedText);
					}
				};
				copySelectionJob.schedule();
			}
		}

		@Override
		public void documentAboutToBeChanged(final DocumentEvent event) {
		}
	}

	private static boolean isWaitingForInput(final IDocument document) {
		final int lastLine = document.getNumberOfLines() - 1;
		IRegion lineInformation;
		boolean isWaitingForInput = false;
		try {
			lineInformation = document.getLineInformation(lastLine);
			final String lastLineAsString = document.get(lineInformation.getOffset(), 
					lineInformation.getLength());
			isWaitingForInput = lastLineAsString.endsWith("=> ");
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return isWaitingForInput;
	}

	private static IStatus copyText(final IDocument document, final String selectedText) {
		try {
			document.replace(document.getLength(), 0, selectedText);
		} catch (final BadLocationException e) {
			final String pluginId = ClojureActivator.getInstance().getBundle().getSymbolicName();
			return new Status(Status.ERROR, pluginId, "Error in copy selection to console!", e);
		}
		return Status.OK_STATUS;
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {		
		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		final ITextSelection textSelection = (ITextSelection) selection;
		final String selectedText = textSelection.getText().concat("\n");
		
		final ClojureReplConsoleFactory consoleFactory = ClojureReplConsoleFactory.getDefault();
		final ClojureConsole clojureConsole = consoleFactory.openAndShowConsole();		
		final IDocument document = clojureConsole.getDocument();
		if (isWaitingForInput(document)) {
			copyText(document, selectedText);
		} else {
			document.addDocumentListener(new ScheduleCopySelection(document, selectedText));
		}
		return null;
	}
}
