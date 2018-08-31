package com.tigerjoys.communication.client.test.command.impl;

import com.andrcid.process.client.core.hotkey.Module;
import com.google.inject.Inject;
import com.tigerjoys.communication.client.test.command.IServiceRemote;
import com.tigerjoys.communication.client.test.command.ITestCommand;

@Module(from=ITestCommand.class)
public class TestCommandImpl implements ITestCommand {
	
	@Inject
	private IServiceRemote serviceRemote;

	@Override
	public void sayHello() {
		System.out.println(serviceRemote.sayHello("abc" , 101));
	}

}
