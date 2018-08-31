package com.andrcid.process.client.core.hotkey;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrcid.process.client.core.config.ClientConstant;
import com.andrcid.process.client.core.message.MessageIdFactory;
import com.andrcid.process.client.core.session.ISession;
import com.andrcid.process.client.core.session.SessionException;
import com.andrcid.process.client.core.session.SessionFactory;
import com.andrcid.process.client.core.utils.ClassHelper;
import com.tigerjoys.communication.protocol.Protocol;
import com.tigerjoys.communication.protocol.enums.MessageFromType;
import com.tigerjoys.communication.protocol.enums.MessageType;
import com.tigerjoys.communication.protocol.exception.ProtocolException;
import com.tigerjoys.communication.protocol.exception.ThrowErrorHelper;
import com.tigerjoys.communication.protocol.message.ExceptionMessage;
import com.tigerjoys.communication.protocol.message.IMessage;
import com.tigerjoys.communication.protocol.message.RequestMessage;
import com.tigerjoys.communication.protocol.message.ResponseMessage;
import com.tigerjoys.communication.protocol.utility.JsonHelper;

public class CommunicationInvocationHandler implements InvocationHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationInvocationHandler.class);

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?> proxyClass = method.getDeclaringClass();
		
		// 如果调用的是从Object继承来的方法，则直接调用。
		if (Object.class.equals(proxyClass)) {
			return method.invoke(this, args);
		}
		//首先判断Session是否正常
		ISession session = SessionFactory.getSession();
		if(session == null || session.isClosed()) {
			throw new SessionException("session not found or session is closed!");
		}
		//获取具体的方法
		MethodInfo methodInfo = MethodHolder.getMethodInfo(proxyClass, method.getName());
		if(methodInfo == null) {
			throw new ProxyMethodNotFoundException(proxyClass , method);
		}
		
		long beginTime = System.currentTimeMillis();
		LOGGER.info("invoke remote method : " + method);
		if(args != null && args.length > 0) {
			String[] paramNames = methodInfo.getParamNames();
			if(paramNames.length == 0) {
				LOGGER.info("invoke remote args " + args[0]);
			} else {
				for(int i=0;i<args.length;i++) {
					LOGGER.info("invoke remote args " +paramNames[i]+ " : " + args[i]);
				}
			}
		}
		
		//获得返回值
		IMessage message = null;
		//调用远程接口
		try {
			//组装参数协议
			message = SessionFactory.getSession().request(createRequestMessage(methodInfo, args));
			if(message == null) {
				throw new NullPointerException("invoke remote command return message is null!");
			}
			
			MessageType type = message.messageType();
			switch(type) {
				case Response:
					LOGGER.debug("invoke " + methodInfo.getMapping()+ " time:" + (System.currentTimeMillis() - beginTime) + "ms");
					return JsonHelper.toObject(((ResponseMessage)message).getBody(), method.getReturnType());
				case Exception:
					ExceptionMessage ex = (ExceptionMessage)message;
					throw ThrowErrorHelper.throwServiceError(ex.getErrCode(), ex.getErrMsg());
				default :
					throw new ProtocolException("Unable to process data information!");
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/** 
     * 获取目标对象的代理对象 
     * @param proxyInterface - 要代理的接口
     * @return 代理对象 
     */  
	public static Object newInstance(Class<?> proxyInterface) {
    	CommunicationInvocationHandler handler = new CommunicationInvocationHandler();
    	
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{proxyInterface}, handler);  
    }
	
	private Protocol createRequestMessage(MethodInfo methodInfo, Object[] args) {
		RequestMessage request = new RequestMessage(ClientConstant.DEVICE_ID , methodInfo.getMapping());
		request.setMessageTime(System.currentTimeMillis());
		
		if(args == null || args.length == 0) {
			request.setBody(JsonHelper.EMPTY_JSON_STRING);
		} else if(args.length == 1) {
			//判断参数是否是基本类型，如果是基本类型，则转换为JSON格式，如果是复杂类型，则直接走JsonHelper.toJson()方法。
			if(ClassHelper.isBasicType(args[0].getClass())) {
				Map<String , Object> data = new HashMap<>();
				data.put(methodInfo.getParamNames()[0], args[0]);
				
				request.setBody(JsonHelper.toJson(data));
			} else {
				//复杂对象需要判断是否有参数名称
				String[] paramNames = methodInfo.getParamNames();
				if(paramNames == null || paramNames.length == 0) {
					request.setBody(JsonHelper.toJson(args[0]));
				} else {
					Map<String , Object> data = new HashMap<>();
					data.put(paramNames[0], args[0]);
					
					request.setBody(JsonHelper.toJson(data));
				}
			}
		} else {
			//映射传输的参数
			Map<String , Object> data = new HashMap<>();
			String[] paramNames = methodInfo.getParamNames();
			for(int i=0;i<args.length;i++) {
				data.put(paramNames[i], args[i]);
			}
			
			request.setBody(JsonHelper.toJson(data));
		}
		
		return new Protocol(MessageIdFactory.createMessageId() , MessageFromType.CLIENT , request);
	}

}
