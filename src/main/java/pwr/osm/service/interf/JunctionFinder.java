package pwr.osm.service.interf;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public interface JunctionFinder {

	public void loadEdges() throws SAXException, 
						IOException, ParserConfigurationException;
	
	public void loadNodes() throws SAXException, 
						IOException, ParserConfigurationException;
}
