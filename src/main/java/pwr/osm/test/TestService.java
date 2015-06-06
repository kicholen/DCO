package pwr.osm.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pwr.osm.service.interf.NodeService;

@Component
public class TestService {

	@Autowired
	private NodeService nodeService;
	
	public void init(){
//		nodeService = new NodeServiceImpl();
        System.out.println(nodeService.findNode(1));
	}

}
