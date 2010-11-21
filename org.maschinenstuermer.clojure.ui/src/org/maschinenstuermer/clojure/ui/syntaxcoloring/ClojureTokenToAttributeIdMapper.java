package org.maschinenstuermer.clojure.ui.syntaxcoloring;

import java.util.Map;

import org.eclipse.xtext.ui.editor.syntaxcoloring.antlr.AbstractAntlrTokenToAttributeIdMapper;

import com.google.common.collect.Maps;

public class ClojureTokenToAttributeIdMapper extends
		AbstractAntlrTokenToAttributeIdMapper {
	private Map<String, String> tokenToId = Maps.newHashMap();
	
	public ClojureTokenToAttributeIdMapper() {
		addIdForTokens(ClojureHighlightingConfiguration.SPECIAL_FORM_ID, 
				"'def'", "'do'", "'dot'", 
				"'finally'", "'fn'",
				"'if'", 
				"'let'", 
				"'loop'",
				"'new'",
				"'meta'", "'monitor-enter'", "'monitor-exit'",
				"'new'",
				"'quote'",
				"'throw'", "'try'", 
				"'recur'", 
				"'var'");
		addIdForTokens(ClojureHighlightingConfiguration.LITERAL_ID, 
				"RULE_STRING", "RULE_CHAR", "'true'", "'false'", "'nil'");
		addIdForTokens(ClojureHighlightingConfiguration.NUMBER_ID, 
			"RULE_NUMBER");
		addIdForTokens(ClojureHighlightingConfiguration.KEYWORD_ID, 
			"RULE_KEYWORD",
			"':as'",
			"':import'",
			"':exclude'",
			"':refer-clojure'",
			"':require'",
			"':use'");
		addIdForTokens(ClojureHighlightingConfiguration.COMMENT_ID, 
			"RULE_SL_COMMENT");
		addIdForTokens(ClojureHighlightingConfiguration.MACRO_CALL_ID, 
			"'defn'", "'defn-'", "'defmacro'", "'defstruct'",
			"'import'", "'in-ns'",
			"'ns'");
	}
	
	@Override
	protected String calculateId(final String tokenName, final int tokenType) {
		final String id = tokenToId.get(tokenName);
		return id == null ? 
				ClojureHighlightingConfiguration.DEFAULT_ID : id;
	}

	private void addIdForTokens(final String id, final String...tokens) {
		for (final String token : tokens) {
			tokenToId.put(token, id);
		}
	}
}
