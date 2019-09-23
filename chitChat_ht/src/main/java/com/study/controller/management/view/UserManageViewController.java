package com.study.controller.management.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.druid.util.StringUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.study.service.management.TeacherChangeRecordsService;
import com.study.service.management.UsersService;
import com.study.util.Contansts;
import com.study.util.bean.MenuBean;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "UserViewController", description = "user管理API")
@Controller
public class UserManageViewController {
    private static Logger logger = LoggerFactory.getLogger(UserManageViewController.class);
    @Autowired
    private UsersService usersService;
    @Autowired
    private TeacherChangeRecordsService teacherChangeRecordsService;
    @Autowired
    private MongoTemplate mongoTemplate;
    private Map<String, Object> res_map;

    @ApiOperation(value = "查看详情", notes = "查看详情")
    @RequestMapping(value = "/userView/getUserDetail", method = RequestMethod.GET)
    public String getUserDetail(HttpServletRequest request, @RequestParam(value = "userId", required = false) Integer userId,
                                @ModelAttribute MenuBean bean) {
        request.setAttribute("menu", bean);

        Map<String, Object> userMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        List<Map<String, Object>> userList = usersService.findUserList(paramMap, null);
        if (CollectionUtils.isNotEmpty(userList)) {
            userMap = userList.get(0);
            String image = userMap.get("MID_IMAGE") == null ? "" : userMap.get("MID_IMAGE").toString();
            String type = userMap.get("TYPE") == null ? "" : userMap.get("TYPE").toString();
            userMap.put("IMAGE", getHeadImage(image, type));
            String sex = userMap.get("SEX") == null ? "" : userMap.get("SEX").toString();
            if ("1".equals(sex)) {
                sex = "男";
            } else if ("2".equals(sex)) {
                sex = "女";
            } else if ("0".equals(sex)) {
                sex = "保密";
            }
            userMap.put("SEX", sex);

            String loginsource = userMap.get("LOGINSOURCE") == null ? "" : userMap.get("LOGINSOURCE").toString();
            String loginsource1 = userMap.get("LOGINSOURCE1") == null ? "" : userMap.get("LOGINSOURCE1").toString();
            userMap.put("WXBIND", "未绑定");
            userMap.put("QQBIND", "未绑定");
            if ("10".equals(loginsource)) {//微信
                userMap.put("WXBIND", "已绑定");
            }
            if ("3".equals(loginsource1)) {//QQ
                userMap.put("QQBIND", "已绑定");
            }

            String userName = userMap.get("USER_NAME") == null ? "" : userMap.get("USER_NAME").toString();
            List<Map<String, Object>> userImagesList = getUserImage(userName);
            if (CollectionUtils.isNotEmpty(userImagesList)) {
                for (int i = 0; i < userImagesList.size(); i++) {
                    Map<String, Object> userImageMap = userImagesList.get(i);
                    String userImage = userImageMap.get("image") == null ? "" : userImageMap.get("image").toString();
                    String userImageId = userImageMap.get("id") == null ? "" : userImageMap.get("id").toString();
                    request.setAttribute("userImage" + (i + 1), userImage);
                    request.setAttribute("userImageId" + (i + 1), userImageId);
                }
            }
        }
        request.setAttribute("entity", userMap);
        return "manage/userManage/userDetail";
    }

    private String getHeadImage(String image, String type) {
        try {
            if (!StringUtils.isEmpty(image)) {
                if(image.contains("http")){
                    return image;
                }
                if ("1".equals(type)) {
                    String sub = image.subSequence(0, 1) + "";
                    if (!"/".equals(sub)) {
                        image = "/" + image;
                    }
                    image = Contansts.PHOTOPATHNEW_fengkuangTY + image;
                } else {
                    if (StringUtil.nullBlank(image).equals("")
                            || StringUtil.nullBlank(image).equals("/face/temp/middlehead.jpg")) {
                        image = Contansts.YCHEADPATH_FTKY;
                    } else {
                        image = Contansts.PHOTOPATH + image;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    private List getUserImage(String userName) {
        List list = new ArrayList();
        try {
//            MgTemplate mgTemplate = new MgTemplate();
            DBCollection collection = mongoTemplate.getCollection("yt_live_user_images");
            BasicDBList condList = new BasicDBList();
            condList.add(new BasicDBObject("user_name", userName));
            BasicDBObject searchCond = new BasicDBObject();
            searchCond.put("$and", condList);

            DBCursor cursor = collection.find(searchCond).sort(new BasicDBObject("create_time", -1));
            while (cursor.hasNext()) {
                Map map1 = (Map) cursor.next();


                String imageId = StringUtil.nullBlank(map1.get("imageId"));
                String image_url = StringUtil.nullBlank((map1.get("image_url")));
                if (image_url != "") {
                    Map ret1 = new HashMap();
                    ret1.put("id", imageId);
                    ret1.put("image", "http://file.fengkuangtiyu.cn" + image_url);

                    list.add(ret1);
                }
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "老师详情", notes = "老师详情")
    @RequestMapping(value = "/userView/getTeacherDetail", method = RequestMethod.GET)
    public String getTeacherDetail(HttpServletRequest request, @RequestParam(value = "id", required = false) Integer id,
                                   @RequestParam(value = "flag", required = false) String flag,
                                @ModelAttribute MenuBean bean) {
        request.setAttribute("menu", bean);

        Map<String, Object> userMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        List<Map<String, Object>> userList = usersService.findTeacherList(paramMap, null);
        if (CollectionUtils.isNotEmpty(userList)) {
            userMap = userList.get(0);
            String image = userMap.get("MID_IMAGE") == null ? "" : userMap.get("MID_IMAGE").toString();
            String type = userMap.get("TYPE") == null ? "" : userMap.get("TYPE").toString();
            userMap.put("IMAGE", getHeadImage(image, type));
            String sex = userMap.get("SEX") == null ? "" : userMap.get("SEX").toString();
            if ("1".equals(sex)) {
                sex = "男";
            } else if ("2".equals(sex)) {
                sex = "女";
            } else if ("0".equals(sex)) {
                sex = "保密";
            }
            userMap.put("SEX", sex);

            String loginsource = userMap.get("LOGINSOURCE") == null ? "" : userMap.get("LOGINSOURCE").toString();
            String loginsource1 = userMap.get("LOGINSOURCE1") == null ? "" : userMap.get("LOGINSOURCE1").toString();
            userMap.put("WXBIND", "未绑定");
            userMap.put("QQBIND", "未绑定");
            if ("10".equals(loginsource)) {//微信
                userMap.put("WXBIND", "已绑定");
            }

            if ("3".equals(loginsource1)) {//QQ
                userMap.put("QQBIND", "已绑定");
            }

            String userName = userMap.get("USER_NAME") == null ? "" : userMap.get("USER_NAME").toString();
            List<Map<String, Object>> userImagesList = getUserImage(userName);
            if (CollectionUtils.isNotEmpty(userImagesList)) {
                for (int i = 0; i < userImagesList.size(); i++) {
                    Map<String, Object> userImageMap = userImagesList.get(i);
                    String userImage = userImageMap.get("image") == null ? "" : userImageMap.get("image").toString();
                    String userImageId = userImageMap.get("id") == null ? "" : userImageMap.get("id").toString();
                    request.setAttribute("userImage" + (i + 1), userImage);
                    request.setAttribute("userImageId" + (i + 1), userImageId);
                }
            }
            String account_new = userMap.get("ACCOUNT_NEW") == null ? "" : userMap.get("ACCOUNT_NEW").toString();
            if(StringUtils.isEmpty(account_new)){
                userMap.put("ACCOUNT_NEW", "未绑定");
            }else{
                userMap.put("ACCOUNT_NEW", "已绑定");
            }
        }
        request.setAttribute("entity", userMap);
        if("update".equals(flag)){
            return "manage/teacherManage/teacher_edit";
        }else if("teacherQuery".equals(flag)){
            return "manage/teacherManage/teacherManageDetail";
        }else{
            return "manage/teacher/teacherDetail";
        }
    }


    @ApiOperation(value = "添加老师", notes = "添加老师")
    @RequestMapping(value = "/userView/addTeacher", method = RequestMethod.GET)
    public String addTeacher(HttpServletRequest request,  @ModelAttribute MenuBean bean) {
        request.setAttribute("menu", bean);
        return "manage/teacherManage/teacher_add";
    }

    @ApiOperation(value = "修改评级", notes = "修改评级")
    @RequestMapping(value = "/userView/editTeacherLevel", method = RequestMethod.GET)
    public String editTeacherLevel(HttpServletRequest request,
                                   @RequestParam(value = "userName", required = false) String userName,
                                   @RequestParam(value = "id", required = false) Integer id,@ModelAttribute MenuBean bean) {
        request.setAttribute("menu", bean);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("userName",userName);
        paramMap.put("type","1");
        Map<String,Object>  teacherChangeRecordsMap = teacherChangeRecordsService.getLastRecord(paramMap);
        request.setAttribute("entity", teacherChangeRecordsMap);
        request.setAttribute("id", id);
        request.setAttribute("userName", userName);
        return "manage/teacherManage/edit_level";
    }

    @ApiOperation(value = "修改排序积分", notes = "修改排序积分")
    @RequestMapping(value = "/userView/editTeacherScore", method = RequestMethod.GET)
    public String editTeacherScore(HttpServletRequest request,
                                   @RequestParam(value = "userName", required = false) String userName,
                                   @RequestParam(value = "id", required = false) Integer id,@ModelAttribute MenuBean bean) {
        request.setAttribute("menu", bean);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("userName",userName);
        paramMap.put("type","2");
        Map<String,Object>  teacherChangeRecordsMap = teacherChangeRecordsService.getLastRecord(paramMap);
        request.setAttribute("entity", teacherChangeRecordsMap);
        request.setAttribute("id", id);
        request.setAttribute("userName", userName);
        return "manage/teacherManage/edit_score";
    }
}
