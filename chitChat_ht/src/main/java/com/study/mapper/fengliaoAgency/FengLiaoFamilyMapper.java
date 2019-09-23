package com.study.mapper.fengliaoAgency;

import java.util.List;
import java.util.Map;

public interface FengLiaoFamilyMapper {

	List<Map<String, Object>> findCurrentDetailOneToOneList(Map<String, Object> param);

	List<Map<String, Object>> findFamilyOrderDetailList(Map<String, Object> param);

	List<Map<String, Object>> findSkillList(Map<String, Object> param);
}
