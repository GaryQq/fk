package com.study.service.detail;

import java.util.List;
import java.util.Map;

import com.study.util.bean.PageBean;

public interface OneToOneDetailService {

	long findOneToOneDetailTotal(Map<String, Object> param);

	List<Map<String, Object>> findOneToOneDetailList(Map<String, Object> param, PageBean bean);

	Map<String, Object> findOneToOneDetailFooter(Map<String, Object> param, PageBean bean);

	long findOneToOneMsgDetailTotal(Map<String, Object> param);

	List<Map<String, Object>> findOneToOneMsgDetailList(Map<String, Object> param, PageBean bean);

	Map<String, Object> findOneToOneMsgDetailFooter(Map<String, Object> param, PageBean bean);

}
