package com.tigerjoys.onion.communication.server.core.hotkey;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 所有的命令集合
 * @author chengang
 *
 */
public final class CommandHolder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandHolder.class);
	
	private static volatile boolean initialized = false;//是否已经被初始化过了

	private static IProxyFactory PROXY_FACTORY;
	
	/**
	 * 根据mapping获取代理类
	 * @param mapping - String
	 * @return IProxyStub
	 */
	public static IProxyStub getProxy(String mapping) {
		return PROXY_FACTORY.getProxy(mapping);
	}

	/**
	 * 随服务器启动
	 * 
	 * @param packageName
	 */
	public static void init(String packageName , IProxyFactory proxyFactory) {
		if(initialized) {
			LOGGER.warn("command holder has been initialized!");
			return;
		}
		synchronized(CommandHolder.class) {
			if(!initialized) {
				PROXY_FACTORY = proxyFactory;
				LOGGER.info("commands wraped. . .");
				try {
					//创建代理工厂
					PROXY_FACTORY.init(scan(packageName));
				} catch (Exception e) {
					LOGGER.error("commands is not wraped. . .", e);
				}
			}
		}
	}

	/**
	 * 扫描指定的包，获取数据库类对象的初始化信息
	 * 
	 * @param packageName - 要扫描的包路径
	 * @return Map<String, CommandInfo>
	 * @throws Exception
	 */
	private static Map<String, CommandInfo> scan(String packageName) throws Exception {
		LOGGER.info("scan package : " + packageName);
		
		Map<String, CommandInfo> commands = new HashMap<>();

		ClassLoader loader = getClassLoader();
		List<String> names = getCurpackClaNames(getPackagePath(packageName), file -> file.getName().endsWith(".class"));
		for (String name : names) {
			String externalName = name.substring(0, name.indexOf('.')).replace('/', '.');
			
			Class<?> service = loader.loadClass(externalName);
			if(service.isAnnotationPresent(Command.class)) {//包含Command注解才解析方法
				for (Method method : service.getDeclaredMethods()) {
					CommandMapping mapping = method.getAnnotation(CommandMapping.class);
					if (mapping != null) {
						LOGGER.info("scan class {}.{}" , service.getName() , method.getName());
						
						String command = mapping.value();
						if (commands.containsKey(command)) {
							throw new RuntimeException("command "+ command +" has been existed,check it...,className " + service.getName()+" , methodName " + method.getName());
						} else {
							commands.put(command, new CommandInfo(method , command));
						}
					}
				}
			}
		}
		
		return commands;
	}

	/**
	 * 获得当前线程的ClassLoader
	 * 
	 * @return ClassLoader
	 */
	private static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 转换包名为路径/
	 * 
	 * @param packageName
	 * @return String
	 */
	private static String getPackagePath(String packageName) {
		return packageName == null ? null : packageName.replace('.', '/');
	}

	/**
	 * 获取当前包下的全限定类名，非递归<br>
	 * notice:该方法可以再优化为递归扫描，多路径扫描等。。暂时不考虑
	 * 
	 * @param path - 要扫描的路径
	 * @param filter - 文件过滤条件
	 * @return List<String>
	 * @throws IOException
	 */
	private static List<String> getCurpackClaNames(String path, FileFilter filter) throws IOException {
		List<String> names = new ArrayList<>();
		String root = CommandHolder.class.getResource("/").getPath().replace("test-classes", "classes");
		File file = new File(root + path);
		
		File[] files = file.listFiles(filter);
		if(files != null && files.length > 0) {
			for (File child : files) {
				names.add(path + "/" + child.getName());
			}
		}
		return names;
	}

}
