package org.maschinenstuermer.clojure.resource;

import org.eclipse.emf.ecore.resource.URIConverter;

import com.google.inject.ImplementedBy;

/**
 * Implementierungen dieses Interfaces koennen einen existierenden URIConverter
 * (in einem ResourceSet oder einer Resource) anpassen oder austauschen.
 * 
 * @author Moritz Eysholdt
 */
@ImplementedBy(IUriConverterProvider.NullImpl.class)
public interface IUriConverterProvider {

    /**
     * Passt den uebergebenen URIConverter an oder tauscht ihn aus.
     * 
     * @return Die angepasste/ausgetausche Version des URIConverters.
     */
    public URIConverter getURIConverter(URIConverter delegate);

    /**
     * @return False, wenn der uebergebene URIConverter die
     *         {@link #getURIConverter(URIConverter)} getaetigten Aenderungen
     *         noch nicht enthaelt.
     */
    public boolean isConverted(URIConverter delegate);

    public class NullImpl implements IUriConverterProvider {

        public URIConverter getURIConverter(URIConverter delegate) {
            return delegate;
        }

        public boolean isConverted(URIConverter delegate) {
            return true;
        }
    }
}