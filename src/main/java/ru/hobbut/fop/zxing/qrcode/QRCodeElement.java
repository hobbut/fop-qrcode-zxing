/*
 * #%L
 * fop-zxing
 * %%
 * Copyright (C) 2012 Dmitriy Yakovlev
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ru.hobbut.fop.zxing.qrcode;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationUtil;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.xmlgraphics.util.UnitConv;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import java.awt.geom.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: hobbut
 * Date: 11.02.12
 * Time: 15:24
 */

public class QRCodeElement extends QRCodeObject {

	public QRCodeElement(FONode parent) {
		super(parent);
	}

	public Point2D getDimension(Point2D view) {
		Configuration configuration = ConfigurationUtil.toConfiguration(getDOMDocument().getDocumentElement());
		int width = UnitConv.convert(configuration.getAttribute("width", "50mm"));
		return new Point2D.Double(width / 1000, width / 1000);
	}

	public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList propertyList) throws FOPException {
		super.processNode(elementName, locator, attlist, propertyList);
		createBasicDocument();
	}
}
