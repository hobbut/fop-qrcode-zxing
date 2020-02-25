package ru.hobbut.fop.zxing.qrcode;

import com.google.zxing.common.BitArray;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.XMLNamespaceEnabledImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageConverter;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;

public class QRCodeSVGImageConverter extends AbstractImageConverter {

	public Image convert(Image source, @SuppressWarnings("rawtypes") Map hints) {
		QRCodeImage qrCodeImage = (QRCodeImage) source;

		final DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		final String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		final Document svg = impl.createDocument(svgNS, "svg", null);
		final Element svgRoot = svg.getDocumentElement();

		final int matrixWidth = qrCodeImage.getBitMatrix().getWidth();
		final int matrixHeight = qrCodeImage.getBitMatrix().getHeight();

		svgRoot.setAttributeNS(null, "width", "" + qrCodeImage.getSize().getWidthPx());
		svgRoot.setAttributeNS(null, "height", "" + qrCodeImage.getSize().getHeightPx());
		svgRoot.setAttributeNS(null, "viewBox", "0 0 " + matrixWidth + " " + matrixHeight);

		for (int y = 0; y < matrixHeight; y++) {
			final BitArray bitArray = qrCodeImage.getBitMatrix().getRow(y, null);

			for (int x = 0; x < bitArray.getSize(); x++) {
				if (bitArray.get(x)) {
					final Element rectangle = svg.createElementNS(svgNS, "rect");
					rectangle.setAttributeNS(null, "x", Integer.toString(x));
					rectangle.setAttributeNS(null, "y", Integer.toString(y));
					rectangle.setAttributeNS(null, "width", "1");
					rectangle.setAttributeNS(null, "height", "1");
					rectangle.setAttributeNS(null, "fill", qrCodeImage.getColor());
					//rectangle.setAttributeNS(null, "stroke", COLOR_BLACK);
					//rectangle.setAttributeNS(null, "stroke-width", "0.01");
					svgRoot.appendChild(rectangle);
				}
			}
		}

		return new ImageXMLDOM(source.getInfo(), svg, SVGDOMImplementation.SVG_NAMESPACE_URI);
	}

	public ImageFlavor getTargetFlavor() {
		return XMLNamespaceEnabledImageFlavor.SVG_DOM;
	}

	public ImageFlavor getSourceFlavor() {
		return QRCodeImage.QR_CODE_IMAGE_FLAVOR;
	}
}
