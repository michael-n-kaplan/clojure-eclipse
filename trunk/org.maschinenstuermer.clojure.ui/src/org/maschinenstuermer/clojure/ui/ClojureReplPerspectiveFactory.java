package org.maschinenstuermer.clojure.ui;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;

public class ClojureReplPerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(final IPageLayout layout) {
 		final String editorArea = layout.getEditorArea();

		final IFolderLayout navigationFolder = layout.createFolder("left", 
				IPageLayout.LEFT, (float) 0.25, editorArea);
		navigationFolder.addView(IPageLayout.ID_PROJECT_EXPLORER);
		navigationFolder.addView(JavaUI.ID_PACKAGES);

		final IFolderLayout outputfolder= layout.createFolder("bottom", IPageLayout.BOTTOM, 
				(float) 0.75, editorArea); 
		outputfolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		outputfolder.addView(IPageLayout.ID_PROBLEM_VIEW);
		outputfolder.addView(IProgressConstants.PROGRESS_VIEW_ID);

		final IFolderLayout outlineFolder = layout.createFolder("right", IPageLayout.RIGHT, 
				(float) 0.75, editorArea); 
		outlineFolder.addView(IPageLayout.ID_OUTLINE);
	}
}
