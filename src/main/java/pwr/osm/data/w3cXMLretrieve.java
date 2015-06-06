package pwr.osm.data;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pwr.osm.data.representation.attributeType;
import pwr.osm.data.representation.attributeXML;
import pwr.osm.data.representation.tagXML;

/**
 * 
 * @author Dawid Cokan
 * @date 16-04-2014
 * 
 * Klasa przetwarza plik XML. W za³o¿eniu plik nie zna struktury pliku. 
 * 
 *
 */
public class w3cXMLretrieve {

	private Document document;
	
	/**
	 * 
	 * @param fileName - Nazwa pliku xml z danymi
	 */
	public w3cXMLretrieve(String fileName){
		this(new File(fileName));
	}
	
	/**
	 * 
	 * @param xml - Uchwyt do pliku XML
	 */
	public w3cXMLretrieve(File xml){
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder dBuilder;

		try {
//			dBuilder = dbFactory.newDocumentBuilder();
//			document = dBuilder.parse(xml);
			DefaultHandler def = new DefaultHandler();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		    saxParser.parse("nodes.xml", def);
		} catch (ParserConfigurationException e) {
			System.err.println("Plik nie jest plikiem XML");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		 
		document.getDocumentElement().normalize();	
	}
	
	
	public Collection<attributeXML> getValues(tagXML nodeType, attributeXML attribute, List<attributeType> allTypes){
//		Collection<attributeXML> allValues = new LinkedList<attributeXML>();
		
//		for (attributeType type: allTypes){
//			allValues.addAll((LinkedList<String>)getValueList(nodeType, attribute, type));
//		}
		return null;
	}
	
	
//	/**
//	 * Metoda zwraca wartoœci atrybutu podanego jako argument type z ca³ego pliku jako List<String>.
//	 * Atrybuty s¹ wyszukiwane w tagach podanych jako nodeType i sprawdzany jest atrybut podany jako
//	 * attribute
//	 * @param nodeType - tagi w których maj¹ byæ wyszukiwane atrybuty
//	 * @param attribute - atrybut który bêdzie sprawdzany
//	 * @param type - atrybut którego wartoœæ nalezy zwróciæ
//	 * @return - Lista z wartoœciami atrybutu z ca³ego pliku
//	 */
	/**
	 * Metoda zwraca wszystkie tagi zawieraj¹ce atrybut <attribute>. Tagi reprezentowane s¹ przez listy (List<attributeXML>)
	 * gdzie kazda kolejna lista odpowiada kolejnemu z tagów, który zawiera podany atrybut. Tzn, mozemy wywo³aæ metode tak ¿eby zwrocila 
	 * wszystkie tagi "<node>"  ktore maj¹ atrybut id="1" i chcemy ¿eby zosta³y zwrócone nam wartoœci atrybutów lon i lat. Wtedy 
	 * ka¿da z list w lisæie reprezentuje tag a kolejne wartoœci to atrybuty. Jeœli w tagu nie ma wszystkich atrybutów podanych w liœcie types
	 * to nie zostaje dodany do kolekcji.
	 * @param nodeType - typ tagu w których bêdzie porównywany atryubut
	 * @param attribute - atrybut do porównania
	 * @param types - atrybuty których wartoœæ chcemy zwrócic
	 * @return - Lista list z  ¿¹danymi atrybutami
	 */
	public List<List<attributeXML>> getValueList(tagXML nodeType, attributeXML attribute, List<attributeType> types){
		LinkedList<Node> list = (LinkedList<Node>) getAllNodes(nodeType, attribute); // pobierz wszystkie tagi z pliku zawieraj¹ce atrybut
		List<List<attributeXML>> values = new LinkedList<List<attributeXML>>();
		for (Node node: list){
//			Node node = list.item(i);// kolejny tag 
			List<attributeXML> temp = new LinkedList<attributeXML>();
			for (attributeType typ: types){ // Ka¿dy z poszukiwanych atrybutów 
				String val = ((Element)node).getAttribute(typ.getAttributeName());
				if (!"".equals(val))//Znaleziono taki atrybut
					temp.add(new attributeXML(typ, val));
			}
			
			if (temp.size() == types.size()) // jeœli znaleziono wszystkie szukane
				values.add(temp);
		}
		return values;
	}
	
	/**
	 * Metoda zwraca wartoœæ atrybutu w podanym tagu. Tag definiujemy podaj¹c typ tagu
	 * oraz atrybut który bêdzie porówynwany z atrybutem w tagu
	 * @param nodeType - typ tagu w których bêdzie porównywany atryubut
	 * @param attribute - atrybut do porównania
	 * @param type - atrybut którego wartoœæ chcemy zwróciæ (to jest key zwracana bêdzie value)
	 * @return - wartoœæ atrybutu w tagu jesli zosta³ znaleziony - null jeœli nie
	 */
	public String getValue(tagXML nodeType, attributeXML attribute, attributeType type){
		Node result = getAccurateNode(nodeType, attribute);
		if (result!= null){
			return ((Element)result).getAttribute(type.getAttributeName());
		}
		else
			System.err.println("Nie znaleziono attrybutu "+type+" w tagu "+nodeType);
		
		return null;
	}
	
	/**
	 * Metoda znajduje odpowiedni Tag w pliku XML. Obs³ugiwane tagi s¹ definiowane w enumie tagXML.
	 * Wyszukuje je porównuj¹c atrybut podany jako drugi argument
	 * @param nodeType - typ szukanego tagu
	 * @param attribute - atrybut szukanego tagu
	 * @return - zwraca znaleziony Tag jako obiekt klasy Node 
	 */
	private Node getAccurateNode(tagXML nodeType, attributeXML attribute){
		NodeList nList = document.getElementsByTagName(nodeType.toString());
		for (int i=0; i<nList.getLength(); i++){
			Node node = nList.item(i);
			String temp = ((Element)node).getAttribute(attribute.getKey());
			if (attribute.equals(temp)){
				return node;
			}
		}
		return null;
	}
	
	private List<Node> getAllNodes(tagXML nodeType, attributeXML attribute){
//		return document.getElementsByTagName(nodeType.toString());
//		return getAccurateNode(nodeType, attribute);
		NodeList nList = document.getElementsByTagName(nodeType.toString());
		List<Node> toRet = new LinkedList<Node>();
		for (int i=0; i<nList.getLength(); i++){
			Node node = nList.item(i);
//			String temp = ;
			if (attribute == null || attribute.equals(((Element)node).getAttribute(attribute.getKey()))){
				toRet.add(node);
			}
		}
		
		return toRet;
	}

	public List<List<attributeXML>> getAllValueList(tagXML nodeType, List<attributeType> types) {
		return getValueList(nodeType, null, types);
	}
	
}
