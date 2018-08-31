package com.tigerjoys.onion.pcserver.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BeatContext
 * @author chengang
 *
 */
public class BeatContext {
	
	/**
	 * HttpServletRequest
	 */
	private HttpServletRequest request;
	
	/**
	 * HttpServletResponse
	 */
	private HttpServletResponse response;
	
	/**
	 * 设备ID
	 */
	private String deviceId;
	
	/**
	 * 是否记录日志
	 */
	private boolean islog = true;

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public boolean isIslog() {
		return islog;
	}

	public void setIslog(boolean islog) {
		this.islog = islog;
	}
	
}
