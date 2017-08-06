package com.jrpup.openbroadcastserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A ConnectionManager stores and tracks socket connections
 * @author Jared Nielson
 *
 */
public class ConnectionManager implements ConnectionListenerCallable, AsynchronousStringSocketPostable {
	
	/**
	 * Threadsafe HashMap that holds our Sockets with the key being the InetAddress
	 * of the Sockets endpoint connection
	 */
	private ConcurrentHashMap<String, AsynchronousStringSocket> clientSockets;
	
	public ConnectionManager(){
		clientSockets = new ConcurrentHashMap<String, AsynchronousStringSocket>();
	}
	
	/**
	 * ConnectionListenerCallable method. Can be called from multiple threads.
	 */
	@Override
	public void OnConnection(Socket s) {
		// TODO Auto-generated method stub
		try {
			AsynchronousStringSocket asyncSocket = new AsynchronousStringSocket(s);
			clientSockets.put(s.getInetAddress().toString() + "::" + s.getPort(), asyncSocket);
			
			asyncSocket.readLinesAsync(this, new Object[]{s.getInetAddress().toString() + "::" + s.getPort()});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<String, AsynchronousStringSocket> getConnections(){
		
		return clientSockets;
	}
	
	/**
	 * Closes all streams and connections associated with this
	 * @throws IOException 
	 */
	public void shutDown() throws IOException{
		for(String key : clientSockets.keySet()){
			clientSockets.get(key).close();
		}
	}
	

	@Override
	public void postString(String s, Object[] payload) {
		String poster = (String) payload[0];
		//check if message is null
		if(s.equalsIgnoreCase("null")){
			//clean up socket
			try {
				clientSockets.get(poster).close();
				clientSockets.remove(poster);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		for(String key : clientSockets.keySet()){
			if(poster.equals(key)){
				continue;
			}
			
			try {
				clientSockets.get(key).sendLinesAsync(s + "\n", this, null);
			} catch (IOException e) {
				try {
					clientSockets.get(key).close();
					clientSockets.remove(key);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		try {
			clientSockets.get(poster).readLinesAsync(this, new Object[]{poster});
		} catch (IOException e) {
			// Close and remove connection
			e.printStackTrace();
		}
		
	}

	@Override
	public void onMessageSent(Object[] payload) {
		
		
	}
}
