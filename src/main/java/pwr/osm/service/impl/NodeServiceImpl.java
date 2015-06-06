package pwr.osm.service.impl;

import java.util.List;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pwr.osm.dao.interf.NodeDAO;
import pwr.osm.data.representation.MapArea;
import pwr.osm.exceptions.WayComputingException;
import pwr.osm.model.Node;
import pwr.osm.service.interf.NodeService;
import pwr.osm.utils.MapUtils;

@Service("nodeService")
public class NodeServiceImpl implements NodeService{

	@Autowired
	private NodeDAO nodeDAO;
    
	@Override
	@Transactional
	public Node findNode(long id) {
		return nodeDAO.findNode(id);
	}
	
	@Override
	@Transactional
	public Node findClosestNode(GeoPosition position) throws WayComputingException {

		List<Node> nodes = null;
		double min = Double.MAX_VALUE;
		Node currentMin = null;
		double startLat = position.getLatitude(), startLon = position.getLongitude();
		
		boolean searchCondition = true;
		MapArea area = null;
		try {
			area = new MapArea(position);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.err.println("Wrong area has been defined");
		}
		
		while (searchCondition ){
			area.generateOffset();
				
			try {
				nodes = nodeDAO.getNodesFromArea(area);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Wrong area has been defined");
			}
			
			
			if (nodes != null && !nodes.isEmpty()){
				searchCondition = false;
			}else if (!MapUtils.validMaxSpan(area, position)){
				throw new WayComputingException("No nodes found in the area");
			}
		}
		
		for (Node nd: nodes){
			double tempMin = MapUtils.countDistance(startLat, nd.getLattitude(), startLon, nd.getLongtitude());
			if (tempMin < min){
				min = tempMin;
				currentMin = nd;
			}
			
		}
		
		return currentMin;
	}

	
	@Override
	@Transactional
	public void saveNodeInDB(Node nodeDB) {
		nodeDAO.saveNodeInDB(nodeDB);
	}

	@Override
	@Transactional
	public List<Node> findNodesInArea(MapArea area) {
		return nodeDAO.getNodesFromArea(area);
	}

}
