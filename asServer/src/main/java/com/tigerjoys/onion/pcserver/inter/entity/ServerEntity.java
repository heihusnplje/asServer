package com.tigerjoys.onion.pcserver.inter.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tigerjoys.extension.jpa.BaseEntity;

/**
 * 数据库中  流媒体服务信息[t_server] 表对应的实体类
 * @author chengang
 * @Date 2018-07-17 10:16:08
 *
 */
@Entity
@Table(name="t_server")
public class ServerEntity extends BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",nullable=false,length=11)
	private Integer id;
	
	/**
	 * 创建时间
	 */
	@Column(name="createDate",nullable=false)
	private Date createdate;
	
	/**
	 * 修改时间
	 */
	@Column(name="updateDate",nullable=false)
	private Date updatedate;
	
	/**
	 * 服务名称
	 */
	@Column(name="serverName",nullable=false,length=100)
	private String servername;
	
	/**
	 * IP地址
	 */
	@Column(name="ip",nullable=false,length=15)
	private String ip;
	
	/**
	 * 端口号
	 */
	@Column(name="port",nullable=false,length=5)
	private Integer port;
	
	/**
	 * 可推送路由数
	 */
	@Column(name="route",nullable=false,length=3)
	private Integer route;
	
	/**
	 * 剩余推送路由
	 */
	@Column(name="currRoute",nullable=false,length=3)
	private Integer currroute;
	
	/**
	 * 0普通服务,1优质服务
	 */
	@Column(name="type",nullable=false,length=1)
	private Integer type;
	
	/**
	 * 权重,按照权重进行排序和优先
	 */
	@Column(name="weight",nullable=false,length=4)
	private Integer weight;
	
	/**
	 * 0暂停,1正常
	 */
	@Column(name="status",nullable=false,length=1)
	private Integer status;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	
	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}
	
	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
	public Integer getRoute() {
		return route;
	}

	public void setRoute(Integer route) {
		this.route = route;
	}
	
	public Integer getCurrroute() {
		return currroute;
	}

	public void setCurrroute(Integer currroute) {
		this.currroute = currroute;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Override
	public Long primaryKey() {
		return id.longValue();
	}
	
}