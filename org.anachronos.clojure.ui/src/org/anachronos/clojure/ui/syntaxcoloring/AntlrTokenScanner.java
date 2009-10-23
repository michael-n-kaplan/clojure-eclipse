package org.anachronos.clojure.ui.syntaxcoloring;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Lexer;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.IColorManagerExtension;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Implementation of an {@link ITokenScanner} based on an ANTLR {@link Lexer}.
 * Inspired by
 * org.eclipse.xtext.ui.common.editor.syntaxcoloring.antlr.AntlrTokenScanner
 * from the xtext eclipse project.
 * 
 * @author km
 */
public class AntlrTokenScanner implements ITokenScanner {
    private final Lexer lexer;
    private final IColorManager colorManager;
    private final IPreferenceStore preferenceStore;

    private final Map<Integer, String> antlrTokenTypeToTokenPrefKey = new HashMap<Integer, String>();

    private CommonToken currentAntlrToken;
    private int dirtyRegionOffset;

    private final Map<String, Token> tokenMap = new HashMap<String, Token>();

    private String[] propertyNamesColor;

    private boolean needsLazyColorLoading;

    private AntlrTokenScanner(final Lexer lexer,
	    final IColorManager colorManager, IPreferenceStore preferenceStore) {
	this.lexer = lexer;
	this.colorManager = colorManager;
	this.preferenceStore = preferenceStore;
    }

    /**
     * Factory method to create an {@link AntlrTokenScanner}.
     * 
     * @param lexer
     * @param colorManager
     *            TODO
     * @param colorManager
     * @return created token scanner
     */
    public static AntlrTokenScanner createTokenScanner(final Lexer lexer,
	    IColorManager colorManager, final IPreferenceStore preferenceStore) {
	return new AntlrTokenScanner(lexer, colorManager, preferenceStore);
    }

    /**
     * Adds the preference key to use for the given antlr tokens.
     * 
     * @param tokenPrefKey
     * @param antlrTokenTypes
     */
    public void addTokenPrefKey(final String tokenPrefKey,
	    final int... antlrTokenTypes) {
	for (int antlrTokenType : antlrTokenTypes) {
	    antlrTokenTypeToTokenPrefKey.put(antlrTokenType, tokenPrefKey);
	}
    }

    /**
     * {@inheritDoc}
     */
    public int getTokenLength() {
	return currentAntlrToken.getStopIndex()
		- currentAntlrToken.getStartIndex() + 1;
    }

    /**
     * {@inheritDoc}
     */
    public int getTokenOffset() {
	return dirtyRegionOffset + currentAntlrToken.getStartIndex();
    }

    /**
     * {@inheritDoc}
     */
    public IToken nextToken() {
	currentAntlrToken = (CommonToken) lexer.nextToken();
	final int type = currentAntlrToken.getType();
	if (type == org.antlr.runtime.Token.EOF) {
	    return Token.EOF;
	}
	final String tokenPrefKey = antlrTokenTypeToTokenPrefKey.get(type);
	final IToken token = getToken(tokenPrefKey);
	return token == null ? Token.UNDEFINED : token;
    }

    /**
     * {@inheritDoc}
     */
    public void setRange(IDocument document, int offset, int length) {
	try {
	    String dirtyRegion = document.get(offset, length);
	    dirtyRegionOffset = offset;
	    lexer.setCharStream(new ANTLRStringStream(dirtyRegion));
	} catch (BadLocationException e) {
	    e.printStackTrace();
	}
    }

    public boolean affectsBehavior(PropertyChangeEvent event) {
	return indexOf(event.getProperty()) >= 0;
    }

    private int indexOf(String property) {
	if (property != null) {
	    for (int i = 0; i < propertyNamesColor.length; i++) {
		if (property.startsWith(propertyNamesColor[i]))
		    return i;
	    }
	}
	return -1;
    }

    public void adaptToPreferenceChange(PropertyChangeEvent event) {
	final String property = event.getProperty();
	final int index = indexOf(property);
	final Token token = getToken(propertyNamesColor[index]);
	if (propertyNamesColor[index].equals(property))
	    adaptToColorChange(token, event);
	else if (property.startsWith(propertyNamesColor[index])) {
	    if (property.endsWith(PreferenceConstants.EDITOR_BOLD_SUFFIX))
		adaptToStyleChange(token, event, SWT.BOLD);
	    else if (property
		    .endsWith(PreferenceConstants.EDITOR_ITALIC_SUFFIX))
		adaptToStyleChange(token, event, SWT.ITALIC);
	    else if (property
		    .endsWith(PreferenceConstants.EDITOR_STRIKETHROUGH_SUFFIX))
		adaptToStyleChange(token, event, TextAttribute.STRIKETHROUGH);
	    else if (property
		    .endsWith(PreferenceConstants.EDITOR_UNDERLINE_SUFFIX))
		adaptToStyleChange(token, event, TextAttribute.UNDERLINE);
	}
    }

    private void adaptToColorChange(Token token, PropertyChangeEvent event) {
	RGB rgb = null;

	Object value = event.getNewValue();
	if (value instanceof RGB)
	    rgb = (RGB) value;
	else if (value instanceof String)
	    rgb = StringConverter.asRGB((String) value);

	if (rgb != null) {

	    String property = event.getProperty();
	    Color color = colorManager.getColor(property);

	    if ((color == null || !rgb.equals(color.getRGB()))
		    && colorManager instanceof IColorManagerExtension) {
		IColorManagerExtension ext = (IColorManagerExtension) colorManager;

		ext.unbindColor(property);
		ext.bindColor(property, rgb);

		color = colorManager.getColor(property);
	    }

	    Object data = token.getData();
	    if (data instanceof TextAttribute) {
		TextAttribute oldAttr = (TextAttribute) data;
		token.setData(new TextAttribute(color, oldAttr.getBackground(),
			oldAttr.getStyle()));
	    }
	}
    }

    private void adaptToStyleChange(Token token, PropertyChangeEvent event,
	    int styleAttribute) {
	boolean eventValue = false;
	Object value = event.getNewValue();
	if (value instanceof Boolean)
	    eventValue = ((Boolean) value).booleanValue();
	else if (IPreferenceStore.TRUE.equals(value))
	    eventValue = true;

	Object data = token.getData();
	if (data instanceof TextAttribute) {
	    TextAttribute oldAttr = (TextAttribute) data;
	    boolean activeValue = (oldAttr.getStyle() & styleAttribute) == styleAttribute;
	    if (activeValue != eventValue)
		token.setData(new TextAttribute(oldAttr.getForeground(),
			oldAttr.getBackground(), eventValue ? oldAttr
				.getStyle()
				| styleAttribute : oldAttr.getStyle()
				& ~styleAttribute));
	}
    }

    protected Token getToken(String key) {
	if (needsLazyColorLoading)
	    resolveProxyAttributes();
	return tokenMap.get(key);
    }

    private void resolveProxyAttributes() {
	if (needsLazyColorLoading && Display.getCurrent() != null) {
	    for (int i = 0; i < propertyNamesColor.length; i++) {
		addToken(propertyNamesColor[i]);
	    }
	    needsLazyColorLoading = false;
	}
    }

    private void addTokenWithProxyAttribute(final String colorKey) {
	tokenMap.put(colorKey, new Token(createTextAttribute(colorKey)));
    }

    private void addToken(final String colorKey) {
	if (colorManager != null && colorKey != null) {
	    final RGB rgb = PreferenceConverter.getColor(preferenceStore,
		    colorKey);
	    if (colorManager instanceof IColorManagerExtension) {
		final IColorManagerExtension colorManagerExtension = (IColorManagerExtension) colorManager;
		colorManagerExtension.unbindColor(colorKey);
		colorManagerExtension.bindColor(colorKey, rgb);
	    }
	}

	if (!needsLazyColorLoading)
	    tokenMap.put(colorKey, new Token(createTextAttribute(colorKey)));
	else {
	    final Token token = tokenMap.get(colorKey);
	    if (token != null)
		token.setData(createTextAttribute(colorKey));
	}
    }

    private TextAttribute createTextAttribute(String colorKey) {
	Color color = null;
	if (colorKey != null)
	    color = colorManager.getColor(colorKey);

	int style = preferenceStore.getBoolean(getBoldKey(colorKey)) ? SWT.BOLD
		: SWT.NORMAL;
	if (preferenceStore.getBoolean(getItalicKey(colorKey)))
	    style |= SWT.ITALIC;

	if (preferenceStore.getBoolean(getStrikethroughKey(colorKey)))
	    style |= TextAttribute.STRIKETHROUGH;

	if (preferenceStore.getBoolean(getUnderlineKey(colorKey)))
	    style |= TextAttribute.UNDERLINE;

	return new TextAttribute(color, null, style);
    }

    private String getBoldKey(String colorKey) {
	return colorKey + PreferenceConstants.EDITOR_BOLD_SUFFIX;
    }

    private String getItalicKey(String colorKey) {
	return colorKey + PreferenceConstants.EDITOR_ITALIC_SUFFIX;
    }

    private String getStrikethroughKey(String colorKey) {
	return colorKey + PreferenceConstants.EDITOR_STRIKETHROUGH_SUFFIX;
    }

    private String getUnderlineKey(String colorKey) {
	return colorKey + PreferenceConstants.EDITOR_UNDERLINE_SUFFIX;
    }

    public void initialize() {
	propertyNamesColor = antlrTokenTypeToTokenPrefKey.values().toArray(
		new String[] {});
	needsLazyColorLoading = Display.getCurrent() == null;
	for (int i = 0; i < propertyNamesColor.length; i++) {
	    if (needsLazyColorLoading)
		addTokenWithProxyAttribute(propertyNamesColor[i]);
	    else
		addToken(propertyNamesColor[i]);
	}
    }
}
