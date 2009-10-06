package org.anachronos.clojure.ui.syntaxcoloring;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Lexer;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;

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
    private final Map<Integer, IToken> antlrTokenTypeToIToken = new HashMap<Integer, IToken>();

    private CommonToken currentAntlrToken;
    private int dirtyRegionOffset;

    private AntlrTokenScanner(final Lexer lexer) {
	this.lexer = lexer;
    }

    /**
     * Factory method to create an {@link AntlrTokenScanner}.
     * 
     * @param lexer
     * @param colorManager
     * @return created token scanner
     */
    public static AntlrTokenScanner createTokenScanner(final Lexer lexer) {
	return new AntlrTokenScanner(lexer);
    }

    /**
     * Adds a mapping from the ANTLR token type to the corresponding eclipse
     * token used for syntax coloring.
     * 
     * @param antlrTokenType
     * @param token
     */
    public void addToken(final int antlrTokenType, final IToken token) {
	antlrTokenTypeToIToken.put(antlrTokenType, token);
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
	final IToken token = antlrTokenTypeToIToken.get(type);
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
}
