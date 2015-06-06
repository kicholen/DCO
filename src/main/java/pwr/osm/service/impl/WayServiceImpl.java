package pwr.osm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pwr.osm.dao.interf.WayDAO;
import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Way;
import pwr.osm.service.interf.WayService;

@Service("wayService")
public class WayServiceImpl implements WayService {
	@Autowired
	private WayDAO wayDAO;
	
	@Override
	@Transactional
	public Way findWay(int id) {
		return wayDAO.findById(id);
	}

	@Override
	public List<Way> getWaysFromArea(MapArea area) {
		return wayDAO.getWaysFromArea(area);
	}

}
