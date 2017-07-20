package com.jrpup.openbroadcastserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class OpenBroadcastServerLauncher {

	private static int port = 60000;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<Socket> clients = new ArrayList<Socket>();
		Object lock = new Object();
		while(true){
			try {
				ServerSocket listener = new ServerSocket(port);
				clients.add(listener.accept());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	

}
