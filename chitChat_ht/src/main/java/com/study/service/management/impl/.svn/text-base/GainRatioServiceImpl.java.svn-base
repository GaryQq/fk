package com.study.service.management.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.study.mapper.management.GainRatioMapper;
import com.study.service.management.GainRatioService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

@Service("gainRatioService")
public class GainRatioServiceImpl implements GainRatioService {

    private static Logger logger = LoggerFactory.getLogger(GainRatioServiceImpl.class);

    @Autowired
    private GainRatioMapper gainRatioMapper;

    /**
     * 声明全局变量返回容器
     */
    private List<Map<String, Object>> res_list;
    /**
     * 声明全局变量返回容器
     */
    private Map<String, Object> res_map;

    public List<Map<String, Object>> findGainRatioList(Map<String, Object> param, PageBean bean) {
        if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = gainRatioMapper.findGainRatioList(param);
        return res_list;
    }

    @Override
    public Map<String, Object> findGainRatioEntity(Integer id) {
        res_map = gainRatioMapper.findGainRatioEntity(id);
        return res_map;
    }

    @Override
    public void updateGainRatio(Map<String, Object> param) {
        gainRatioMapper.updateGainRatio(param);
    }
}
