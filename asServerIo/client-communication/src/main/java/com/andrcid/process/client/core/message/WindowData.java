package com.andrcid.process.client.core.message;

import com.andrcid.process.client.core.session.ISession;
import com.tigerjoys.communication.protocol.Protocol;

public class WindowData {
	
	private AutoReceiveEvent event;
	
	private int sessionId;
	
	private Protocol protocol;
	
	private long timestamp;
	
	private ISession session;
	
	public WindowData(AutoReceiveEvent event) {
		this.event = event;
	}

	public AutoReceiveEvent getEvent() {
		return event;
	}

	public void setEvent(AutoReceiveEvent event) {
		this.event = event;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public ISession getSession() {
		return session;
	}

	public void setSession(ISession session) {
		this.session = session;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

}
