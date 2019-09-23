package com.study.model.permissions;

import javax.persistence.*;

import java.io.Serializable;

@Table(name = "CHITCHAT_USER")
public class User implements Serializable {

	private static final long serialVersionUID = -8736616045315083846L;

	// 主键由数据库自动生成（主要是自动增长型）
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	/**
	 * 是否启用
	 */
	@Column(name = "ENABLE")
	private Integer enable;

	@Column(name = "NAME")
	private String name;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "MARK")
	private String mark;

	@Column(name = "PHONE")
	private String phone;

	@Column(name = "DEPT_ID")
	private Integer deptId;

	/**
	 * 账户类型0企业账户1私人账户
	 */
	@Column(name = "USER_CODE")
	private Integer userCode;

	/**
	 * 关联疯狂用户
	 */
	@Column(name = "FK_USER_NAME")
	private String fkUserName;

	@Transient
	private String deptName;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	/**
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取是否启用
	 *
	 * @return enable - 是否启用
	 */
	public Integer getEnable() {
		return enable;
	}

	/**
	 * 设置是否启用
	 *
	 * @param enable
	 *            是否启用
	 */
	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	/**
	 * 账号类型0企业账号1私人账号
	 * 
	 * @return userCode
	 */
	public Integer getUserCode() {
		return userCode;
	}

	public void setUserCode(Integer userCode) {
		this.userCode = userCode;
	}

	/**
	 * 关联账号
	 * 
	 * @return fkUserName
	 */
	public String getFkUserName() {
		return fkUserName;
	}

	public void setFkUserName(String fkUserName) {
		this.fkUserName = fkUserName;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", enable=" + enable + ", name="
				+ name + ", address=" + address + ", mark=" + mark + ", phone=" + phone + ", deptId=" + deptId
				+ ", userCode=" + userCode + ", fkUserName=" + fkUserName + ", deptName=" + deptName + "]";
	}

}