package pwr.osm.data;

import java.util.LinkedList;
import java.util.List;

import pwr.osm.data.representation.attributeType;
import pwr.osm.data.representation.attributeXML;
import pwr.osm.data.representation.XMLElements.NodeXMLInterface;
import pwr.osm.model.Edge;

/**
 * Klasa nie sprawdzona i nie do uzytku !
 * 
 * @author Dawid Cokan
 * @date ????? 
 * @deprecated
 *
 */
public class XMLRetriever {

	private XMLRetriever(){
		
	}
	
	private static String fileName = "junctions.xml";
	
//	public static NodeXML getElements(){
//		return getElements(null, -1);
//	}
	
//	public static NodeXML getElements(int limit){
////		if (nodeType.equals(tagXML.EDGE)){
////			NodeXML edge = getElements(null, limit);
////
////			return edge;
////		}
//			
//		return getElements(null, limit);
//	}
	
//	public static NodeXML getElements(List<attributeXML> attributes, int limit){
//		return (new largeXML(fileName)).getElements();
//	}
	
//	public static NodeXML getElements(List<attributeXML> attributes){
//		return getElements(attributes, -1);
//	}
	
	public static void completeEdges(List<NodeXMLInterface> edges){
		for (NodeXMLInterface edge : edges){
			long min=((Edge)edge).getBegin().getId();
			long max=((Edge)edge).getEnd().getId();
			
			if (min>max){
				long t = max;
				max = min;
				min = t;
			}

        	List<attributeXML> attributes = new LinkedList<attributeXML>();
			for (int i=(int)min; i<=max; i++)
	        	attributes.add(new attributeXML(attributeType.ID, ""+i));
	    	
//        	largeXML xml = new largeXML("nodes.xml");
//        	ArrayList<GeoPosition> coordinates = new ArrayList<GeoPosition>();
//        	for (NodeXML node: xml.getElements(tagXML.NODE, attributes)){
//        		node = ((Node)node);
//        		coordinates.add(new GeoPosition(((Node)node).getLat(), ((Node)node).getLon()));
////        		ready.add((Node) n);
//        		System.out.println(node);
//        	}
//        	((Edge)edge).addCoordinates(coordinates);
		}
	}
	
	public static void setName(String fileName){
		XMLRetriever.fileName = fileName;
	}
}
