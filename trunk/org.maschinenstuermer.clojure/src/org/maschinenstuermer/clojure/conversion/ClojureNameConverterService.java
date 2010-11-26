package org.maschinenstuermer.clojure.conversion;

import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.conversion.impl.AbstractDeclarativeValueConverterService;
import org.eclipse.xtext.conversion.impl.AbstractValueConverter;
import org.eclipse.xtext.parsetree.AbstractNode;
import org.eclipse.xtext.parsetree.CompositeNode;
import org.maschinenstuermer.clojure.clojure.QuotedSymbol;

public class ClojureNameConverterService extends
		AbstractDeclarativeValueConverterService {

	private final IValueConverter<String> newJavaTypeValueConverter = new AbstractValueConverter<String>() {

		@Override
		public String toValue(final String string, final AbstractNode node)
				throws ValueConverterException {
			final CompositeNode parent = node.getParent();
			final boolean removeDot = parent == null || !(parent.getElement() instanceof QuotedSymbol);
			if (removeDot) 				
				return string.substring(0, string.length() - 1);
			else
				return string;
		}

		@Override
		public String toString(final String value) throws ValueConverterException {
			return value.endsWith(".") ? 
					value : value.concat(".");
		}
	};

	@ValueConverter(rule = "NewJavaType")
	public IValueConverter<String> NewJavaType() {
		return newJavaTypeValueConverter;
	}
}
