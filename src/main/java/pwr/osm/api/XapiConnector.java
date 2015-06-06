package pwr.osm.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import pwr.osm.data.representation.Edge;
import pwr.osm.data.representation.tagXML;
import pwr.osm.data.representation.XMLElements.Nd;
import pwr.osm.data.representation.XMLElements.NodeXML;
import pwr.osm.data.representation.XMLElements.NodeXMLInterface;
import pwr.osm.data.representation.XMLElements.Osm;
import pwr.osm.data.representation.XMLElements.Tag;
import pwr.osm.data.representation.XMLElements.WayXML;
import pwr.osm.data.representation.XMLElements.bBox;
import pwr.osm.data.saxRetriew.largeXML;
import pwr.osm.utils.MapUtils;

/**
 * Klasa sluzaca do komunikacji z XAPI. Sluzy do pobierania zawartosci map OSM. 
 * Klasa wysyla zapytania na http://open.mapquestapi.com/xapi/api/0.6/... 
 * Mozna przetwarzac 3 rodzaje tagow oraz je filtrowac (po wartosciac tagow).
 * Klasa automatycznie inicjalizuje odpowiednie obiekty. Konieczne jest aby te 
 * klasy posiadaly odpowiednie settery oraz publiczny domyslny konstruktor. 
 * Do API przesyla sie typ tagu, ktory chcemy dostac:
 * WAY - Zwroci tagi WAY oraz NODE z nimi powiazane
 * NODE - Zwroci tagi NODE nizaleznie czego dotycza
 * 
 * @author Dawid Cokan
 * @date 12-05-2014
 *
 */
@Deprecated
public class XapiConnector {

	/**
	 * Adres do API do pobierania zawartoœci map OSM
	 */
	private static final String url = "http://open.mapquestapi.com/xapi/api/0.6/";
	
	/**
	 * Metoda sluzy do pobrania zawartosci mapy. Odpowiedz API jest w postaci 
	 * XML. Metoda zwraca calosc jako niesformaty XML, zawarty w String.
	 * Jako argument konieczny trzeba podac bBox czyli obszar, ktory ma byc
	 * przeszukiwany. Inne parametry moga byc null'em.
	 * @param nodeType - typ tagu, ktory chcemy otrzymac od API
	 * @param area - obszar z ktorego maja zostac znalezione elementy
	 * @param tag - tag (para klucz-wartosc) ktore szukane musza posiadac
	 * @return - String z zawartoscia typu XML lub null, jesli nie zostanie
	 * podany obszar
	 */
	private static String getSource(tagXML nodeType, bBox area, Tag tag)  throws ConnectException{
		String result = null;
		
		if (area == null ){
			System.err.println("Nie podano obszaru do przeszukiwania !");
			return null;
		}
		
		try {
			URL adress = new URL(url+(nodeType == null ? "*" : nodeType)+
					(tag==null ? "" : tag)+area); // Jesli nic nie podamy wyszukujemy wszystkie
			System.out.println("XAPI: "+(url+(nodeType == null ? "*" : nodeType)+
					(tag==null ? "" : tag)+area));
			
			result = XapiConnector.getSourceFromURL(adress);
 
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return result;
	}
	
	private static String getSourceFromURL(URL adress) throws IOException, ConnectException, SocketTimeoutException{
		StringBuilder a = new StringBuilder();
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		
		
		URLConnection conn = adress.openConnection();
//		try{
			BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
			String newLine;

	        while ((newLine = br.readLine()) != null)
	            a.append(newLine);
	        
			br.close();
			end = System.currentTimeMillis();
			System.out.println("Pobrano w "+(end-start)+" ms.");
//			
//		}catch (ConnectException e){
//			return "";
//		}
		return a.toString();
	}
	/**
	 * Metoda znajduje punkt najblizszy od zadanego punktu, ktory 
	 * lezy na drodze. 
	 * @param point - lokalizacja, w okol ktorej nalezy szukac pukntu
	 * @return - znaleziony punkt
	 * @throws ConnectException 
	 */
	public static NodeXML findBestLocation(GeoPosition point) throws ConnectException{
		NodeXML bestPosition = null;
		//Pobierane sa elementy typu WAY a nastepnie eliminowane te ktorych wlasciwosci zapamietane
		// sa w liscie ABBORTED
		Osm osm  = (Osm) XapiConnector.getFilteredElements(tagXML.WAY, new bBox(point, true), Tag.ABBORTED);

		double min = 99999;
		boolean firstCall = true;
		System.out.println("Znaleziono "+osm.getWays().size()+" dróg");
		for (WayXML w: osm.getWays()){
			for (Nd nd: w.getNds()){
				NodeXML n = osm.getNode(nd.getRef());
				double temp = MapUtils.countDistance(n, point);

				if (firstCall){// Przy pierwszym sprawdzeniu zainicjuj
					firstCall = false;
					min = temp;
					bestPosition = n;
				}
				if (temp < min){ 
					bestPosition = n;
					min = temp;
				}
			}
		}
			
		return bestPosition;
	}
	


	/**
	 * Metoda zwraca Liste (dokladnie dwa lub jeden element) z krawedziami. Sluzy 
	 * do tego, zeby znalezc droge ktora przechodzi przez punkt, ale punkt ten nie
	 * jest ani jej poczatkiem ani koncem. Metoda znajduje te droge dzieli na pol 
	 * (od poczatku drogi do tego punktyu i od tego punktyu do konca drogi). 
	 * Jesli punkt jest koncem lub poczatkiem drogi zostaje zwrocona jednoelementowa 
	 * lista z tym punktem
	 * @depracated
	 * @param node - Punkt nalezacy do drogi
	 * @return Dwie krawedzie podzielone w danym punkcie lub droga rozpoczynajaca lub konczaca na tym punkcie
	 */
//	public static List<Edge> getWayWith(NodeXML node){
//		String source =  XapiConnector.getSource(tagXML.WAY, new bBox(node.getLongtitude(), node.getLattitude(), node.getLongtitude(), node.getLattitude()), null);
//		largeXML xml = new largeXML(source);
//		List<Edge> edges = new LinkedList<Edge>();
//		Osm osm = (Osm) xml.getElements();
//		for (int i=0; i<osm.getWays().size()  && edges.isEmpty(); i++){
//			WayXML w = osm.getWays().get(i);
//			if (w.containsNode(node)){
//				NodeXML first = osm.getNode(w.getFirstNode());
//				NodeXML last = osm.getNode(w.getLastNode());
//				if (node.equals(first) || node.equals(last)){
//					edges.add(new Edge(first, last));
//					break;
//				}
//				else{
//					edges.add(new Edge(first, node));
//					edges.add(new Edge(node, last));
//					break;
//				}
//			}
//		}
//		
//		return edges;
//	}

	/**
	 * Zwraca dane zawarte w pliku przetworzone na struktury przystosowane
	 * do operacji na daych z OSM
	 * @param file - Uchwyt do pliku z danymi. Plik formatu xml
	 * @return Strukture z wypelnionymi danymi - odpowiadajaca strukturze pliku 
	 */
	public static NodeXMLInterface getFromFile(File file ){
		largeXML xml = new largeXML(file);
		return xml.getElements();
	}


	/**
	 * Metoda zwraca dane z OSM zawarte w danym obszarze podanego typu. 
	 * Nastepnie filtruje je odrzucajac te- ktore posiadaja chociaz 
	 * jeden tag z listy "abborted"	
	 * @param nodeType - Typ tagu, ktore maja zostac wyszukane w Mapach	
	 * @param area - Obszar, w ktorym maja byc wyszukane dane
	 * @param abborted - Lista TAG'ow (para klucz-wartosc) ktorych zwracane tagi 
	 * nie moga miec (sa odrzucane)
	 * @return Strukture z zainicjowanymi danymi odpowiadajace strukturze zwroonej z XAPI
	 * @throws ConnectException 
	 */
	public static NodeXMLInterface getFilteredElements(tagXML nodeType, bBox area, List<Tag> abborted) throws ConnectException{
		String source =  XapiConnector.getSource(nodeType, area, null);
		largeXML xml = new largeXML(source);
		return xml.getElements(abborted);
	}
	
	public static NodeXML getNodeByGivenId(long id) throws ConnectException{
		String result = null;
		try {
			URL adress = new URL(url+"/node/"+id);
			System.out.println("XAPI: "+adress.toString());
			result = XapiConnector.getSourceFromURL(adress);
		}catch (SocketTimeoutException e1){
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		largeXML xml = new largeXML(result);
		Osm osm =  xml.getElements();
		
		return osm.getNodes().isEmpty() ? 
				null : osm.getNodes().get(0);
	}

}
