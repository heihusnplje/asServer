package com.tigerjoys.onion.communication.server.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tigerjoys.onion.communication.server.core.config.ServerConstant;
import com.tigerjoys.onion.communication.server.core.context.BeatContext;

/**
 * 代码执行时间告警输出过滤器
 * @author chengang
 *
 */
public class ExecuteTimeAlarmFilter implements IFilter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteTimeAlarmFilter.class);

	@Override
	public void filter(BeatContext context) throws Exception {
		long cost = context.getInvokeEndTime() - context.getInvokeBeginTime();
		String msg = "time:" + cost + "ms, remoteIP:" +context.getRemoteIP() + ", mapping:" + context.getMapping();
		
		if (cost >= ServerConstant.DEFAULT_EXECUTE_SLOW_METHOD_MILLIS) {//调用超过指定毫秒，打印日志
			LOGGER.warn(msg);
		} else {
			LOGGER.info(msg);
		}
	}
	
	@Override
	public FilterType getFilterType() {
		return FilterType.Response;
	}

}
