package pwr.osm.dao.interf;

import java.util.List;

import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Node;

public interface NodeDAO {

	/**
	 * Metoda zwraca obiekt o identyfikatorze przekazany jako argument
	 * @param id - id punktu
	 * @return
	 */
	Node findNode(long id);

	/**
	 * Metoda zwraca listê Punktów, które znajduj¹ 
	 * siê w obszarze przekazanym do metody jako parametr area
	 * 
	 * @param area - Obszar, z którego metoda ma wyszukaæ punkty
	 * @return Lista Node'ów le¿¹cych w tym obszarze
	 */
	List<Node> getNodesFromArea(MapArea area);

	/**
	 * Metoda zapisuje do bazy danych przekazany jako argument punkt
	 * @param nodeDB - punkt do zapisania w bazie danych
	 */
	void saveNodeInDB(Node nodeDB);
}
