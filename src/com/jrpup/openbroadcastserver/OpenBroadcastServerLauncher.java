package com.jrpup.openbroadcastserver;

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
		
		in.close();
	}

}
