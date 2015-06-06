package pwr.osm.test;

import java.util.HashSet;
import java.util.List;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;


public class WayPainter extends WaypointPainter<JXMapViewer> {
    public void setWaypoints(List<? extends Waypoint> waypoints) {
        super.setWaypoints(new HashSet<Waypoint>(waypoints));
    }
}
