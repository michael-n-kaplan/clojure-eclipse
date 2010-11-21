package org.maschinenstuermer.clojure.ui;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.text.ITextSelection;

public class TextSelectionPropertyTester extends PropertyTester {

	public TextSelectionPropertyTester() {
	}

	@Override
	public boolean test(final Object receiver, final String property, final Object[] args,
			final Object expectedValue) {
		final ITextSelection textSelection = (ITextSelection) receiver;
		if ("notEmpty".equals(property)) {
			final String selectedText = textSelection.getText();
			final boolean notEmpty = selectedText != null 
				&& selectedText.length() > 0;
			return notEmpty;
		}
		return false;
	}
}
