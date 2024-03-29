package org.maschinenstuermer.clojure.ui.contentassist;

import java.util.regex.Pattern;

import org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher;

import com.google.inject.Inject;

public class ClojurePrefixMatcher extends PrefixMatcher {
	@Inject
	private PrefixMatcher.CamelCase delegate;
	
	@Override
	public boolean isCandidateMatchingPrefix(final String name, final String prefix) {
		final String regex = Pattern.quote(prefix).
			replaceAll("-", ".*-").concat(".*");
		return name.matches(regex) 
			|| delegate.isCandidateMatchingPrefix(name, prefix);
	}	
}
