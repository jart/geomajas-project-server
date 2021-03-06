/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.filter;

import org.geotools.factory.Hints;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FilterFactoryImpl;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Filter factory that creates non-lenient {@link AttributeExpression} instances. These will throw an exception when the
 * property cannot be accessed. To use this factory, pass the factory class as a hint to the factory finder:
 * 
 * <pre>
 * Hints hints = new Hints();
 * hints.put(Hints.FILTER_FACTORY, NonLenientFilterFactoryImpl.class);
 * FF = CommonFactoryFinder.getFilterFactory2(hints);
 * </pre>
 * 
 * @author Jan De Moerloose
 * 
 */
public class NonLenientFilterFactoryImpl extends FilterFactoryImpl {

	public NonLenientFilterFactoryImpl() {
		super();
	}

	public NonLenientFilterFactoryImpl(Hints hints) {
		super(hints);
	}

	@Override
	public PropertyName property(String name) {
		AttributeExpressionImpl expression = (AttributeExpressionImpl) super.property(name);
		expression.setLenient(false);
		return expression;
	}

	@Override
	public PropertyName property(Name name) {
		AttributeExpressionImpl expression = (AttributeExpressionImpl) super.property(name);
		expression.setLenient(false);
		return expression;
	}

	@Override
	public PropertyName property(String name, NamespaceSupport namespaceContext) {
		AttributeExpressionImpl expression = (AttributeExpressionImpl) super.property(name, namespaceContext);
		expression.setLenient(false);
		return expression;
	}

}
