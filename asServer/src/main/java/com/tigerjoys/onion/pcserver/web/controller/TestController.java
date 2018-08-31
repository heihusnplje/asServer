package com.tigerjoys.onion.pcserver.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tigerjoys.onion.pcserver.basic.BaseController;
import com.tigerjoys.onion.pcserver.inter.dao.IServerRepository;
import com.tigerjoys.onion.pcserver.inter.entity.ServerEntity;

@RestController
public class TestController extends BaseController {
	
	@Autowired
	private IServerRepository serverRepository;
	
	@GetMapping("/test")
	public ServerEntity fff() {
		return serverRepository.findOne(1);
	}

}
