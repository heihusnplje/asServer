package com.tigerjoys.communication.client.test.command;

import java.util.List;

import com.andrcid.process.client.core.hotkey.Controller;
import com.andrcid.process.client.core.hotkey.Module;
import com.andrcid.process.client.core.hotkey.Param;
import com.andrcid.process.client.core.hotkey.RequestMapping;

@Module
@Controller
public interface IServiceRemote {
	
	@RequestMapping("/api/test/123")
	public List<String> sayHello(@Param("aa") String x , @Param("b") int b);

}
