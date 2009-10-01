package org.anchronos.clojure.ui.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

/**
 * Scanner used to syntax highlight clojure files.
 * 
 * @author km
 */
public class ClojureScanner extends RuleBasedScanner implements
	ICharacterScanner2 {

    private final class FunctionNameRule implements IRule {
	private final SymbolWordDetector symbolWordDetector = new SymbolWordDetector();
	private final IToken functionNameToken;
	private final IToken reservedFunctionNameToken;
	private final Set<String> reservedFunctionNames = new HashSet<String>();

	private FunctionNameRule(final ColorManager colorManager,
		final String... reservedFunctionNames) {
	    functionNameToken = createToken(colorManager,
		    ClojureColorConstants.SYMBOL, SWT.BOLD);
	    reservedFunctionNameToken = createToken(colorManager,
		    ClojureColorConstants.RESERVED_FUNCTION, SWT.BOLD);
	    this.reservedFunctionNames.addAll(Arrays
		    .asList(reservedFunctionNames));
	}

	@Override
	public IToken evaluate(final ICharacterScanner scanner) {
	    return internalEvaluate((ICharacterScanner2) scanner);
	}

	private IToken internalEvaluate(final ICharacterScanner2 scanner) {
	    IToken token = Token.UNDEFINED;
	    int c = scanner.read();
	    if (c == '(') {
		scanner.skip();
		c = scanner.read();
		char ch = (char) c;
		if (symbolWordDetector.isWordStart(ch)) {
		    final StringBuffer tokenBuffer = new StringBuffer(String
			    .valueOf(ch));
		    do {
			c = scanner.read();
			ch = (char) c;
			tokenBuffer.append(ch);
		    } while (c != ICharacterScanner.EOF
			    && symbolWordDetector.isWordPart(ch));
		    scanner.unread();
		    tokenBuffer.deleteCharAt(tokenBuffer.length() - 1);
		    final String tokenString = tokenBuffer
			    .toString();
		    token = reservedFunctionNames.contains(tokenString) ? reservedFunctionNameToken
			    : functionNameToken;
		} else {
		    scanner.unread();
		}
	    } else {
		scanner.unread();
	    }
	    return token;
	}

    }

    private final class KeywordDetector implements IWordDetector {
	private final Pattern keywordPartRegex = Pattern.compile("[a-zA-Z0-9]");

	@Override
	public boolean isWordStart(final char c) {
	    return c == ':';
	}

	@Override
	public boolean isWordPart(char c) {
	    return keywordPartRegex.matcher(String.valueOf(c)).matches();
	}
    }

    private final class SymbolWordDetector implements IWordDetector {
	private final Pattern symbolStartRegex = Pattern
		.compile("[a-zA-Z0-9\\.]");

	private final Pattern symbolPartRegex = Pattern
		.compile("[a-zA-Z0-9\\*\\+!-_\\?/\\.]");

	@Override
	public boolean isWordStart(final char c) {
	    return symbolStartRegex.matcher(String.valueOf(c)).matches();
	}

	@Override
	public boolean isWordPart(final char c) {
	    return symbolPartRegex.matcher(String.valueOf(c)).matches();
	}
    }

    private final FunctionNameRule functionNameRule;

    public ClojureScanner(final ColorManager colorManager) {
	final List<IRule> rules = new ArrayList<IRule>();

	final IToken symbolToken = createToken(colorManager,
		ClojureColorConstants.SYMBOL);
	final WordRule symbolRule = new WordRule(new SymbolWordDetector(),
		symbolToken);
	final IToken literalToken = createToken(colorManager,
		ClojureColorConstants.LITERAL);
	addWords(symbolRule, literalToken, "false", "nil", "true");
	rules.add(symbolRule);

	final NumberRule numberRule = new NumberRule(literalToken);
	rules.add(numberRule);

	final IToken stringToken = createToken(colorManager,
		ClojureColorConstants.STRING);
	final MultiLineRule stringRule = new MultiLineRule("\"", "\"",
		stringToken);
	rules.add(stringRule);

	final WordRule keywordRule = new WordRule(new KeywordDetector(),
		literalToken);
	rules.add(keywordRule);

	final IToken commentToken = createToken(colorManager,
		ClojureColorConstants.COMMENT);
	final EndOfLineRule commentRule = new EndOfLineRule(";", commentToken);
	rules.add(commentRule);

	functionNameRule = new FunctionNameRule(colorManager, "catch", "def",
		"do", "defmethod", "defmulti", "defmacro", "defstruct", "defn",
		"finally", "fn", "fn*", "if", "identical?", "in-ns", "let",
		"let*", "loop", "loop*", "new", "monitor-enter",
		"monitor-exit", "quote", "recur", "the-var", "throw", "try",
		"set!");
	rules.add(functionNameRule);

	setRules(rules.toArray(new IRule[] {}));
	setDefaultReturnToken(createToken(colorManager,
		ClojureColorConstants.DEFAULT));
    }

    @Override
    public void skip() {
	fTokenOffset = fOffset;
	fColumn = UNDEFINED;
    }

    private void addWords(final WordRule rule, final IToken token,
	    final String... words) {
	for (final String word : words) {
	    rule.addWord(word, token);
	}
    }

    private IToken createToken(final ColorManager colorManager,
	    final RGB foreground) {
	return createToken(colorManager, foreground, SWT.NONE);
    }

    private IToken createToken(final ColorManager colorManager,
	    final RGB foreground, final int style) {
	final IToken symbolToken = new Token(new TextAttribute(colorManager
		.getColor(foreground), null, style));
	return symbolToken;
    }
}
