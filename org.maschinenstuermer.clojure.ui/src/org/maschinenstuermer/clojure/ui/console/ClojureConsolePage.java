package org.maschinenstuermer.clojure.ui.console;

import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.console.TextConsoleViewer;

public class ClojureConsolePage extends TextConsolePage {

	public ClojureConsolePage(final TextConsole console, final IConsoleView view) {
		super(console, view);
	}

	@Override
	protected TextConsoleViewer createViewer(final Composite parent) {
		final TextConsole console = (TextConsole) getConsole();
		final ClojureConsoleViewer viewer = new ClojureConsoleViewer(parent, console);
		viewer.configure(new SourceViewerConfiguration());
		return viewer;
	}
}
