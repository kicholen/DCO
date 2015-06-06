package pwr.osm.data.representation.XMLElements;

import java.util.LinkedList;
import java.util.List;

import pwr.osm.data.representation.tagXML;

public class Tag implements NodeXMLInterface{

	/**
	 * Lista z parami klucz-wartosc elementow. 
	 * Lista wykorzystywana do wskazywania tagow ktore maja byc
	 * odrzucane (te ktore posiadaja przynajmniej jeden
	 * ze zdefiniowanych tu tagow)
	 */
	public static final List<Tag> ABBORTED;
	private String k;
	private String v;
	
	static {
		ABBORTED = new LinkedList<Tag>();
		ABBORTED.add(new Tag("building", "yes"));
		ABBORTED.add(new Tag("railway", "*"));
		ABBORTED.add(new Tag("waterway", "*"));
		ABBORTED.add(new Tag("*building", "*"));
		ABBORTED.add(new Tag("natural", "*"));
		ABBORTED.add(new Tag("landuse", "*"));
		ABBORTED.add(new Tag("place", "*"));
	}
	
	public Tag(){
	}
	
	public Tag(String key, String value) {
		this.k = key;
		this.v = value;
	}
	
	public String getK() {
		return k;
	}
	public void setK(String k) {
		this.k = k;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	
	@Override
	public String toString(){
		return "<tag k="+k+" v="+v+"/>";
	}
	@Override
	public tagXML getTagType() {
		return tagXML.TAG;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((k == null) ? 0 : k.hashCode());
		result = prime * result + ((v == null) ? 0 : v.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (k.equals("*"))
			return !other.k.isEmpty() && v.equals(other.v);
		if (k.contains("*"))
			return (other.k.contains(k.replace("*", "")) 
					&& (v.equals("*") ? !other.v.isEmpty() : other.v.equals(v)));
		if (v.equals("*"))
			return k.equals(other.k) && !other.v.isEmpty();
		
		return k.equals(other.k)&& v.equals(other.v) ;
	}
}
