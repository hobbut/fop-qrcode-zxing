package ru.hobbut.fop.zxing.qrcode;

import org.apache.xmlgraphics.image.loader.*;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoader;

import java.io.IOException;
import java.util.Map;

public class QRCodeImageLoader extends AbstractImageLoader {

	private final ImageFlavor targetFlavor;

	public QRCodeImageLoader(ImageFlavor targetFlavor) {
		this.targetFlavor = targetFlavor;
	}

	public ImageFlavor getTargetFlavor() {
		return this.targetFlavor;
	}

	public Image loadImage(ImageInfo info, @SuppressWarnings("rawtypes") Map hints, ImageSessionContext session) throws ImageException, IOException {
		return info.getOriginalImage();
	}
}
