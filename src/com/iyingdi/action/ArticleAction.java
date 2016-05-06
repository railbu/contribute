package com.iyingdi.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.iyingdi.model.Article;
import com.iyingdi.model.User;
import com.iyingdi.model.Userinfo;
import com.iyingdi.service.ArticleService;
import com.iyingdi.service.UserService;
import com.iyingdi.service.UserinfoService;
import com.iyingdi.utils.EncrypAES;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Controller
@Scope("request")
public class ArticleAction extends ActionSupport implements Preparable, ModelDriven<Article> {

	/**
	 * 本类主要完成有关稿件请求的一切功能
	 */
	private static final long serialVersionUID = 1L;

	@Autowired private ArticleService articleService;
	@Autowired private UserService userService;
	@Autowired private UserinfoService userinfoService;
	
	private int id;
	private int[] counts = null;
	private String token;
	private Article article = new Article();
	private String articleType;
	private String module;
	private int operation;
	private List<Article> articleList;
	private Userinfo userinfo = null;
	private User user = null;
	private String adminRemark = null;
	private int level = 0;
	private String title;
	private String content;
	private String first;
	private HashMap<String, String> modules = new HashMap<String, String>(){

		private static final long serialVersionUID = 6316359297284386696L;

	{
		put("top", "头条");
		put("yingdi", "营地");
		put("hearthstone", "炉石传说");
		put("sgstcg", "阵面对决");
		put("lego", "乐高LEGO");
		put("anli", "好物安利");
	}};
//	private String[] modules = {"炉石传说","皇室战争","万智牌","守望先锋","电子游戏","桌面游戏","好物安利","乐高LEGO","阵面对决","趣闻网事"};
	private HashMap<String, String> articleTypes = new HashMap<String, String>(){
		
		private static final long serialVersionUID = 2360843543814923114L;

	{
		put("published", "已发布");
		put("tobepublish", "待发布");
		put("handle", "待处理");
		put("return", "退回");
	}};
//	private String[] articleTypes = {"已发布","待发布","待处理","退回"};
	
	//用作存储对象方便之后struts转换为json数据传输
	private Map<String,Object> dataMap = new HashMap<String, Object>();
	
	//判断是否第一次进入投稿平台
	public String isfirst() {
		try {
			
			int userid = Integer.parseInt(EncrypAES.decrypt(this.token));
			
			this.userinfo = this.userinfoService.loadUserinfoByUserid(userid);
			
			if (this.userinfo == null) {
				dataMap.put("first", true);
				dataMap.put("success", true);
			} else {
				dataMap.put("first", false);
				dataMap.put("success", true);
			}
		} catch(Exception e) {
			e.printStackTrace();
			dataMap.put("msg", "列表请求失败");
			dataMap.put("success", false);
		}
		
		return "userarticle";
	}
	
	//获取用户个人稿件列表
	public String list() {
		try{
	
			// dataMap中的数据将会被Struts2转换成JSON字符串，所以这里要先清空其中的数据
	        dataMap = new HashMap<String, Object>();
			
			int userid = Integer.parseInt(EncrypAES.decrypt(this.token));
			this.user = this.userService.loadUser(userid);
			
			if (this.user.getUserGroup() == 2) {
				dataMap.put("admin", false);
			} else {
				dataMap.put("admin", true);
			}
			
			this.articleList = this.articleService.userArticleList(userid);
			
			dataMap.put("article", this.articleList);
			
			dataMap.put("success", true);
		
		} catch (Exception e) {
			
			dataMap.put("msg", "列表请求失败");
			dataMap.put("success", false);
		}
		
		return "userarticle";
	}
	
	//查看、编辑文章详情
	public String info() {
		try {
			int userid = Integer.valueOf(EncrypAES.decrypt(this.token));
			
			this.user = this.userService.loadUser(userid);
			
			this.article = this.articleService.loadArticle(this.id);
			
			if (this.article == null) {
				dataMap.put("msg", "没有该稿件");
				dataMap.put("success", false);
				return "userarticle";
			}
	        
			String s = ServletActionContext.getRequest().getParameter("id");
			if (s == null || s.equals("")) {
				dataMap.put("msg", "请选择要查看稿件ID");
				dataMap.put("success", false);
				
				return "userarticle";
			}
			
			if (this.user.getUserGroup() == 2) {
				if (userid == this.article.getUserid()) {
					
					dataMap.put("article", this.article);
					dataMap.put("admin", false);
					dataMap.put("success", true);
				} else {
					
					dataMap.put("msg", "你没有权限查看该稿件");
					dataMap.put("success", false);
					
					return "userarticle";
				}
			} else if (this.user.getUserGroup() > 2) {
				if (userid == this.article.getUserid()) {
					
					dataMap.put("article", this.article);
					dataMap.put("admin", true);
					if (this.article.getState().equals("草稿") || this.article.getState().equals("被退回")) {
						dataMap.put("edit", true);
					} else {
						dataMap.put("edit", false);
					}
					dataMap.put("success", true);
					
				} else {
					if (this.article.getState().equals("草稿")) {
						
						dataMap.put("msg", "您没有权限查看稿件");
						dataMap.put("success", false);
						
						return "userarticle";
					} else {
						dataMap.put("article", this.article);
						dataMap.put("admin", true);
						dataMap.put("edit", false);
						dataMap.put("success", true);
					}
				}
			} else {
				dataMap.put("msg", "你没有权限查看该稿件");
				dataMap.put("success", false);
				
				return "userarticle";
			}
		
		} catch (Exception e) {
			
			dataMap.put("msg", "没有要查看的稿件");
			dataMap.put("success", false);
		}
		
		
		return "userarticle";
	}
	
	//用户保存草稿
	public String draft() {
		try {
			
			dataMap = new HashMap<String, Object>();
			
			if (this.id != 0) {
				this.article.setId(id);
				
				if (this.articleService.loadArticleUserid(id) != Integer.parseInt(EncrypAES.decrypt(this.token))) {
					dataMap.put("msg", "您没有该稿件编辑权限");
					dataMap.put("success", false);
					return "userarticle";
				}
				
				if (this.article.getState().equals("已提交") || this.article.getState().equals("审核通过") || this.article.getState().equals("已发布")) {
					dataMap.put("msg", "对不起，该稿件已提交，无法编辑");
					dataMap.put("success", false);
					return "userarticle";
				}
				
				
				if (this.articleService.loadArticleState(id).equals("已提交")) {
					this.article.setState("已提交");
					this.article.setCheckstate("待处理");
					dataMap.put("msg", "您的稿件已被编辑撤销退回，稿件将保存为你最新修改的情况");
				}
				
			}
			
			if (this.article.getState() == null || this.article.getState().equals("") || this.article.getState().equals("被退回"))
				this.article.setState("草稿");
				
			if (this.article == null) {
				this.article = new Article();
			}
			
			int userid = Integer.parseInt(EncrypAES.decrypt(this.token));
			

			this.user = this.userService.loadUser(userid);
			this.userinfo = this.userinfoService.loadUserinfoByUserid(userid);
			this.article.setUserid(userid);
			this.article.setUsername(user.getUsername());
			
			
			
			
			this.article.setAdminRemark(null);
			
			if ((this.article.getCreateTime() == null || this.article.getCreateTime().equals("")) && this.id == 0) {
				this.article.setCreateTime(new Date().getTime());
			}
			if (this.article.getAuthor() == null || this.article.getAuthor().equals("")) {
				this.article.setAuthor(this.user.getUsername());
			}
			if (this.article.getEmail() == null || this.article.getEmail().equals("")) {
				this.article.setEmail(this.userinfo.getEmail());
			}
			if (this.article.getTel() == null || this.article.getTel().equals("")) {
				this.article.setTel(this.userinfo.getTel());
			}
			
			this.article.setCheckstate("");
			this.article.setDraftTime(new Date().getTime());
			
//			if (a != null) {
//				a = this.article;
//				this.articleService.save(a);
//			} else {
				this.articleService.save(this.article);
//			}
			
			
			dataMap.put("success", true);
		
		} catch (Exception e) {
			e.printStackTrace();
			dataMap.put("msg", "填入信息有误");
			dataMap.put("success", false);
		}
		
		return "userarticle";
	}
	
	//用户删除稿件
	public String delete() {
//		this.id = Integer.parseInt(ServletActionContext.getRequest().getParameter("id"));
		try {
			
			dataMap = new HashMap<String, Object>();
			
			this.article = this.articleService.loadArticle(this.id);
			
			if (this.article == null) {
				dataMap.put("msg", "没有要删除的稿件");
				dataMap.put("success", false);
				
				return "userarticle";
			}
			
			if (this.article.getState().equals("已提交")  || this.article.getState().equals("审核通过") || this.article.getState().equals("已发布") || this.article.getState().equals("撤销退回")) {
				dataMap.put("msg", "该稿件已提交，删除失败");
				dataMap.put("success", false);
				
				return "userarticle";
			}
		
			int userid = Integer.parseInt(EncrypAES.decrypt(this.token));
			
			if (userid == this.article.getUserid()) {
				this.articleService.delete(this.article);
				dataMap.put("success", true);
			} else {
				dataMap.put("msg", "你没有权限删除该稿件");
				dataMap.put("success", false);
			}
			
		} catch (Exception e) {
			
			dataMap.put("msg", "删除失败");
			dataMap.put("success", false);
		}
		
		return "userarticle";
	}
	
	//用户提交稿件
	public String submit() {
		try {	
		
			dataMap = new HashMap<String, Object>();
			
			if (this.id != 0 && this.title == null) {
				this.article.setId(this.id);
				
				this.article = this.articleService.loadArticle(id);
				
				if (this.articleService.loadArticleUserid(id) != Integer.parseInt(EncrypAES.decrypt(this.token))) {
					dataMap.put("msg", "您没有该稿件提交权限");
					dataMap.put("success", false);
					return "userarticle";
				}
				
				if (this.article.getState().equals("已提交") || this.article.getState().equals("审核通过") || this.article.getState().equals("已发布")) {
					dataMap.put("msg", "对不起，该稿件已提交，请勿重复提交");
					dataMap.put("success", true);
					
					return "userarticle";
				}
			}
			
			if (this.id != 0 && this.title != null) {
				this.article.setId(this.id);
				
				if (this.articleService.loadArticleUserid(id) != Integer.parseInt(EncrypAES.decrypt(this.token))) {
					dataMap.put("msg", "您没有该稿件提交权限");
					dataMap.put("success", false);
					return "userarticle";
				}
				
				if (this.article.getState().equals("已提交") || this.article.getState().equals("审核通过") || this.article.getState().equals("已发布")) {
					dataMap.put("msg", "对不起，该稿件已提交，请勿重复提交");
					dataMap.put("success", true);
					
					return "userarticle";
				}
				
				if (this.articleService.loadArticleState(id).equals("已提交")) {
					dataMap.put("msg", "您的稿件已被编辑撤销退回，稿件将保存为你最新修改的情况");
				}
			}
			
			if (this.article == null) {
				this.article = new Article();
			}
		
			int userid = Integer.parseInt(EncrypAES.decrypt(this.token));
			
			
			
			this.user = this.userService.loadUser(userid);
			this.userinfo = this.userinfoService.loadUserinfoByUserid(userid);
			
			this.article.setUserid(userid);
			this.article.setUsername(user.getUsername());
			
			this.article.setState("已提交");
			this.article.setCheckstate("待处理");
			
			if ((this.article.getCreateTime() == null || this.article.getCreateTime().equals("")) && this.id == 0) {
				this.article.setCreateTime(new Date().getTime());
			}
			if (this.article.getAuthor() == null || this.article.getAuthor().equals("")) {
				this.article.setAuthor(this.user.getUsername());
			}
			if (this.article.getEmail() == null || this.article.getEmail().equals("")) {
				this.article.setEmail(this.userinfo.getEmail());
			}
			if (this.article.getTel() == null || this.article.getTel().equals("")) {
				this.article.setTel(this.userinfo.getTel());
			}
			
			this.article.setSubmitTime(new Date().getTime());
			
			this.articleService.save(this.article);
			
			dataMap.put("success", true);
		
		} catch (Exception e) {
			e.printStackTrace();
			
			dataMap.put("msg", "填入信息有误");
			dataMap.put("success", false);
		}
		
		return "userarticle";
	}
	
	//编辑查看各稿件列表
	public String table() {
		try {
			
			// dataMap中的数据将会被Struts2转换成JSON字符串，所以这里要先清空其中的数据  
	        dataMap = new HashMap<String, Object>();  
	        
			if (articleType == null || articleType.equals("")) {
				dataMap.put("msg", "请选择查看稿件列表的类型");
				dataMap.put("success", false);
				
				return "userarticle";
			}
		
			this.counts = new int[4];
		
			if (this.module == null) {
				this.counts[0] = this.articleService.count("已发布");
				this.counts[1] = this.articleService.count("待发布");
				this.counts[2] = this.articleService.count("待处理");
				this.counts[3] = this.articleService.count("退回");
			} else {
				
				
				this.counts[0] = this.articleService.count(this.articleTypes.get("published"), this.modules.get(module));
				this.counts[1] = this.articleService.count(this.articleTypes.get("tobepublish"), this.modules.get(module));
				this.counts[2] = this.articleService.count(this.articleTypes.get("handle"), this.modules.get(module));
				this.counts[3] = this.articleService.count(this.articleTypes.get("return"), this.modules.get(module));
			}
			
			if (this.module == null) {
				
				this.articleList = this.articleService.articleList(this.articleTypes.get(articleType));
				
			} else if (this.articleType != null && this.module != null) {
				
				this.articleList = this.articleService.articleList(this.articleTypes.get(articleType), this.modules.get(module));
			} else {
				dataMap.put("msg", "没有你要查询的稿件列表");
				dataMap.put("success", false);
				
				return "userarticle";
			}
			
			dataMap.put("article", this.articleList);
			dataMap.put("published", this.counts[0]);
			dataMap.put("tobepublish", this.counts[1]);
			dataMap.put("handle", this.counts[2]);
			dataMap.put("return", this.counts[3]);
			dataMap.put("success", true);
			

		} catch (Exception e) {
			
			dataMap.put("msg", "没有你要查询的稿件列表");
			dataMap.put("success", false);
		}
	
		
		return "userarticle";
	}
	
	//编辑操作稿件
	public String operation() {
		try {

			String s = ServletActionContext.getRequest().getParameter("operation");
	
			// dataMap中的数据将会被Struts2转换成JSON字符串，所以这里要先清空其中的数据  
	        dataMap = new HashMap<String, Object>();  
	        
	        this.article = this.articleService.loadArticle(id);
	        
	        if (this.article == null) {
	
				dataMap.put("msg", "没有你要操作的稿件");
				dataMap.put("success", false);
				
				return "userarticle";
			}
	        
			if (s == null || s.equals("")) {
				dataMap.put("msg", "请选择操作的类型");
				dataMap.put("success", false);
				
				return "userarticle";
			}
			
			if (this.article.getState().equals("草稿")) {
				dataMap.put("msg", "对不起，您没有该操作权限");
				dataMap.put("success", false);
				
				return "userarticle";
			}
		
			switch (this.operation) {
			case 0:
				this.article.setCheckstate("退回");
				this.article.setState("被退回");
				this.article.setReturnTime(new Date().getTime());
				this.article.setAdminRemark(this.adminRemark);
				break;
			case 1:
				this.article.setLevel(this.getLevel());
				this.article.setAdminRemark(this.adminRemark);
				this.article.setPassTime(new Date().getTime());
				this.article.setCheckstate("待发布");
				this.article.setState("审核通过");
				break;
			case 2:
				this.article.setAdminRemark(this.adminRemark);
				break;
			case 3:
				this.article.setLevel(this.getLevel());
				this.article.setAdminRemark(this.adminRemark);
				this.article.setPassTime(null);
				this.article.setCheckstate("待处理");
				this.article.setState("已提交");
				break;
			case 4:
				this.article.setCheckstate("待处理");
				this.article.setState("已提交");
				this.article.setAdminRemark(this.adminRemark);

				break;
			default:
				dataMap.put("msg", "请选择正确的操作类型");
				dataMap.put("success", false);
				
				return "userarticle";
			}
			
			this.articleService.save(this.article);
				
			dataMap.put("success", true);
		
		} catch (Exception e) {
			
			dataMap.put("msg", "填入信息有误");
			dataMap.put("success", false);
		}
		
		return "userarticle";
	}
	
	//编辑保存稿件
	public String save() {
		try {
			
			if (this.id == 0) {
				dataMap.put("msg", "保存信息有误，保存失败");
				dataMap.put("success", false);
			}
			
			this.article = this.articleService.loadArticle(id);
			
			if (this.article.getState().equals("草稿") || this.article.getState().equals("被退回")) {
				dataMap.put("msg", "您没有权限操作该稿件");
				dataMap.put("success", false);
			}
			
			this.article.setContent(this.content);
			
			this.articleService.save(article);
			
			dataMap.put("success", true);
			
		} catch(Exception e) {
			e.printStackTrace();
			
			dataMap.put("msg", "保存失败");
			dataMap.put("success", false);
		}
		
		return "userarticle";
	}
	
	public Article getModel() {
		return this.article;
	}
	
	//做一些准备工作
	public void prepare() throws Exception {
		
		if (this.article == null && this.id != 0) {
			this.article = this.articleService.loadArticle(this.id);
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Map<String,Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String,Object> dataMap) {
		this.dataMap = dataMap;
	}

	public List<Article> getArticleList() {
		return articleList;
	}


	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	public String getArticleType() {
		return articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	public int[] getCounts() {
		return counts;
	}

	public void setCounts(int[] counts) {
		this.counts = counts;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAdminRemark() {
		return adminRemark;
	}

	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

}
