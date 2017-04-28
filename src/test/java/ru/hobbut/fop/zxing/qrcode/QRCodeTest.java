package ru.hobbut.fop.zxing.qrcode;

import junit.framework.Assert;
import org.apache.fop.apps.*;
import org.apache.fop.events.Event;
import org.apache.fop.events.EventFormatter;
import org.apache.fop.events.EventListener;
import org.apache.fop.events.model.EventSeverity;
import org.junit.Test;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;

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

		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		foUserAgent.getEventBroadcaster().addEventListener(new EventListener() {

			public void processEvent(Event event) {
				EventSeverity severity = event.getSeverity();
				if (severity == EventSeverity.ERROR) {
					Assert.fail(EventFormatter.format(event));
				}
			}
		});

		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, new ByteArrayOutputStream());

		Source source = new StreamSource(getClass().getResourceAsStream("/sample.xml"));
		Result result = new SAXResult(fop.getDefaultHandler());
		TransformerFactory.newInstance().newTransformer().transform(source, result);
	}
}
