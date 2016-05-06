package com.iyingdi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="contribute_article")
public class Article {

	/*
	 * 本类是稿件的对象类
	 */
	
	private int id;
	private int userid;					//用户id
	private int level;					//稿件等级
	private String title;				//标题
	private String author;				//作者
	private String username;			//用户名
	private String state;				//状态
	private String checkstate;			//审核状态
	private String userRemark;			//用户备注
	private String adminRemark;			//管理员备注
	private String module;				//模块
	private String type;				//类型(原创、转载、翻译)
	private String source;				//来源
	private String cover;				//封面
	private String label;				//标签
	private String content;				//内容
	
	private String email;				//邮箱
	private String tel;					//手机号
	
	private Long createTime;			//撰写时间
	private Long submitTime;			//提交时间
	private Long draftTime;				//存稿时间
	private Long passTime;				//通过时间
	private Long returnTime;			//退回时间
	private Long publishTime;			//发布时间
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCheckstate() {
		return checkstate;
	}
	public void setCheckstate(String checkstate) {
		this.checkstate = checkstate;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	public String getAdminRemark() {
		return adminRemark;
	}
	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Lob
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Long getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Long submitTime) {
		this.submitTime = submitTime;
	}
	public Long getDraftTime() {
		return draftTime;
	}
	public void setDraftTime(Long draftTime) {
		this.draftTime = draftTime;
	}
	public Long getPassTime() {
		return passTime;
	}
	public void setPassTime(Long passTime) {
		this.passTime = passTime;
	}
	public Long getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Long returnTime) {
		this.returnTime = returnTime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Long getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Long publishTime) {
		this.publishTime = publishTime;
	}
	
	
}
