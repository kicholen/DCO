package pwr.osm.utils;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import pwr.osm.data.constants.NodeConstants;
import pwr.osm.data.representation.MapArea;
import pwr.osm.data.representation.XMLElements.NodeXML;
import pwr.osm.model.Node;
import static java.lang.Math.*;

public class MapUtils {
	
	private MapUtils(){}
	
	/**
	 * Metoda wylicza odleglosc miedzy dwoma punktami na mapie (w lini prostej)
	 * @param node - punkt pierwszy
	 * @param point - punkt drugi
	 * @return - odleglosc jako liczba typu double
	 */
	public static double countDistance(Node node, Node point) {
		if (node == null)
			return Double.MAX_VALUE;
		double Lat_A = node.getLattitude();
		double Lat_B = point.getLattitude();
		double Lon_A = node.getLongtitude();
		double Lon_B = point.getLongtitude();
		
		return countDistance(Lat_A, Lat_B, Lon_A, Lon_B);
	}
	
	public static double countDistance(double lattitudeA, double lattitudeB,
						double longtitudeA, double longtitudeB){
		return Math.sqrt(Math.pow(Math.abs((longtitudeA-longtitudeB)), 2)+
				Math.pow(Math.abs(lattitudeB-lattitudeA),2));
	}

	public static double countDistance(NodeXML node, GeoPosition point) {
		double Lat_A = node.getLattitude();
		double Lat_B = point.getLatitude();
		double Lon_A = node.getLongtitude();
		double Lon_B = point.getLongitude();

		return countDistance(Lat_A, Lat_B, Lon_A, Lon_B);
	}

	public static boolean validMaxSpan(MapArea area, GeoPosition position) {
		double longtitude = position.getLongitude();
		double latitude = position.getLatitude();
		double extremeLatitude = max(
					abs(latitude - area.getMaxLattitude()),
					abs(latitude - area.getMinLattitude()));
		double extremeLongtitude = max(
				abs(longtitude - area.getMaxLongtitude()),
				abs(longtitude - area.getMinLongtitude()));
						
		return extremeLatitude < NodeConstants.MAX_AREA ||
				extremeLongtitude < NodeConstants.MAX_AREA;
	}
}
