package com.tigerjoys.onion.pcserver.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * 常量
 * @author chengang
 *
 */
public final class Constant {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Constant.class);
	
	private static Properties properties = new Properties();
	
	static {
		try(InputStream is = new ClassPathResource("appservice.properties").getInputStream()) {
			properties.load(is);
		} catch (IOException e) {
			LOGGER.error(e.getMessage() , e);
		}
	}
	
	/**
	 * 项目的属性dev test prod
	 */
	public static final String PROJECT_PROFILE = properties.getProperty("env");
	
	//是否是测试环境
	public static final boolean IS_TEST = !"prod".equals(PROJECT_PROFILE);

}
