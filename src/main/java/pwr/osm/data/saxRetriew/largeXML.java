package pwr.osm.data.saxRetriew;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pwr.osm.data.representation.XMLElements.Nd;
import pwr.osm.data.representation.XMLElements.NodeXMLInterface;
import pwr.osm.data.representation.XMLElements.Osm;
import pwr.osm.data.representation.XMLElements.Tag;
import pwr.osm.data.representation.XMLElements.WayXML;

/**
 * Klasa zezwala na przeszukiwanie duzych plikow XML. Testowane
 * na pliku ~330 MB. Nie nalezy ladowac calego pliku do pamieci. 
 * Klasa do tego tez nie sluzy. Klasy nalezy uzywac do wyszukiwania
 * elementow spelniajacych zadane kryteria, badz znajdowac okreslona liczbe 
 * elementow. Klasa umozliwia bezposrednie ladowanie danych z pliku. Dane 
 * mozna dostarczyc jako String.
 * 
 * UWAGA ! : Kolejne parsowanie pliku (po znalezieniu odpowiednich tagow,
 * lub po osiagnieciu limitu) rozpoczya sie od poczatku pliku.
 * 
 * @author Coki
 * @date 12-05-2014
 *
 */
public class largeXML extends DefaultHandler{
	private UserXMLParser parser;
//	private  SAXParserFactory factory;
//	private  SAXParser saxParser;
	private String source;
	private File file;
//	private int limit;
//	private List<NodeXML> elements;
//	private NodeXML parent;
//	boolean inNode = false;
//	
	
	public largeXML(){
		parser = new UserXMLParser();
	}
//	/**
//	 * Konstruktor domyslny nie definiuje skad nalezy pobierac dane.
//	 */
//	public largeXML()	{
//		factory = SAXParserFactory.newInstance();
//    	elements = new LinkedList<NodeXML>();
//		try {
//			saxParser = factory.newSAXParser();
//		} catch (ParserConfigurationException | SAXException e1) {
//			e1.printStackTrace();
//		}
//	}
//	
	/**
	 * Konstruktor inicjuje wszystkie potrzebne dane a jako 
	 * zrodlo ustawione jest jako pole w klasie;
	 * @param source - zrodlo z danymi do przetwarzania
	 */
	public largeXML(String source){
		this();
		this.source = source;
	}
	
	/**
	 * Konstruktor inicjuje wszystkie dane a jako 
	 * zrodlo ustawia plik 
	 * @param file - gotowy uchwyt do pliku z danymi
	 */
	public largeXML(File file){
		this();
		this.file = file;
	}
	
	public Osm getElements(){
		try {
			if (file == null )
				return parser.parse(new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)));
			else
				return parser.parse(new FileInputStream(file));
		} catch (SAXException | IOException | NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
//	
//	/**
//	 * Metoda wywo³uje setter na egzemplarzu podanym jako node. Wywo³uje metodê
//	 * set<KEY> przy czym zmienia zawsze pierwsz¹ literê w zmiennej key na 
//	 * wielk¹ literê. 
//	 * @param node - obiekt do zaktualizowania
//	 * @param key - w³aœciwoœæ, która ma byæ zmodyfikowana
//	 * @param value - wartoœæ która zostanie podsttawiona
//	 */
//	private void invokeSetter(Object node, String key, String value){
//		Class<?> c;
//		try {
//			c = Class.forName(node.getClass().getCanonicalName());
//			String methodName = "set"+firstUpper(key);
//			Method  method = c.getDeclaredMethod (methodName, String.class);
//			method.invoke (node, value);
//		} catch (Exception e) {
//		}
//	}
//	
//	
//
//	/**
//	 * Metoda wywoluje metode add... na podanym obiekcie i wartoscia
//	 * @param node - obiekt na ktorym nalezy wywolac metode
//	 * @param value - wartosc do dodania
//	 * @param key - nazwa wartosci do zaktualizowania
//	 */
//	private void invokeAdder(Object node, Object value, String key){
//		Class<?> c;
//		try {
//			c = Class.forName(node.getClass().getCanonicalName());
//			String methodName = "add"+firstUpper(key);
//			Method  method = c.getDeclaredMethod (methodName, value.getClass());
//			method.invoke (node, value);
//		} catch (Exception e) {
//		}
//	}
//	
//	/**
//	 * Metoda zmienia w podanym wyrazie pierwsz¹ literê na wielk¹
//	 * @param text - tekst do zmiany
//	 * @return Zwraca zmieniony wyraz lub oryginalny jeœli wyraz nie zaczyna siê z ma³ej
//	 */
//	private String firstUpper(String text){
//		if ((int)text.charAt(0)>96) // czy zaczyna siê z ma³ej litery
//			return (char)((int)text.charAt(0)-32) + text.substring(1);
//		
//		return text;
//	}
//	
//	
//	/*
//	 * (non-Javadoc)
//	 * Przy rozpoczeciu tagu sprawdzane jest czy limit ilosci danych nie zostal osiganiety
//	 * (domyslnie limit w ogole nie jest ustawiany). Nastepnie tworzony jest obiekt 
//	 * reprezentujacy dany tag oraz wywolywane odpowiednie na nim metody. Na poczatku 
//	 * inicjalizowane sa wszystkie atrybuty w danym obiekcie. Jesli
//	 * obecnie otwarty tag jest pod tagiem to zostaje dodana do elementu bedacego
//	 * tagiem wyzszym. Jesli nie ma wyzej otwartego tagu to
//	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
//	 */
//	@Override
//	public void startElement(String uri, String localName,String qName, 
//	            Attributes attributes) throws SAXException {
//		if (limit>0 && elements.size()>=limit)
//			throw new SAXException();
//		
//		
//			Class<?> clazz;
//			Object node = null;
//			try {
//				clazz = Class.forName(tagXML.getSuitableClass(qName));
//				Constructor<?> ctor = clazz.getConstructor();
//				node = ctor.newInstance();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			int attCount = attributes.getLength();
//					
//			for (int i=0; i<attCount; i++){
//				String key = attributes.getLocalName(i);
//				String value = attributes.getValue(key);
//				invokeSetter(node, key, value);
//			}
//			
//			if (!elements.isEmpty()){
//				Object obj = elements.get(elements.size()-1);
//				invokeAdder(obj, node, ((NodeXML)node).getTagType().getNodeName());
//			}
//			
//			elements.add((NodeXML) node);
//		}
//	
//	/*
//	 * (non-Javadoc)
//	 * Przy zakanczaniu tagu zostaje zmieniona flaga, aby zawiadomic cala klase
//	 * o tym, ze obecnie nie ma otwartego tagu. a nastepnie usuniety zostaje
//	 * ostatnio  otwarty tag. Pod parent zostaje przypisywany zawsze tag wrzucony
//	 * jako pierwszy. Zaklada sie ze wszystkie tagi beda pod-tagami tego tagu.
//	 * Natomiast jesli wystapia dwa rownowazne tagi na szczycie hierarchii
//	 * zwracany bedzie ten ostatni w pliku 
//	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void endElement(String uri, String localName,
//		String qName) throws SAXException {
//		inNode = false;
//		parent = elements.get(0);
//		elements.remove(elements.remove(elements.size()-1));
//	
//	}
//	
//	/**
//	 * Metoda zwraca wszystkie tagi ze zrodla. reprezentowane jako
//	 * jedna klasa z innymi obiektami w niej.
//	 * @return zainicjalizowana odpowiednia instancja klasy
//	 */
//	public NodeXML getElements(){
//		elements = new LinkedList<NodeXML>();
//
//		try {
//			if (file != null)
//				saxParser.parse(file, this);
//			else if (source != null)
//				saxParser.parse(new InputSource(new StringReader(source)), this);
//			
//		} catch (SAXException e) {
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return parent;
//	}
//	
//	
	/**
	 * Metoda zwraca wszystkie tagi danego typu ze zrodla. 
	 * Zrodlo ustawiane jest w konstruktorze. Metoda zwraca 
	 * strukture danych, ktora odpowiada dokladnie temu 
	 * co jest w zrodle. Odrzuca te elementy, ktore
	 * posiadaja conajmniej jeden z tagow podanych w 
	 * argumencie abborted
	 * @param abborted - Lista tagow "zabronionych"
	 * @return zainicjalizowana odpowiednia instancja klasy
	 */ 	
	public NodeXMLInterface getElements(List<Tag> abborted){
		Osm osm =  getElements();
		Osm filtered = new Osm(osm);
		
		List<WayXML> ways = osm.getWays();
		for (WayXML way: ways){
			boolean contains = false;
			for (Tag tag: abborted){
				if (way.getTags().contains(tag)){
					contains = true;
					break;
				}
			}

			if (!contains){
				for (Nd nd: way.getNds()){
					filtered.addNode(osm.getNode(nd.getRef()));
				}
				filtered.addWay(way);
			}
		}
		
		return filtered;
	}
//	/**
//	 * Metoda sluzy do ustawienia limitu liczebnego zwracanych danych
//	 * @param limit - Limit nie do przekroczenia
//	 */
//	public void setLimit(int limit) {
//		this.limit = limit;
//	}

	
}
