package org.maschinenstuermer.clojure.ui.preference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.swtdesigner.FieldLayoutPreferencePage;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ClojureInstallPreferencePage extends FieldLayoutPreferencePage 
	implements IWorkbenchPreferencePage {
	private Table table;

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
		
		table = new Table(container, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 3));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnName = new TableColumn(table, SWT.LEFT);
		tblclmnName.setWidth(181);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnLocation = new TableColumn(table, SWT.LEFT);
		tblclmnLocation.setWidth(281);
		tblclmnLocation.setText("Location");
		
		Button btnAdd = new Button(container, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ClojureInstallDialog(getShell()).open();
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
	}
}
