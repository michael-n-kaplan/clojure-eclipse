package org.maschinenstuermer.clojure.ui.syntaxcoloring;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parsetree.AbstractNode;
import org.eclipse.xtext.parsetree.LeafNode;
import org.eclipse.xtext.parsetree.NodeUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.maschinenstuermer.clojure.clojure.Call;

public class ClojureSemanticHighlightingCalculator implements
ISemanticHighlightingCalculator {

	@Override
	public void provideHighlightingFor(final XtextResource resource, final IHighlightedPositionAcceptor acceptor) {
		if (resource == null)
			return;

		final Iterable<AbstractNode> allNodes = NodeUtil.getAllContents(
				resource.getParseResult().getRootNode());
		for (final AbstractNode node : allNodes) {
			final EObject element = node.getElement();
			if (element instanceof Call) {
				for (final LeafNode leafNode : node.getLeafNodes()) {
					if ("fn".equals(leafNode.getFeature())) {
						acceptor.addPosition(leafNode.getOffset(), leafNode.getLength(), 
								ClojureHighlightingConfiguration.FUNCTION_CALL_ID);
						break;
					}
				}
			}
		}
	}
}
