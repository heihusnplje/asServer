package com.tigerjoys.onion.communication.server.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.tigerjoys.communication.protocol.message.RequestMessage;
import com.tigerjoys.onion.communication.server.core.hotkey.Command;
import com.tigerjoys.onion.communication.server.core.hotkey.CommandMapping;
import com.tigerjoys.onion.communication.server.service.ITestService;
import com.tigerjoys.onion.communication.server.utils.JsonHelper;
import com.tigerjoys.onion.communication.server.vo.TestVO;

@Command
public class TestCommand {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestCommand.class);
	
	@Autowired
	private ITestService testService;
	
	@CommandMapping("/api/test/{abc}")
	public List<String> test(RequestMessage request , int abc , JSONObject json , TestVO vo){
		LOGGER.info("我进来了此方法！！！");
		LOGGER.info("server receive body : " + request.getBody());
		LOGGER.info("abc = " + abc);
		LOGGER.info("json = " + json.toJSONString());
		LOGGER.info("vo = " + JsonHelper.toJson(json));
		
		return testService.test(request, abc);
	}
	
}
