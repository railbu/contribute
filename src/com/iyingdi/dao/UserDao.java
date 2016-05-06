package com.iyingdi.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import com.iyingdi.model.User;

@Component
public class UserDao extends HibernateDaoSupport {

	//加载用户稿件详情
	public User loadUser(int id) {
		return (User) super.getSession().load(User.class, id);
	}
	
	
	
}
