package ru.hobbut.fop.zxing.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlgraphics.image.loader.*;
import org.apache.xmlgraphics.image.loader.impl.AbstractImagePreloader;
import org.apache.xmlgraphics.util.UnitConv;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeImagePreloader extends AbstractImagePreloader {

	private static final String CORRECTION_ATTRIBUTE = "correction";
	private static final String ENCODING_ATTRIBUTE = "encoding";
	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String MARGIN_ATTRIBUTE = "margin";
	private static final String COLOR_ATTRIBUTE = "color";

	private static final String COLOR_BLACK = "black";
	
	private static final String DEFAULT_ERROR_CORRECTION_TYPE = "L";

	private static ErrorCorrectionLevel getErrorCorrectionLevel(String level) {
		if ("h".equalsIgnoreCase(level)) {
			return ErrorCorrectionLevel.H;
		} else if ("l".equalsIgnoreCase(level)) {
			return ErrorCorrectionLevel.L;
		} else if ("m".equalsIgnoreCase(level)) {
			return ErrorCorrectionLevel.M;
		} else if ("q".equalsIgnoreCase(level)) {
			return ErrorCorrectionLevel.Q;
		} else {
			throw new IllegalArgumentException("allowed correction levels: H/L/M/Q");
		}
	}

	@SuppressWarnings("unchecked")
	public ImageInfo preloadImage(String uri, Source source, ImageContext context) throws ImageException, IOException {
		try {
			if (source instanceof DOMSource) {
				DOMSource xml = (DOMSource) source;
				Document document = (Document) xml.getNode();
				Element element = document.getDocumentElement();

				if (!QRCodeElementMapping.NAMESPACE.equals(element.getNamespaceURI())) {
					return null;
				}

				String message;
				message = element.getAttribute(MESSAGE_ATTRIBUTE);
				if (message.isEmpty()) {
					message = element.getTextContent();
				}

				String charSet = element.getAttribute(ENCODING_ATTRIBUTE);
				String margin = element.getAttribute(MARGIN_ATTRIBUTE);
				ErrorCorrectionLevel correction = getErrorCorrectionLevel(StringUtils.defaultIfEmpty(element.getAttribute(CORRECTION_ATTRIBUTE), DEFAULT_ERROR_CORRECTION_TYPE));
				int width = UnitConv.convert(StringUtils.defaultIfEmpty(element.getAttribute("width"), "50mm"));

				Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
				hints.put(EncodeHintType.ERROR_CORRECTION, correction);
				if (!charSet.isEmpty()) hints.put(EncodeHintType.CHARACTER_SET, charSet);
				if (!margin.isEmpty()) hints.put(EncodeHintType.MARGIN, Integer.parseInt(margin));

				Writer writer = new QRCodeWriter();
				BitMatrix matrix = writer.encode(message, BarcodeFormat.QR_CODE, 0, 0, hints);

				ImageInfo info = new ImageInfo(uri, QRCodeImageLoaderFactory.MIME_TYPE);
				ImageSize size = new ImageSize();
				size.setSizeInMillipoints(width, width);
				size.setResolution(context.getSourceResolution());
				size.calcPixelsFromSize();
				info.setSize(size);

				Image image = new QRCodeImage(info, matrix, StringUtils.defaultIfEmpty(element.getAttribute(COLOR_ATTRIBUTE), COLOR_BLACK));
				info.getCustomObjects().put(ImageInfo.ORIGINAL_IMAGE, image);

				return info;
			}
		} catch (NullPointerException exception) {

			throw new ImageException("Missing attribute. Message= " + exception.getMessage(), exception);

		} catch (WriterException writerException) {

			throw new ImageException("error while encoding image", writerException);
		}

		return null;
	}
}
