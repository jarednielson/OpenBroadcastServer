package com.jrpup.openbroadcastserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The ConnectionManager is primarily responsible for listening for and managing
 * connections with clients.
 * 
 * The ConnectionManager follows the singleton pattern as such only one instance of the
 * ConnectionManager can be constructed in a single session. To get a reference to
 * the manager use the static method ConstructionManager.getConstructionManager().
 * @author Jared Nielson
 *
 */
public class ConnectionManager {
	/**
	 * Singleton intance of the ConnectionManager
	 */
	private static ConnectionManager instance = null;
	/**
	 * The maximum number of Connections allowable by the ConnectionManager
	 */
	private static int maxConnections = 12;
	/**
	 * The port the ConnectionManager listens on
	 */
	private static int port = 60000;
	
	/**
	 * ServerSocket to listen for incoming connections
	 */
	private ServerSocket ss;
	/**
	 * Thread this ConnectionsListener listens.
	 */
	private Thread listenerThread;
	
	/**
	 * Current connections for this ConnectionManager
	 */
	private ArrayList<Socket> connections;
	
	/**
	 * List of callBack subscribers
	 */
	private ConnectionManagerCallable callBack;
	
	/**
	 * Flag which is true if the ConnectionManager is listening
	 * Also used to stop listenerThread
	 */
	private volatile boolean isListening;
	/**
	 * The current number of connections open by the ConnectionManager
	 */
	private int numConnections;
	
	/**
	 * Private constructor as per the Singleton pattern.
	 * @throws IOException
	 */
	private ConnectionManager(){
		isListening = false;
		connections = new ArrayList<Socket>();
		numConnections = 0;
		
		
	}
	
	/**
	 * Returns the current instance of the connection manager
	 * @return
	 * @throws IOException 
	 */
	public static ConnectionManager getConnectionManager(){
		if(instance == null){
			instance = new ConnectionManager();
		}
		
		return instance;
	}
	
	/**
	 * Returns an instance of the ConnectionManager and attempts to
	 * set the port number if it has not already been set for the singleton.
	 * @param port - The port number
	 * @return
	 * @throws IOException 
	 */
	public static ConnectionManager getConnectionManager(int port){
		if(instance == null){
			ConnectionManager.port = port;
		}
		
		return getConnectionManager();
	}
	
	public ArrayList<Socket> getConnections(){
		return connections;
	}
	
	/**
	 * Starts the listening process for this ConnectionManager. The 
	 * ConnectionManager will begin listening for and accepting 
	 * connections on it's own managed thread. The ConnectionManager 
	 * will continue listening until MaxConnections have been established or
	 * endListen is called.
	 * 
	 * If the ConnectionManager is already listening method has no effect
	 */
	public void beginListen(){
		if(isListening){
			return;
		}
		
		listenerThread = new Thread(new RunnableListener());
		isListening = true;
		listenerThread.start();
		
	}
	
	/**
	 * Sets a callback method whena  connection is made.
	 * @param callBack
	 */
	public void beginListen(ConnectionManagerCallable callBack){
		if(!isListening){
			return;
		}
		
		this.callBack = callBack;
		beginListen();
		
	}
	
	/**
	 * Starts the listening process for this ConnectionManager. The 
	 * ConnectionManager will begin listening for and accepting 
	 * connections on it's own managed thread. The ConnectionManager 
	 * will continue listening until MaxConnections have been established or
	 * endListen is called.
	 * 
	 * If the ConnectionManager is already listening method has no effect
	 * 
	 * @param maxConnections - The maximum number of connections to listen for.
	 */
	public void beginListen(int port, int maxConnections){
		if(isListening){
			return;
		}
		
		if(maxConnections < 2 ){
			throw new IllegalArgumentException("maxConnections must be greater than 2");
		}
		ConnectionManager.maxConnections = maxConnections;
		
		beginListen(port);
	}
	
	
	/**
	 * Sets the port number for this ConnectionManager and starts this ConnnectionManager listening 
	 * @param port
	 */
	public void beginListen(int port){
		if(isListening){
			return;
		}
		
		ConnectionManager.port = port;
		
		beginListen();	
	}
	
	/**
	 * Sets the port number for this ConnectionManager and starts this ConnnectionManager listening 
	 * @param callBack - callback for connections
	 * @param port
	 */
	public void beginListen(ConnectionManagerCallable callBack, int port){
		if(isListening){
			return;
		}
		
		ConnectionManager.port = port;
		
		beginListen(callBack);	
	}
	
	/**
	 * Terminates the listening Thread of this ConnEctionManager
	 * @return - True guarantees termination of the thread.
	 */
	public boolean endListen(){
		if(!isListening){
			return false;
		}
		
		isListening = false;
		
		try {
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		while(listenerThread.isAlive()){
			
		}
		return true;	
	}
	
	/**
	 * Runnable wrapper for the listenerThread
	 * @author Jared Nielson
	 *
	 */
	private class RunnableListener implements Runnable{

		@Override
		public void run() {
			System.out.println("ConnectionManager::BeginListen on Port " + port);
			
			try{
				ss = new ServerSocket(port);
			} catch(IOException e){
				e.printStackTrace();
				isListening = false;
				return;
			}
			
			while(numConnections < maxConnections){
				try {
					Socket connection = ss.accept();
					connections.add(connection);
					numConnections++;
					if(callBack != null){
						Thread callable = new Thread(new Runnable(){

							@Override
							public void run() {
								callBack.OnConnection(connection);
								
							}});
						callable.start();
					}
					
					System.out.println("Connection established");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					isListening = false;
				}
				
				//If we're no longer listening break out of loop which will terminate thread on its own
				if(!isListening){
					break;
				}
			}
			
			try {
				//Close the server socket if it's still open
				if(!ss.isClosed()){
					ss.close();					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("ConnectionManager::EndListen");
			
		}
		
	}
}
