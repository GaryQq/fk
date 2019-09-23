package com.study.service.permissions;

import java.util.List;
import java.util.Map;

import com.study.model.permissions.Resources;
import com.study.util.bean.PageBean;

public interface ResourcesService extends IService<Resources> {

	public List<Resources> queryAll();

	public List<Resources> loadUserResources(Map<String, Object> map);

	public List<Resources> queryResourcesListWithSelected(Integer rid);

	public List<Resources> queryByType(Resources resources, PageBean pageBean);

	public void insertResources(Resources resources);

	public void updateResources(Resources resources);

	public List<Resources> findByUserButtonList(Map<String, Object> param);
}
