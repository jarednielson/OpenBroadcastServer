package com.jrpup.openbroadcastserver;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A ConnectionManager stores and tracks socket connections
 * @author Jared Nielson
 *
 */
public class ConnectionManager implements ConnectionListenerCallable {
	
	/**
	 * Threadsafe HashMap that holds our Sockets with the key being the InetAddress
	 * of the Sockets endpoint connection
	 */
	private ConcurrentHashMap<String, Socket> clientSockets;
	
	public ConnectionManager(){
		clientSockets = new ConcurrentHashMap<String, Socket>();
	}
	
	/**
	 * ConnectionListenerCallable method. Can be called from multiple threads.
	 */
	@Override
	public void OnConnection(Socket s) {
		// TODO Auto-generated method stub
		clientSockets.put(s.getInetAddress().toString(), s);
	}
	
	public Map<String, Socket> getConnections(){
		
		for(String key : clientSockets.keySet()){
			
		}
		
		return clientSockets;
	}
	
	public void markSocketAsDirty(String inetAddress){
		
	}

}
