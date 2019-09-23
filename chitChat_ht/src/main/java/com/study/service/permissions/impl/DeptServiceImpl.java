package com.study.service.permissions.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.study.mapper.permissions.DeptMapper;
import com.study.model.permissions.Dept;
import com.study.service.permissions.DeptService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

@Service("deptService")
public class DeptServiceImpl extends BaseService<Dept> implements DeptService {
	@Autowired
	private DeptMapper deptMapper;

	@Override
	public List<Dept> selectAllDept(Dept dept, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		List<Dept> selectAllDept = deptMapper.selectAllDept(dept);
		return selectAllDept;
	}

	@Override
	public void insertDept(Dept dept) {
		deptMapper.insertDept(dept);
	}

}
