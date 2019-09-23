package com.study.service.management.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.study.mapper.management.GiftMapper;
import com.study.service.management.GiftService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

@Service("giftService")
public class GiftServiceImpl implements GiftService {

    private static Logger logger = LoggerFactory.getLogger(GiftServiceImpl.class);

    @Autowired
    private GiftMapper giftMapper;

    /**
     * 声明全局变量返回容器
     */
    private List<Map<String, Object>> res_list;
    /**
     * 声明全局变量返回容器
     */
    private Map<String, Object> res_map;

    @Override
    public List<Map<String, Object>> findGiftList(Map<String, Object> param, PageBean bean) {
        res_list = giftMapper.findGiftList(param);
        return res_list;
    }

    public List<Map<String, Object>> findGiftList1(Map<String, Object> param, PageBean bean) {
        if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = giftMapper.findGiftList1(param);
        return res_list;
    }

    @Override
    public void insertGift(Map<String, Object> param) {
        int giftId = giftMapper.getGiftId();
        param.put("id", giftId);
        giftMapper.insertGift(param);
        giftMapper.insertGainRatio(param);
    }

    @Override
    public Map<String, Object> findGiftEntity(Integer id) {
        res_map = giftMapper.findGiftEntity(id);
        return res_map;
    }

    @Override
    public void updateGift(Map<String, Object> param) {
        giftMapper.updateGift(param);
    }

}
