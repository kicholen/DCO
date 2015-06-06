package pwr.osm.data.representation.XMLElements;

import pwr.osm.data.representation.tagXML;

public class Junction implements NodeXMLInterface{
//    <junction id="976698820" type="priority" x="33523.57" y="39644.19" incLanes="133607265_0 28458758#0_0 28458758#0_1" intLanes="" shape="33522.33,39649.93 33528.38,39647.56 33529.10,39645.30 33526.48,39639.36 33517.79,39643.19 33520.41,39649.14">
//    <request index="0" response="11100" foes="11100"/>
//    <request index="1" response="10000" foes="10000"/>
//    <request index="2" response="00000" foes="00001"/>
//    <request index="3" response="00000" foes="00001"/>
//    <request index="4" response="00000" foes="00011"/>
//</junction>
	
	private int id;
	private String type;
	private double x;
	private double y;
	private String incLanes;
	private String shape;
	
	public int getId() {
		return id;
	}
	public void setId(String id) {
		this.id = Integer.parseInt(id);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getX() {
		return x;
	}
	public void setX(String x) {
		this.x = Double.parseDouble(x);
	}
	public double getY() {
		return y;
	}
	public void setY(String y) {
		this.y = Double.parseDouble(y);
	}
	public String getIncLanes() {
		return incLanes;
	}
	public void setIncLanes(String incLanes) {
		this.incLanes = incLanes;
	}
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	@Override
	public tagXML getTagType() {
		return tagXML.JUNCTION;
	}
}
