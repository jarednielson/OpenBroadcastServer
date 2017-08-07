package com.jrpup.openbroadcastserver;

import java.io.IOException;
import java.util.Scanner;

public class OpenBroadcastServerLauncher {

	private static int port = 60000;
	
	public static void main(String[] args) {
		if(args.length > 1){
			try{
				port = Integer.parseInt(args[0]);
			} catch(NumberFormatException e){
				System.out.println("Could not parse Port number");
				return;
			}
		}
		
		if(port < 0 ){
			System.out.println("Invalid Port number. Port must be greater than or equal to 0");
			return;
		}
		
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
