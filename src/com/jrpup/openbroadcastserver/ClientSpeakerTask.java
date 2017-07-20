package com.jrpup.openbroadcastserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientSpeakerTask implements Runnable {

	private ArrayList<Socket> clients;
	private Object lock;
	private boolean isRunning;
	
	private byte[] bytes;
	private final int BUFFER_SIZE = 2024;
	
	public ClientSpeakerTask(){
		clients = new ArrayList<Socket>();
		lock = new Object();
		isRunning = true;
		
		bytes = new byte[BUFFER_SIZE];
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning){
			for(int i = 0; i < clients.size(); i++){
				Socket s = clients.get(i);
				
			}
		}
	}
	
	public void addClient(Socket socket){
		
		synchronized(lock){
			clients.add(socket);
			
		}
	}
	
	public void killTask(){
		isRunning = false;
	}

}
