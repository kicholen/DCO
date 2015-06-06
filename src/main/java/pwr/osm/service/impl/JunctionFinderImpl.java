package pwr.osm.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.persistence.PersistenceException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pwr.osm.api.XapiConnector;
import pwr.osm.data.representation.XMLElements.NodeXML;
import pwr.osm.model.Edge;
import pwr.osm.service.interf.EdgeService;
import pwr.osm.service.interf.JunctionFinder;
import pwr.osm.service.interf.NodeService;

@Service("junctionService")
public class JunctionFinderImpl implements JunctionFinder{


	@Autowired
	private NodeService nodeService;
	@Autowired 
	private EdgeService edgeService;
	
	
	public void findUnresolvedProblems() throws FileNotFoundException{

		BufferedReader read = new BufferedReader(new FileReader("Failed.txt"));
		String line=null;
		ArrayList<Long> wrongInputs = new ArrayList<Long>();
		int allInputs=0;
		try {
			while ( (line = read.readLine()) != null){
				allInputs++;
				line = line.substring(13);
				line = line.substring(0,line.lastIndexOf(" was"));
				if (line.contains("_")){
					// There are defined id with underlines which were skipped.
					// All are treat as -1
					wrongInputs.add(new Long(-1));
				} else {
					long id = Long.parseLong(line);
					if ( nodeService.findNode(id) == null
							&& !wrongInputs.contains(id)){
						wrongInputs.add(id);
						
					}
				}
//				System.out.println(id);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			System.out.println("All logged in file: "+allInputs+
					", wrongInputs: "+wrongInputs.size());
		}
	}
	
	public void loadEdges() throws SAXException, IOException, ParserConfigurationException{
	    DocumentBuilderFactory factory =
	        DocumentBuilderFactory.newInstance();

	    DocumentBuilder builder = factory.newDocumentBuilder();


	    Document document =
	      builder.parse(new File("C:/Users/dco/PWR/wroclaw/wroclaw.net.xml"));


	    NodeList nodeList = document.getElementsByTagName("edge");

	    for (int i = 0; i < nodeList.getLength(); i++) {
	    	
	    	Node node = nodeList.item(i);
	    	Edge edge = new Edge();
	    	NamedNodeMap attributes = node.getAttributes();
	    	Node nodeID =  attributes.getNamedItem("id");
	    	Node begin =  attributes.getNamedItem("from");
	    	Node destination =  attributes.getNamedItem("to");
	    	if (nodeID != null && begin != null && destination != null ){
		    	String beginId = begin.getNodeValue();
		    	String destinationId = destination.getNodeValue();
		    	try{
			    	long beginIdVal = Long.parseLong(beginId);
			    	long destIdVal = Long.parseLong(destinationId);

			    	NodeXML beginNodeOSM = XapiConnector.getNodeByGivenId(beginIdVal);
			    	NodeXML destNodeOSM = XapiConnector.getNodeByGivenId(destIdVal);
				    	
				    if (validNodesFromOSM(beginNodeOSM,beginIdVal, 
				    		destNodeOSM,destIdVal)){
					   	edge.setBegin(beginNodeOSM);
					   	edge.setEnd(destNodeOSM);
					   	try{
					   		edgeService.saveEdgeInDB(edge);
					    } catch (PersistenceException e){
					   		System.err.println("Way is al.ready defined");
					   	}
				    }	else{
				    	registerFail(beginIdVal);
				    	registerFail(destIdVal);
				    }
			    	
		    	} catch(NumberFormatException e){
		    		registerFail(beginId);
		    		registerFail(destinationId);
		    	}
			  }
	    }
	}
	
	
	private boolean validNodesFromOSM(NodeXML beginNodeOSM, long beginIdVal, 
			NodeXML destNodeOSM, long destIdVal) {


		return verifyDataFromApi(beginIdVal, beginNodeOSM) && 
				verifyDataFromApi(destIdVal, destNodeOSM);
		
	}


	public void loadNodes() throws SAXException, IOException, ParserConfigurationException{
	    DocumentBuilderFactory factory =
	        DocumentBuilderFactory.newInstance();

	    DocumentBuilder builder = factory.newDocumentBuilder();


	    Document document =
	      builder.parse(new File("C:/Users/dco/PWR/wroclaw/legnicka.net.xml"));


	    NodeList nodeList = document.getElementsByTagName("junction");

	    for (int i = 0; i < nodeList.getLength(); i++) {
	    	Node node = nodeList.item(i);
	    	pwr.osm.model.Node nodeDB = new pwr.osm.model.Node();
	    	NamedNodeMap attributes = node.getAttributes();
	    	String textId = attributes.getNamedItem("id").getNodeValue();
	    	try{
		    	long id = Long.parseLong(textId);

		    	NodeXML nodeXML = XapiConnector.getNodeByGivenId(id);
			    if (verifyDataFromApi(id, nodeXML)){
			    	nodeDB.setId(id);
			    	nodeDB.setDeleted(false);
			    	nodeDB.setLattitude(nodeXML.getLattitude());
			    	nodeDB.setLongtitude(nodeXML.getLongtitude());
			    	
			    	nodeService.saveNodeInDB(nodeDB);
		    	}	else
		    		registerFail(id);
	    	} catch(NumberFormatException e){
	    		registerFail(textId);
	    	}
		  }
	}


	private boolean verifyDataFromApi(long id, NodeXML nodeXML) {

		return (nodeXML == null || nodeXML.getId() != id ||
				!nodeXML.isLattitudeSet() ||
				!nodeXML.isLongtitudeSet()) &&
				nodeService.findNode(id) != null;
	}


	private void registerFail(String id) {
		try(PrintWriter out = new PrintWriter
				(new BufferedWriter(new FileWriter("Failed.txt", true)))) {
		    out.println("Node with id "+id+" was not ound in OSM");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void registerFail(long id) {
		registerFail(""+id);
	}

}
