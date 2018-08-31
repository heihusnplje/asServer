package com.tigerjoys.onion.pcserver.basic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * spring bean复制工具类
 * 
 * @author chengang
 *
 */
@SuppressWarnings("rawtypes")
public final class BeanUtils {

	/**
	 * 复制bean属性
	 * 
	 * @param source
	 *            - 源对象
	 * @param target
	 *            - 目标对象
	 */
	public static void copyBean(Object source, Object target) {
		org.springframework.beans.BeanUtils.copyProperties(source, target);
	}

	/**
	 * 复制bean属性
	 * 
	 * @param source
	 *            - 源对象
	 * @param target
	 *            - 目标对象
	 * @return K
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <K> K copyBean(Object source, Class<K> targetClassType)
			throws InstantiationException, IllegalAccessException {
		K k = targetClassType.newInstance();
		copyBean(source, k);
		return k;
	}

	/**
	 * 将Collection<?>转换为List<T>
	 * 
	 * @param sourceList
	 *            - 源集合
	 * @param targetClass
	 *            - 类型T对应的class
	 * @return List<T>
	 */
	public static <T> List<T> copyBeanList(Collection<?> sourceList, Class<T> targetClass) {
		return copyBeanList(sourceList, targetClass, null);
	}

	/**
	 * 将Collection<?>转换为List<T>，同时会调用propProcess对对象中的某些属性做处理
	 * 
	 * @param sourceList
	 *            - 源集合
	 * @param targetClass
	 *            - 类型T对应的class
	 * @param propProcess
	 *            - PropertyProcess<T>
	 * @return List<T>
	 */
	public static <T> List<T> copyBeanList(Collection<?> sourceList, Class<T> targetClass,
			PropertyProcess<T> propProcess) {
		if (sourceList == null || sourceList.isEmpty())
			return null;

		List<T> list = new ArrayList<T>();
		try {
			for (Object object : sourceList) {
				T bean = targetClass.newInstance();
				if (bean != null) {
					copyBean(object, bean);

					if (propProcess != null) {
						propProcess.process(object, bean);
					}

					list.add(bean);
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	/**
	 * 将java bean 转换成Map
	 * @param bean - Object
	 * @return Map
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Map describe(Object bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Map<? , ?> m = org.apache.commons.beanutils.BeanUtils.describe(bean);
		if(m != null) m.remove("class");
		
		return m;
	}

	/**
	 * 将Map转换为java bean
	 * 
	 * @param source
	 *            - Map
	 * @param target
	 *            - 要注入的Bean对象
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public static void populate(Map source, Object target) throws IllegalAccessException, InvocationTargetException {
		org.apache.commons.beanutils.BeanUtils.populate(target, source);
	}

	/**
	 * 将Map转换为java bean
	 * 
	 * @param source
	 *            -Map
	 * @param targetClassType
	 *            - 要转换的Bean Class
	 * @return K
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static <K> K populate(Map source, Class<K> targetClassType)
			throws InstantiationException, IllegalAccessException, InvocationTargetException {
		K k = targetClassType.newInstance();
		populate(source, k);
		return k;
	}

	/**
	 * 将Collection<Map<? , ?>>转换为List<T>
	 * 
	 * @param sourceList
	 *            - 源集合
	 * @param targetClass
	 *            - 类型T对应的class
	 * @return List<T>
	 */
	public static <T> List<T> populate(List<Map<String, Object>> sourceList, Class<T> targetClass) {
		return populate(sourceList, targetClass, null);
	}

	/**
	 * 将Collection<Map<? , ?>>转换为List<T>，同时会调用propProcess对对象中的某些属性做处理
	 * 
	 * @param sourceList
	 *            - 源集合
	 * @param targetClass
	 *            - 类型T对应的class
	 * @param propProcess
	 *            - PropertyProcess<T>
	 * @return List<T>
	 */
	public static <T> List<T> populate(Collection<Map<String, Object>> sourceList, Class<T> targetClass,
			PropertyProcess<T> propProcess) {
		if (sourceList == null || sourceList.isEmpty())
			return null;

		List<T> list = new ArrayList<T>();
		try {
			for (Map source : sourceList) {
				T bean = populate(source, targetClass);

				if (propProcess != null) {
					propProcess.process(source, bean);
				}

				list.add(bean);
			}
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 对象的属性处理接口
	 */
	public interface PropertyProcess<T> {

		public void process(Object fromObj, T toObj);

	}

}
