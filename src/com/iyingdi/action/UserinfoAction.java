package com.iyingdi.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.iyingdi.model.User;
import com.iyingdi.model.Userinfo;
import com.iyingdi.service.UserService;
import com.iyingdi.service.UserinfoService;
import com.iyingdi.utils.EncrypAES;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Controller
@Scope("request")
public class UserinfoAction extends ActionSupport implements Preparable,ModelDriven<Userinfo> {

	/**
	 * 本类是控制层，主要完成用户信息部分的功能
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired private UserinfoService userinfoService;
	@Autowired private UserService userService;
	
	private int id;
	private Userinfo userinfo = new Userinfo();
	private String token;
	private User user;
	
	//用作存储对象方便之后struts转换为json数据传输
	private Map<String,Object> dataMap = new HashMap<String, Object>();
	
	//获取userGroup信息
	public String user() {
		try {
		int userid = Integer.parseInt(EncrypAES.decrypt(this.token));
		
		this.user = this.userService.loadUser(userid);
		
		getDataMap().put("user", user);
		getDataMap().put("success", true);
		
		} catch(Exception e) {
			e.printStackTrace();
			
			getDataMap().put("msg", "获取信息失败");
			getDataMap().put("success", false);
		}
		return "ensure";
	}

	//第一次进入投稿平台，确认授权
	public String ensure() {
 
		try {
			
			int userid = Integer.parseInt(EncrypAES.decrypt(this.token));
			
			
			if (this.userinfoService.loadUserinfoByUserid(userid) != null) {
				this.dataMap.put("msg", "该账号已授权");
				this.dataMap.put("success", false);
			} else {
				this.user = this.userService.loadUser(userid);
				if (this.user.getEmail().equals("") || this.user.getEmail() == null || 
						this.user.getPhone().equals("") || this.user.getPhone() == null ) {
					this.dataMap.put("msg", "该账号未绑定手机邮箱，授权失败");
					this.dataMap.put("success", false);

				} else {
					this.userinfo.setEmail(this.user.getEmail());
					this.userinfo.setTel(this.user.getPhone());
					
					this.userinfoService.save(this.userinfo);
					
					getDataMap().put("success", true);
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			getDataMap().put("msg", "授权失败");
			getDataMap().put("success", false);
		}
		
		return "ensure";
	}
	
	//准备工作
	public void prepare() throws Exception {
		
	}
	
	public Userinfo getModel() {
		
		return this.userinfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Userinfo getUserinfo() {
		return userinfo;
	}
	
	public void setUserinfo(Userinfo userinfo) {
		this.userinfo = userinfo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Map<String,Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String,Object> dataMap) {
		this.dataMap = dataMap;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
}
