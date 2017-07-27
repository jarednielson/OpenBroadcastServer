package com.jrpup.openbroadcastserver;

import java.io.IOException;
import java.util.Scanner;

public class OpenBroadcastServerLauncher {

	private static int port = 60000;
	
	public static void main(String[] args) {
		ConnectionListener conListener = ConnectionListener.getConnectionManager(port);
		
		
		ConnectionManager conManager = new ConnectionManager();
		conListener.beginListen(conManager);
		
		
		
		Scanner in = new Scanner(System.in);
		
		in.nextLine();
		
		
		conListener.endListen();
		try {
			conManager.shutDown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in.close();
	}

}
