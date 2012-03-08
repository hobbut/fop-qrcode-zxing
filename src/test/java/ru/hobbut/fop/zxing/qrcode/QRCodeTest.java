package ru.hobbut.fop.zxing.qrcode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Assert;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.events.Event;
import org.apache.fop.events.EventListener;
import org.apache.fop.events.model.EventSeverity;
import org.junit.Test;

public class QRCodeTest {

    /**
     * If META-INF/services/org.apache.xmlgraphics.image.loader.spi.Image* are
     * removed from the classpath, rendering will fail (unless fop is configured
     * with <code>&lt;prefer-renderer&gt;true&lt;/prefer-renderer&gt;</code>).
     * 
     * @throws FOPException
     * @throws TransformerException
     */
    @Test
    public void renderingShouldNotRaiseErrors() throws IOException, FOPException, TransformerException {

        FopFactory fopFactory = FopFactory.newInstance();

        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        foUserAgent.getEventBroadcaster().addEventListener(new EventListener() {

            public void processEvent(Event event) {
                EventSeverity severity = event.getSeverity();
                if (severity == EventSeverity.ERROR) {
                    Assert.fail("an error has occured while rendering");
                }
            }
        });

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, new ByteArrayOutputStream());

        Source source = new StreamSource(getClass().getResourceAsStream("/sample.xml"));
        Result result = new SAXResult(fop.getDefaultHandler());
        TransformerFactory.newInstance().newTransformer().transform(source, result);
    }
}
