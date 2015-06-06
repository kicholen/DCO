package pwr.osm.service.interf;

import java.util.List;

import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Way;

public interface WayService {
	
	Way findWay(int id);

	List<Way> getWaysFromArea(MapArea area);
}
