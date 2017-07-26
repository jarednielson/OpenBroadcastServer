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
			clientSockets.put(s.getInetAddress().toString(), asyncSocket);
			
			asyncSocket.readLinesAsync(this, new Object[]{s.getInetAddress()});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<String, AsynchronousStringSocket> getConnections(){
		
		return clientSockets;
	}
	

	@Override
	public void postString(String s, Object[] payload) {
		//TODO: Need buffer system
		String poster = (String) payload[0];
		for(String key : clientSockets.keySet()){
			if(poster.equals(key)){
				continue;
			}
			
			try {
				clientSockets.get(key).sendLinesAsync(s, this, null);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onMessageSent(Object[] payload) {
		
		
	}
}
