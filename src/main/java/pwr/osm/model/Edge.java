package pwr.osm.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@NamedQueries({
	@NamedQuery(name="Edge.getEdgesFromArea",
				query="SELECT t FROM Edge t WHERE "+
						"(t.begin.longtitude >= :minLong AND " + 
						"t.begin.longtitude <= :maxLong AND "+
						"t.begin.lattitude >= :minLat AND "+
						"t.begin.lattitude <= :maxLat) OR "+
						"(t.end.longtitude >= :minLong AND " + 
						"t.end.longtitude <= :maxLong AND "+
						"t.end.lattitude >= :minLat AND "+
						"t.end.lattitude <= :maxLat)"),
	@NamedQuery(name = "Edge.getAllEdges",
				query = "SELECT t FROM Edge t")
})
@Entity
public class Edge {
	
	@Id
	protected long id;
	
	
	@ManyToOne
	@JoinColumn(name="BEGIN_ID")
	protected Node begin;

	
	@ManyToOne
	@JoinColumn(name="DEST_ID")
	protected Node end;

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Node getBegin() {
		return begin;
	}

	public void setBegin(Node begin) {
		this.begin = begin;
	}

	public Node getEnd() {
		return end;
	}

	public void setEnd(Node end) {
		this.end = end;
	}
	
	@Override
	public String toString(){
		
		return (begin == null || end == null) ? "Empty" :
			"Edge id: "+id+" begin: "+begin.id+" end: "+end.id;
	}
	


}
