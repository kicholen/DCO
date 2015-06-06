package pwr.osm.data.representation;

import pwr.osm.data.representation.XMLElements.NodeXML;
import pwr.osm.data.representation.XMLElements.WayXML;
import pwr.osm.model.Node;
import pwr.osm.utils.MapUtils;

/**
 * Klasa reprezentuje Krawedz w grafie do znajdowania sciezki
 * wskazanymi miejscami w miescie. Krawedz posiada referencje 
 * do dwoch wezlow (startowego i koncowego) oraz dlugosc tej 
 * krawedzi. 
 * @author Dawid Cokan
 * @date 10-05-2014
 *
 */
public class Edge {

	private Node begin;
	private Node end;
	private double wayLength;
	
	public Edge(Node start, Node dest, double wayLength){
		this.wayLength = wayLength;
		this.begin = start;
		this.end = dest;
	}

	public Edge() {
	}

	public Edge(Node begin, Node end) {
		this(begin, end, MapUtils.countDistance(begin,  end));
	}
	


	public Node getBegin() {
		return begin;
	}

	public void setBegin(NodeXML begin) {
		this.begin = begin;
	}

	public Node getEnd() {
		return end;
	}

	public void setEnd(NodeXML end) {
		this.end = end;
	}

	public double getWayLength() {
		return wayLength;
	}

	public void setWayLength(double wayLength) {
		this.wayLength = wayLength;
	}
	
	@Override
	public String toString(){
		return "s: "+begin.getId()+" d: "+end.getId()+", w="+wayLength;
	}
}
