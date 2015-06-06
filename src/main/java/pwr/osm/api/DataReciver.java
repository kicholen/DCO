package pwr.osm.api;

import java.net.ConnectException;
import java.util.LinkedList;
import java.util.List;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import pwr.osm.data.representation.MapPosition;
import pwr.osm.data.representation.tagXML;
import pwr.osm.data.representation.XMLElements.Nd;
import pwr.osm.data.representation.XMLElements.NodeXML;
import pwr.osm.data.representation.XMLElements.NodeXMLInterface;
import pwr.osm.data.representation.XMLElements.Osm;
import pwr.osm.data.representation.XMLElements.Tag;
import pwr.osm.data.representation.XMLElements.WayXML;
import pwr.osm.data.representation.XMLElements.bBox;
import pwr.osm.model.Edge;
import pwr.osm.model.Node;

/**
 * Klasa Wspomagajaca do obslugi XAPI. Dziala wielowatkowo. Sluzy do 
 * operowania na danych zwroconych przez XAPI. Jedna instancja
 * obsluguje jedno zapytanie (dotyczace jednej konwersacji)
 * @author Dawid Cokan
 * @date 12-05-2014
 *
 */
@Deprecated
public class DataReciver {
	
	/**
	 * Wierzcholek z poczatkiem zadanej sciezki
	 */
	private NodeXML begin;
	
	/**
	 * Wierzcholek z  koncem zadanej sciezki
	 */
	private NodeXML end;
	
	
	
	public List<MapPosition> generateWay(MapPosition b, MapPosition end){
    	GeoPosition start = new GeoPosition(b.getLatitude(), b.getLongitude());
    	GeoPosition destination = new GeoPosition(end.getLatitude(), end.getLongitude());
    	
    	return generateWay(start, destination);
	}
	
	public List<MapPosition> generateWay(GeoPosition start, GeoPosition destination){
    	List<Node> edges = null;
    	boolean generated = true;

    	try{
    		edges = findWay(start, destination, Tag.ABBORTED);
    	}catch (NullPointerException ex){
    		generated = false;
    	}
    	while (!generated){
    		try{
    			edges = findWayExtraArea(start, destination, Tag.ABBORTED);
    			generated = true;
    		}catch (NullPointerException ex){
    			generated = false;
    		}
    	}
    	
    	List<MapPosition> way = new LinkedList<MapPosition>();
    	for (Node e: edges){
    		way.add(new MapPosition(e.getLattitude(), e.getLongtitude()));
    	}
    	
    	return way;
	}
	
	/**
	 * Metoda od podanych dwoch punktow - interpretowanych jako poczatek
	 * i koniec - znajduje dwa punkty lezace na drogach, a nastepnie na podstawie
	 * tych punktow na drodze znajduje obszar przeszukiwan (znajduje drogi w tym 
	 * obszarze i jesli ustawimy flage <@code generateArea = true> to doda siê margines).
	 *  Nastepnie znajduje elementy typu WAY
	 * (Pobiera dane z OSM) w wyznaczonym obszarze i je zwraca 
	 * (Jako strukture odpowiadaj¹ca strukturze zwroconych danych przez xAPI)
	 * @param startPoint - punkt poczatkowy
	 * @param endPoint - punkt koncowy
	 * @param abborted - Lista z elementami (Para klucz-wartosc), ktorych zwracane 
	 * @param generateArea - Flaga determinuj¹ca czy nalezy dodac margines do obszaru przeszukiwan
	 * @return Instancje obiektu intepretowana jako dane z drogami w danym obszarze
	 * @throws ConnectException 
 */
	private NodeXMLInterface getArea(final GeoPosition startPoint, final GeoPosition endPoint, List<Tag> abborted, boolean generateArea) throws ConnectException{
		Thread findBegin = new Thread (new Runnable() {
			@Override
			public void run() {
				try {
					begin = XapiConnector.findBestLocation(startPoint);
				} catch (ConnectException e) {
					e.printStackTrace();
				}
			}
		});
		
		Thread findDest = new Thread (new Runnable() {
			@Override
			public void run() {
				try {
					end = XapiConnector.findBestLocation(endPoint);
				} catch (ConnectException e) {
					e.printStackTrace();
				}
			}
		});
		
		//Uruchom dwa watki do zapytania o drogi o wezly
		findBegin.start();
		findDest.start();
		try {
			findBegin.join();
			findDest.join();
			System.out.println("Znaleziono optymalne punkty startowe");
			System.out.println(begin);
			System.out.println(end);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		bBox wholeArea = recomputeBBox(generateArea);

		return XapiConnector.getFilteredElements(tagXML.WAY, wholeArea, abborted);
	}
	
	/**
	 * Metoda od podanych dwoch punktow - interpretowanych jako poczatek
	 * i koniec - znajduje dwa punkty lezace na drogach, a nastepnie na podstawie
	 * tych punktow na drodze znajduje obszar przeszukiwan (NIE DODAJE do tego
	 * obszaru marginesu). Nastepnie znajduje elementy typu WAY
	 * (Pobiera dane z OSM) w wyznaczonym obszarze i je zwraca 
	 * (Jako strukture odpowiadaj¹ca strukturze zwroconych danych przez xAPI)
	 * @param startPoint - punkt poczatkowy
	 * @param endPoint - punkt koncowy
	 * @param abborted - Lista z elementami (Para klucz-wartosc), ktorych zwracane 
	 * nie moga miec
	 * @return Instancje obiektu intepretowana jako dane z drogami w danym obszarze
 */
	public List<Node> findWay(GeoPosition begin, GeoPosition destination, List<Tag> abborted){
		Osm osm;
		try {
			osm = (Osm) getArea(begin, destination, abborted, false);
			return generateEdges(osm);
		} catch (ConnectException e) {
			e.printStackTrace();
			return null;
		}
//		return null;
	}
	
	/**
	 * Metoda dla kazdej drogi, ktora jest w instancji Osm podanej 
	 * jako argument, tworzy krawedz, ktora przetrzymuje referencje 
	 * do swojego poczatku oraz konca. Dlugosc wylicczana jest dynamicznie
	 * Nie nalezy utozsamiac obiektow typu Way z obiektami typu
	 * Edge, poniewaz Way sklada sie zwykle z wielu Wezlow. Natomiast 
	 * Edge to tylko i wylacznie odcinek pomiedzy dwoma wezlami. Metoda
	 * nastepnie uruchamia algorytm Dijkstry do znalezienia drogi
	 * pomiedzy poczatkiem (zmienna globalna <@code begin>) oraz 
	 * koncem (zmienna globalna <@code end>). 
	 * @param osm - Instancja klasy Osm z danymi z ktorych 
	 * chcemy wydobyc liste krawedzi
	 * @return Lista wezlow reprezentujaca droge od punktu poczatkowego
	 * do punktu koncowego
	 */
	private List<Node> generateEdges(Osm osm) {
		List<Edge> edges = new LinkedList<Edge>();
		
		System.out.println("start: "+this.begin);
		System.out.println("koniec: "+this.end);

		for (WayXML way: osm.getWays()){
			NodeXML first = osm.getNode(way.getFirstNode());
			NodeXML last = null;
			List<Nd> nds = way.getNds();
			for (int i=1; i<nds.size(); i++){
				last = osm.getNode(nds.get(i).getRef());
				Edge edge = new Edge();
				edge.setBegin((Node)first);
				edge.setEnd((Node)last);
				edges.add(edge);
				first = last;
			}
		}
//		Dijkstry dj = new Dijkstry();
//		return dj.findWay((Node)this.begin, (Node)this.end, edges);
		return null;
	}



	/**
	 * Metoda zwraca obszar wygenerowany na podstawie dwoch punktow. Dodaje
	 * pewny margines w zaleznosci czy argumentem jest true czy false
	 * @param generateArea - Jesli true - doda sie margines, false - nie doda
	 * @return Obszar reprezentowany przez klase bBox
	 */
	private bBox recomputeBBox(boolean generateArea) {
		double l,b,r,t;
		//Ustal ktory  punkt jest wysuniety bardziej na zachod
		if (begin.getLongtitude() < end.getLongtitude()){
			l = begin.getLongtitude();
			r = end.getLongtitude();
		}else{
			l = end.getLongtitude();
			r = begin.getLongtitude();
		}
		// orza ktory bardziej na poludnie
		if (begin.getLattitude() < end.getLattitude()){
			b = begin.getLattitude();
			t = end.getLattitude();
		}else{
			b = end.getLattitude();
			t = begin.getLattitude();
		}
		if (generateArea){
			l = l - bBox.Offset;
			r = r + bBox.Offset;
			t = t + bBox.Offset;
			b = b - bBox.Offset;
			bBox.increaseOffset();
			System.out.println("Poszerzam obszar w pionie");
			System.out.println("Poszerzam obszar w poziomie");
		}
		else{
			l = l - bBox.minOffset;
			r = r + bBox.minOffset;
			t = t + bBox.minOffset;
			b = b - bBox.minOffset;
		}
		return new bBox(l,b,r,t);
	}

	/**
	 * Metoda od podanych dwoch punktow - interpretowanych jako poczatek
	 * i koniec - znajduje dwa punkty lezace na drogach, a nastepnie na podstawie
	 * tych punktow na drodze znajduje obszar przeszukiwan (DODAJE do tego
	 * obszaru margines). Nastepnie znajduje elementy typu WAY
	 * (Pobiera dane z OSM) w wyznaczonym obszarze i je zwraca 
	 * (Jako strukture odpowiadaj¹ca strukturze zwroconych danych przez xAPI)
	 * @param startPoint - punkt poczatkowy
	 * @param endPoint - punkt koncowy
	 * @param abborted - Lista z elementami (Para klucz-wartosc), ktorych zwracane 
	 * nie moga miec
	 * @return Instancje obiektu intepretowana jako dane z drogami w danym obszarze
	 */
	public List<Node> findWayExtraArea(GeoPosition from, GeoPosition destination,
			List<Tag> abborted) {

		Osm osm;
		try {
			osm = (Osm) getArea(from, destination, abborted, true);
			return generateEdges(osm);
		} catch (ConnectException e) {
			e.printStackTrace();
			return null;
		}
	}
}
