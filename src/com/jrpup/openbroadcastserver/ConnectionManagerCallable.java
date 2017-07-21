package com.jrpup.openbroadcastserver;

import java.net.Socket;

/**
 * Interface for subscribers to the connection manager
 * @author Jared Nielson
 *
 */
public interface ConnectionManagerCallable {
	public void OnConnection(Socket s);
}
