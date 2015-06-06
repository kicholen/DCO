package pwr.osm.converters;

import java.io.Serializable;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

public class YNBooleanCOnverter implements Converter, Serializable{


    @Override
    public Object convertDataValueToObjectValue(Object dataValue, Session session) {
        if (dataValue == null) {
            return null;
        }

        String str = (String) dataValue;
        
        return str.equals("Y");
    }


	@Override
    public Object convertObjectValueToDataValue(Object objectValue, Session session) {
        Boolean b = (Boolean) objectValue;
        
        return b ? "Y" : "N";
    }
	
	
	@Override
	public void initialize(DatabaseMapping arg0, Session arg1) {
		return;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

}
