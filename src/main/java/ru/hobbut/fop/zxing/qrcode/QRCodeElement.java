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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: hobbut
 * Date: 11.02.12
 * Time: 15:24
 */

public class QRCodeElement extends QRCodeObject {

    public static final String DEFAULT_WIDTH = "50mm";
    public static final Pattern SIZE_PATTERN = Pattern.compile("^(\\d+(?:.\\d+)?)(mm|pt|in|cm)?$");

    public QRCodeElement(FONode parent) {
        super(parent);
    }

    public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList propertyList)
            throws FOPException {
        super.processNode(elementName, locator, attlist, propertyList);
        createBasicDocument();
    }

    public Point2D getDimension(Point2D view) {
        Configuration cfg = ConfigurationUtil.toConfiguration(this.doc.getDocumentElement());
        String length = cfg.getAttribute("width", DEFAULT_WIDTH);
        double size = getSizeInPt(length);
        return new Point2D.Double(size, size);
    }

    public static double getSizeInPt(String length) {
        Matcher matcher = SIZE_PATTERN.matcher(length);
        if (!matcher.matches()) throw new RuntimeException("error calculating size");
        double size = Double.valueOf(matcher.group(1));
        double sizeInPt = 0;
        if (matcher.groupCount() > 1) {
            String measure = matcher.group(2);
            if ("mm".equals(measure)) {
                sizeInPt = UnitConv.mm2pt(size);
            } else if ("pt".equals(measure)) {
                sizeInPt = size;
            } else if ("in".equals(measure)) {
                sizeInPt = UnitConv.in2pt(size);
            } else if ("cm".equals(measure)) {
                sizeInPt = UnitConv.mm2pt(10*size);
            }
        } else {
            sizeInPt = size;
        }
        return sizeInPt;
    }
}
