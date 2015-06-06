package pwr.osm.dao.interf;

import java.util.List;

import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Way;

public interface WayDAO {

	Way findById(int id);
	
	/**
	 * Metoda zwraca wszystkie drogi, kt�rych Node'y
	 * znajduj� sie wewn�trz podanego obszaru. 
	 * @param area - Obszar w kt�rym nale�y znale�� drogi
	 *
	 * @return Lista z wszystkimi drogami, kt�rych cho� 
	 * jeden punkt le�y wewn�trz zadanego obszaru
	 */
	List<Way> getWaysFromArea(MapArea area);

}
