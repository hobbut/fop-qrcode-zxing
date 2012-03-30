package ru.hobbut.fop.zxing.qrcode;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.impl.AbstractImage;

import com.google.zxing.common.BitMatrix;

public class QRCodeImage extends AbstractImage {
    
    public static final ImageFlavor QR_CODE_IMAGE_FLAVOR = new ImageFlavor("QRCode");
    
    private final BitMatrix bitMatrix;

    public QRCodeImage(ImageInfo info, BitMatrix bitMatrix) {
        super(info);
        this.bitMatrix = bitMatrix;
    }
    
    public BitMatrix getBitMatrix() {
        return this.bitMatrix;
    }

    public ImageFlavor getFlavor() {
        return QR_CODE_IMAGE_FLAVOR;
    }

    public boolean isCacheable() {
        return true;
    }
}
