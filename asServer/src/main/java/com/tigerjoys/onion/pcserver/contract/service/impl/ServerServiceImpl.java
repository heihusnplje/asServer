package com.tigerjoys.onion.pcserver.contract.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tigerjoys.onion.pcserver.contract.service.IServerService;
import com.tigerjoys.onion.pcserver.inter.dao.IServerRepository;
import com.tigerjoys.onion.pcserver.inter.entity.ServerEntity;

@Service
public class ServerServiceImpl implements IServerService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IServerRepository serverRepository;

	@Override
	public ServerEntity findServer(int port , String ip) {
		logger.info("findServer port : " + port);
		logger.info("findServer ip : " + ip);
		
		return serverRepository.findByPortAndIp(port, ip);
	}

}
