package pwr.osm.data.representation;

/**
 * Klasa wraz z <attributeType> mozna interpretowac jako 
 * strukture do przechowywania pary klucz-wartosc z tagu 
 * XML. Obecnie nie potrzebna i nie uzywana
 * @author Dawid Cokan
 *
 */
public class attributeXML{
	
	private attributeType type;
	
	private String value;
	
	public attributeXML(attributeType type, String value){
		this.type = type;
		this.value = value;
	}
	
	public String getKey(){
		return type.getAttributeName();
	}
	
	public String getValue(){
		return value;
	}
	
	public double getAsDouble(){
		return Double.parseDouble(value);
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj==null)
			return false;
		try{
			String temp = (String)obj;
			return value.toString().equals(temp);
		}catch (ClassCastException e){
			attributeXML attr = (attributeXML) obj;
			return (attr.toString().equals(toString()));
		}
	}
	
	@Override
	public String toString(){
		return type+"="+value;
	}
}