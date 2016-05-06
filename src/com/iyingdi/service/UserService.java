package com.iyingdi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iyingdi.dao.UserDao;
import com.iyingdi.model.User;

@Component
public class UserService {

	@Autowired private UserDao userDao;
	
	public User loadUser(int id) {
		return this.userDao.loadUser(id);
	}
}
