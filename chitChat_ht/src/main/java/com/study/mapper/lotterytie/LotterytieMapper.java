package com.study.mapper.lotterytie;

import java.util.List;
import java.util.Map;

public interface LotterytieMapper {

	public Map<String, Object> findUser(Map<String, Object> param);

	public void insertChangeRecordsEntity(Map<String, Object> param);

	public List<Map<String, Object>> finWhiteUserList();

	public List<Map<String, Object>> findOperatingUserList();

	public List<Map<String, Object>> findWhiteUserList();

}
