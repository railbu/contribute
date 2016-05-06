package com.iyingdi.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import com.iyingdi.model.Userinfo;

@Component
public class UserinfoDao extends HibernateDaoSupport {
	
	/*
	 * 本类主要与数据库交互用户信息的数据
	 */
	
	//加载用户信息对象
	public Userinfo loadUserinfo(int id) {
		return (Userinfo) super.getSession().load(Userinfo.class, id);
	}
	
	//通过用户ID加载用户详细信息
	public Userinfo loadUserinfoByUserid(int userid) {
		return (Userinfo) super.getSession()
				.createCriteria(Userinfo.class)
				.add(Restrictions.eq("userid",userid))
				.uniqueResult();
	}
	
	//新建或保存用户信息
	public void save(Userinfo userinfo) {
		try {
			Session session = super.getSession();
			
			session.saveOrUpdate(userinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
