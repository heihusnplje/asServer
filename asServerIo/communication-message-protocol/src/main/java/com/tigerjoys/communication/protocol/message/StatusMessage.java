package com.tigerjoys.communication.protocol.message;

import com.tigerjoys.communication.protocol.enums.MessageType;

public class StatusMessage extends DefaultMessage {
	
	public StatusMessage() {
		
	}
	
	public StatusMessage(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public MessageType messageType() {
		return MessageType.Status;
	}

}
