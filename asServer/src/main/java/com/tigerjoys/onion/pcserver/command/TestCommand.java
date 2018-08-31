package com.tigerjoys.onion.pcserver.command;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.tigerjoys.communication.protocol.message.RequestMessage;
import com.tigerjoys.onion.communication.server.core.hotkey.Command;
import com.tigerjoys.onion.communication.server.core.hotkey.CommandMapping;
import com.tigerjoys.onion.communication.server.utils.JsonHelper;
import com.tigerjoys.onion.pcserver.contract.dto.TestDto;
import com.tigerjoys.onion.pcserver.contract.service.IServerService;
import com.tigerjoys.onion.pcserver.inter.entity.ServerEntity;

@Command
public class TestCommand {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestCommand.class);
	
	@Autowired
	private IServerService serverService;
	
	@CommandMapping("/api/test/{abc}")
	public List<String> test(RequestMessage request , int abc , JSONObject json , TestDto vo){
		LOGGER.info("我进来了此方法！！！");
		LOGGER.info("server receive body : " + request.getBody());
		LOGGER.info("abc = " + abc);
		LOGGER.info("json = " + json.toJSONString());
		LOGGER.info("vo = " + JsonHelper.toJson(json));
		
		ServerEntity ss = serverService.findServer(1000, "192.168.0.20");
		
		List<String> dto = new ArrayList<>();
		dto.add(ss.getIp());
		
		return dto;
	}
	
}
