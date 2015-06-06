package pwr.osm.dao.interf;

import java.util.List;

import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Edge;

public interface EdgeDAO {

	/**
	 * Metoda zwraca krawêdŸ o identyfikatorze przekazanym jako argument
	 * @param id
	 * @return
	 */
	public Edge findEdge(long id);

	/**
	 * Metoda zapisuje do bazy danych krawêdŸ przekazan¹ jako argument
	 * @param edge
	 */
	public void saveEdgeInDB(Edge edge);

	/**
	 * Metoda zwraca Listê z krawêdziami, których przynajmniej jeden punkt
	 *  (pocz¹tek lub koniec) znajduje siê w obszarze podanym jako argument metody
	 * @param area
	 * @return
	 */
	public List<Edge> getEdgesFromArea(MapArea area);

	/**
	 * Metoda zwracawszystkie encje znajduj¹ce siê w tabeli EDGE
	 * @return
	 */
	public List<Edge> getAllEdges();
}
