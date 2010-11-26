package org.maschinenstuermer.clojure.ui.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.PlatformResourceURIHandlerImpl.PlatformResourceOutputStream;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipse.xtext.ui.resource.IStorage2UriMapper;
import org.maschinenstuermer.clojure.resource.IUriConverterProvider;

import com.google.inject.Inject;

/**
 * Diese Implementierung des {@link IUriConverterProvider} konfiguriert fuer den
 * URIConverter einen StorageURIHandler. Der StorageURIHandler delegiert direkt
 * an Xtexts {@link IStorage2UriMapper}.
 * 
 * Das bringt massive Performacevorteile fuer Archive-URIs, da EMF die
 * Archive-URIs die Archive-Datei bei jeder Anfrage neu einliest und
 * dekomprimiert. {@link IStorage2UriMapper} delegiert dagegen an die cachenden
 * Mechanismen des JDTs.
 * 
 * @author Moritz Eysholdt
 */
public class JavaUriConverterProvider implements IUriConverterProvider {

	public URIConverter getURIConverter(URIConverter delegate) {
		delegate.getURIHandlers().add(0, new StorageURIHandler());
		return delegate;
	}

	public boolean isConverted(URIConverter delegate) {
		for (URIHandler h : delegate.getURIHandlers())
			if (h instanceof StorageURIHandler)
				return true;
		return false;
	}

	@Inject
	private IStorage2UriMapper mapper;

	public class StorageURIHandler extends URIHandlerImpl {

		@SuppressWarnings("unchecked")
		public Map<String, ?> getAttributes(URI uri, Map<?, ?> options) {
			Map<String, Object> attribs = (Map<String, Object>) super
					.getAttributes(uri, options);
			// the readonly tag will be always set to true by
			// super.getAttributes(),
			// because the uri is no HttpURLConnection. For our purpose this is
			// wrong, so remove it again
			attribs.remove(URIConverter.ATTRIBUTE_READ_ONLY);
			// here comes the proper condition for readonly resources
			if (uri.isArchive())
				attribs.put(URIConverter.ATTRIBUTE_READ_ONLY, true);
			return attribs;
		}

		@Override
		public boolean canHandle(URI uri) {
			return mapper.getStorages(uri).iterator().hasNext();
		}

		private IStorage getStorage(URI uri) {
			Iterator<IStorage> s = mapper.getStorages(uri).iterator();
			if (s.hasNext())
				return s.next();
			throw new RuntimeException("Can not handle " + uri);
		}

		@Override
		public boolean exists(URI uri, Map<?, ?> options) {
			Iterator<IStorage> i = mapper.getStorages(uri).iterator();
			if (!i.hasNext())
				return false;
			IStorage s = i.next();
			if (s instanceof IResource)
				return ((IResource) s).exists();
			return true;
		}

		@Override
		public InputStream createInputStream(URI uri, Map<?, ?> options)
				throws IOException {
			try {
				return getStorage(uri).getContents();
			} catch (CoreException e) {
				throw new IOException(e.getMessage());
			}
		}

		@Override
		public OutputStream createOutputStream(URI uri, Map<?, ?> options)
				throws IOException {
			IStorage s = getStorage(uri);
			if (s instanceof IFile) {
				IFile f = (IFile) s;
				@SuppressWarnings("unchecked")
				final Map<Object, Object> response = options == null ? null
						: (Map<Object, Object>) options
								.get(URIConverter.OPTION_RESPONSE);
				return new PlatformResourceOutputStream(f, false, true, null) {
					@Override
					public void close() throws IOException {
						try {
							super.close();
						} finally {
							if (response != null) {

								response.put(
										URIConverter.RESPONSE_TIME_STAMP_PROPERTY,
										file.getLocalTimeStamp());
							}
						}
					}
				};
			}
			throw new IOException("Unable to create output stream for " + uri
					+ " (IStorage type:" + s.getClass());
		}

	}
}