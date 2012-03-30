package ru.hobbut.fop.zxing.qrcode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.ConfigurationUtil;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.impl.AbstractImagePreloader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeImagePreloader extends AbstractImagePreloader {

    private static final String CORRECTION_ATTRIBUTE = "correction";
    private static final String DEFAULT_CHARACTER_SET = "ISO-8859-1";
    private static final String DEFAULT_ERROR_CORRECTION_TYPE = "L";
    private static final String ENCODING_ATTRIBUTE = "encoding";
    private static final String MESSAGE_ATTRIBUTE = "message";

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

            DOMSource xml = (DOMSource) source;
            Document document = (Document) xml.getNode();
            Element element = document.getDocumentElement();

            if (!QRCodeElementMapping.NAMESPACE.equals(element.getNamespaceURI())) {
                return null;
            }

            Configuration cfg = ConfigurationUtil.toConfiguration(element);

            String message = cfg.getAttribute(MESSAGE_ATTRIBUTE);
            String charSet = cfg.getAttribute(ENCODING_ATTRIBUTE, DEFAULT_CHARACTER_SET);
            ErrorCorrectionLevel correction = getErrorCorrectionLevel(cfg.getAttribute(CORRECTION_ATTRIBUTE, DEFAULT_ERROR_CORRECTION_TYPE));
            QRCodeDimension dimension = new QRCodeDimension(cfg);

            Map<EncodeHintType, Object> qrHints = new HashMap<EncodeHintType, Object>();
            qrHints.put(EncodeHintType.ERROR_CORRECTION, correction);
            qrHints.put(EncodeHintType.CHARACTER_SET, charSet);

            Writer writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(message, BarcodeFormat.QR_CODE, 0, 0, qrHints);

            ImageInfo info = new ImageInfo(uri, QRCodeImageLoaderFactory.MIME_TYPE);
            info.setSize(dimension.toImageSize(context));
            
            Image image = new QRCodeImage(info, matrix);
            info.getCustomObjects().put(ImageInfo.ORIGINAL_IMAGE, image);

            return info;

        } catch (ConfigurationException configurationException) {

            throw new ImageException("missing attribute message", configurationException);

        } catch (WriterException writerException) {

            throw new ImageException("error while encoding image", writerException);
        }
    }
}
