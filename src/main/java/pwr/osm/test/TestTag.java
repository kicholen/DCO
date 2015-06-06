package pwr.osm.test;

import java.util.LinkedList;
import java.util.List;

import pwr.osm.data.representation.Edge;
import pwr.osm.data.representation.XMLElements.NodeXML;

public class TestTag {

	public static void main(String[] args){
		NodeXML n1 = new NodeXML (15,10);
		NodeXML n2 = new NodeXML (10, 20);
		NodeXML n3 = new NodeXML (15, 20);
		NodeXML n4 = new NodeXML (10, 5);
		NodeXML n5 = new NodeXML (5, 15);
		Edge e1 = new Edge (n1, n2);
		Edge e2 = new Edge (n1, n3);
		Edge e3 = new Edge (n2, n4);
		Edge e4 = new Edge (n3, n4);
		Edge e5 = new Edge (n1, n5);
		
		List<Edge> list = new LinkedList<Edge>();
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		list.add(e5);
		
//		new Dijkstry(list).findWay(n1, n4);
//		Tag tag1 = new Tag("building", "yes");
//		Tag tag2 = new Tag("*", "yes");
//		Tag tag3 = new Tag("*", "no");
//		Tag tag4 = new Tag("building", "no");
//		Tag tag5 = new Tag("*building", "*");
//		System.out.println(tag2+"=="+tag1+" -> "+tag2.equals(tag1));
//		System.out.println(tag3+"=="+tag1+" -> "+tag3.equals(tag1));
//		System.out.println(tag2+"=="+tag3+" -> "+tag2.equals(tag4));
//		System.out.println(tag2+"=="+tag4+" -> "+tag2.equals(tag4));
//		System.out.println(tag4+"=="+tag1+" -> "+tag4.equals(tag1));
//		System.out.println(tag5+"=="+tag1+" -> "+tag5.equals(tag1));
	}
}
