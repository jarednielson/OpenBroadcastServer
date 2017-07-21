package com.jrpup.openbroadcastserver;

import java.util.Scanner;

public class OpenBroadcastServerLauncher {

	private static int port = 60000;
	
	public static void main(String[] args) {
		ConnectionManager conManager = ConnectionManager.getConnectionManager();
		
		conManager.beginListen();
		
		Scanner in = new Scanner(System.in);
		
		in.nextLine();
		
		conManager.endListen();
		
		in.nextLine();
		
		conManager.beginListen();
		
		in.nextLine();
		
		conManager.endListen();
	}

}
