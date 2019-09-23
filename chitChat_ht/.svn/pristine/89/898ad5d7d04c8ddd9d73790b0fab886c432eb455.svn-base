package com.study.mapper.permissions;

import com.study.model.permissions.Resources;
import com.study.util.permissions.MyMapper;

import java.util.List;
import java.util.Map;

public interface ResourcesMapper extends MyMapper<Resources> {

	public List<Resources> queryAll();

	public List<Resources> loadUserResources(Map<String, Object> map);

	public List<Resources> queryResourcesListWithSelected(Integer rid);

	public List<Resources> queryByType(Resources resources);

	public void insertResources(Resources resources);

	public void updateResources(Resources resources);

	public List<Resources> findByUserButtonList(Map<String, Object> param);
}