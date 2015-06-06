package pwr.osm.test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import pwr.osm.connection.constants.ServerContants;
import pwr.osm.connection.data.Information;
import pwr.osm.connection.data.Message;
import pwr.osm.data.representation.MapPosition;

public class Client {
	
	public void connect(){
		try (
				Socket serverSocket = new Socket("localhost", ServerContants.PORT);
		        ObjectOutputStream output = new ObjectOutputStream	(serverSocket.getOutputStream());
		        ObjectInputStream input =
	                new ObjectInputStream(serverSocket.getInputStream());
			){

	        List<MapPosition> way = new LinkedList<MapPosition>();

	    	MapPosition b = new MapPosition(51.12181546866023, 17.120347023010254);// Dane testowe
	    	MapPosition e = new MapPosition(51.111362911846264, 17.136483192443848);// Dane testowe
	    	way.add(b);
	    	way.add(e);
	    	
	    	Message message = new Message(1, Information.FIND_WAY, way);
	        output.writeObject(message);
	        output.flush();

	        System.out.println("[TEST_CLIENT] Odebrano: "+input.readObject());

		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Client client = new Client();
		client.connect();
	}
}
