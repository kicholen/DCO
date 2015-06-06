package pwr.osm.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import pwr.osm.dao.BaseDAO;
import pwr.osm.dao.interf.WayDAO;
import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Node;
import pwr.osm.model.Way;

@Repository("wayDAO")
public class WayDAOImpl extends BaseDAO implements WayDAO {

	private static final String WAYS_IN_AREA =
			"SELECT * FROM WAY WHERE ID IN ("+
			"SELECT distinct(Way.id) "+
			  "FROM Way "+
			"LEFT OUTER JOIN node_way "+
			 " ON Way.ID = node_way.way_id "+
			" AND Way.iD = :wayId "+
			"LEFT OUTER JOIN Node"+
			 " ON node_way.node_id = Node.ID "+
			"WHERE "+
			 "Node.longtitude >= :minLong and Node.longtitude <= :maxLong and "+
			 "Node.lattitude >= :minLat and Node.lattitude <= :maxLat)";
	@Override
	public Way findById(int id) {
		return em.find(Way.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Way> getWaysFromArea(MapArea area) {
		return em.createNativeQuery(WayDAOImpl.WAYS_IN_AREA, Way.class)
				.setParameter("minLong", area.getMinLongtitude())
				.setParameter("maxLong", area.getMaxLongtitude())
				.setParameter("minLat", area.getMinLattitude())
				.setParameter("maxLat", area.getMaxLattitude())
				.setParameter("wayId", 1)
				.getResultList();
	}


//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Node> getNodesFromWay(int id) {
//		
//		return em.createNativeQuery(query,Node.class)
//			.setParameter("wayId", 1)
//			.getResultList();
//	}

}
