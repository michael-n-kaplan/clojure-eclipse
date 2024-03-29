package org.maschinenstuermer.clojure.ui.syntaxcoloring;

import java.util.Stack;
import java.util.regex.Pattern;

import org.eclipse.xtext.parsetree.AbstractNode;
import org.eclipse.xtext.parsetree.CompositeNode;
import org.eclipse.xtext.parsetree.LeafNode;
import org.eclipse.xtext.parsetree.util.ParsetreeSwitch;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.maschinenstuermer.clojure.clojure.Call;
import org.maschinenstuermer.clojure.clojure.SymbolRef;

public class ClojureSemanticHighlightingCalculator 
	implements ISemanticHighlightingCalculator {

	private static class HighlightingSwitch extends ParsetreeSwitch<Boolean> 
		implements ISemanticHighlightingCalculator {

		private static Pattern OPEN_PARENS = Pattern.compile("#?[\\({\\[]");
		private static Pattern CLOSE_PARENS = Pattern.compile("[\\)}\\]]");
		
		private Stack<Integer> parenIdIndexStack = new Stack<Integer>();

		private IHighlightedPositionAcceptor acceptor;

		private boolean inCall;
		private boolean nextSymbolRefIsCallTarget;

		@Override
		public void provideHighlightingFor(final XtextResource resource,
				final IHighlightedPositionAcceptor acceptor) {
			if (resource == null)
				return;

			this.acceptor = acceptor;
			final CompositeNode rootNode = resource.getParseResult().getRootNode();
			doSwitch(rootNode);
		}

		@Override
		public Boolean caseCompositeNode(final CompositeNode compositeNode) {
			final boolean isCall = compositeNode.getElement() instanceof Call;
			if (isCall)
				inCall = isCall;
			if (nextSymbolRefIsCallTarget && compositeNode.getElement() instanceof SymbolRef) {
				nextSymbolRefIsCallTarget = false;
				acceptor.addPosition(compositeNode.getOffset(), compositeNode.getLength(), 
						ClojureHighlightingConfiguration.FUNCTION_CALL_ID);
			}
			try {
				for (final AbstractNode child : compositeNode.getChildren()) {
					doSwitch(child);
				}
			} finally { 
				if (isCall)
					inCall = false;
			}
			return true;
		}

		@Override
		public Boolean caseLeafNode(final LeafNode leafNode) {
			if (OPEN_PARENS.matcher(leafNode.getText()).matches()) {
				final int parenIdIndex;
				if (parenIdIndexStack.isEmpty()) 
					parenIdIndex = 0;
				else
					parenIdIndex = (parenIdIndexStack.peek() + 1) 
					% ClojureHighlightingConfiguration.PAREN_IDS.length;
				addParenPosition(leafNode, parenIdIndex);
				parenIdIndexStack.push(parenIdIndex);
			} else if (CLOSE_PARENS.matcher(leafNode.getText()).matches() 
					&& !parenIdIndexStack.isEmpty()) {
				final int parenIdIndex = parenIdIndexStack.pop();
				addParenPosition(leafNode, parenIdIndex);
			} 
			if (inCall && "(".equals(leafNode.getText())) {
				nextSymbolRefIsCallTarget = true;
			}
			return true;
		}

		private void addParenPosition(LeafNode object, final int parenIdIndex) {
			acceptor.addPosition(object.getOffset(), object.getLength(), 
					ClojureHighlightingConfiguration.PAREN_IDS[parenIdIndex]);
		}
	};

	@Override
	public void provideHighlightingFor(final XtextResource resource, 
			final IHighlightedPositionAcceptor acceptor) {
		new HighlightingSwitch().
			provideHighlightingFor(resource, acceptor);
	}
}

