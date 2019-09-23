package com.study.service.management;

import com.study.util.bean.PageBean;

import java.util.List;
import java.util.Map;


public interface UsersService {


    List<Map<String, Object>> findUserList(Map<String, Object> param, PageBean bean);

    public void delHeadImage(Map<String, Object> param);

    public void delSign(Map<String, Object> param);

    public String getUserNameById(String userId);

    public List<Map<String, Object>> findTeacherList(Map<String, Object> param, PageBean bean);

    public void teacherVerify(Map<String, Object> param);

    public String getReason(String id);

    public void operateTeacher(Map<String, Object> param);

    public void insertTeacher(Map<String, Object> param);

    public Integer getUserByNickName(String nickName);

	List<Map<String, Object>> getAgencyTeacherList(Map<String, Object> param, PageBean bean);

	List<Map<String, Object>> findAgencyUserList(Map<String, Object> param, PageBean bean);

	List<Map<String, Object>> findAgencyDeleteDetailList(Map<String, Object> param, PageBean bean);

    public Map<String,Object> getUserInfo(String id);

    public String getUserLastModifyTime(String id);

    public Map<String,Object> getLiveUserHome(String id);
}
