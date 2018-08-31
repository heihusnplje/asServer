package com.andrcid.process.client.core.hotkey;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.andrcid.process.client.core.utils.ClassHelper;

/**
 * 命令描述
 * @author chengang
 *
 */
public class MethodInfo {
	
	/**
	 * 所属类
	 */
	private final Class<?> clazz;
	
	/**
	 * 所属的方法
	 */
	private final Method actionMethod;
	
	/**
	 * 映射的URL
	 */
	private final String mapping;
	
	/**
	 * 方法的返回类型
	 */
	private final Class<?> returnType;
	
	/**
	 * 参数名称集合
	 */
	private final String[] paramNames;
	
	/**
	 * 参数类型集合
	 */
	private final Class<?>[] paramTypes;
	
	public MethodInfo(Method method , String mapping) {
		this.clazz = method.getDeclaringClass();
		this.actionMethod = method;
		this.mapping = mapping;
		this.paramTypes = method.getParameterTypes();
	    this.paramNames = getMethodParamNames();
	    this.returnType = method.getReturnType();
	}
	
	/**
	 * 通过javassist解析到方法的参数名称
	 * @return String[]
	 */
	private String[] getMethodParamNames() {
		Method method = this.actionMethod;
		Parameter[] parameters = method.getParameters();
		if(parameters != null && parameters.length > 0) {
			//如果是一个参数， 需要额外的逻辑
			if(parameters.length == 1) {
				//如果是基本类型，则必须要加@Param注解
				if(ClassHelper.isBasicType(parameters[0].getType())) {
					Param param = parameters[0].getAnnotation(Param.class);
					if(param == null) {
						throw new RuntimeException("proxyClass:"+this.clazz.getName()+",method:"+method.getName()+",parameter have not @Param!");
					}
					
					return new String[] {param.value()};
				} else {
					//否则，不加和加都可以
					Param param = parameters[0].getAnnotation(Param.class);
					if(param == null) {
						return new String[0];
					} else {
						return new String[] {param.value()};						
					}
				}
			} else {
				String[] p = new String[parameters.length];
				for(int i=0;i<parameters.length;i++) {
					Param param = parameters[i].getAnnotation(Param.class);
					if(param == null) {
						throw new RuntimeException("proxyClass:"+this.clazz.getName()+",method:"+method.getName()+",parameter have not @Param!");
					}
					
					p[i] = param.value();
				}
				return p;
			}
		}
		
		return new String[0];
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public Method getActionMethod() {
		return actionMethod;
	}

	public String getMapping() {
		return mapping;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public Class<?>[] getParamTypes() {
		return paramTypes;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

}
