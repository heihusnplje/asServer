package com.tigerjoys.communication.protocol.message;

import com.tigerjoys.communication.protocol.enums.MessageType;

public class HeartBeatMessage extends DefaultMessage {
	
	public HeartBeatMessage() {
		
	}
	
	public HeartBeatMessage(String deviceId) {
		this.deviceId = deviceId;
	}
	
	@Override
	public MessageType messageType() {
		return MessageType.HeartBeat;
	}

}
