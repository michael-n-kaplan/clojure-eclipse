package org.maschinenstuermer.clojure.ui.preference;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.maschinenstuermer.clojure.install.ClojureInstall;
import org.maschinenstuermer.clojure.install.ClojureInstalls;
import org.osgi.service.prefs.BackingStoreException;

import com.swtdesigner.FieldLayoutPreferencePage;

public class ClojureInstallPreferencePage extends FieldLayoutPreferencePage 
	implements IWorkbenchPreferencePage {
	private final ClojureInstalls clojureInstalls = 
		new ClojureInstalls();
	
	private CheckboxTableViewer tableViewer;
	
	/**
	 * Create the preference page.
	 */
	public ClojureInstallPreferencePage() {
		setTitle("Runtime installations");
	}

	/**
	 * Create contents of the preference page.
	 * @param parent
	 */
	@Override
	public Control createPageContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout gl_container = new GridLayout();
		gl_container.numColumns = 3;
		container.setLayout(gl_container);
		
		Label lblAdd = new Label(container, SWT.NONE);
		lblAdd.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 3, 1));
		lblAdd.setText("Add, remove or edit Clojure runtime installations. By default the checked \ninstallation is automatically added when creating a new Clojure project.\n");
		
		Label lblInstalledClojureRuntimes = new Label(container, SWT.NONE);
		lblInstalledClojureRuntimes.setText("Installed Clojure runtimes:");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		tableViewer = CheckboxTableViewer.newCheckList(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.setColumnProperties(new String[] {});
		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 3));
	
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn.getColumn();
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnLocation = tableViewerColumn_1.getColumn();
		tblclmnLocation.setWidth(100);
		tblclmnLocation.setText("Location");
		final ClojureInstallLabelProvider labelProvider = new ClojureInstallLabelProvider();
		tableViewer.setLabelProvider(labelProvider);
		tableViewer.setContentProvider(new ClojureInstallContentProvider());
		tableViewer.setInput(clojureInstalls);
		tableViewer.setCheckStateProvider(labelProvider);
		
		Button btnAdd = new Button(container, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final ClojureInstallDialog clojureInstallDialog = new ClojureInstallDialog(getShell());
				if (clojureInstallDialog.open() == Window.OK) {
					final ClojureInstall newClojureInstall = clojureInstallDialog.getClojureInstall();
					clojureInstalls.add(newClojureInstall);
					try {
						clojureInstalls.storeToPreferences();
					} catch (BackingStoreException e1) {
						throw new RuntimeException(e1);
					}
					tableViewer.setInput(clojureInstalls);
					final boolean hasDefaultInstall = clojureInstalls.getDefault() != null;
					if (!hasDefaultInstall)
						setErrorMessage("Select a default Clojure runtime!");
					setValid(hasDefaultInstall);
				}
			}
		});
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAdd.setText("Add...");
		
		Button btnEdit = new Button(container, SWT.NONE);
		btnEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnEdit.setText("Edit...");
		
		Button btnRemove = new Button(container, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		btnRemove.setText("Remove");
		return container;
	}
	
	/**
	 * Initialize the preference page.
	 */
	public void init(final IWorkbench workbench) {
		try {
			clojureInstalls.initFromPreferences();
		} catch (BackingStoreException e) {
			throw new RuntimeException(e);
		}
	}
}