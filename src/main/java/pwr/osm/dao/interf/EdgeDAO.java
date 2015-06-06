package pwr.osm.dao.interf;

import java.util.List;

import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Edge;

public interface EdgeDAO {

	/**
	 * Metoda zwraca kraw�d� o identyfikatorze przekazanym jako argument
	 * @param id
	 * @return
	 */
	public Edge findEdge(long id);

	/**
	 * Metoda zapisuje do bazy danych kraw�d� przekazan� jako argument
	 * @param edge
	 */
	public void saveEdgeInDB(Edge edge);

	/**
	 * Metoda zwraca List� z kraw�dziami, kt�rych przynajmniej jeden punkt
	 *  (pocz�tek lub koniec) znajduje si� w obszarze podanym jako argument metody
	 * @param area
	 * @return
	 */
	public List<Edge> getEdgesFromArea(MapArea area);

	/**
	 * Metoda zwracawszystkie encje znajduj�ce si� w tabeli EDGE
	 * @return
	 */
	public List<Edge> getAllEdges();
}
