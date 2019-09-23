package com.study.service.management;

import com.study.util.bean.PageBean;

import java.util.List;
import java.util.Map;


public interface GainRatioService {

    List<Map<String, Object>> findGainRatioList(Map<String, Object> param, PageBean bean);

    public Map<String, Object> findGainRatioEntity(Integer id);

    public void updateGainRatio(Map<String, Object> param);
}
