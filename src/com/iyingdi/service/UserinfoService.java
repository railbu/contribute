package com.iyingdi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iyingdi.dao.UserinfoDao;
import com.iyingdi.model.Userinfo;

@Component
public class UserinfoService {

	@Autowired private UserinfoDao userinfoDao;
	
	public void save(Userinfo userinfo) {
		this.userinfoDao.save(userinfo);
	}
	
	public Userinfo loadUserinfoByUserid(int userid) {
		return this.userinfoDao.loadUserinfoByUserid(userid);
	}
}
