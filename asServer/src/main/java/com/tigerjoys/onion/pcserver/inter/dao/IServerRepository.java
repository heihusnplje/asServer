package com.tigerjoys.onion.pcserver.inter.dao;

import com.tigerjoys.extension.jpa.IJPACustomRepository;
import com.tigerjoys.onion.pcserver.inter.entity.ServerEntity;

/**
 * 数据库  流媒体服务信息[t_server]表 DAO操作类
 * @author chengang
 * @Date 2018-07-17 10:16:08
 *
 */
public interface IServerRepository extends IJPACustomRepository<ServerEntity, Integer> {
	
	/**
	 * 根据 端口号, IP地址 获得对象
	 * @param port - int 端口号
	 * @param ip - String IP地址
	 * @return ServerEntity
	 * @throw Exception
	 *
	 */
	public ServerEntity findByPortAndIp(int port, String ip);
	
}
