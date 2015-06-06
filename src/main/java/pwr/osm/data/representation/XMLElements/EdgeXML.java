package pwr.osm.data.representation.XMLElements;

import java.util.ArrayList;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import pwr.osm.data.representation.tagXML;

public class EdgeXML implements NodeXMLInterface {
	
	private String id;
	private int from;
	private int to;
	private String name;
	private int priority;
	private String type;
	private double[] shape;
	private ArrayList<GeoPosition> coordinates;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = Integer.parseInt(from);
	}
	public int getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = Integer.parseInt(to);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = Integer.parseInt(priority);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double[] getShape() {
		return shape;
	}
	public void setShape(String stringShape) {
		String shapes[] = stringShape.split(",");
		if (shape==null)
			shape = new double[shapes.length];
		int i=0;
		for (String s:shapes)
			shape[i++] = Double.parseDouble(s);
		
	}
	
	public void addCoordinates(ArrayList<GeoPosition> coordinates) {
		if (this.coordinates == null)
			this.coordinates = new ArrayList<GeoPosition>();
		this.coordinates.addAll(coordinates);
	}
	
	public ArrayList<GeoPosition> getCoordinates() {
		return coordinates;
	}
	
	@Override
	public tagXML getTagType() {
		return tagXML.EDGE;
	}
	
	@Override
	public String toString(){
		return "<edge id="+id+" from="+from+" to="+to+" name="+name+" priority="+priority+
				" type="+type+" shape="+shape+">";
	}


}
