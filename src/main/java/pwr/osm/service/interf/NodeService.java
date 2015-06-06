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
	 * Metoda znajduje Node, kt�ry po�o�ony jest najbli�ej
	 * podanego punktu geograficznego. 
	 * @param position - pozycja w ok� kt�rej wyszukiwany b�dzie Node
	 * @return Najbli�ej po�o�ony Node znajduj�cy si� w bazie danych
	 * @throws WayComputingException - Je�li w pobli�u punktu nie mo�e
	 * 		 zostac znaleziony zaden punkt
	 */
	Node findClosestNode(GeoPosition position) throws WayComputingException;
	
	/**
	 * Metoda zapisuje w bazie danych punkt przekazany jako argument
	 * @param nodeDB
	 */
	void saveNodeInDB(pwr.osm.model.Node nodeDB);
	
	/**
	 * Metoda zwraca list� punkt�w znajduj�cych si� 
	 * w podanym jako argument obszarze
	 * @param area
	 * @return
	 */
	public List<Node> findNodesInArea(MapArea area);
	
}
