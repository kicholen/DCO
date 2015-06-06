package pwr.osm.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 * Klasa reprezentujaca tag WAY z OSM. Struktura odpowiada 
 * tagowi oraz posiada wszystkie settery i gettery
 * @author Dawid Cokan
 * @date 10-05-2014
 *
 */

@NamedQueries({
//	@NamedQuery(name="Way.getWaysFromArea",
//			query="SELECT Node FROM Way "+
//					"LEFT OUTER JOIN node_way "+
//					"ON Way.ID = node_way.way_id "+
//					"AND Way.iD = :wayId "+
//					"LEFT OUTER JOIN n "+
//					"ON node_way.node_id = Node.ID"),
	@NamedQuery(name="Way.getNode",
			query = "from Node t where t.id=37899387")
	
})
@Entity
public class Way {

	@Converter(
		name = "ynBoolean",
		converterClass=pwr.osm.converters.YNBooleanCOnverter.class
	)
	
	@Id		
	protected int id;

	@Convert("ynBoolean")
	protected boolean deleted;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "NODE_WAY", joinColumns = { 
			@JoinColumn(name = "WAY_ID", referencedColumnName="ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "NODE_ID", referencedColumnName="ID") })
	protected List<Node> nodes = new ArrayList<Node>();


	public Way(){
//		nodes = new LinkedList<Nd>();
//		tags = new LinkedList<Tag>();
	}
	
	public List<Node> getNodes() {
		return nodes;
	}


	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}


	public void setId(int id) {
		this.id = id;
	}

	
	public int getId() {
		return id;
	}
	

	public void setId(String id) {
		this.id = Integer.parseInt(id);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}



}

