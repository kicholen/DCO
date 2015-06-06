package pwr.osm.dao.interf;

import java.util.List;

import pwr.osm.data.representation.MapArea;
import pwr.osm.model.Way;

public interface WayDAO {

	Way findById(int id);
	
	/**
	 * Metoda zwraca wszystkie drogi, których Node'y
	 * znajduj¹ sie wewn¹trz podanego obszaru. 
	 * @param area - Obszar w którym nale¿y znaleŸæ drogi
	 *
	 * @return Lista z wszystkimi drogami, których choæ 
	 * jeden punkt le¿y wewn¹trz zadanego obszaru
	 */
	List<Way> getWaysFromArea(MapArea area);

}
