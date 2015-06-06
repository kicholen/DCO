package pwr.osm.data.representation.XMLElements;

import pwr.osm.data.representation.tagXML;

public class Bound implements NodeXMLInterface{

	private bBox box;
	private String origin;
	
	public Bound(){
		
	}

	@Override
	public tagXML getTagType() {
		return tagXML.BOUND;
	}

	@Override
	public String toString(){
		return "<bound box="+box+" origin="+origin+"/>";
	}
	
	public bBox getBox() {
		return box;
	}

	public void setBox(String box) {
		String coord[] = box.split(",");
		
		this.box = new bBox(Double.parseDouble(coord[0]), Double.parseDouble(coord[1])
				, Double.parseDouble(coord[2]), Double.parseDouble(coord[3]));
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
}
