package pwr.osm.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.persistence.annotations.Convert;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import pwr.osm.data.representation.XMLElements.NodeXML;

@NamedQueries({
		@NamedQuery(name="Node.getNodesFromArea",
			query="select t from Node t where "+
					"t.longtitude >= :minLong and t.longtitude <= :maxLong and "+
					"t.lattitude >= :minLat and t.lattitude <= :maxLat")
	}					
)

@Entity
public class Node {
//asas
	@Id
	protected long id;
	
	@Convert("ynBoolean")
	protected boolean deleted;
	
	
	protected double lattitude;
	
	protected double longtitude;
	
	
	public Node(){
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public double getLattitude() {
		return lattitude;
	}

	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public void setId(long id2) {
		this.id = id2;
	}

	public long getId() {
		return id;
	}
	
	
	@Override
	public String toString(){
//		String s=  "<node id="+id+" version="+version+" timestamp="+timestamp+" uid="+uid+" user="+user+
//				" changeset="+changeset+" lat="+lat+" lon="+lon+(tags.isEmpty() ? "/>" : ">\n");
//		for (Tag t: tags){
//			s = s + t+"\n";
//		}
//		
//		if (!s.isEmpty())
//			s = s+"<node/>";
		return "Node: "+id;
	}



	@Override
	public boolean equals(Object obj){
		if (obj== null )
			return false;
		return ((Node) obj).id == id;
		
	}

}
