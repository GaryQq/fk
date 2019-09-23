package com.study.service.management.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.mapper.management.TeacherChangeRecordsMapper;
import com.study.service.management.TeacherChangeRecordsService;

@Service("teacherChangeRecordsService")
public class TeacherChangeRecordsImpl implements TeacherChangeRecordsService {

    private static Logger logger = LoggerFactory.getLogger(TeacherChangeRecordsImpl.class);

    @Autowired
    private TeacherChangeRecordsMapper teacherChangeRecordsMapper;

    /**
     * 声明全局变量返回容器
     */
    private List<Map<String, Object>> res_list;
    /**
     * 声明全局变量返回容器
     */
    private Map<String, Object> res_map;

    public void insertTeacherChangeRecords(Map<String, Object> param) {
        teacherChangeRecordsMapper.insertTeacherChangeRecords(param);
    }

    public Map<String, Object> getLastRecord(Map<String, Object> param) {
        return teacherChangeRecordsMapper.getLastRecord(param);
    }

}
