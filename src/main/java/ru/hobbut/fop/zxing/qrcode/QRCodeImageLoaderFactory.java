package ru.hobbut.fop.zxing.qrcode;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoaderFactory;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;

public class QRCodeImageLoaderFactory extends AbstractImageLoaderFactory {

    public static final String MIME_TYPE = "application/qrcode+xml";

    private static final ImageFlavor[] SUPPORTED_FLAVORS = {
        QRCodeImage.QR_CODE_IMAGE_FLAVOR
    };
    
    private static final String[] SUPPORTED_MIME_TYPES = {
        MIME_TYPE
    };

    public ImageFlavor[] getSupportedFlavors(String mime) {
        return SUPPORTED_FLAVORS;
    }

    public String[] getSupportedMIMETypes() {
        return SUPPORTED_MIME_TYPES;
    }

    public boolean isAvailable() {
        return true;
    }

    public ImageLoader newImageLoader(ImageFlavor targetFlavor) {
        return new QRCodeImageLoader(targetFlavor);
    }
}
