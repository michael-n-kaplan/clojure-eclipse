package org.anchronos.clojure.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ClojureBasePreferencePage extends PreferencePage implements
	IWorkbenchPreferencePage {

    public ClojureBasePreferencePage() {
    }

    @Override
    protected Control createContents(Composite parent) {
	initializeDialogUnits(parent);
	final Composite result = new Composite(parent, SWT.NONE);
	return result;
    }

    @Override
    public void init(IWorkbench workbench) {
    }
}
