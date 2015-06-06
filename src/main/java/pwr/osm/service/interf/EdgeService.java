package pwr.osm.service.interf;

import java.util.List;

import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Edge;

public interface EdgeService {

	public Edge findEdge(long id);

	public void saveEdgeInDB(Edge edge);

	public List<Edge> getEdgesInArea(MapArea area);

	List<Edge> getAllEdges();

	public double countEdgeValue(Edge e);
}
