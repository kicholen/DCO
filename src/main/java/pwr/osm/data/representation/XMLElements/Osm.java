package pwr.osm.data.representation.XMLElements;

import java.util.LinkedList;
import java.util.List;

import pwr.osm.data.representation.tagXML;

public class Osm implements NodeXMLInterface{
//	<osm version="0.6" generator="Osmosis SNAPSHOT-r26564">
//	<bound box="51.12591,16.98612,51.12894,16.99471" origin="Osmosis SNAPSHOT-r26564"/>
//	<node id="163019451" version="3" timestamp="2010-04-14T21:49:17Z" uid="117617" user="lms" changeset="4426883" lat="51.1249003" lon="16.9841755"/>
	private double version;
	private String generator;
	private Bound bound;
	private List<NodeXML> nodes;
	private List<WayXML> ways;
	
	public Osm(){
		nodes = new LinkedList<NodeXML>();
		ways = new LinkedList<WayXML>();
	}
	
	public Osm(Osm osm) {
		this();
		this.version = osm.version;
		this.bound = osm.bound;
		this.generator = osm.generator; 
	}
	
	@Override
	public tagXML getTagType() {
		return tagXML.OSM;
	}
	
	@Override
	public String toString(){
		String s = "<osm version="+version+" generator="+generator+">"+"\n";
		
		s = s+bound+"\n";
		for (NodeXML n:nodes)
			s = s+n+"\n";
		for (WayXML w:ways)
			s = s+w+"\n";
		return s;
	}
	
	public void addWay(WayXML way){
		ways.add(way);
	}
	public void addNode(NodeXML node){
		nodes.add(node);
	}
	public void addBound(Bound bound){
		this.bound = bound;
	}
	
	public double getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = Double.parseDouble(version);
	}
	public String getGenerator() {
		return generator;
	}
	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public List<NodeXML> getNodes() {
		return nodes;
	}
	public List<WayXML> getWays() {
		return ways;
	}
	public NodeXML getNode(int id) {
		for (NodeXML node: nodes){
			if (node.getId() == id)
				return node;
		}
		
		return null;
	}
}
