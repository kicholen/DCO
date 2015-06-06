package pwr.osm.data.representation.XMLElements;

import java.util.LinkedList;
import java.util.List;

import pwr.osm.data.representation.tagXML;
import pwr.osm.model.Node;
import pwr.osm.model.Way;

/**
 * Klasa reprezentujaca tag WAY z OSM. Struktura odpowiada 
 * tagowi oraz posiada wszystkie settery i gettery
 * @author Dawid Cokan
 * @date 10-05-2014
 *
 */

public class WayXML extends Way implements NodeXMLInterface {

	private int version;
	private String timestamp;
	private int uid;
	private String user;
	private int changeset;
	private LinkedList<Nd> nodes;
	private LinkedList<Tag> tags;
	
	public WayXML(){
		nodes = new LinkedList<Nd>();
		tags = new LinkedList<Tag>();
	}
	
	
	@Override
	public tagXML getTagType() {
		return tagXML.WAY;
	}

	@Override
	public String toString(){
		String s = "";
		s = "<way id="+id+" version="+version+
				" timestamp="+timestamp+" uid="+uid+" user="+user+" changeset="+changeset+">";
		for (Nd n: nodes)
			s = s+ n+"\n";
		for (Tag t: tags)
			s = s+t+"\n";
		
		return s;
	}
	public void addNd(Nd nd){
		nodes.add(nd);
	}

//	public int getId() {
//		return id;
//	}
	
	public void addTag(Tag tag){
		tags.add(tag);
	}


//	public void setId(String id) {
//		this.id = Integer.parseInt(id);
//	}
//
//	public boolean isDeleted() {
//		return deleted;
//	}
//
//	public void setDeleted(boolean deleted) {
//		this.deleted = deleted;
//	}



	public int getVersion() {
		return version;
	}



	public void setVersion(String version) {
		this.version = Integer.parseInt(version);
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



	public List<Tag> getTags(){
		return tags;
	}
	
	public void setUid(String uid) {
		this.uid = Integer.parseInt(uid);
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
		this.changeset = Integer.parseInt(changeset);
	}


	public int getFirstNode() {
		return nodes.get(0).getRef();
	}


	public int getLastNode() {
		return nodes.get(nodes.size()-1).getRef();
	}





	public List<Nd> getNds() {
		return nodes;
	}


	public boolean containsNode(NodeXML node) {

		for (Nd n: nodes)
			if (node.getId() == n.getRef())
				return true;
		
		return false;
	}

}
