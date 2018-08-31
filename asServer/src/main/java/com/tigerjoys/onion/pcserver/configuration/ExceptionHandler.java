package com.tigerjoys.onion.pcserver.configuration;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.tigerjoys.onion.pcserver.basic.ActionResult;
import com.tigerjoys.onion.pcserver.enums.ErrCodeEnum;
import com.tigerjoys.onion.pcserver.utils.LoggerUtil;

/**
 * 全局性抓取异常
 * 
 * @author chengang
 *
 */
public class ExceptionHandler implements HandlerExceptionResolver {

	private static final Logger EXCEPT_LOGGER = LoggerUtil.getLogger(LoggerUtil.exceptLogger);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		HandlerMethod handlerMethod = null;
		Method method = null;

		if (handler != null) {
			if (handler instanceof HandlerMethod) {
				handlerMethod = (HandlerMethod) handler;
				method = handlerMethod.getMethod();
			}
		}

		if (ex != null) {
			StringBuilder buf = new StringBuilder("拦截器拦截到异常，class:");
			buf.append(handlerMethod != null ? handlerMethod.getBeanType() : null);
			buf.append(",method:").append(method != null ? method.getName() : null);
			buf.append(",url:").append(request.getRequestURI());

			EXCEPT_LOGGER.info(buf.toString(), ex);
		}

		return ActionResult.errorModel(response, ErrCodeEnum.error.getDesc());
	}

}
