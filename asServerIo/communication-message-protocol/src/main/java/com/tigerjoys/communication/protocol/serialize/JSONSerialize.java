package com.tigerjoys.communication.protocol.serialize;

import java.nio.charset.Charset;

import com.tigerjoys.communication.protocol.utility.JsonHelper;

/**
 * JSON序列化接口
 * @author chengang
 *
 */
public class JSONSerialize implements ISerialize {
	
	private Charset utf8 = Charset.forName("UTF-8");

	@Override
	public byte[] serialize(Object obj) throws Exception {
		return JsonHelper.toJson(obj).getBytes(utf8);
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
		return JsonHelper.toObject(new String(bytes , utf8), clazz);
	}
	
}
