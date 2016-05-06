package com.iyingdi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iyingdi.dao.ArticleDao;
import com.iyingdi.model.Article;

@Component
public class ArticleService {

	@Autowired private ArticleDao articleDao;
	
	public Article loadArticle(int id) {
		return this.articleDao.loadArticle(id);
	}
	
	public String loadArticleState(int id) {
		return this.articleDao.loadArticleState(id);
	}
	
	public int loadArticleUserid(int id) {
		return this.articleDao.loadArticleUserid(id);
	}
	
	public void save(Article article) {
		this.articleDao.save(article);
	}
	
	public void delete(Article article) {
		this.articleDao.delete(article);
	}
	
	public List<Article> userArticleList(int userid) {
		return this.articleDao.userArticleList(userid);
	}
	
	public int count(String checkstate) {
		return this.articleDao.count(checkstate);
	}
	
	public int count(String checkstate, String module) {
		return this.articleDao.count(checkstate, module);
	}
	
	public List<Article> articleList(String checkstate) {
		return this.articleDao.articleList(checkstate);
	}
	
	public List<Article> articleList(String checkstate, String module) {
		return this.articleDao.articleList(checkstate, module);
	}
}
