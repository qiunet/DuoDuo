package org.qiunet.project.init.xsd;

import org.apache.commons.digester.Digester;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TestValidXsd {


	@Test
	public void vliadXMLWithXsd() throws SAXException, IOException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL url = Thread.currentThread().getContextClassLoader().getResource("xsd/Entity.xsd");

		Assert.assertNotNull(url);

		Schema schema = factory.newSchema(url);

		Validator validator = schema.newValidator();
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("xml/player.xml");
		validator.validate(new StreamSource(inputStream));
	}
}
