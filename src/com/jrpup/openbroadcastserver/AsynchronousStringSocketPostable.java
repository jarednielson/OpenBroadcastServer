package com.jrpup.openbroadcastserver;

public interface AsynchronousStringSocketPostable {
	public void postString(String s, Object[] payload);
	
	public void onMessageSent(Object[] payload);
}
