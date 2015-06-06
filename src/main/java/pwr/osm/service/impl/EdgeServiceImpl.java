package pwr.osm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pwr.osm.dao.interf.EdgeDAO;
import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Edge;
import pwr.osm.service.interf.EdgeService;
import pwr.osm.utils.MapUtils;

@Service("edgeService")
public class EdgeServiceImpl implements EdgeService {

	@Autowired
	private EdgeDAO edgeDAO;
	
	@Override
	public Edge findEdge(long id) {
		return edgeDAO.findEdge(id);
	}

	@Override
	public void saveEdgeInDB(Edge edge) {
		edgeDAO.saveEdgeInDB(edge);
	}
	
	@Override
	public List<Edge> getEdgesInArea(MapArea area){
		return edgeDAO.getEdgesFromArea(area);
	}
	
	@Override
	public List<Edge> getAllEdges(){
		return edgeDAO.getAllEdges();
	}

	@Override
	public double countEdgeValue(Edge e) {
		return MapUtils.countDistance(e.getBegin(), e.getEnd());
	}
	
	
}
