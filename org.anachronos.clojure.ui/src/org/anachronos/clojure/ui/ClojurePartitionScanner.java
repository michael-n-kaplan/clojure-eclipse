package org.anachronos.clojure.ui;

import java.util.ArrayList;
import java.util.List;

import org.anachronos.clojure.ui.internal.text.IClojurePartitions;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class ClojurePartitionScanner extends RuleBasedPartitionScanner {

    public ClojurePartitionScanner() {
	super();
	
	final IToken stringPartitionToken = new Token(IClojurePartitions.STRING);
	final IToken commentPartitionToken = new Token(
		IClojurePartitions.COMMENT);

	final List<IPredicateRule> rules= new ArrayList<IPredicateRule>();

	rules.add(new EndOfLineRule(";", commentPartitionToken));
	rules.add(new MultiLineRule("\"", "\"", stringPartitionToken));
	
	final IPredicateRule[] rulesArray = new IPredicateRule[rules.size()];
	rules.toArray(rulesArray);
	
	setPredicateRules(rulesArray);

	setDefaultReturnToken(new Token(IDocument.DEFAULT_CONTENT_TYPE));
    }
}
