/*
 * generated by Xtext
 */
package org.maschinenstuermer.clojure.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.builder.clustering.CurrentDescriptions;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.scoping.impl.AbstractGlobalScopeProvider;
import org.eclipse.xtext.ui.editor.contentassist.AbstractJavaBasedContentProposalProvider.ReferenceProposalCreator;
import org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher;
import org.eclipse.xtext.ui.editor.outline.transformer.ISemanticModelTransformer;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.eclipse.xtext.ui.editor.syntaxcoloring.antlr.AbstractAntlrTokenToAttributeIdMapper;
import org.eclipse.xtext.ui.wizard.IProjectCreator;
import org.maschinenstuermer.clojure.resource.IUriConverterProvider;
import org.maschinenstuermer.clojure.ui.contentassist.ClojurePrefixMatcher;
import org.maschinenstuermer.clojure.ui.contentassist.ClojureReferenceProposalCreator;
import org.maschinenstuermer.clojure.ui.outline.ClojureTransformer;
import org.maschinenstuermer.clojure.ui.resource.JavaUriConverterProvider;
import org.maschinenstuermer.clojure.ui.syntaxcoloring.ClojureHighlightingConfiguration;
import org.maschinenstuermer.clojure.ui.syntaxcoloring.ClojureSemanticHighlightingCalculator;
import org.maschinenstuermer.clojure.ui.syntaxcoloring.ClojureTokenToAttributeIdMapper;
import org.maschinenstuermer.clojure.ui.wizard.CustomClojureProjectCreator;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Use this class to register components to be used within the IDE.
 */
public class ClojureUiModule extends org.maschinenstuermer.clojure.ui.AbstractClojureUiModule {
	public ClojureUiModule(final AbstractUIPlugin plugin) {
		super(plugin);
	}

	public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration() {
		return ClojureHighlightingConfiguration.class;
	}
	
	public Class<? extends AbstractAntlrTokenToAttributeIdMapper> bindAbstractAntlrTokenToAttributeIdMapper() {
		return ClojureTokenToAttributeIdMapper.class;
	}
	
	public Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator() {
		return ClojureSemanticHighlightingCalculator.class;
	}
	
	public Class<? extends ISemanticModelTransformer> bindISemanticModelTransformer() {
		return ClojureTransformer.class;
	}
	
	public Class<? extends ReferenceProposalCreator> bindAbstractJavaBasedContentProposalProvider$ReferenceProposalCreator() {
		return ClojureReferenceProposalCreator.class;
	}

	@Override
	public Class<? extends IProjectCreator> bindIProjectCreator() {
		return CustomClojureProjectCreator.class;
	}

	@Override
	public Class<? extends PrefixMatcher> bindPrefixMatcher() {
		return ClojurePrefixMatcher.class;
	}

	@Override  
	public void configureIResourceDescriptionsBuilderScope(final Binder binder) {  
		binder.bind(IResourceDescriptions.class).annotatedWith(  
				Names.named(AbstractGlobalScopeProvider.NAMED_BUILDER_SCOPE))  
					.to(CurrentDescriptions.ResourceSetAware.class);  
	}
	
	public Class<? extends IUriConverterProvider> bindIUriConverterProvider() {
		return JavaUriConverterProvider.class;
	}
}
