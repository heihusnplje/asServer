package com.andrcid.process.client.core.utils;

/**
 * 系统工具
 * @author chengang
 *
 */
public class SystemUtils {
	
	/**
	 * 判断是否是苹果系统
	 * @return boolean
	 */
    public static boolean isMacOs() {
        String os = System.getProperties().getProperty("os.name");
        return os.equalsIgnoreCase("Mac OS X");
    }
    
    /**
     * 判断是否是win系统
     * @return boolean
     */
    public static boolean isWinOs() {
    	String os = System.getProperties().getProperty("os.name");
        return os.toLowerCase().indexOf("windows") > -1;
    }
    
}
