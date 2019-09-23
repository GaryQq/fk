package com.fengkuang.author.service;

import com.fengkuang.author.bean.VipOrder;

import java.util.List;

public interface VipOrderService {

    List<VipOrder> getVipOrderByPostId(String postId);

}
