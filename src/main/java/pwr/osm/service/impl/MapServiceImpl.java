package pwr.osm.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pwr.osm.data.algorithm.Dijkstry;
import pwr.osm.data.representation.MapArea;
import pwr.osm.data.representation.MapPosition;
import pwr.osm.exceptions.WayComputingException;
import pwr.osm.model.Edge;
import pwr.osm.model.Node;
import pwr.osm.service.interf.EdgeService;
import pwr.osm.service.interf.MapService;
import pwr.osm.service.interf.NodeService;

@Service("mapService")
public class MapServiceImpl implements MapService {


	private double[][] connArray;
	ArrayList<Node> allNodes;
	ArrayList<Node> originalNodes;
	private double[] D;
	private Boolean abort;
	private boolean completed;
	
	@Autowired
	private NodeService nodeService;
	@Autowired
	private EdgeService edgeService;
	@Autowired
	private Dijkstry pathService;
	
	public MapServiceImpl(){

		abort = new Boolean(false);
		allNodes = new ArrayList<Node>();
		originalNodes = new ArrayList<Node>();
	}
	
	@Override
	public List<MapPosition> getWayFromWithTwoPoints(GeoPosition start,
			GeoPosition end) throws WayComputingException {

		Node begin = nodeService.findClosestNode(start);
		Node destination = nodeService.findClosestNode(end);
		
		List<Edge> edges = edgeService.getEdgesInArea(
				MapArea.creteAreaWithOffset(begin, destination));

		List<Node> path = findWay(begin, destination, edges);
		
		return parseToMapPosition(path);
	}
	

	private List<MapPosition> parseToMapPosition(List<Node> path) {
		ArrayList<MapPosition> positions = new ArrayList<MapPosition>();
		for (Node node: path){
			positions.add(0, new MapPosition
					(node.getLattitude(), node.getLongtitude()));
		}
		
		return positions;
	}

	@Override
	public List<MapPosition> getWayFromWithTwoPoints(MapPosition begin,
			MapPosition destination) throws WayComputingException {
		return getWayFromWithTwoPoints(new GeoPosition(begin.getLatitude(), begin.getLongitude()), 
				new GeoPosition(destination.getLatitude(), destination.getLongitude()));
	}

	/**
	 * Inicjalizacja zmiennych. Odbywa sie tutaj znalzienie wszystkich dostepnych
	 * wezlow z wezla poczatkowego. Te ktore nie sa osiagalne z poczatkowego
	 * wezla nie beda dalej przetwardzane. Nastepnie zbudowanie macierzy polaczen.
	 * Czyli dwuwymiarowej tablicy zawierajacej inormacje o odleglosci miedzy
	 * wezlami. 
	 * @param edges - Lista z krawedziami sposrod ktorych znajdowana bedzie droga	
	 * @param begin - Wezel poczatkowy
	 * @param destination - Wezel koncowy
	 */
	private void init(List<Edge> edges, Node begin, Node destination){

		ArrayList<Integer> temp = new ArrayList<Integer>();
		
		for (Edge e: edges){
			Node b = e.getBegin();
			Node end = e.getEnd();
			if (!allNodes.contains(b)){
				allNodes.add(b);
			}
			if (!allNodes.contains(end)){
				allNodes.add(end);
			}
		}
		
//		System.out.println("Nodes "+(allNodes.contains(begin) ? "" : "nie")+" zawiera begin\n"+begin);
//		System.out.println("Nodes "+(allNodes.contains(destination) ? "" : "nie")+" zawiera destination\n"+destination);
		connArray = new double[allNodes.size()][allNodes.size()];
		

		for (Edge e: edges){
			int i=allNodes.indexOf(e.getBegin());
			int j = allNodes.indexOf(e.getEnd());
			double w = edgeService.countEdgeValue(e);


			connArray[i][j] = w;
			connArray[j][i] = w;
		}
		
		int indeks = allNodes.indexOf(begin);
		temp.add(indeks);
		for (int i=0; i<temp.size(); i++){
			indeks = temp.get(i);
			for (int k=0; k< connArray.length; k++)
				if (connArray[indeks][k] != 0 && !temp.contains(k)){
					temp.add(k);
				}
		}
		
		ArrayList<Node> tempNodes = allNodes;
		originalNodes = new ArrayList<Node>();
		allNodes = new ArrayList<Node>();
		for (int nr: temp){
			Node n = tempNodes.get(nr);
			originalNodes.add(n);
			allNodes.add(n);
		}

		System.out.println("Po filtracji jest "+allNodes.size()+" wêz³ów");
		connArray = new double[allNodes.size()][allNodes.size()];
		D = new double[allNodes.size()];
		
		for (int i=0; i<D.length; i++)
			D[i] = Double.MAX_VALUE;
		
		for (Edge e: edges){
			int i=allNodes.indexOf(e.getBegin());
			int j = allNodes.indexOf(e.getEnd());
			if (i != -1 && j != -1){
				double w = edgeService.countEdgeValue(e);

				connArray[i][j] = w;
				connArray[j][i] = w;
			}
		}

	}
	
	
	/**
	 * Metoda wylicza droge (znajduje najmniejsza odleglosc) miedzy zadanaymi
	 * punktami wybierajac sciezke sposrod wezlow podanych jako argument metody. 
	 * Metoda implementuje algorytm Dijkstry
	 * @param begin - Wezel poczatkowy
	 * @param destination - Wezel koncowy
	 * @param edges - Lista krawedzi sposrod ktorej trzeba znalezc sciezke
	 * @return Lista wezlow przez ktora przechodzi znaleziona droga
	 * @throws WayComputingException - gdy dane bêd¹ nie poprawne
	 */
	public List<Node> findWay(Node begin, Node destination, List<Edge> edges) throws WayComputingException{

		init(edges, begin, destination);
		if (!allNodes.contains(begin) || !allNodes.contains(destination)){
			System.err.println("Start point: "+begin);
			System.err.println("Destiantion point: "+destination);
			throw new WayComputingException("Start point or destination point not found !");
		}
		int indx = allNodes.indexOf(destination);
		int ndIndx = allNodes.indexOf(begin);
		D[ndIndx] = 0;
		int i=0, minId = -1; 
		double min = Double.MAX_VALUE;
		Node  node = begin;
		allNodes.remove(begin);
		while (!allNodes.isEmpty() && !isAborted()){

			for (i=0; i<connArray.length; i++){
				double val =connArray[ndIndx][i];
				if (val != 0){
					if (D[ndIndx] + val < D[i]){
						D[i] = D[ndIndx] + val;
					}
				}
			}

			min=Double.MAX_VALUE;
			minId= -1;
			
			for (Node n: allNodes){
				int index = originalNodes.indexOf(n);
				if (min > D[index]){
					min = D[index];
					minId = allNodes.indexOf(n);
				}
			}

			node = allNodes.get(minId);
			ndIndx = originalNodes.indexOf(node);
			allNodes.remove(minId);
		}
		completed = true;
		if (isAborted()){
			return new ArrayList<Node>();
		}
		
		System.out.println("Droga to: "+D[indx]);
		return restore(begin, destination);
	}
	
	private synchronized boolean isAborted() {
		return abort;
	}
	
	/**
	 * Metoda odtwarza znaleziona droge algorytmem Dijkstry 
	 * @param begin - Wezel z ktorego ma byc szukana droga
	 * @param destination - Wezel do ktorego ma byc odnaleziona droga
	 * @return Lista Wezlow w kolejnosci od koncowego do pierwszego
	 */
	private List<Node> restore(Node begin, Node destination){
		List<Node> way = new LinkedList<Node>();
		way.add(destination);

		double val=0;
		int index = originalNodes.indexOf(destination);
		while (!way.contains(begin)){
			val = D[index];
			for (int i=0; i<connArray.length; i++){
				if (connArray[i][index] != 0){
					if (D[i] + connArray[i][index] == val){
						index = i;
						way.add(originalNodes.get(i));
						break;
					}
				}
			}
		}
		
		return way;
	}
	
	@Override
	public boolean isComplete() {
		return completed;
	}
	
	@Override
	public synchronized void stopAction() {
		abort = new Boolean(true);
	}



}
