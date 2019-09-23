package com.fengkuang.author.service.impl;

import com.fengkuang.author.bean.VipOrder;
import com.fengkuang.author.dao.VipOrderDao;
import com.fengkuang.author.service.VipOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VipOrderServiceImpl implements VipOrderService {

    @Resource
    private VipOrderDao vipOrderDao;

    @Override
    public List<VipOrder> getVipOrderByPostId(String postId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("postId", postId);
        paramMap.put("status", 1);
        return vipOrderDao.getByParams(paramMap);
    }

}
