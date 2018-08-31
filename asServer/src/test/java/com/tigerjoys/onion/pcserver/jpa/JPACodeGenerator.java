package com.tigerjoys.onion.pcserver.jpa;

import com.tigerjoys.extension.jpa.code.databases.generator.JPAFreeMarkerHandler;

public class JPACodeGenerator {

	public static void main(String[] args) {
		String author = System.getProperty("user.name");
		
		String directory = getDirName();
		String[] ss = new String[]{"t_server","t_server_anchor"};
		for(String table : ss) {
			JPAFreeMarkerHandler.makeFiles(table, author, directory ,OnionDatabaseService.class);
		}
	}

	/**
	 * 用户文件保存的相对路径
	 * @return String
	 */
	private static String getDirName(){
		String osname = System.getProperty("os.name").toLowerCase();
		if(osname.indexOf("mac") > -1) {
			return "tmp/test";
		}
		return "test";
	}

}
