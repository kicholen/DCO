package pwr.osm.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pwr.osm.dao.BaseDAO;
import pwr.osm.dao.interf.NodeDAO;
import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Node;

@Repository("nodeDAO")
public class NodeDAOImpl extends BaseDAO implements NodeDAO {
	
    
    
	@Override
	@Transactional
	public Node findNode(long id) {
		return em.find(Node.class, id);
	}
	
	@Override
	@Transactional
	public List<Node> getNodesFromArea(MapArea area) {
		return em.createNamedQuery("Node.getNodesFromArea", Node.class)
				.setParameter("minLong", area.getMinLongtitude())
				.setParameter("maxLong", area.getMaxLongtitude())
				.setParameter("minLat", area.getMinLattitude())
				.setParameter("maxLat", area.getMaxLattitude())
				.getResultList();
	}
	
	@Override
	@Transactional
	public void saveNodeInDB(Node nodeDB) {
		em.persist(nodeDB);
    	em.flush();
	}

}
