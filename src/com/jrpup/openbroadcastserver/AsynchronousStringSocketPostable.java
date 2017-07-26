package com.jrpup.openbroadcastserver;

public interface AsynchronousStringSocketPostable {
	public void postString(String s);
	
	public void onMessageSent(Object[] payload);
}
