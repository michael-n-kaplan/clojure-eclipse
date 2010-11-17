package org.maschinenstuermer.clojure.ui.console;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IFlushableStreamMonitor;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.TextConsoleViewer;
import org.eclipse.ui.internal.console.IOConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;

@SuppressWarnings("restriction")
public class ClojureConsole extends IOConsole {
	private final class StreamListener implements IStreamListener, ITextListener {

		private final IOConsoleOutputStream outputStream;
		private final IStreamMonitor streamMonitor;
		private boolean flushed;

		private StreamListener(IStreamMonitor monitor, IOConsoleOutputStream outputStream) {
			this.streamMonitor = monitor;
			this.outputStream = outputStream;
			streamAppended(null, monitor);
		}
		
		@Override
		public void streamAppended(String text, IStreamMonitor monitor) {
			final TextConsoleViewer viewer = page != null ? page.getViewer() : null;
			if (viewer != null) 
				viewer.addTextListener(this);
			if (flushed) {
				try {
					if (outputStream != null) {
						outputStream.write(text);
					}
				} catch (IOException e) {
					// TODO
					e.printStackTrace();
				}
			} else {
				String contents = null;
				synchronized (streamMonitor) {
					flushed = true;
					contents = streamMonitor.getContents();
					if (streamMonitor instanceof IFlushableStreamMonitor) {
						IFlushableStreamMonitor m = (IFlushableStreamMonitor) streamMonitor;
						m.flushContents();
						m.setBuffered(false);
					}
				}
				try {
					if (contents != null && contents.length() > 0) {
						if (outputStream != null) {
							outputStream.write(contents);
						}
					}
				} catch (IOException e) {
					// TODO
					e.printStackTrace();
				}
			}
		}

		@Override
		public void textChanged(TextEvent _event) {
			final StyledText styledText = (StyledText) page.getViewer().getControl();
			styledText.setCaretOffset(styledText.getCharCount());
		}
	}

	private class InputReadJob extends Job {
		private final IStreamsProxy streamsProxy;

		InputReadJob(IStreamsProxy streamsProxy) {
			super("Clojure REPL Input Job"); //$NON-NLS-1$
			this.streamsProxy = streamsProxy;
		}

		protected IStatus run(final IProgressMonitor monitor) {
			try {
				final byte[] b = new byte[1024];
				int read = 0;
				final IOConsoleInputStream inputStream = getInputStream();
				while (inputStream != null && read >= 0) {
					read = inputStream.read(b);
					if (read > 0) {
						final String s = new String(b, 0, read);
						streamsProxy.write(s);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Status.OK_STATUS;
		}
	}

	private IOConsolePage page;
	private IProcess process;

	public ClojureConsole(final String name, final ImageDescriptor imageDescriptor) {
		super(name, imageDescriptor);
	}

	@Override
	public IPageBookViewPage createPage(final IConsoleView view) {
		final IOConsolePage page = (IOConsolePage) super.createPage(view);
		this.page = page;
		return page;
	}

	public IProcess getProcess() {
		return process;
	}
	
	public void connect(final ILaunch launch) {
		process = launch.getProcesses()[0];
		final IStreamsProxy streamsProxy = process.getStreamsProxy();
		
		final IOConsoleOutputStream outputStream = newOutputStream();
		outputStream.setActivateOnWrite(true);
		outputStream.setFontStyle(SWT.ITALIC);
		final IStreamMonitor outputStreamMonitor = streamsProxy.getOutputStreamMonitor();
		outputStreamMonitor.addListener(new StreamListener(outputStreamMonitor, outputStream));

		getInputStream().setFontStyle(SWT.BOLD);
		final InputReadJob readJob = new InputReadJob(streamsProxy);
		readJob.setSystem(true);
		readJob.schedule();
	}
}
