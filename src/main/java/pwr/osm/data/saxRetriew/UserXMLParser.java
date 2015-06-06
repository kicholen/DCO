package pwr.osm.data.saxRetriew;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import pwr.osm.data.representation.XMLElements.Osm;

public class UserXMLParser {

	public Osm parse(InputStream in) throws SAXException, IOException{
		 UserParserHandler handler = new UserParserHandler();
		 
         XMLReader parser = XMLReaderFactory.createXMLReader();

         parser.setContentHandler(handler);
         InputSource source = new InputSource(in);

         parser.parse(source);

         return handler.getData();
	}
}
