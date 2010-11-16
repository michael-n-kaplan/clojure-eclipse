package org.maschinenstuermer.clojure.ui.repl;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.debug.ui.launcher.AbstractJavaMainTab;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

@SuppressWarnings("restriction")
public class ClojureReplMainTab extends AbstractJavaMainTab {

	@Override
	public void createControl(final Composite parent) {
		final Composite composite = SWTFactory.createComposite(parent, parent.getFont(), 1, 1, GridData.FILL_BOTH);
		final GridLayout layout = (GridLayout)composite.getLayout();
		layout.verticalSpacing = 0;
		createProjectEditor(composite);
		setControl(composite);
	}

	@Override
	public void setDefaults(final ILaunchConfigurationWorkingCopy config) {
		IJavaElement javaElement = getContext();
		if (javaElement != null) {
			initializeJavaProject(javaElement, config);
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, fProjText.getText().trim());
		mapResources(config);		
	}

	@Override
	public String getName() {
		return "REPL";
	}
}
