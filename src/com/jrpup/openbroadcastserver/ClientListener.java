package com.jrpup.openbroadcastserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.Socket;

public class ClientListener implements ConnectionManagerCallable {

	private Writer writer;
	
	
	private Object lock;
	private boolean continueTask;
	
	
	public ClientListener(Socket socket, Writer writer, Object lock){
		continueTask = true;
		this.writer = writer;
		this.lock = lock;
	}
	
	@Override
	public void OnConnection(Socket s) {
		BufferedReader in;
		
		try {
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		while(continueTask){
			String msg = "";
			try {
				msg = in.readLine();
				
				if(msg == null){
					s.close();
					break;
				}
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

}
