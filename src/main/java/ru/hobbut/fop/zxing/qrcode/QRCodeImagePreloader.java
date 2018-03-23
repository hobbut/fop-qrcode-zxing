package ru.hobbut.fop.zxing.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.ConfigurationUtil;
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

				Configuration cfg = ConfigurationUtil.toConfiguration(element);

				String message;
				try {
					message = cfg.getAttribute(MESSAGE_ATTRIBUTE);
				} catch (ConfigurationException configurationException) {
					message = cfg.getValue();
				}

				String charSet = cfg.getAttribute(ENCODING_ATTRIBUTE, null);
				String margin = cfg.getAttribute(MARGIN_ATTRIBUTE, null);
				ErrorCorrectionLevel correction = getErrorCorrectionLevel(cfg.getAttribute(CORRECTION_ATTRIBUTE, DEFAULT_ERROR_CORRECTION_TYPE));
				int width = UnitConv.convert(cfg.getAttribute("width", "50mm"));

				Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
				hints.put(EncodeHintType.ERROR_CORRECTION, correction);
				if (charSet != null) hints.put(EncodeHintType.CHARACTER_SET, charSet);
				if (margin != null) hints.put(EncodeHintType.MARGIN, Integer.parseInt(margin));

				Writer writer = new QRCodeWriter();
				BitMatrix matrix = writer.encode(message, BarcodeFormat.QR_CODE, 0, 0, hints);

				ImageInfo info = new ImageInfo(uri, QRCodeImageLoaderFactory.MIME_TYPE);
				ImageSize size = new ImageSize();
				size.setSizeInMillipoints(width, width);
				size.setResolution(context.getSourceResolution());
				size.calcPixelsFromSize();
				info.setSize(size);

				Image image = new QRCodeImage(info, matrix, cfg.getAttribute(COLOR_ATTRIBUTE, COLOR_BLACK));
				info.getCustomObjects().put(ImageInfo.ORIGINAL_IMAGE, image);

				return info;
			}
		} catch (ConfigurationException configurationException) {

			throw new ImageException("Missing attribute. Message= " + configurationException.getMessage(), configurationException);

		} catch (WriterException writerException) {

			throw new ImageException("error while encoding image", writerException);
		}

		return null;
	}
}
