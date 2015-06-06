package pwr.osm.data.representation;


import pwr.osm.data.representation.XMLElements.Bound;
import pwr.osm.data.representation.XMLElements.Nd;
import pwr.osm.data.representation.XMLElements.NodeXML;
import pwr.osm.data.representation.XMLElements.Osm;
import pwr.osm.data.representation.XMLElements.Tag;
import pwr.osm.model.Way;

/**
 * Typ wyliczeniowy reprezentujacy obslugiwane tagi w pliku XML
 * @author Dawid Cokan
 * @date 12-05-2014
 *
 */

public enum tagXML {
	NODE("node"),
	JUNCTION("junction"), 
	EDGE("edge"),
	WAY("way"),
	ND("nd"),
	BOUND("bound"), 
	TAG("tag"), 
	OSM("osm");
	
	private String tagName;
	
	
	private tagXML(String tagName){
		this.tagName = tagName;
	}
	
	
	@Override
	public String toString(){
		return tagName;
	}


	public String getNodeName() {
		return tagName;
	}

	/**
	 * Zwraca typ klasy, ktora reprezentuje dany tag zadany jako nazwa tagu
	 * @param qName - nazwa tagu kotrego reprezntacje chcemy poznac
	 * @return  Pelna nazwa klasy (wraz z pakietem) - jako String
	 * @throws Exception - jesli dany tag nie jest obslugiwany
	 */
	public static String getSuitableClass(String qName) throws Exception {
		switch(qName){
		case "node":
			return NodeXML.class.getCanonicalName();
		case "way":
			return Way.class.getCanonicalName();
		case "nd":
			return Nd.class.getCanonicalName();
		case "osm":
			return Osm.class.getCanonicalName();
		case "bound": 
			return Bound.class.getCanonicalName();
		case "tag":
			return Tag.class.getCanonicalName();
		default:
			throw new Exception("No related class to Node: <"+qName+"/>");
		}
		
	}
}
