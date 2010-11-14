package org.maschinenstuermer.clojure.ui.preference;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.ResourceManager;

public class ClojureInstallDialog extends TitleAreaDialog implements ModifyListener {
	private Text nameText;
	private Text locationText;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ClojureInstallDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.RESIZE);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(ResourceManager.getPluginImage("org.eclipse.jdt.debug.ui", "/icons/full/wizban/library_wiz.png"));
		setMessage("Select a clojure runtime jar and enter a name for it.");
		setTitle("Clojure runtime installation");
		Composite area = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) area.getLayout();
		gridLayout.verticalSpacing = 1;
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		GridData gd_container = new GridData(GridData.FILL_BOTH);
		gd_container.horizontalIndent = 5;
		gd_container.verticalIndent = 5;
		container.setLayoutData(gd_container);
		
		Label lblLocation = new Label(container, SWT.NONE);
		lblLocation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLocation.setText("Location:");
		
		locationText = new Text(container, SWT.BORDER);
		locationText.addModifyListener(this);
		locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnFile = new Button(container, SWT.NONE);
		btnFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final FileDialog fileDialog = new FileDialog(getParentShell());
				fileDialog.setFilterPath(locationText.getText());
				fileDialog.setFilterExtensions(new String[] { "*.jar" });
				fileDialog.setText("Select Clojure runtime");
				final String selectedFile = fileDialog.open();
				if (selectedFile != null) {
					locationText.setText(selectedFile);
				}
			}
		});
		btnFile.setText("File...");
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name:");
		
		nameText = new Text(container, SWT.BORDER);
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
			}
		});
		nameText.addModifyListener(this);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		new Label(container, SWT.NONE);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true).setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	public void modifyText(ModifyEvent e) {
		final String errorMessage;
		if (e.getSource() == locationText) {
			final String location = locationText.getText();
			final boolean noValidFileSelected = location == null
			|| !location.endsWith(".jar")
			|| !new File(location).exists();
			errorMessage = noValidFileSelected ?
					"Select an existing jar file!" : null;
			
		} else { // e.getSource() == nameText
			final String name = nameText.getText();
			final boolean noNameEntered = name == null 
			|| name.length() == 0;
			errorMessage = noNameEntered ? 
					"Enter a name!" : null;
		}
		setErrorMessage(errorMessage);
		getButton(OK).
			setEnabled(getErrorMessage() == null);
	}
}
