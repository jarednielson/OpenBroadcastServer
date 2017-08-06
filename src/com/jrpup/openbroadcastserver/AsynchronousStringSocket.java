package com.jrpup.openbroadcastserver;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Wrapper class for a Socket that can send and receive strings.
 * @author Jared Nielson
 *
 */
public class AsynchronousStringSocket implements Closeable, AutoCloseable {
	
	private Socket socket;
	
	private BufferedReader in;
	private PrintWriter out;
	private StringBuilder incoming;
	private volatile StringBuilder outgoing;
	
	private boolean isReading;
	private boolean isWriting;
	
	public AsynchronousStringSocket(Socket s) throws IOException{
		socket = s;
		incoming = new StringBuilder();
		outgoing = new StringBuilder();
		isReading = false;
		isWriting = false;
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
	}
	
	public boolean isClosed(){
		return socket.isClosed();
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		socket.close();
	}
	
	/**
	 * Reads all complete lines available from the underlying socket on another thread.
	 * The results are then posted to the given AsynchronousStringSocketPostable object
	 * @param postable
	 * @throws IOException
	 */
	public void readLinesAsync(AsynchronousStringSocketPostable postable, Object[] payload) throws IOException{
		if(isReading){
			return;
		}
		
		if(isClosed()){
			throw new IOException("The underlying socket has been closed.");
		}
		
		isReading = true;
		
		
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					incoming.append(in.readLine());
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					return;
				}
				
				isReading = false;
				
				if(postable != null){
					postable.postString(incoming.toString(), payload);
				}
				incoming = new StringBuilder();
				
			}
			
		});
		thread.start();
	}
	
	/**
	 * Sends the given string over the underlying socket on another thread.
	 * When the message has been sent successfully the onMessageSent method is called
	 * on with the payload as an argument.
	 * @param message
	 * @param payload
	 * @throws IOException 
	 */
	public void sendLinesAsync(String message, AsynchronousStringSocketPostable postable, Object[] payload) throws IOException{
		//TODO: Make this more thread safe and buffered track postables and call them all.
		outgoing.append(message);
		if(isWriting){
			return;
		}
		
		if(socket.isClosed()){
			throw new IOException("The underlying socket has been closed");
		}
		
		isWriting = true;
		
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				out.print(outgoing.toString());
				out.flush();
				outgoing = new StringBuilder();
				
				isWriting = false;
				postable.onMessageSent(payload);
			}
			
		});
		
		thread.start();
				
		
	}
	
	public InetAddress getInetAddress(){
		return socket.getInetAddress();
	}
	
	public InetAddress getLocalAddress(){
		return socket.getLocalAddress();
	}
	
	
}
