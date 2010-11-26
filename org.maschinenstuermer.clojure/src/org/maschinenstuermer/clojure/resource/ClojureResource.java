package org.maschinenstuermer.clojure.resource;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.xtext.linking.lazy.LazyLinkingResource;

import com.google.inject.Inject;

public class ClojureResource extends LazyLinkingResource {
	@Inject
	private IUriConverterProvider converterProvider;
	
	@Override
	public NotificationChain basicSetResourceSet(ResourceSet resourceSet,
			NotificationChain notifications) {
		if (resourceSet != null) {
			final URIConverter uriConverter = resourceSet.getURIConverter();
			if (!converterProvider.isConverted(uriConverter)) {
				converterProvider.getURIConverter(uriConverter);
			}
		}
		return super.basicSetResourceSet(resourceSet, notifications);
	}
}
