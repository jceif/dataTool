package com.jceif.data.common.mybatis;

import java.util.HashMap;
import java.util.Map;

/**
 * 本类是共通的参数处理类。
 * 
 * 
 */
public class ParameterUtil {
	private static final String CON_PAGEINFO_KEY = "pageAllInfo";
	private static final String CON_SUCCESS_KEY = "success";
	private static final String CON_ERROR_KEY = "error";

	/**
	 * 返回所有参数
	 * 
	 * @param paramMap 一般参数map
	 * @param pageOptionParameter where  排序条件等map
	 * @param page 分页
	 * @return
	 */
	public static Map getParamMap(Map paramMap,
			PageOptionParameter pageOptionParameter, PageParameter page) {
		PageAllInfo pageAllInfo = new PageAllInfo();
		pageAllInfo.setPage(page);

		// 设置可选分页参数
		pageAllInfo.setOptionParam(pageOptionParameter);

		// 设置分页基本参数
		Map<String, Object> parmas = new HashMap<String, Object>();
		parmas.putAll(paramMap);
		parmas.put(CON_PAGEINFO_KEY, pageAllInfo);

		return parmas;
	}

	/**
	 *  取得排序、分页的参数对象
	 * @param paramMap 所有参数的map
	 * @return
	 */
	public static PageAllInfo getPageAllInfo(Map paramMap) {

		PageAllInfo pageAllInfo = (PageAllInfo) paramMap.get(CON_PAGEINFO_KEY);

		return pageAllInfo;
	}

	/**
	 *  获得成功消息map
	 * @param msg 成功消息
	 * @return
	 */
	public static Map getSuccessMsg(String msg) {

		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(CON_SUCCESS_KEY, msg);

		return resultMap;
	}
	
	/**
	 *  活动错误消息map
	 * @param msg 错误消息
	 * @return
	 */
	public static Map getErrorMsg(String msg) {

		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(CON_ERROR_KEY, msg);

		return resultMap;
	}
}
