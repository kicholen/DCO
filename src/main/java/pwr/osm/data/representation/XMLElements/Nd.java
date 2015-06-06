package pwr.osm.data.representation.XMLElements;

import pwr.osm.data.representation.tagXML;

public class Nd implements NodeXMLInterface{

	private int ref;

	public Nd(){
		
	}
	public int getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = Integer.parseInt(ref);
	}
	@Override
	public tagXML getTagType() {
		return tagXML.ND;
	}
	
	@Override
	public String toString(){
		return "<nd ref="+ref+"/>";
	}
}
