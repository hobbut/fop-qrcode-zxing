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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationUtil;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.XMLHandler;
import org.apache.fop.render.pdf.PDFRenderer;
import org.apache.fop.render.ps.PSRenderer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hobbut
 * Date: 11.02.12
 * Time: 15:15
 */

public class QRCodeXMLHandler implements XMLHandler {

    private static final Log log = LogFactory.getLog(QRCodeXMLHandler.class);

    private static final String DEFAULT_ERROR_CORRECTION_TYPE = "L";
    private static final String DEFAULT_CHARACTER_SET = "ISO-8859-1";

    private static final String MESSAGE_ATTRIBUTE = "message";
    private static final String ENCODING_ATTRIBUTE = "encoding";
    private static final String CORRECTION_ATTRIBUTE = "correction";

    private static final String SVG_ATTR_WIDTH = "width";
    private static final String SVG_ATTR_HEIGHT = "height";
    private static final String SVG_ATTR_FILL = "fill";
    private static final String SVG_ATTR_STROKE = "stroke";
    private static final String SVG_ATTR_STROKE_WIDTH = "stroke-width";
    private static final String SVG_ATTR_X = "x";
    private static final String SVG_ATTR_Y = "y";

    private static final String SVG_ELEMENT_SVG = "svg";
    private static final String SVG_ELEMENT_RECT = "rect";

    private static final String COLOR_BLACK = "black";
    private static final String STROKE_WIDTH = "0.01";
    private static final String SIZE = "1";

    public void handleXML(RendererContext rendererContext, Document document, String s) throws Exception {
        Configuration cfg = ConfigurationUtil.toConfiguration(document.getDocumentElement());

        String msg = cfg.getAttribute(MESSAGE_ATTRIBUTE);
        String charSet = cfg.getAttribute(ENCODING_ATTRIBUTE, DEFAULT_CHARACTER_SET);
        ErrorCorrectionLevel correction = getErrorCorrectionLevel(
                cfg.getAttribute(CORRECTION_ATTRIBUTE, DEFAULT_ERROR_CORRECTION_TYPE));

        BitMatrix matrix;
        Writer writer = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, correction);
        hints.put(EncodeHintType.CHARACTER_SET, charSet);

        matrix = writer.encode(msg, BarcodeFormat.QR_CODE, 0, 0, hints);
        Document svg = makeSvgQrCode(matrix);
        rendererContext.getRenderer().renderXML(rendererContext, svg, SVGDOMImplementation.SVG_NAMESPACE_URI);
    }

    private Document makeSvgQrCode(BitMatrix matrix) {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document svg = impl.createDocument(svgNS, SVG_ELEMENT_SVG, null);
        Element svgRoot = svg.getDocumentElement();
        int matrixWidth = matrix.getWidth();
        int matrixHeight = matrix.getHeight();
        svgRoot.setAttributeNS(null, SVG_ATTR_WIDTH, "" + matrixWidth);
        svgRoot.setAttributeNS(null, SVG_ATTR_HEIGHT, "" + matrixHeight);
        for (int y = 0; y < matrixHeight; y++) {
            BitArray bitArray = matrix.getRow(y, null);
            for (int x = 0; x < bitArray.getSize(); x++) {
                if (bitArray.get(x)) {
                    Element rectangle = svg.createElementNS(svgNS, SVG_ELEMENT_RECT);
                    rectangle.setAttributeNS(null, SVG_ATTR_X, "" + x);
                    rectangle.setAttributeNS(null, SVG_ATTR_Y, "" + y);
                    rectangle.setAttributeNS(null, SVG_ATTR_WIDTH, SIZE);
                    rectangle.setAttributeNS(null, SVG_ATTR_HEIGHT, SIZE);
                    rectangle.setAttributeNS(null, SVG_ATTR_FILL, COLOR_BLACK);
                    rectangle.setAttributeNS(null, SVG_ATTR_STROKE, COLOR_BLACK);
                    rectangle.setAttributeNS(null, SVG_ATTR_STROKE_WIDTH, STROKE_WIDTH);
                    svgRoot.appendChild(rectangle);
                }
            }
        }
        return svg;
    }

    public boolean supportsRenderer(Renderer renderer) {
        boolean support = null != renderer.getGraphics2DAdapter();
        support = support || renderer instanceof PDFRenderer;
        support = support || renderer instanceof PSRenderer;
        return support;
    }

    public String getNamespace() {
        return QRCodeElementMapping.NAMESPACE;
    }

    public String getMimeType() {
        return XMLHandler.HANDLE_ALL;
    }

    private ErrorCorrectionLevel getErrorCorrectionLevel(String level) {
        level = level.toLowerCase();
        if ("h".equals(level)) {
            return ErrorCorrectionLevel.H;
        } else if ("l".equals(level)) {
            return ErrorCorrectionLevel.L;
        } else if ("m".equals(level)) {
            return ErrorCorrectionLevel.M;
        } else if ("q".equals(level)) {
            return ErrorCorrectionLevel.Q;
        } else {
            throw new IllegalArgumentException("allowed correction levels: H/L/M/Q");
        }
    }

}
