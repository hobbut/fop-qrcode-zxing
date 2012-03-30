package ru.hobbut.fop.zxing.qrcode;

import java.awt.geom.Point2D;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.util.UnitConv;

public class QRCodeDimension {

    private static final String DEFAULT_WIDTH = "50mm";
    private static final String WIDTH_ATTRIBUTE = "width";
    private static final Pattern WIDTH_PATTERN = Pattern.compile("^(\\d+(?:.\\d+)?)(mm|pt|in|cm)?$");

    private static double getSizeInPt(String length) {
        Matcher matcher = WIDTH_PATTERN.matcher(length);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("error calculating width");
        }
        double size = Double.valueOf(matcher.group(1));
        double sizeInPt = 0;
        if (matcher.groupCount() > 1) {
            String measure = matcher.group(2);
            if ("mm".equals(measure)) {
                sizeInPt = UnitConv.mm2pt(size);
            } else if ("pt".equals(measure)) {
                sizeInPt = size;
            } else if ("in".equals(measure)) {
                sizeInPt = UnitConv.in2pt(size);
            } else if ("cm".equals(measure)) {
                sizeInPt = UnitConv.mm2pt(10 * size);
            }
        } else {
            sizeInPt = size;
        }
        return sizeInPt;
    }

    private final double width;

    public QRCodeDimension(Configuration configuration) {
        this.width = getSizeInPt(configuration.getAttribute(WIDTH_ATTRIBUTE, DEFAULT_WIDTH));
    }

    public ImageSize toImageSize(ImageContext context) {
        ImageSize size = new ImageSize();
        size.setSizeInMillipoints((int) this.width, (int) this.width);
        size.setResolution(context.getSourceResolution());
        size.calcPixelsFromSize();
        return size;
    }

    public Point2D toPoint2D() {
        return new Point2D.Double(this.width, this.width);
    }
}
