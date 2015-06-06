package pwr.osm.test;

import java.io.FileNotFoundException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pwr.osm.service.impl.JunctionFinderImpl;
import pwr.osm.service.interf.JunctionFinder;

public class DBConnectionTest {
	
//	@Autowired
//	private TestService ss;
	
    public static void main(String[] args) {

    	ApplicationContext context = 
    	    	  new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
    	
    	JunctionFinderImpl service = (JunctionFinderImpl) context.getBean("junctionService");
    	try {
			service.findUnresolvedProblems();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	JunctionFinder ff = (JunctionFinder) context.getBean("junctionService");
//    	WayServiceImpl way =  (WayServiceImpl)context.getBean(WayService.class);
//		ff.equals(null);
//		try {
//			ff.parseDocument();
//			try {
//				ff.loadEdges();
//			} catch (SAXException | IOException | ParserConfigurationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (SAXException | IOException | ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	try {
//			System.out.println(way.getWaysFromArea(new MapArea(51.0493875000,
//					51.0503566000, 16.9540589000, 16.9704987000)).get(0).getNodes());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    	MapService map = (MapService)context.getBean("mapService");
//    	map.getWayFromWithTwoPoints(new GeoPosition(0,0), new GeoPosition(0,0));
  }

}
