package org.maschinenstuermer.clojure.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.common.types.xtext.ui.TypeAwareReferenceProposalCreator;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class ClojureReferenceProposalCreator extends TypeAwareReferenceProposalCreator {

//	public void lookupCrossReference(EObject model, EReference reference, ICompletionProposalAcceptor acceptor,
//			Predicate<IEObjectDescription> filter, Function<IEObjectDescription, ICompletionProposal> proposalFactory) {
//		if (TypesPackage.Literals.JVM_TYPE.isSuperTypeOf(getEReferenceType(model, reference))) {
//			super.lookupCrossReference(model, reference, acceptor, filter, proposalFactory);
//		} else if (model != null) {
//			IScope scope = getScopeProvider().getScope(model, reference);
//			Iterable<IEObjectDescription> candidates = scope.getContents();
//			for (IEObjectDescription candidate: candidates) {
//				if (!acceptor.canAcceptMoreProposals())
//					return;
//				if (filter.apply(candidate)) {
//					acceptor.accept(proposalFactory.apply(candidate));
//				}
//			}
//		}
//	}

}
