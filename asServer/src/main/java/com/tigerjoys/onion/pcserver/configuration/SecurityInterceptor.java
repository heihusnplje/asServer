package com.tigerjoys.onion.pcserver.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tigerjoys.onion.pcserver.basic.BeatContext;
import com.tigerjoys.onion.pcserver.basic.RequestUtils;
import com.tigerjoys.onion.pcserver.constant.WebParamConst;

/**
 * 登录验证拦截器
 * @author chengang
 *
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityInterceptor.class);
	
	/**
	 * 记录接口调用完毕的执行时间
	 */
	private static ThreadLocal<Long> executiveTime = new ThreadLocal<Long>();

	/*
	 * 内存管理Bean
	 */
	//private MemoryMXBean memory = ManagementFactory.getMemoryMXBean();

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 *
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		executiveTime.set(System.currentTimeMillis());

		// 此处必须重置线程变量
		BeatContext context = new BeatContext();
		RequestUtils.bindBeatContextToCurrentThread(context);
		context.setRequest(request);
		context.setResponse(response);
		context.setDeviceId(request.getHeader(WebParamConst.HEADER_DEVICE_ID));

		try {
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage() , e);
			return false;
		} finally {
			if(context.isIslog()) {
				//TODO 记录访问日志。
			}
		}
	}

}
