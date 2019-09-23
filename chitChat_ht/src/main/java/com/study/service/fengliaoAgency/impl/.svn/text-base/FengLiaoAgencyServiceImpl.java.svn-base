package com.study.service.fengliaoAgency.impl;

import com.github.pagehelper.PageHelper;
import com.study.mapper.fengliaoAgency.FengLiaoAgencyMapper;
import com.study.service.fengliaoAgency.FengLiaoAgencyService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;
import com.study.util.tool.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("fengLiaoAgencyService")
public class FengLiaoAgencyServiceImpl implements FengLiaoAgencyService {

    /**
     * 声明全局变量返回容器
     */
    private List<Map<String, Object>> res_list;

    @Autowired
    private FengLiaoAgencyMapper fengLiaoAgencyMapper;

    @Override
    public List<Map<String, Object>> findUserRegisterList(Map<String, Object> param, PageBean bean) {
        if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = fengLiaoAgencyMapper.findUserRegisterList(param);
        return res_list;
    }

    @Override
    public List<Map<String, Object>> findUserPayDetailList(Map<String, Object> param, PageBean bean) {
        if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = fengLiaoAgencyMapper.findUserPayDetailList(param);
        return res_list;
    }

    @Override
    public int findUserPaySumInfo(Map<String, Object> param) {
        return fengLiaoAgencyMapper.findUserPaySumInfo(param);
    }

    @Override
    public List<Map<String, Object>> findAgencyPayDetailList(Map<String, Object> param, PageBean bean) {
        if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = fengLiaoAgencyMapper.findAgencyPayDetailList(param);
        return res_list;
    }

    @Override
    public int findAgencyPaySumInfo(Map<String, Object> param) {
        String sumInfo = fengLiaoAgencyMapper.findAgencyPaySumInfo(param);
        return StringUtil.nullZero(sumInfo);
    }

    @Override
    public Double findAgencyPayIncome(Map<String, Object> param) {
        String income = fengLiaoAgencyMapper.findAgencyPayIncome(param);
        return StringUtil.nullDouble(income);
    }

    @Override
    public Double findAgencyPayBalance(Map<String, Object> param) {
        String balance = fengLiaoAgencyMapper.findAgencyPayBalance(param);
        return StringUtil.nullDouble(balance);
    }

    @Override
    public String findAgencyPayRatio(Map<String, Object> param) {
        return fengLiaoAgencyMapper.findAgencyPayRatio(param);
    }

    @Override
    public void saveAgencyPayDetail(Map<String, Object> param) {
        fengLiaoAgencyMapper.saveAgencyPayDetail(param);
    }

    @Override
    public void updateAgencyAccount(Map<String, Object> param) {
        fengLiaoAgencyMapper.updateAgencyAccount(param);
    }

    @Override
    public void saveAgencyAccountDetail(Map<String, Object> param) {
        fengLiaoAgencyMapper.saveAgencyAccountDetail(param);
    }

}
