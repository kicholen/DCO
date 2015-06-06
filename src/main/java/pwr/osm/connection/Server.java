package pwr.osm.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pwr.osm.connection.constants.ServerContants;
import pwr.osm.connection.data.Information;
import pwr.osm.connection.data.Message;
import pwr.osm.data.representation.MapPosition;
import pwr.osm.exceptions.WayComputingException;
import pwr.osm.service.interf.MapService;

public class Server {

	
	private static Map<Long, MapService> jobsMap;
	
	
	
	private static int threadId;
	
	static{
		jobsMap = new HashMap<Long, MapService>();
		threadId = 1;
	}

	
	@SuppressWarnings("resource")
	public void start() throws IOException, ClassNotFoundException{
	      {
	          ServerSocket welcomeSocket = new ServerSocket(ServerContants.PORT);
	          System.out.println("Stworzy³em serwer");
	          while(true)
	          {
	             Socket clientSocket = welcomeSocket.accept();
	             this.onClientConnected(clientSocket);
	          }
	       }
	}



	private void onClientConnected(final Socket clientSocket) {
		final String threadName = getThreadName();
		final Thread t = new Thread( new Runnable(){

				@Override
				public void run(){
					try (
							ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
							ObjectOutputStream output = new ObjectOutputStream	(clientSocket.getOutputStream());
							){

								Message message = (Message) input.readObject();	
								switch (message.getInformation()){
								case FIND_WAY:{
									if (!haveToMuchWork()){
								        MapService worker = getServiceJob();
								        jobsMap.put(message.getId(), worker);
										output.writeObject(findSendClose(message,worker));
									} else {
										output.writeObject(new Message(message.getId(), 
												Information.TOO_MUCH_REQUESTS, null));
									}
									break;
								}
								case WAY_IS_ALREADY_FOUND:{
									// Stop job
									stopAction(message.getId());
									break;
								}
								default:
									break;
								}
						}
						catch (Exception e){
							e.printStackTrace();
						}
				}

				private MapService getServiceJob() {

					ApplicationContext context = 
				        	  new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
				    return (MapService)context.getBean("mapService");
				}

		}, threadName);
		t.start();
	}
	
	private boolean haveToMuchWork() {
		if (jobsMap.size() >= ServerContants.MAX_REQUEST_COUNT){
			validAllJobs();
			if (jobsMap.size() >= ServerContants.MAX_REQUEST_COUNT){
				return true;
			}
		}
		
		return false;
	}

	private static synchronized int getIncreasedThreadId(){
		return Server.threadId++;
	}

	private String getThreadName() {
		return "Client-"+getIncreasedThreadId();
	}

	protected void validAllJobs() {
		List<Long> toDelete = new ArrayList<Long>();

		for (long id: jobsMap.keySet()){
			MapService job = jobsMap.get(id);

			if (job.isComplete()){
				toDelete.add(id);
			}
		}
		
		for (long id: toDelete){
			jobsMap.remove(id);
		}
	}
	

	private Message findSendClose(final Message message, MapService job) {
		final List<MapPosition> geoPoints = (List<MapPosition>)message.getData();

		    	List<MapPosition> way = null;
		    	try {
					way = job.getWayFromWithTwoPoints(geoPoints.get(0), geoPoints.get(1));
				} catch (WayComputingException e) {
					System.err.println("Problem occured during cumputing - way was not found");
					message.setData(null);
					message.setInformation(Information.WAY_NOT_FOUND);
					
					return message;
				}
		    	
		        message.setData(way);
		        message.setInformation(Information.WAY_IS_FOUND);
		return message;
	}

//	private List<MapPosition> inReverseOrder(List<MapPosition> way) {
//		List<MapPosition> map = new ArrayList<MapPosition>();
//		for (MapPosition pos: way){
//			map.add(0,pos);
//		}
//		
//		return map;
//	}


	private void stopAction(long jobId) {

		for (long id: jobsMap.keySet()){
			if (id == jobId){
				jobsMap.get(id).stopAction();
			}
		}
		jobsMap.remove(jobId);
	}
	
	public static void main(String args[]){
		Server server = new Server();
		try {
			server.start();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
