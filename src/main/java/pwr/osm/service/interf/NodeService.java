package pwr.osm.service.interf;

import java.util.List;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import pwr.osm.data.representation.MapArea;
import pwr.osm.exceptions.WayComputingException;
import pwr.osm.model.Node;

public interface NodeService {

	/**
	 * Metoda zwraca punkt o identyfikatorze przekazanym jako argument
	 * @param id
	 * @return
	 */
	Node findNode(long id);
	/**
	 * Metoda znajduje Node, który po³o¿ony jest najbli¿ej
	 * podanego punktu geograficznego. 
	 * @param position - pozycja w okó³ której wyszukiwany bêdzie Node
	 * @return Najbli¿ej po³o¿ony Node znajduj¹cy siê w bazie danych
	 * @throws WayComputingException - Jeœli w pobli¿u punktu nie mo¿e
	 * 		 zostac znaleziony zaden punkt
	 */
	Node findClosestNode(GeoPosition position) throws WayComputingException;
	
	/**
	 * Metoda zapisuje w bazie danych punkt przekazany jako argument
	 * @param nodeDB
	 */
	void saveNodeInDB(pwr.osm.model.Node nodeDB);
	
	/**
	 * Metoda zwraca listê punktów znajduj¹cych siê 
	 * w podanym jako argument obszarze
	 * @param area
	 * @return
	 */
	public List<Node> findNodesInArea(MapArea area);
	
}
