package org.maschinenstuermer.clojure.ui.syntaxcoloring;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

public class ClojureHighlightingConfiguration extends
		DefaultHighlightingConfiguration {
	
	public static final String FUNCTION_CALL_ID = "functionCall";
	public static final String LITERAL_ID = "literal";
	public static final String MACRO_CALL_ID = "macroCall";
	public static final String SPECIAL_FORM_ID = "specialForm";

	@Override
	public void configure(final IHighlightingConfigurationAcceptor acceptor) {
		acceptor.acceptDefaultHighlighting(FUNCTION_CALL_ID, "Function call", functionCallTextStyle());
		acceptor.acceptDefaultHighlighting(KEYWORD_ID, "Keyword", keywordTextStyle());
		acceptor.acceptDefaultHighlighting(MACRO_CALL_ID, "Macro call", macroCallTextStyle());
		acceptor.acceptDefaultHighlighting(PUNCTUATION_ID, "Punctuation character", punctuationTextStyle());
		acceptor.acceptDefaultHighlighting(SPECIAL_FORM_ID, "Special form", specialFormTextStyle());
		
		acceptor.acceptDefaultHighlighting(LITERAL_ID, "Literal", stringTextStyle());
		
		acceptor.acceptDefaultHighlighting(COMMENT_ID, "Comment", commentTextStyle());
		acceptor.acceptDefaultHighlighting(DEFAULT_ID, "Default", defaultTextStyle());
		acceptor.acceptDefaultHighlighting(INVALID_TOKEN_ID, "Invalid Symbol", errorTextStyle());
	}

	@Override
	public TextStyle keywordTextStyle() {
		final TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(127, 0, 0));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle macroCallTextStyle() {
		TextStyle textStyle = specialFormTextStyle().copy();
		textStyle.setStyle(SWT.NONE);
		return textStyle;
	}

	public TextStyle functionCallTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(0, 0, 85));
		textStyle.setStyle(SWT.NONE);
		return textStyle;
	}

	public TextStyle specialFormTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(127, 0, 85));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}
}
