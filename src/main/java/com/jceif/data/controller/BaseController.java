package com.jceif.data.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

	 public static enum SerialNumberKey{
	        AC_EMP_ROLE_NO,
	        AC_MENU_NO,
	        AC_PAGE_NO,
	        AC_PAGE_ITEM_NO,
	        AC_PAGE_ITEM_PRIV_NO,
	        AC_ROLE_NO,
	        AC_ROLE_MENU_NO,
	        AC_ROLE_PAGE_PRIV_NO,
	        AT_SYSTEM_PARAM_NO,
	        BS_BELONG_CUST_NO,
	        BS_CALLER_ENTER_NO,
	        BS_COMMUNICATE_INFO_NO,
	        BS_CUSTOMER_NO,
	        BS_OPERATE_RECORD_NO,
	        BS_VISIT_REMIND_NO,
	        OM_EMPLOYEE_NO
	    }



	    protected HttpServletRequest request ;

	    protected HttpServletResponse response ;

	    protected HttpSession session ;

	    public HttpServletRequest getRequest() {
	        return request;
	    }
	    public void setRequest(HttpServletRequest request) {
	        this.request = request;
	    }
	    public HttpServletResponse getResponse() {
	        return response;
	    }
	    public void setResponse(HttpServletResponse response) {
	        this.response = response;
	    }
	    public HttpSession getSession() {
	        return session;
	    }
	    public void setSession(HttpSession session) {
	        this.session = session;
	    }

	    public void writeModelInfo(String userId){
	    	session.setAttribute("userId", userId);
	    }
	
}
