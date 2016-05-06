package com.iyingdi.filter;

import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.iyingdi.action.ArticleAction;
import com.iyingdi.model.User;
import com.iyingdi.service.UserService;
import com.iyingdi.utils.EncrypAES;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class UserFilterInterceptor extends MethodFilterInterceptor {

	/**
	 * 本类主要拦截对某些Action方法的请求操作，从而进行权限管理   Create by RailBu  2016/4/6
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired private UserService userService;
	
	private String userid;					//用户id
	private String name;					//name属性用来标识拦截器的名称，方便控制台的输出
	private int userGroup;					//用户权限
	private String token;					//加密字符串

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		
		//取得请求相关的ActionContext实例 
//		ActionContext ctx = invocation.getInvocationContext(); 
		
//		this.token = "4CDEFFECDAA4364078AEB701F6553961";
		
		
		this.token = ServletActionContext.getRequest().getParameter("token");
		
		ArticleAction articleAction = (ArticleAction) invocation.getAction();
		
		Map<String,Object> dataMap = articleAction.getDataMap();
		
		try {
			
			
			if (this.token == null || this.token.equals("")) {
				dataMap.put("msg", "对不起，您没有该访问权限");
				dataMap.put("success", false);
				
				return "userarticle";
			}
			
			//通过token获取用户的权限
			this.userid = EncrypAES.decrypt(this.token);
			User user = this.userService.loadUser(Integer.parseInt(this.userid));
			this.userGroup = user.getUserGroup();
			
			
			//如果用户是有权限，即可进行操作，否则返回
			if (this.userGroup >= 2) 
			{ 
				return invocation.invoke(); 
			} else {
				System.out.println("不是用户，访问失败");
				
				dataMap.put("msg", "对不起，您没有该访问权限");
				dataMap.put("success", false);
				
				return "userarticle";
			}
		} catch (Exception e) {
			dataMap.put("msg", "对不起，您没有访问权限");
			dataMap.put("success", false);
			
			return "userarticle";
		}
		
		//没有登陆，将服务器提示设置成一个HttpServletRequest属性 
//		ctx.put("tip" , "您还没有登陆，请输入scott,tiger登陆系统"); 
	}


	
	public int getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(int userGroup) {
		this.userGroup = userGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
