package pwr.osm.data.representation;

/**
 * KLASA NIE DO UZYTKU !
 * Typ wyliczeniowy reprezentujacy mozliwe wartosci atrybutow
 * elementow XML. Wlasciwie nie potrzeban
 * @author Dawid Cokan
 *
 */
public enum attributeType {

	LAT("lat"),
	LON("lon"),
	VERSION("version"),
	ID("id");
	
	private String attributeName;
	
	private attributeType(String name){
		this.attributeName = name;
	}
	
	public String getAttributeName(){
		return attributeName;
	}
	
	@Override
	public String toString(){
		return attributeName;
	}
}
