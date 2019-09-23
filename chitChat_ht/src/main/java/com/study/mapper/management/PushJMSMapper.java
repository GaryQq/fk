package com.study.mapper.management;

import java.util.List;
import java.util.Map;

public interface PushJMSMapper {

	List<Map<String, Object>> findPushJmsView(Map<String, Object> param);

	Map<String, Object> findPushJMSEntity(Integer id);

	int findNickNameUserEntity(String nick_name);

	void insertPushJMSEntity(Map<String, Object> param);

	void enablePushJMSEntity(Map<String, Object> param);

}
