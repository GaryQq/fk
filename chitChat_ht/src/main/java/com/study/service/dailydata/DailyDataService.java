package com.study.service.dailydata;

import java.util.List;
import java.util.Map;

import com.study.util.bean.PageBean;

public interface DailyDataService {
	
	List<Map<String, Object>> findOneToOneDay(Map<String, Object> param, PageBean bean);

	long findOneToOneDayTotal(Map<String, Object> param);

	Map<String, Object> findOneToOneDayFooter(Map<String, Object> param);
}
