package pwr.osm.data.representation.XMLElements;

import java.util.LinkedList;
import java.util.List;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import pwr.osm.data.representation.tagXML;
import pwr.osm.model.Node;

public class NodeXML extends Node implements NodeXMLInterface {
//	 <node id="999765145" version="4" timestamp="2011-12-30T19:05:56Z" uid="429794" user="SennaHB" changeset="10248571" lat="51.1414903" lon="16.9395742"/>
//	private int id;
	private static int staticId=1;
	private int version;
	private String timestamp;
	private int uid;
	private String user;
	private int changeset;
//	private double lat;
//	private double lon;
//	public double D;
//	private int treeId;
	private List<Tag> tags;
	
	public boolean isLongtitudeSet() {
		return longtitudeSet;
	}


	public void setLongtitudeSet(boolean longtitudeSet) {
		this.longtitudeSet = longtitudeSet;
	}


	public boolean isLattitudeSet() {
		return lattitudeSet;
	}


	public void setLattitudeSet(boolean lattitudeSet) {
		this.lattitudeSet = lattitudeSet;
	}

private boolean longtitudeSet;
private boolean lattitudeSet;
	
	
	public NodeXML(){
		tags = new LinkedList<Tag>();
//		treeId = Integer.MAX_VALUE;
	}
	
	
	public NodeXML(double i, double j) {
		this();
		id = staticId++;
//		this.lat = i;
//		this.lon = j;
	}


	public void addTag(Tag tag){
		tags.add(tag);
	}
	
//	public int getId() {
//		return id;
//	}
	public void setId(String id) {
		this.id = Integer.parseInt(id);
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = Integer.parseInt(version);;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = Integer.parseInt(uid);;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getChangeset() {
		return changeset;
	}
	public void setChangeset(String changeset) {
		this.changeset = Integer.parseInt(changeset);;
	}
//	public double getLat() {
//		return lattitude;
//	}
	public void setLat(String lat) {
		this.setLattitude(Double.parseDouble(lat));
		this.lattitudeSet = true;
	}
//	public double getLon() {
//		return longtitude;
//	}
	public void setLon(String lon) {
		this.setLongtitude (Double.parseDouble(lon));
		this.longtitudeSet = true;
	}
//	
//	@Override
//	public String toString(){
//		String s=  "<node id="+id+" version="+version+" timestamp="+timestamp+" uid="+uid+" user="+user+
//				" changeset="+changeset+" lat="+lat+" lon="+lon+(tags.isEmpty() ? "/>" : ">\n");
//		for (Tag t: tags){
//			s = s + t+"\n";
//		}
//		
//		if (!s.isEmpty())
//			s = s+"<node/>";
//		return s;
//	}


	@Override
	public tagXML getTagType() {
		return tagXML.NODE;
	}
//
//	@Override
//	public boolean equals(Object obj){
//		if (obj== null || ! (obj instanceof NodeXML))
//			return false;
//		return ((NodeXML) obj).id == id;
//		
//	}

	public GeoPosition getGeoPosition() {
		return new GeoPosition(super.lattitude, super.longtitude);
	}


//	public int getTreeId(){
//		return treeId;
//	}
//	
//	public void setTree(Node other) {
//		this.treeId = Math.min(treeId, other.treeId);
//	}


//	public void setTree(int i) {
//		this.treeId = i;
//	}
//	

//	public void setTrigger(attributeXML attribute){
////		this.trigger = attribute;
//	}
}
