package com.tigerjoys.onion.pcserver.enums;

/**
 * 错误信息枚举接口
 * @author chengang
 *
 */
public interface IErrorCodeEnum {
	
	/**
	 * 获得错误编号
	 * @return int
	 */
	public int getCode();
	
	/**
	 * 获得错误描述
	 * @return String
	 */
	public String getDesc();

}
