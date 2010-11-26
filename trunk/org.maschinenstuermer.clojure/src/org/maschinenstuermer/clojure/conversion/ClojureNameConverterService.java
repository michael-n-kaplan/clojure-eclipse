package org.maschinenstuermer.clojure.conversion;

import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.conversion.impl.AbstractDeclarativeValueConverterService;
import org.eclipse.xtext.conversion.impl.AbstractValueConverter;
import org.eclipse.xtext.parsetree.AbstractNode;

public class ClojureNameConverterService extends
		AbstractDeclarativeValueConverterService {

	private final IValueConverter<String> newJavaTypeValueConverter = new AbstractValueConverter<String>() {

		@Override
		public String toValue(final String string, final AbstractNode node)
				throws ValueConverterException {
			return string.substring(0, string.length() - 1);
		}

		@Override
		public String toString(final String value) throws ValueConverterException {
			return value.concat(".");
		}
		
	};

	@ValueConverter(rule = "NewJavaType")
	public IValueConverter<String> NewJavaType() {
		return newJavaTypeValueConverter;
	}
}
