package ru.hobbut.fop.zxing.qrcode;

import java.io.IOException;
import java.util.Map;

import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoader;

public class QRCodeImageLoader extends AbstractImageLoader {

    private final ImageFlavor targetFlavor;

    public QRCodeImageLoader(ImageFlavor targetFlavor) {
        this.targetFlavor = targetFlavor;
    }

    public ImageFlavor getTargetFlavor() {
        return this.targetFlavor;
    }

    public Image loadImage(ImageInfo info, @SuppressWarnings("rawtypes") Map hints, ImageSessionContext session) throws ImageException, IOException {
        return (QRCodeImage) info.getOriginalImage();
    }
}
