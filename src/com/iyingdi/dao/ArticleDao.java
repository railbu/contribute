package com.iyingdi.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import com.iyingdi.model.Article;

@Component
public class ArticleDao extends HibernateDaoSupport {

	/*
	 * 本类主要处理article数据的访问
	 */
	
	//加载用户稿件详情
	public Article loadArticle(int id) {
		return (Article) super.getSession().get(Article.class, id);
	}
	
	//获取当前稿件state
	public String loadArticleState(int id) {
		try {
			String hql="select state from Article where id = ?";
			Session session = super.getSession();

			return ((String) session
					.createQuery(hql)
					.setParameter(0, id)
					.uniqueResult());
		} catch (Exception e) {

			e.printStackTrace();
			return "error";
		}
	}
	
	//获取当前稿件state
		public int loadArticleUserid(int id) {
			try {
				String hql="select userid from Article where id = ?";
				Session session = super.getSession();

				return ((int) session
						.createQuery(hql)
						.setParameter(0, id)
						.uniqueResult());
			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}
	
	//新建或保存稿件
	public void save(Article article) {
		super.getSession().saveOrUpdate(article);
	}
	
	//删除稿件
	public void delete(Article article) {
		super.getSession().delete(article);
	}
	
	//获取用户稿件列表
	@SuppressWarnings("unchecked")
	public List<Article> userArticleList(int userid) {
		List<Article> articles;
		articles = super.getSession()
				.createCriteria(Article.class)
				.add(Restrictions.eq("userid",userid))
				.list();
		return articles;
	}
	
	//获取某种稿件数量
	public int count(String checkstate) {
		try {
			String hql="select count(*) from Article where checkstate = ?";
			Session session = super.getSession();

			return ((Long) session
					.createQuery(hql)
					.setParameter(0, checkstate)
					.uniqueResult()).intValue();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int count(String checkstate, String module) {
		try {
			String hql="select count(*) from Article where checkstate = ? and module = ?";
			Session session = super.getSession();

			return ((Long) session
					.createQuery(hql)
					.setParameter(0, checkstate)
					.setParameter(1, module)
					.uniqueResult()).intValue();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	//编辑获取某稿件列表
	@SuppressWarnings("unchecked")
	public List<Article> articleList(String checkstate) {
		List<Article> articles;
		articles = super.getSession()
				.createCriteria(Article.class)
				.add(Restrictions.eq("checkstate",checkstate))
				.list();
		
		return articles;
	}
	
	//编辑获取某稿件某模块列表
		@SuppressWarnings("unchecked")
		public List<Article> articleList(String checkstate, String module) {
			List<Article> articles;
			articles = super.getSession()
					.createCriteria(Article.class)
					.add(Restrictions.eq("checkstate",checkstate))
					.add(Restrictions.eq("module", module))
					.list();
			
			return articles;
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
