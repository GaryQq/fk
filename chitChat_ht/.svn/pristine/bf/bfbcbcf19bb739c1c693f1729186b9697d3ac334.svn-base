package com.study.service.management.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.study.mapper.management.UsersMapper;
import com.study.service.management.UsersService;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;

@Service("usersService")
public class UsersServiceImpl implements UsersService {

    private static Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 声明全局变量返回容器
     */
    private List<Map<String, Object>> res_list;
    /**
     * 声明全局变量返回容器
     */
    private Map<String, Object> res_map;

    public List<Map<String, Object>> findUserList(Map<String, Object> param, PageBean bean) {
        if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = usersMapper.findUserList(param);
        return res_list;
    }

    @Override
    public void delHeadImage(Map<String, Object> param) {
        usersMapper.delHeadImage(param);
    }

    @Override
    public void delSign(Map<String, Object> param) {
        usersMapper.delSign(param);
    }

    @Override
    public String getUserNameById(String userId) {
        return usersMapper.getUserNameById(userId);
    }

    public List<Map<String, Object>> findTeacherList(Map<String, Object> param, PageBean bean) {
        if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = usersMapper.findTeacherList(param);
        return res_list;
    }

    @Override
    public void teacherVerify(Map<String, Object> param) {
        usersMapper.updateFirstPage(param);
    }

    @Override
    public String getReason(String id) {
        Map<String, Object> map = usersMapper.getFirstPageById(Integer.parseInt(id));
        String reason = map.get("REASON") == null ? "" : map.get("REASON").toString();
        return reason;
    }

    @Override
    public void operateTeacher(Map<String, Object> param) {
        usersMapper.updateFirstPage(param);
    }

    @Override
    public void insertTeacher(Map<String, Object> param) {
        usersMapper.insertTeacher(param);
    }

    @Override
    public Integer getUserByNickName(String nickName) {
        return usersMapper.getUserByNickName(nickName);
    }

	@Override
	public List<Map<String, Object>> getAgencyTeacherList(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = usersMapper.getAgencyTeacherList(param);
        return res_list;
	}

	@Override
	public List<Map<String, Object>> findAgencyUserList(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = usersMapper.findAgencyUserList(param);
        return res_list;
	}

	@Override
	public List<Map<String, Object>> findAgencyDeleteDetailList(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
            PageHelper.startPage(bean.getPage(), bean.getRows());
        }
        res_list = usersMapper.findAgencyDeleteDetailList(param);
		return res_list;
	}

    @Override
    public Map<String,Object> getUserInfo(String id) {
        return usersMapper.getUserInfo(Integer.parseInt(id));
    }

    @Override
    public String getUserLastModifyTime(String id) {
        Map<String,Object> map =  usersMapper.getUserInfo(Integer.parseInt(id));
        String lastModifyTime = "";
        if(map != null){
            lastModifyTime = map.get("LAST_UPDATED") == null ? "" : map.get("LAST_UPDATED").toString();
        }
        return lastModifyTime;
    }

    @Override
    public Map<String,Object> getLiveUserHome(String id) {
        return usersMapper.getLiveUserHome(Integer.parseInt(id));
    }


}
