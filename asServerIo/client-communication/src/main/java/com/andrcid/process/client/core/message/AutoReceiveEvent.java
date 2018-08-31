package com.andrcid.process.client.core.message;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 接收数据定时器
 * @author chengang
 *
 */
public class AutoReceiveEvent {
	
	CountDownLatch cdl;
	
	public AutoReceiveEvent() {
		this.cdl = new CountDownLatch(1);
	}
	
	public AutoReceiveEvent(int waitCount) {
		this.cdl = new CountDownLatch(waitCount);
	}
	
	public void set() {
		this.cdl.countDown();
	}

	public boolean waitOne(long time) throws InterruptedException {
		return this.cdl.await(time, TimeUnit.MILLISECONDS);
	}

}
