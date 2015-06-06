package pwr.osm.data.saxRetriew;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pwr.osm.data.representation.tagXML;
import pwr.osm.data.representation.XMLElements.NodeXMLInterface;
import pwr.osm.data.representation.XMLElements.Osm;

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
public class UserParserHandler extends DefaultHandler {

	private String source;
	private File file;
	private int limit;
	private List<NodeXMLInterface> elements;
	private NodeXMLInterface parent;
	boolean inNode = false;

		public UserParserHandler(){
			elements = new ArrayList<NodeXMLInterface>();
		}

		/**
		 * Metoda wywo³uje setter na egzemplarzu podanym jako node. Wywo³uje metodê
		 * set<KEY> przy czym zmienia zawsze pierwsz¹ literê w zmiennej key na 
		 * wielk¹ literê. 
		 * @param node - obiekt do zaktualizowania
		 * @param key - w³aœciwoœæ, która ma byæ zmodyfikowana
		 * @param value - wartoœæ która zostanie podsttawiona
		 */
		private void invokeSetter(Object node, String key, String value){
			Class<?> c;
			try {
				c = Class.forName(node.getClass().getCanonicalName());
				String methodName = "set"+firstUpper(key);
				Method  method = c.getDeclaredMethod (methodName, String.class);
				method.invoke (node, value);
			} catch (Exception e) {
			}
		}
		
		

		/**
		 * Metoda wywoluje metode add... na podanym obiekcie i wartoscia
		 * @param node - obiekt na ktorym nalezy wywolac metode
		 * @param value - wartosc do dodania
		 * @param key - nazwa wartosci do zaktualizowania
		 */
		private void invokeAdder(Object node, Object value, String key){
			Class<?> c;
			try {
				c = Class.forName(node.getClass().getCanonicalName());
				String methodName = "add"+firstUpper(key);
				Method  method = c.getDeclaredMethod (methodName, value.getClass());
				method.invoke (node, value);
			} catch (Exception e) {
			}
		}
		
		/**
		 * Metoda zmienia w podanym wyrazie pierwsz¹ literê na wielk¹
		 * @param text - tekst do zmiany
		 * @return Zwraca zmieniony wyraz lub oryginalny jeœli wyraz nie zaczyna siê z ma³ej
		 */
		private String firstUpper(String text){
			if ((int)text.charAt(0)>96) // czy zaczyna siê z ma³ej litery
				return (char)((int)text.charAt(0)-32) + text.substring(1);
			
			return text;
		}
		
		
		/*
		 * (non-Javadoc)
		 * Przy rozpoczeciu tagu sprawdzane jest czy limit ilosci danych nie zostal osiganiety
		 * (domyslnie limit w ogole nie jest ustawiany). Nastepnie tworzony jest obiekt 
		 * reprezentujacy dany tag oraz wywolywane odpowiednie na nim metody. Na poczatku 
		 * inicjalizowane sa wszystkie atrybuty w danym obiekcie. Jesli
		 * obecnie otwarty tag jest pod tagiem to zostaje dodana do elementu bedacego
		 * tagiem wyzszym. Jesli nie ma wyzej otwartego tagu to
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName,String qName, 
		            Attributes attributes) throws SAXException {
			if (limit>0 && elements.size()>=limit)
				throw new SAXException();
			
			
				Class<?> clazz;
				Object node = null;
				try {
					clazz = Class.forName(tagXML.getSuitableClass(qName));
					Constructor<?> ctor = clazz.getConstructor();
					node = ctor.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				int attCount = attributes.getLength();
						
				for (int i=0; i<attCount; i++){
					String key = attributes.getLocalName(i);
					String value = attributes.getValue(key);
					invokeSetter(node, key, value);
				}
				
				if (!elements.isEmpty()){
					Object obj = elements.get(elements.size()-1);
					invokeAdder(obj, node, ((NodeXMLInterface)node).getTagType().getNodeName());
				}
				
				elements.add((NodeXMLInterface) node);
			}
		
		/*
		 * (non-Javadoc)
		 * Przy zakanczaniu tagu zostaje zmieniona flaga, aby zawiadomic cala klase
		 * o tym, ze obecnie nie ma otwartego tagu. a nastepnie usuniety zostaje
		 * ostatnio  otwarty tag. Pod parent zostaje przypisywany zawsze tag wrzucony
		 * jako pierwszy. Zaklada sie ze wszystkie tagi beda pod-tagami tego tagu.
		 * Natomiast jesli wystapia dwa rownowazne tagi na szczycie hierarchii
		 * zwracany bedzie ten ostatni w pliku 
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public void endElement(String uri, String localName,
			String qName) throws SAXException {
			inNode = false;
			parent = elements.get(0);
			elements.remove(elements.remove(elements.size()-1));
		
		}
		

		/**
		 * Metoda sluzy do ustawienia limitu liczebnego zwracanych danych
		 * @param limit - Limit nie do przekroczenia
		 */
		public void setLimit(int limit) {
			this.limit = limit;
		}

		

	public Osm getData() {
		return (Osm) parent;
	}

}
