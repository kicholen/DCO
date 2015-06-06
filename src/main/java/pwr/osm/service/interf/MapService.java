package pwr.osm.service.interf;

import java.util.List;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import pwr.osm.data.representation.MapPosition;
import pwr.osm.exceptions.WayComputingException;

public interface MapService {

	List<MapPosition> getWayFromWithTwoPoints(GeoPosition begin,
			GeoPosition destination) throws WayComputingException;

	List<MapPosition> getWayFromWithTwoPoints(MapPosition begin,
			MapPosition destnation) throws WayComputingException;

	boolean isComplete();

	void stopAction();
}
