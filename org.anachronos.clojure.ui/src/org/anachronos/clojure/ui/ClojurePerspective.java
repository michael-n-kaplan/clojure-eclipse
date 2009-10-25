package org.anachronos.clojure.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;

public class ClojurePerspective implements IPerspectiveFactory {

    @Override
    public void createInitialLayout(final IPageLayout layout) {
	final String editorArea = layout.getEditorArea();
	final IFolderLayout folder = layout.createFolder(
		"left", IPageLayout.LEFT, (float) 0.2, editorArea); //$NON-NLS-1$
	final String navigator = "org.eclipse.dltk.ui.ScriptExplorer";

	folder.addView(navigator);

	layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.RIGHT, (float) 0.75,
		editorArea);

	layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);

	layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
	layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
	layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
	layout.addShowViewShortcut(navigator);
	layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
	layout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID);
    }
}
