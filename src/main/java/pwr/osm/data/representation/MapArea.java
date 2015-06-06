package pwr.osm.data.representation;

import java.io.Serializable;

import org.jdesktop.swingx.mapviewer.GeoPosition;

import pwr.osm.data.constants.NodeConstants;
import pwr.osm.model.Node;

public class MapArea implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private double minLattitude;
	private double maxLattitude;
	private double minLongtitude;
	private double maxLongtitude;
	
	/**
	 * Konstruktor tworz¹cy obszar z czterech danych. 
	 * Minimalne wartoœci dla sszerokoœci i d³ugoœci geograficznej
	 * nie mog¹ byæ wiêksze od odpowiadaj¹cych im wartoœci maksymalnych
	 * @param minLattitude - minimalna szerokoœæ geograficzna w obszarze
	 * @param maxLattitude - maksymalna szerokoœæ geograficzna w obszarze
	 * @param minLongtitude - minimalna d³ugoœæ geograficzna w obszarze 
	 * @param maxLongtitude - maksymalna d³ugoœæ geograficzna w obszarze 
	 * @throws Exception Jeœli minimalna wartoœæ szerokoœci lub d³ugoœci
	 *  geograficznej bêdzie wiêksza od odpowiadaj¹cej jej wartoœci 
	 *  maksymalnej
	 */
	public MapArea(double minLattitude, double maxLattitude,
				double minLongtitude, double maxLongtitude) throws Exception{
		if (minLattitude > maxLattitude)
			throw new Exception("Max Lattitude has to be greater than minimum Lattitude!");
		
		if (minLongtitude > maxLongtitude)
			throw new Exception("Max Longtitude has to be greater than minimum Longtitude!");
		
		this.maxLattitude = maxLattitude;
		this.minLattitude = minLattitude;
		this.minLongtitude = minLongtitude;
		this.maxLongtitude = maxLongtitude;
	}
	
	public MapArea(GeoPosition position) {
		this.minLattitude = position.getLatitude();
		this.minLongtitude = position.getLongitude();
		this.maxLattitude = position.getLatitude();
		this.maxLongtitude = position.getLongitude();
	}

	public double getMinLattitude() {
		return minLattitude;
	}
	public void setMinLattitude(double minLattitude) {
		this.minLattitude = minLattitude;
	}
	public double getMaxLattitude() {
		return maxLattitude;
	}
	public void setMaxLattitude(double maxLattitude) {
		this.maxLattitude = maxLattitude;
	}
	public double getMinLongtitude() {
		return minLongtitude;
	}
	public void setMinLongtitude(double minLongtitude) {
		this.minLongtitude = minLongtitude;
	}
	public double getMaxLongtitude() {
		return maxLongtitude;
	}
	public void setMaxLongtitude(double maxLongtitude) {
		this.maxLongtitude = maxLongtitude;
	}

	public static MapArea creteAreaWithOffset(Node begin, Node end) {
		
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

		try {
			return new MapArea(b - NodeConstants.NODE_OFFSET,
					t + NodeConstants.NODE_OFFSET,
					l - NodeConstants.NODE_OFFSET,
					r + NodeConstants.NODE_OFFSET);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static MapArea generateWithOffset(GeoPosition position) throws Exception {

		return new MapArea(position.getLatitude() - NodeConstants.NODE_OFFSET,
				position.getLatitude() + NodeConstants.NODE_OFFSET, 
				position.getLongitude() - NodeConstants.NODE_OFFSET, 
				position.getLongitude() + NodeConstants.NODE_OFFSET);
	}

	public void generateOffset() {
		this.minLattitude = getMinLattitude() - NodeConstants.NODE_OFFSET;
		this.maxLattitude = getMaxLattitude() + NodeConstants.NODE_OFFSET;
		this.minLongtitude = getMinLongtitude() - NodeConstants.NODE_OFFSET;
		this.maxLongtitude = getMaxLongtitude() + NodeConstants.NODE_OFFSET;
	}
	
}
