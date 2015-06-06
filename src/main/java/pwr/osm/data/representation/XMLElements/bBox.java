package pwr.osm.data.representation.XMLElements;

import org.jdesktop.swingx.mapviewer.GeoPosition;

/**
 * Klasa reprezentuje obszar jako 4 wspolrzedne (prostokat)
 * Powstala do reprezentacji tagu bbox w mapach OSM. Jest przystosowana do 
 * automatycznej inicjacji obiektow prosto z XML - posiada settery przyjmujace
 * jako argumenty pola typu String. Posiada konstruktor umozliwiajacy 
 * wygenerowanie obszaru rownomiernie rozlozonego wokol zadanego 
 * punktu.
 * 
 * @author Dawid Cokan
 * @date 12-05-2014
 *
 */
public class bBox {
	public static final double MIN_VERTICAL = 0.03;
	public static final double MIN_HORIZONTAL = 0.03;
	private double left;
	private double bottom;
	private double right;
	private double top;
	private GeoPosition centralPosition;
	public static double Offset;
	private final static double defaultOffset = 0.01;
	public static final double minOffset = 0.005;
	
	static{
		Offset = defaultOffset;
	}
	
	public bBox(double left, double bottom, double right, double top){
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		this.top = top;
		countCentralPosition();
	}

	/**
	 * Konstruktor umozliwiajacy 
	 * wygenerowanie obszaru rownomiernie rozlozonego wokol zadanego 
	 * @param centralPosition - centralny punkt nowego obszaru
	 */
	public bBox(GeoPosition centralPosition, boolean generate){
		this.centralPosition = centralPosition;
		generateArea(false);
	}
	
	public static double getOffset(){
		return bBox.Offset;
	}
	private void countCentralPosition(){
		centralPosition = new GeoPosition((top-bottom)/2, (right-left)/2);
	}
	
	private void generateArea(boolean generate){

		left = centralPosition.getLongitude() + (generate ? - Offset : 0);
		right = centralPosition.getLongitude() + (generate ? Offset : 0);
		top = centralPosition.getLatitude() + (generate ? Offset : 0);
		bottom = centralPosition.getLatitude() -(generate ?  Offset : 0);
	}
	
	@Override
	public String toString(){
		return "[bbox="+left+","+bottom+","+right+","+top+"]";
	}
	public double getLeft() {
		return left;
	}

	public void setLeft(double left) {
		this.left = left;
	}

	public double getBottom() {
		return bottom;
	}

	public void setBottom(double bottom) {
		this.bottom = bottom;
	}

	public double getRight() {
		return right;
	}

	public void setRight(double right) {
		this.right = right;
	}

	public double getTop() {
		return top;
	}

	public void setTop(double top) {
		this.top = top;
	}

	public GeoPosition getCentralPosition() {
		return centralPosition;
	}

	public void setCentralPosition(GeoPosition centralPosition) {
		this.centralPosition = centralPosition;
	}

	public static void increaseOffset() {
		Offset = Offset + defaultOffset;
	}
	
	
	
}
