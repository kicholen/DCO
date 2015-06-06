package pwr.osm.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapKit.DefaultProviders;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.DefaultWaypoint;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.painter.Painter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pwr.osm.data.representation.MapArea;
import pwr.osm.data.representation.MapPosition;
import pwr.osm.exceptions.WayComputingException;
import pwr.osm.model.Node;
import pwr.osm.service.interf.EdgeService;
import pwr.osm.service.interf.MapService;
import pwr.osm.service.interf.NodeService;

public class TestFrame {
	private static List<DefaultWaypoint> waypoints = new ArrayList<DefaultWaypoint>();
	private static List<GeoPosition> edges = new ArrayList<GeoPosition>();
	private static  GeoPosition start;
	private static  GeoPosition desitnation;
	private static  JXMapKit jxMapKit;
	
	private static void findPointsInArea(GeoPosition position){
    	ApplicationContext context = 
  	    	  new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});


		NodeService cust = (NodeService)context.getBean("nodeService");
		
		List<Node> nodes;
		try {
			nodes = cust.findNodesInArea(MapArea.generateWithOffset(position));
	    	for (Node node: nodes){
				waypoints.add(new DefaultWaypoint( new GeoPosition(node.getLattitude(),node.getLongtitude())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static List<GeoPosition> findAlledges(GeoPosition b, GeoPosition end) {
    	ApplicationContext context = 
    	    	  new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
      	EdgeService map = (EdgeService)context.getBean("edgeService");
      	List<GeoPosition> positions = new ArrayList<GeoPosition>();
      	Node begin = new Node();
      	begin.setLattitude(b.getLatitude());
      	begin.setLongtitude(b.getLongitude());
      	Node ee = new Node();
      	ee.setLattitude(end.getLatitude());
      	ee.setLongtitude(end.getLongitude());
      	for (pwr.osm.model.Edge e: map.getEdgesInArea(MapArea.creteAreaWithOffset(begin, ee))){
      		positions.add(new GeoPosition(e.getBegin().getLattitude(), e.getBegin().getLongtitude()));
      		positions.add(new GeoPosition(e.getEnd().getLattitude(), e.getEnd().getLongtitude()));
      	}
      	
      	return positions;
	}
	
	private static List<GeoPosition> findAlledges() {
    	ApplicationContext context = 
  	    	  new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
    	EdgeService map = (EdgeService)context.getBean("edgeService");
    	List<GeoPosition> positions = new ArrayList<GeoPosition>();
    	
    	for (pwr.osm.model.Edge e: map.getAllEdges()){
    		positions.add(new GeoPosition(e.getBegin().getLattitude(), e.getBegin().getLongtitude()));
    		positions.add(new GeoPosition(e.getEnd().getLattitude(), e.getEnd().getLongtitude()));
    	}
    	
    	return positions;
	}
	
	
	private static void findWay(GeoPosition from, GeoPosition to){
//    	DataReciver dt = new DataReciver();


//    	List<MapPosition> way = dt.generateWay(from, to);

    	ApplicationContext context = 
    	    	  new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
    	long total = 0;
    	int count = 1;
    	for (int i=0; i<count; i++){
	    	long begin = System.nanoTime();
	    	MapService map = (MapService)context.getBean("mapService");
	    	List<MapPosition> way=null;
			try {
				way = map.getWayFromWithTwoPoints(from, to);    	
				for (MapPosition pos: way){
		    		waypoints.add(new DefaultWaypoint( new GeoPosition(pos.getLatitude(),pos.getLongitude())));
		    		waypoints.add(new DefaultWaypoint( new GeoPosition(pos.getLatitude(),pos.getLongitude())));
		    		edges.add( new GeoPosition(pos.getLatitude(),pos.getLongitude()));
		    		edges.add( new GeoPosition(pos.getLatitude(),pos.getLongitude()));
		    	}
			} catch (WayComputingException e) {
				System.err.println("Problem occured during cumputing - way was not found");
				// TODO Auto-generated catch block
	//			e.printStackTrace();
			}
			
			long end = System.nanoTime();
			total = total + (end-begin);
    	}
//		NodeServiceImpl cust = (NodeServiceImpl)context.getBean("nodeService");
//    	System.out.println("max: "+jxMapKit.getMainMap().getVisibleRect().g
//		List<Node> nodes = cust.findNodesInArea(MapArea.generateWithOffset(position));
//    	List<MapPosition>
    	double time = (double)total / 1000000000.0;
    	
    	System.out.println("Time is: "+ time/count);
	}
	
	
    public static void main(String[] args){

//		List<Edge> edges = new LinkedList<Edge>();
//    	int begin_id = 267890146, dest_id = 1690977998;
//    	GeoPosition b = new GeoPosition(51.1502063000, 17.0290721000);// Dane testowe
//    	GeoPosition e = new GeoPosition(51.1504333000, 17.0291459000);
//    	lat:51.144543 long:17.127841, lat:51.111123 long:16.970720999999998
//    	GeoPosition b = new GeoPosition(51.144543, 17.127841); // Poleska
//    	GeoPosition e = new GeoPosition(51.111123, 16.970720999999998); // muchoborska

//    	GeoPosition b = new GeoPosition(51.098763, 16.997027); // Grabiszyñska
//    	GeoPosition e = new GeoPosition(51.093480, 17.041336); // Dawida
    	
//    	GeoPosition b = new GeoPosition(51.121444, 16.941019); //  ¯ernicka 
//    	GeoPosition e = new GeoPosition(51.108024, 17.049489); // Plac Spo³eczny

//    	GeoPosition b = new GeoPosition(51.114472, 17.006500); //  Kruszwicka 
//    	GeoPosition e = new GeoPosition(51.108024, 17.049489); // Plac Spo³eczny
    	
    	GeoPosition b = new GeoPosition(51.099906, 17.003016); // Spi¿owa
    	GeoPosition e = new GeoPosition(51.112517, 17.040953); // Œw. Ducha
    	
//    	longitude:
        final JFrame frame = new JFrame();
        jxMapKit = new JXMapKit();
        jxMapKit.setAddressLocationShown(true);
        jxMapKit.setAddressLocation(b);   
        jxMapKit.setCenterPosition(b); 
//        edges = findAlledges(b,e);
//        jxMapKit.getMainMap().setRecenterOnClickEnabled(true);
//        jxMapKit.setAddressLocation(new GeoPosition(51.0833292000,17.0350600000));
//    	findWay(b, e);
        jxMapKit.getMainMap().addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
//				if (start == null){
//				start = jxMapKit.getMainMap().convertPointToGeoPosition
//						(e.getPoint());
//		    	findPointsInArea(start);
//				WayPainter painter = new WayPainter();
//				painter.setWaypoints(waypoints);
//				jxMapKit.getMainMap().setOverlayPainter(painter);
//				frame.revalidate();
//				frame.repaint();
//				
//				start = null;
//			}
//				
				if (start == null){
					start = jxMapKit.getMainMap().convertPointToGeoPosition
							(e.getPoint());
					waypoints = new ArrayList<DefaultWaypoint>();
					edges = new ArrayList<GeoPosition>();
					waypoints.add(new DefaultWaypoint(start));
					WayPainter painter = new WayPainter();
					painter.setWaypoints(waypoints);
					jxMapKit.getMainMap().setOverlayPainter(painter);
					frame.revalidate();
					frame.repaint();
					System.out.println(start);
				}
				else if (desitnation == null){
					desitnation = jxMapKit.getMainMap().convertPointToGeoPosition
							(e.getPoint());
			    	findWay(start, desitnation);
					WayPainter painter = new WayPainter();
					painter.setWaypoints(waypoints);
					jxMapKit.getMainMap().setOverlayPainter(painter);
					frame.revalidate();
					frame.repaint();
					
					start = null;
					desitnation = null;
				}
			}
		});
        jxMapKit.setMiniMapVisible(true);
        jxMapKit.setZoomSliderVisible(true);
        jxMapKit.setZoomButtonsVisible(true);
        jxMapKit.setDefaultProvider(DefaultProviders.OpenStreetMaps);
//        WayPainter painter = new WayPainter();
//        painter.setWaypoints(waypoints);
//        jxMapKit.getMainMap().setOverlayPainter(painter);
        final Painter<JXMapViewer> lineOverlay = new Painter<JXMapViewer>() {

            @Override
            public void paint(Graphics2D g, final JXMapViewer map, final int w, final int h) {
                g = (Graphics2D) g.create();
                // convert from viewport to world bitmap
                final Rectangle rect = jxMapKit.getMainMap().getViewportBounds();
                g.translate(-rect.x, -rect.y);

                // do the drawing
                g.setColor(Color.RED);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(2));

                int lastX = -1;
                int lastY = -1;
                for (final GeoPosition gp : edges) {
                    // convert geo to world bitmap pixel
                    final Point2D pt = jxMapKit.getMainMap().getTileFactory().geoToPixel(gp, jxMapKit.getMainMap().getZoom());
                    if (lastX != -1 && lastY != -1) {
                        g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
                    }
                    lastX = (int) pt.getX();
                    lastY = (int) pt.getY();
                }

                g.dispose();

            }

        };

        jxMapKit.getMainMap().setOverlayPainter(lineOverlay);
        frame.add(jxMapKit);
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }


}

