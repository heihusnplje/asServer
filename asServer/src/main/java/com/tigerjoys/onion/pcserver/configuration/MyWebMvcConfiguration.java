package com.tigerjoys.onion.pcserver.configuration;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;

/**
 * MVC配置类
 * @author chengang
 *
 */
@Configuration
public class MyWebMvcConfiguration extends WebMvcConfigurerAdapter {
	
	/**
	 * 校验的拦截器
	 * @return SecurityInterceptor
	 */
	@Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }
	
	/**
	 * 设置全局的错误处理
	 * @return ExceptionHandler
	 */
	@Bean
	public ExceptionHandler getExceptionHandler() {
		return new ExceptionHandler();
	}
	
	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.add(getExceptionHandler());
		super.extendHandlerExceptionResolvers(exceptionResolvers);
	}

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getSecurityInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/res/favicon.ico");
		registry.addResourceHandler("/res/**").addResourceLocations("classpath:/res/");
		super.addResourceHandlers(registry);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.clear();
		
		//初始化jackson2
		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(new WebMvcJsonMapper());
        jackson2HttpMessageConverter.setSupportedMediaTypes(ImmutableList.of(MediaType.APPLICATION_JSON));
        
        //放到第一个
        converters.add(jackson2HttpMessageConverter);
        converters.add(new StringHttpMessageConverter());
	}
	
	private class WebMvcJsonMapper extends ObjectMapper {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -850347337734891025L;

		public WebMvcJsonMapper(){
			super();
	        //去掉默认的时间戳格式
	        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	        //设置为中国上海时区
	        this.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	        //设置输入:禁止把POJO中值为null的字段映射到json字符串中
	        this.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
	        //空值不序列化
	        this.setSerializationInclusion(Include.NON_NULL);
	        //反序列化时，属性不存在的兼容处理
	        this.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	        //序列化时，日期的统一格式
	        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

	        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	        //单引号处理
	        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		}

	}

}
