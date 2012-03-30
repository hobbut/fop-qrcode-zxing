package ru.hobbut.fop.zxing.qrcode;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;

import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageConverter;
import org.apache.xmlgraphics.image.loader.impl.ImageBuffered;

import com.google.zxing.client.j2se.MatrixToImageWriter;

public class QRCodeBufferedImageConverter extends AbstractImageConverter {

    public Image convert(Image source, @SuppressWarnings("rawtypes") Map hints) {
        QRCodeImage qrCodeImage = (QRCodeImage) source;
        BufferedImage image = MatrixToImageWriter.toBufferedImage(qrCodeImage.getBitMatrix());
        return new ImageBuffered(source.getInfo(), image, Color.WHITE);
    }

    public ImageFlavor getTargetFlavor() {
        return ImageFlavor.BUFFERED_IMAGE;
    }

    public ImageFlavor getSourceFlavor() {
        return QRCodeImage.QR_CODE_IMAGE_FLAVOR;
    }
}
