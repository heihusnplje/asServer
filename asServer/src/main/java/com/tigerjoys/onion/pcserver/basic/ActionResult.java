package com.tigerjoys.onion.pcserver.basic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.tigerjoys.onion.pcserver.enums.IErrorCodeEnum;

/**
 * 返回结果
 * @author chengang
 *
 */
public class ActionResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//返回码
	private int code;
	//返回描述
	private String codemsg;
	//返回的数据
	private Object data;
	//分页戳
	private Object stamp;
	//是否有下一页
	private Boolean nextPage;
	
	/**
	 * 返回ActionResult
	 * @param code - 返回码
	 * @param codemsg - 返回描述
	 * @param data - 返回的数据
	 * @param stamp - 分页戳
	 * @return ActionResult
	 */
	public static ActionResult getResult(int code , String codemsg , Object data , Object stamp){
		ActionResult result = new ActionResult();
		result.code = code;
		result.codemsg = codemsg;
		result.data = data;
		result.stamp = stamp;
		
		return result;
	}
	
	/**
	 * 返回成功
	 * @return ActionResult
	 */
	public static ActionResult success() {
		return success(null , null , null);
	}
	
	/**
	 * 返回成功，并且指定data下的key和value数据
	 * @param key - String
	 * @param value - Object
	 * @return ActionResult
	 */
	public static ActionResult successJson(String key , Object value) {
		ActionResult result = success(null , null , null);
		result.addJsonData(key, value);
		
		return result;
	}
	
	/**
	 * 返回成功
	 * @param data - 返回的数据
	 * @return ActionResult
	 */
	public static ActionResult success(Object data) {
		return success(data , null , null);
	}
	
	/**
	 * 返回成功
	 * @param data - 返回的数据
	 * @param successMsg - 自定义文案
	 * @return ActionResult
	 */
	public static ActionResult success(Object data,String successMsg) {
		 return success(data , successMsg , null , null);
	}
	
	/**
	 * 返回成功
	 * @param data - 返回的数据
	 * @param stamp - 分页戳
	 * @param nextPage - 是否有下一页
	 * @return ActionResult
	 */
	public static ActionResult success(Object data , Object stamp , Boolean nextPage){
		ActionResult result = getResult(0 , "成功" , data , stamp);
		result.setNextPage(nextPage);
		
		return result;
	}
	
	/**
	 * 返回成功
	 * @param data - 返回的数据
	 * @param successMsg - 自定义文案
	 * @param stamp - 分页戳
	 * @param nextPage - 是否有下一页
	 * @return ActionResult
	 */
	public static ActionResult success(Object data , String successMsg , Object stamp , Boolean nextPage){
		ActionResult result = getResult(0 , successMsg , data , stamp);
		result.setNextPage(nextPage);
		
		return result;
	}
	
	/**
	 * 返回默认的错误失败
	 * @return - ActionResult
	 */
	public static ActionResult fail() {
		return getResult(1 , "系统异常" , null , null);
	}
	
	/**
	 * 返回错误信息
	 * @param errorCode - 错误枚举
	 * @return ActionResult
	 */
	public static ActionResult fail(IErrorCodeEnum errorCode) {
		return getResult(errorCode.getCode() , errorCode.getDesc() , null , null);
	}
	
	/**
	 * 返回错误信息
	 * @param errorCode - 错误枚举
	 * @param data - 返回数据
	 * @return ActionResult
	 */
	public static ActionResult fail(IErrorCodeEnum errorCode , Object data) {
		return getResult(errorCode.getCode() , errorCode.getDesc() , data , null);
	}
	
	/**
	 * 返回错误信息，并且指定data返回值的key value 的值
	 * @param errorCode - 错误枚举
	 * @param key - String
	 * @param value - Object
	 * @return ActionResult
	 */
	public static ActionResult failJson(IErrorCodeEnum errorCode , String key , Object value) {
		ActionResult result = getResult(errorCode.getCode() , errorCode.getDesc() , null , null);
		result.addJsonData(key, value);
		return result;
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @param codemsg - 返回描述
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , String codemsg) {
		return getResult(code , codemsg , null , null);
	}
	
	/**
	 * 返回失败
	 * @param code - 返回码
	 * @param codemsg - 返回描述
	 * @param data - 返回的数据
	 * @return ActionResult
	 */
	public static ActionResult fail(int code , String codemsg , Object data) {
		return getResult(code , codemsg , data , null);
	}
	
	/**
	 * 返回失败，并且指定data返回值的key value 的值
	 * @param code - 返回码
	 * @param codemsg - 返回描述
	 * @param key - String
	 * @param value - Object
	 * @return ActionResult
	 */
	public static ActionResult failJson(int code , String codemsg , String key , Object value) {
		ActionResult result = getResult(code, codemsg, null, null);
		result.addJsonData(key, value);
		
		return result;
	}
	
	/**
	 * 返回一个错误视图，网页视图
	 * @param response - HttpServletResponse
	 * @param err - 错误原因
	 * @return ModelAndView
	 */
	public static ModelAndView errorModel(HttpServletResponse response ,String err){
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		
		ModelAndView model =  new ModelAndView("error");
		model.addObject("msg", err);
		
		return model;
	}
	
	/**
	 * 返回一个错误信息视图，非网页的视图
	 * @param response - HttpServletResponse
	 * @param code - 错误码
	 * @param err - 错误信息
	 * @return ModelAndView
	 */
	public static ModelAndView msgModel(HttpServletResponse response , int code , String err) {
		response.setStatus(code);
		
		ModelAndView model =  new ModelAndView("msg");
		model.addObject("msg", err);
		
		return model;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getCodemsg() {
		return codemsg;
	}

	public void setCodemsg(String codemsg) {
		this.codemsg = codemsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * 必须保证data=null或者data是Map类型
	 * @param key - String
	 * @param value - Object
	 */
	@SuppressWarnings("unchecked")
	public void addJsonData(String key , Object value) {
		if(this.data == null) {
			this.data = new HashMap<String , Object>();
		}
		((Map<String , Object>)this.data).put(key , value);
	}

	public Object getStamp() {
		return stamp;
	}

	public void setStamp(Object stamp) {
		this.stamp = stamp;
	}

	public Boolean getNextPage() {
		return nextPage;
	}

	public void setNextPage(Boolean nextPage) {
		this.nextPage = nextPage;
	}

}
