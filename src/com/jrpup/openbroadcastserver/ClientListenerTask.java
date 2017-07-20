package com.jrpup.openbroadcastserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.Socket;

public class ClientListenerTask implements Runnable {

	private Socket socket;
	private Writer writer;
	
	
	private Object lock;
	private boolean continueTask;
	
	
	public ClientListenerTask(Socket socket, Writer writer, Object lock){
		this.socket = socket;
		continueTask = true;
		this.writer = writer;
		this.lock = lock;
	}
	
	@Override
	public void run() {
		BufferedReader in;
		
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		while(continueTask){
			String msg = "";
			try {
				msg = in.readLine();
				synchronized(lock){
					writer.write(msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		try {
			in.close();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void killThread(){
		continueTask = false;
	}

}
