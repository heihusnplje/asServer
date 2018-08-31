package com.tigerjoys.onion.pcserver.contract.service;

import com.tigerjoys.onion.pcserver.inter.entity.ServerEntity;

public interface IServerService {
	
	public ServerEntity findServer(int port , String ip);

}
