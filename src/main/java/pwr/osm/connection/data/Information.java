package pwr.osm.connection.data;

import java.io.Serializable;

public enum Information implements Serializable{

	WAY_IS_ALREADY_FOUND("Stop you job"),
	FIND_WAY("Find way"),
	WAY_IS_FOUND("Way is found"), 
	WAY_NOT_FOUND("Unable to find way"),
	TOO_MUCH_REQUESTS("Too much connections to server");
	
	private String info;

	private Information(String info){
		this.info = info;
	} 
	
	@Override 
	public String toString(){ 
		return info; 
	}
}
