package org.anachronos.clojure.ui.preferences;

import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class ClojureEditorPreferencePage extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

    public ClojureEditorPreferencePage() {
	super(GRID);
	setPreferenceStore(ClojureUIPlugin.getDefault().getPreferenceStore());
    }

    @Override
    public void createFieldEditors() {
	addField(new BooleanFieldEditor(
		ClojurePreferenceConstants.HIGHLIGHT_MATCHING_BRACKETS,
		"Hightlight &matching brackets", getFieldEditorParent()));
	addField(new ColorFieldEditor(
		ClojurePreferenceConstants.MATCHING_BRACKETS_COLOR,
		"Matching brackets highlight", getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }

}