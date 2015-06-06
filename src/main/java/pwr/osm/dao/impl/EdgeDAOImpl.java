package pwr.osm.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pwr.osm.dao.BaseDAO;
import pwr.osm.dao.interf.EdgeDAO;
import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Edge;

@Repository("edgeDAO")
public class EdgeDAOImpl extends BaseDAO implements EdgeDAO {
	

	@Override
	@Transactional
	public Edge findEdge(long id) {
		return em.find(Edge.class, id);
	}

	@Override
	@Transactional
	public void saveEdgeInDB(Edge edge) {
		em.persist(edge);
		em.flush();
	}

	@Override
	@Transactional
	public List<Edge> getEdgesFromArea(MapArea area) {
		return em.createNamedQuery("Edge.getEdgesFromArea", Edge.class)
				.setParameter("minLong", area.getMinLongtitude())
				.setParameter("maxLong", area.getMaxLongtitude())
				.setParameter("minLat", area.getMinLattitude())
				.setParameter("maxLat", area.getMaxLattitude())
				.getResultList();
	}

	@Override
	public List<Edge> getAllEdges() {
		return em.createNamedQuery("Edge.getAllEdges", Edge.class)
				.getResultList();
	}

}
