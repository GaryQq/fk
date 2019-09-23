package com.study.service.permissions.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.study.mapper.permissions.ResourcesMapper;
import com.study.model.permissions.Resources;
import com.study.service.permissions.ResourcesService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

@Service("resourcesService")
public class ResourcesServiceImpl extends BaseService<Resources> implements ResourcesService {
	@Resource
	private ResourcesMapper resourcesMapper;

	@Override
	public List<Resources> queryAll() {
		return resourcesMapper.queryAll();
	}

	@Override
	public List<Resources> loadUserResources(Map<String, Object> map) {
		return resourcesMapper.loadUserResources(map);
	}

	@Override
	public List<Resources> queryResourcesListWithSelected(Integer rid) {
		return resourcesMapper.queryResourcesListWithSelected(rid);
	}

	@Override
	public List<Resources> queryByType(Resources resources, PageBean pageBean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(pageBean)) {
			PageHelper.startPage(pageBean.getPage(), pageBean.getRows());
		}
		return resourcesMapper.queryByType(resources);
	}

	@Override
	public void insertResources(Resources resources) {
		resourcesMapper.insertResources(resources);

	}

	@Override
	public void updateResources(Resources resources) {
		resourcesMapper.updateResources(resources);
	}

	@Override
	public List<Resources> findByUserButtonList(Map<String, Object> param) {
		return resourcesMapper.findByUserButtonList(param);
	}
}
