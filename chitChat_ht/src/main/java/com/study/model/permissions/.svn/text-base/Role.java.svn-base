package com.study.model.permissions;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "CHITCHAT_ROLE")
public class Role implements Serializable {
	private static final long serialVersionUID = -6140090613812307452L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "ROLEDESC")
	private String roledesc;
	
	@Transient
	private Integer selected;
	
	@Transient
	private String roleIds;

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
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
	 * @return roleDesc
	 */
	public String getRoledesc() {
		return roledesc;
	}

	/**
	 * @param roledesc
	 */
	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}
}