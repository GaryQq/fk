package com.study.controller.management;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.study.config.ESTransportClient;
import com.study.model.permissions.User;
import com.study.service.lottery.LotteryService;
import com.study.service.management.TeacherChangeRecordsService;
import com.study.service.management.UsersService;
import com.study.util.EsUserUtil;
import com.study.util.bean.DataGridResultInfo;
import com.study.util.bean.PageBean;
import com.study.util.permissions.ResultUtil;
import com.study.util.tool.DateUtil;
import com.study.util.tool.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "UserManage", description = "userManage管理API")
@RestController
@RequestMapping("/userManage")
public class UserManageController {
	private static Logger logger = LoggerFactory.getLogger(UserManageController.class);

	@Autowired
	private UsersService usersService;

	@Autowired
	private LotteryService lotteryService;

	@Autowired
	private TeacherChangeRecordsService teacherChangeRecordsService;

	@Autowired
	private MongoTemplate mongoTemplate;

	private static TransportClient esClient = ESTransportClient.getInstance();

	@ApiOperation(value = "userManage管理", notes = "userManage管理API")
	@RequestMapping(value = "/getUserList", method = { RequestMethod.GET })
	public DataGridResultInfo getData(HttpServletRequest request,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "nickName", required = false) String nickName,
			@RequestParam(value = "sid", required = false) String sid, @ModelAttribute PageBean bean) {
		if (bean.getPage() == null) {
			bean.setPage(1);
		}
		if (bean.getRows() == null) {
			bean.setRows(15);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("page_num", bean.getPage());
		param.put("page_size", bean.getRows());
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
			param.put("startTime", startTime + " 00:00:00");
			param.put("endTime", endTime + " 23:59:59");
		}
		param.put("userId", userId);
		param.put("nickName", nickName);
		param.put("sid", sid);
		List<Map<String, Object>> dailyDataViewList = usersService.findUserList(param, bean);
		if (CollectionUtils.isNotEmpty(dailyDataViewList)) {
			for (Map<String, Object> userMap : dailyDataViewList) {
				String birthday = userMap.get("BIRTHDAY") == null ? "" : userMap.get("BIRTHDAY").toString();
				userMap.put("AGE", getAge(birthday));
				String userLevel = userMap.get("USER_LEVEL") == null ? "" : userMap.get("USER_LEVEL").toString();
				if (StringUtils.isEmpty(userLevel)) {
					userMap.put("USER_LEVEL", "");
				} else {
					userMap.put("USER_LEVEL", "lv." + userLevel);
				}
				String userid = userMap.get("USERID") == null ? "" : userMap.get("USERID").toString();
				Map<String, Object> esUserMap = EsUserUtil.getUserInfo(userid);
				if (esUserMap != null) {
					String lastLoginTime = esUserMap.get("create_date") == null ? ""
							: esUserMap.get("create_date").toString();
					userMap.put("LAST_LOGIN_TIME", lastLoginTime);
					// appType 0:约彩 1:体育 2:疯聊 3:中超 4:红单 5:世界杯头条 6:语伴
					String appType = esUserMap.get("app_type") == null ? "" : esUserMap.get("app_type").toString();
					if (!StringUtils.isEmpty(appType)) {
						userMap.put("APP_TYPE", getAppType(Integer.parseInt(appType)));
					}
				}

				String user_name = userMap.get("USER_NAME") == null ? "" : userMap.get("USER_NAME").toString();
				String mobile = getMobileByUserName(user_name);
				userMap.put("MOBILE", mobile);
			}
		}
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(dailyDataViewList);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	private String getAppType(int appType) {
		// appType 0:约彩 1:体育 2:疯聊 3:中超 4:红单 5:世界杯头条 6:语伴
		String returnInfo = "";
		switch (appType) {
		case 0:
			returnInfo = "约彩";
			break;
		case 1:
			returnInfo = "体育";
			break;
		case 2:
			returnInfo = "疯聊";
			break;
		case 3:
			returnInfo = "中超";
			break;
		case 4:
			returnInfo = "红单";
			break;
		case 5:
			returnInfo = "世界杯头条";
			break;
		case 6:
			returnInfo = "语伴";
			break;
		default:
			returnInfo = "其他";
		}
		return returnInfo;
	}

	private String getAge(String birthday) {
		String age = "";
		try {
			if (!StringUtils.isEmpty(birthday)) {
				String year = birthday.substring(0, 4);
				age = Integer.parseInt(DateUtil.getSysYear()) - Integer.parseInt(year) + "";
			}
		} catch (Exception e) {
			logger.error("计算年龄异常!!!", e);
		}

		return age;
	}

	@ApiOperation(value = "删除头像", notes = "删除头像")
	@RequestMapping(value = "/delHeadImage")
	public String delHeadImage(HttpServletRequest request) {
		try {
			String userId = request.getParameter("user_id");
			String image = request.getParameter("image");
			String userName = request.getParameter("user_name");
			if (!StringUtils.isEmpty(userId)) {
				Map<String, Object> param = new HashMap<>();
				param.put("userId", userId);
				usersService.delHeadImage(param);

				Session session = SecurityUtils.getSubject().getSession();
				User user = (User) session.getAttribute("userSession");
				String operator = user.getName();
				Map<String, Object> map = new HashMap<>();
				map.put("userName", userName);
				map.put("type", "3");
				map.put("context", image);
				map.put("operator", operator);
				map.put("modify_time", usersService.getUserLastModifyTime(userId));
				teacherChangeRecordsService.insertTeacherChangeRecords(map);
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "删除签名", notes = "删除签名")
	@RequestMapping(value = "/delSign")
	public String delSign(HttpServletRequest request) {
		try {
			String userId = request.getParameter("user_id");
			String userName = request.getParameter("user_name");
			String autograph = request.getParameter("autograph");

			if (!StringUtils.isEmpty(userId)) {
				Map<String, Object> param = new HashMap<>();
				param.put("userId", userId);
				usersService.delSign(param);

				Session session = SecurityUtils.getSubject().getSession();
				User user = (User) session.getAttribute("userSession");
				String operator = user.getName();
				Map<String, Object> map = new HashMap<>();
				map.put("userName", userName);
				map.put("type", "4");
				map.put("context", autograph);
				map.put("operator", operator);
				Map<String, Object> liveUserHomeMap = usersService.getLiveUserHome(userId);
				String updateTime = "";
				if (liveUserHomeMap != null) {
					updateTime = liveUserHomeMap.get("UPDATETIME") == null ? ""
							: liveUserHomeMap.get("UPDATETIME").toString();
				}
				map.put("modify_time", updateTime);
				teacherChangeRecordsService.insertTeacherChangeRecords(map);
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "删除照片", notes = "删除照片")
	@RequestMapping(value = "/delUserImage")
	public String delUserImage(HttpServletRequest request) {
		try {
			String userImageId = request.getParameter("userImageId");
			String image = request.getParameter("image");
			String userName = request.getParameter("user_name");
			if (!StringUtils.isEmpty(userImageId)) {
				DBCollection forwardUrlNum = mongoTemplate.getCollection("yt_live_user_images");
				DBObject query = new BasicDBObject();
				query.put("imageId", Long.valueOf(userImageId));
				forwardUrlNum.remove(query, WriteConcern.SAFE);

				Session session = SecurityUtils.getSubject().getSession();
				User user = (User) session.getAttribute("userSession");
				String operator = user.getName();
				Map<String, Object> map = new HashMap<>();
				map.put("userName", userName);
				map.put("type", "5");
				map.put("context", image);
				map.put("operator", operator);
				map.put("modify_time", timeStamp2Date(userImageId, "yyyy-MM-dd HH:mm:ss"));

				teacherChangeRecordsService.insertTeacherChangeRecords(map);
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "teacher管理", notes = "teacher管理API")
	@RequestMapping(value = "/getTeacherList", method = { RequestMethod.GET })
	public DataGridResultInfo getTeacherList(HttpServletRequest request,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "nickName", required = false) String nickName,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "agentName", required = false) String agentName,
			@RequestParam(value = "anchorStatus", required = false) String anchorStatus,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "sortNum", required = false) String sortNum,
			@RequestParam(value = "range", required = false) String range,
			@RequestParam(value = "region", required = false) String region,
			@RequestParam(value = "ratioLevel", required = false) String ratioLevel, @ModelAttribute PageBean bean) {
		if (bean.getPage() == null) {
			bean.setPage(1);
		}
		if (bean.getRows() == null) {
			bean.setRows(15);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("page_num", bean.getPage());
		param.put("page_size", bean.getRows());
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
			param.put("startTime", startTime + " 00:00:00");
			param.put("endTime", endTime + " 23:59:59");
		}
		param.put("nickName", nickName);
		param.put("status", status);
		param.put("agentName", agentName);
		param.put("anchorStatus", anchorStatus);
		param.put("language", language);
		param.put("ratioLevel", ratioLevel);
		param.put("region", region);
		if ("1".equals(range)) {// 去除白名单
			if ("1".equals(sortNum)) {
				sortNum = "13";
			}

			if ("2".equals(sortNum)) {
				sortNum = "14";
			}

			if ("3".equals(sortNum)) {
				sortNum = "15";
			}

			if ("4".equals(sortNum)) {
				sortNum = "16";
			}

			if ("5".equals(sortNum)) {
				sortNum = "17";
			}

			if ("6".equals(sortNum)) {
				sortNum = "18";
			}

			if ("7".equals(sortNum)) {
				sortNum = "19";
			}

			if ("8".equals(sortNum)) {
				sortNum = "20";
			}
		}
		// sort 排序
		// 1:视频接通率倒序
		// 2:视频接通率正序
		// 3:语音接通率倒序
		// 4:语音接通率正序
		// 5:收到视频次数倒序
		// 6:收到视频次数正序
		// 7:收到语音次数倒序
		// 8:收到语音次数正序
		// 9:评分倒序
		// 10:评分正序
		// 11:注册时间倒序
		// 12:排序初始积分倒序
		// 13:视频接通率倒序 去除白名单
		// 14:视频接通率正序 去除白名单
		// 15:语音接通率倒序 去除白名单
		// 16:语音接通率正序 去除白名单
		// 17:收到视频次数倒序 去除白名单
		// 18:收到视频次数正序 去除白名单
		// 19:收到语音次数倒序 去除白名单
		// 20:收到语音次数正序 去除白名单
		// 21:申请时间正序
		// 101:视频接通次数正序
		// 102:视频接通次数倒序
		// 103:语音接通次数正序
		// 104:语音接通次数倒序
		// 105:总接通率正序
		// 106:总接通率倒序
		// 107:个人主页点击次数倒叙
		param.put("sort", sortNum);
		param.put("range", range);
		logger.info("getTeacherList========>range:" + range);
		List<Map<String, Object>> dailyDataViewList = usersService.findTeacherList(param, bean);

		if (CollectionUtils.isNotEmpty(dailyDataViewList)) {
			for (Map<String, Object> userMap : dailyDataViewList) {

				logger.info("getTeacherList========>userMap:" + userMap);

				String userid = userMap.get("USER_ID") == null ? "" : userMap.get("USER_ID").toString();
				String userName = userMap.get("USER_NAME") == null ? "" : userMap.get("USER_NAME").toString();
				Map<String, Object> esUserMap = EsUserUtil.getUserInfo(userid);
				if (esUserMap != null) {
					String lastLoginTime = esUserMap.get("create_date") == null ? ""
							: esUserMap.get("create_date").toString();
					userMap.put("LAST_LOGIN_TIME", lastLoginTime);
				}
				// userMap.put("TIPOFFTIMES", getTipOffTimes(userName));

				String user_name = userMap.get("USER_NAME") == null ? "" : userMap.get("USER_NAME").toString();
				String mobile = getMobileByUserName(user_name);
				userMap.put("MOBILE", mobile);
			}
		}

		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(dailyDataViewList);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiOperation(value = "老师审核", notes = "老师审核")
	@RequestMapping(value = "/teacherVerify")
	public String teacherVerify(HttpServletRequest request) {
		try {
			Session session = SecurityUtils.getSubject().getSession();
			User user = (User) session.getAttribute("userSession");
			String operator = user.getName();
			String status = request.getParameter("status");
			String id = request.getParameter("id");
			String userName = request.getParameter("userName");
			if (!StringUtils.isEmpty(id)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("status", status);
				paramMap.put("id", id);
				if ("-1".equals(status)) {// 拒绝
					paramMap.put("reason", request.getParameter("reason"));
					paramMap.put("content", request.getParameter("content"));
					SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_onetoone_live_list")
							.setTypes("onetoone_live_list");

					// 声明where 条件
					BoolQueryBuilder qbs = QueryBuilders.boolQuery();

					QueryBuilder qb1 = QueryBuilders.termQuery("user_name", userName);
					BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
					qbs.must(qbs1);

					requestBuilder.setQuery(qbs);
					SearchResponse response = requestBuilder.setFrom(0).setSize(5).execute().actionGet();
					SearchHits hits = response.getHits();
					if (hits.getHits().length > 0) {
						for (int a = 0; a < hits.getHits().length; a++) {
							SearchHit hit = hits.getHits()[a];
							EsUserUtil.deleteDocumentEntity(hit.getIndex(), hit.getType(), hit.getId());
						}
					}
				}
				if ("2".equals(status)) {// 同意
					paramMap.put("anchorStatus", "1");
					paramMap.put("operator", operator);
					paramMap.put("content", request.getParameter("content"));
				}
				paramMap.put("audit_name", operator);
				logger.info("teacherVerify===================>paramMap:" + paramMap);
				usersService.teacherVerify(paramMap);
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "查看原因", notes = "查看原因")
	@RequestMapping(value = "/getReason")
	public String getReason(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			return usersService.getReason(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	/**
	 * 获取举报次数
	 *
	 * @param userName
	 * @return
	 */
	private String getTipOffTimes(String userName) {
		try {
			DBCollection collection = mongoTemplate.getCollection("yt_live_tipoff_user");
			BasicDBList condList = new BasicDBList();
			condList.add(new BasicDBObject("tipoff_user_name", userName));
			BasicDBObject searchCond = new BasicDBObject();
			searchCond.put("$and", condList);
			DBCursor cursor = collection.find(searchCond).sort(new BasicDBObject("create_time", -1));
			return StringUtil.nullBlank(cursor.length());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@ApiOperation(value = "屏蔽,解除屏蔽", notes = "屏蔽,解除屏蔽")
	@RequestMapping(value = "/operateTeacher")
	public String operateTeacher(HttpServletRequest request) {
		try {
			Session session = SecurityUtils.getSubject().getSession();
			User user = (User) session.getAttribute("userSession");
			String operator = user.getName();
			String anchorStatus = request.getParameter("anchorStatus");
			String id = request.getParameter("id");
			String userName = request.getParameter("userName");
			if (!StringUtils.isEmpty(id)) {
				if ("2".equals(anchorStatus)) {
					SearchRequestBuilder requestBuilder = esClient.prepareSearch("fk_onetoone_live_list")
							.setTypes("onetoone_live_list");

					// 声明where 条件
					BoolQueryBuilder qbs = QueryBuilders.boolQuery();

					QueryBuilder qb1 = QueryBuilders.termQuery("user_name", userName);
					BoolQueryBuilder qbs1 = QueryBuilders.boolQuery().must(qb1);
					qbs.must(qbs1);

					requestBuilder.setQuery(qbs);
					SearchResponse response = requestBuilder.setFrom(0).setSize(5).execute().actionGet();
					SearchHits hits = response.getHits();
					if (hits.getHits().length > 0) {
						for (int a = 0; a < hits.getHits().length; a++) {
							SearchHit hit = hits.getHits()[a];
							EsUserUtil.deleteDocumentEntity(hit.getIndex(), hit.getType(), hit.getId());
						}
					}
				}
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("anchorStatus", anchorStatus);
				paramMap.put("id", id);
				paramMap.put("operator", operator);
				usersService.operateTeacher(paramMap);
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "通过昵称查找用户", notes = "通过昵称查找用户")
	@RequestMapping(value = "/getUserByNickName")
	public String getUserByNickName(HttpServletRequest request) {
		try {
			String nickName = request.getParameter("nickName");
			int count = usersService.getUserByNickName(nickName);
			return count + "";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "添加老师", notes = "添加老师")
	@RequestMapping(value = "/addTeacher")
	public String addTeacher(HttpServletRequest request) {
		try {
			String nickName = request.getParameter("nickName");
			String region = request.getParameter("region");
			String language = request.getParameter("language");
			String masterlanguage = request.getParameter("masterlanguage");
			String education = request.getParameter("education");
			String school = request.getParameter("school");

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("nickName", nickName);
			paramMap.put("region", region);
			paramMap.put("language", language);
			paramMap.put("masterlanguage", masterlanguage);
			paramMap.put("education", education);
			paramMap.put("school", school);
			paramMap.put("status", "1");
			paramMap.put("anchor_status", "2");
			usersService.insertTeacher(paramMap);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "修改老师资料", notes = "修改老师资料")
	@RequestMapping(value = "/editTeacher")
	public String editTeacher(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			String region = request.getParameter("region");
			String language = request.getParameter("language");
			String masterlanguage = request.getParameter("masterlanguage");
			String education = request.getParameter("education");
			String school = request.getParameter("school");

			if (!StringUtils.isEmpty(id)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("id", id);
				paramMap.put("region", region);
				paramMap.put("language", language);
				paramMap.put("masterlanguage", masterlanguage);
				paramMap.put("education", education);
				paramMap.put("school", school);
				usersService.operateTeacher(paramMap);
			}

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "修改老师评级", notes = "修改老师评级")
	@RequestMapping(value = "/editTeacherLevel")
	public String editTeacherLevel(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			String userName = request.getParameter("userName");
			String ratioLevel = request.getParameter("ratioLevel");

			if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(userName)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("id", id);
				paramMap.put("ratioLevel", ratioLevel);
				usersService.operateTeacher(paramMap);

				Session session = SecurityUtils.getSubject().getSession();
				User user = (User) session.getAttribute("userSession");
				String operator = user.getName();
				Map<String, Object> tcrParamMap = new HashMap<String, Object>();
				tcrParamMap.put("userName", userName);
				tcrParamMap.put("type", "1");
				tcrParamMap.put("context", ratioLevel);
				tcrParamMap.put("operator", operator);
				teacherChangeRecordsService.insertTeacherChangeRecords(tcrParamMap);
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	@ApiOperation(value = "修改老师排序积分", notes = "修改老师排序积分")
	@RequestMapping(value = "/editTeacherScore")
	public String editTeacherScore(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			String userName = request.getParameter("userName");
			String weight = request.getParameter("weight");

			if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(userName)) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("id", id);
				paramMap.put("weight", weight);
				usersService.operateTeacher(paramMap);

				Session session = SecurityUtils.getSubject().getSession();
				User user = (User) session.getAttribute("userSession");
				String operator = user.getName();
				Map<String, Object> tcrParamMap = new HashMap<String, Object>();
				tcrParamMap.put("userName", userName);
				tcrParamMap.put("type", "2");
				tcrParamMap.put("context", weight);
				tcrParamMap.put("operator", operator);
				teacherChangeRecordsService.insertTeacherChangeRecords(tcrParamMap);
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}

	private String getMobileByUserName(String userName) {
		Map<String, Object> map = new HashMap<>();
		map.put("user_name", userName);
		Map<String, Object> userMap = lotteryService.findLotteryUsers(map);
		String userMobile = userMap.get("user_mobile") == null ? "" : userMap.get("user_mobile").toString();
		return userMobile;
	}

	@ApiOperation(value = "代理teacher管理", notes = "代理teacher管理API")
	@RequestMapping(value = "/getAgencyTeacherList", method = { RequestMethod.GET })
	public DataGridResultInfo getAgencyTeacherList(HttpServletRequest request,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "nickName", required = false) String nickName,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "agentName", required = false) String agentName,
			@RequestParam(value = "anchorStatus", required = false) String anchorStatus,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "sortNum", required = false) String sortNum,
			@RequestParam(value = "range", required = false) String range,
			@RequestParam(value = "ratioLevel", required = false) String ratioLevel, @ModelAttribute PageBean bean) {
		if (bean.getPage() == null) {
			bean.setPage(1);
		}
		if (bean.getRows() == null) {
			bean.setRows(15);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("page_num", bean.getPage());
		param.put("page_size", bean.getRows());
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
			param.put("startTime", startTime + " 00:00:00");
			param.put("endTime", endTime + " 23:59:59");
		}
		param.put("nickName", nickName);
		param.put("status", status);
		param.put("agentName", agentName);
		param.put("anchorStatus", anchorStatus);
		param.put("language", language);
		param.put("ratioLevel", ratioLevel);

		if ("1".equals(range)) {// 去除白名单
			if ("1".equals(sortNum)) {
				sortNum = "13";
			}

			if ("2".equals(sortNum)) {
				sortNum = "14";
			}

			if ("3".equals(sortNum)) {
				sortNum = "15";
			}

			if ("4".equals(sortNum)) {
				sortNum = "16";
			}

			if ("5".equals(sortNum)) {
				sortNum = "17";
			}

			if ("6".equals(sortNum)) {
				sortNum = "18";
			}

			if ("7".equals(sortNum)) {
				sortNum = "19";
			}

			if ("8".equals(sortNum)) {
				sortNum = "20";
			}
		}
		// sort 排序
		// 1:视频接通率倒序
		// 2:视频接通率正序
		// 3:语音接通率倒序
		// 4:语音接通率正序
		// 5:收到视频次数倒序
		// 6:收到视频次数正序
		// 7:收到语音次数倒序
		// 8:收到语音次数正序
		// 9:评分倒序
		// 10:评分正序
		// 11:注册时间倒序
		// 12:排序初始积分倒序
		// 13:视频接通率倒序 去除白名单
		// 14:视频接通率正序 去除白名单
		// 15:语音接通率倒序 去除白名单
		// 16:语音接通率正序 去除白名单
		// 17:收到视频次数倒序 去除白名单
		// 18:收到视频次数正序 去除白名单
		// 19:收到语音次数倒序 去除白名单
		// 20:收到语音次数正序 去除白名单
		// 21:申请时间正序
		param.put("sort", sortNum);
		param.put("range", range);

		List<Map<String, Object>> dailyDataViewList = new ArrayList<Map<String, Object>>();
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User) session.getAttribute("userSession");
		String fkUserName = StringUtil.nullBlank(user.getFkUserName());
		if (!"".equals(fkUserName)) {
			param.put("fkUserName", fkUserName);
			dailyDataViewList = usersService.getAgencyTeacherList(param, bean);
		}

		if (CollectionUtils.isNotEmpty(dailyDataViewList)) {
			for (Map<String, Object> userMap : dailyDataViewList) {
				String userid = userMap.get("USER_ID") == null ? "" : userMap.get("USER_ID").toString();
				String userName = userMap.get("USER_NAME") == null ? "" : userMap.get("USER_NAME").toString();
				Map<String, Object> esUserMap = EsUserUtil.getUserInfo(userid);
				if (esUserMap != null) {
					String lastLoginTime = esUserMap.get("create_date") == null ? ""
							: esUserMap.get("create_date").toString();
					userMap.put("LAST_LOGIN_TIME", lastLoginTime);
				}
				userMap.put("TIPOFFTIMES", getTipOffTimes(userName));

				String user_name = userMap.get("USER_NAME") == null ? "" : userMap.get("USER_NAME").toString();
				String mobile = getMobileByUserName(user_name);
				userMap.put("MOBILE", mobile);
			}
		}

		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(dailyDataViewList);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiOperation(value = "userManage代理管理", notes = "userManage代理管理API")
	@RequestMapping(value = "/getAgencyUserList", method = { RequestMethod.GET })
	public DataGridResultInfo getAgencyUserData(HttpServletRequest request,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "nickName", required = false) String nickName,
			@RequestParam(value = "sid", required = false) String sid, @ModelAttribute PageBean bean) {
		if (bean.getPage() == null) {
			bean.setPage(1);
		}
		if (bean.getRows() == null) {
			bean.setRows(15);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("page_num", bean.getPage());
		param.put("page_size", bean.getRows());
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
			param.put("startTime", startTime + " 00:00:00");
			param.put("endTime", endTime + " 23:59:59");
		}
		param.put("userId", userId);
		param.put("nickName", nickName);
		param.put("sid", sid);
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User) session.getAttribute("userSession");
		List<Map<String, Object>> dailyDataViewList = new ArrayList<Map<String, Object>>();
		String fkUserName = StringUtil.nullBlank(user.getFkUserName());
		if (!"".equals(fkUserName)) {
			param.put("fkUserName", fkUserName);
			dailyDataViewList = usersService.findAgencyUserList(param, bean);
		}

		if (CollectionUtils.isNotEmpty(dailyDataViewList)) {
			for (Map<String, Object> userMap : dailyDataViewList) {

				String birthday = userMap.get("BIRTHDAY") == null ? "" : userMap.get("BIRTHDAY").toString();
				userMap.put("AGE", getAge(birthday));
				String userLevel = userMap.get("USER_LEVEL") == null ? "" : userMap.get("USER_LEVEL").toString();
				if (StringUtils.isEmpty(userLevel)) {
					userMap.put("USER_LEVEL", "");
				} else {
					userMap.put("USER_LEVEL", "lv." + userLevel);
				}
				String userid = userMap.get("USERID") == null ? "" : userMap.get("USERID").toString();
				Map<String, Object> esUserMap = EsUserUtil.getUserInfo(userid);
				if (esUserMap != null) {
					String lastLoginTime = esUserMap.get("create_date") == null ? ""
							: esUserMap.get("create_date").toString();
					userMap.put("LAST_LOGIN_TIME", lastLoginTime);
					// appType 0:约彩 1:体育 2:疯聊 3:中超 4:红单 5:世界杯头条 6:语伴
					String appType = esUserMap.get("app_type") == null ? "" : esUserMap.get("app_type").toString();
					if (!StringUtils.isEmpty(appType)) {
						userMap.put("APP_TYPE", getAppType(Integer.parseInt(appType)));
					}
				}

				String user_name = userMap.get("USER_NAME") == null ? "" : userMap.get("USER_NAME").toString();
				String mobile = getMobileByUserName(user_name);
				userMap.put("MOBILE", mobile);

				Map<String, Object> map = new HashMap<>();
				map.put("user_name", user_name);
				Map<String, Object> lottery_map = lotteryService.findLotteryUsers(map);
				userMap.put("user_status", lottery_map.get("user_status"));
			}
		}
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(dailyDataViewList);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@ApiOperation(value = "代理删除记录", notes = "代理删除记录管理API")
	@RequestMapping(value = "/findAgencyDeleteDetailList", method = { RequestMethod.GET })
	public DataGridResultInfo findAgencyDeleteDetailList(HttpServletRequest request,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "nickName", required = false) String nickName, @ModelAttribute PageBean bean) {

		// 如果开始时间与结束时间为空查询推送时间在前后7天之内的
		if (StringUtil.eqBlank(startTime)) {
			startTime = DateUtil.getPastDate(7);
		}
		if (StringUtil.eqBlank(endTime)) {
			endTime = DateUtil.getFetureDate(7);
		}

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		param.put("nickName", nickName);
		Session session = SecurityUtils.getSubject().getSession();
		User user = (User) session.getAttribute("userSession");
		List<Map<String, Object>> dailyDataViewList = new ArrayList<Map<String, Object>>();
		param.put("username", user.getUsername());
		dailyDataViewList = usersService.findAgencyDeleteDetailList(param, bean);

		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(dailyDataViewList);
		return ResultUtil.createDataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	public static String timeStamp2Date(String timeStamp, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);// 这个是你要转成后的时间的格式
		return sdf.format(new Date(Long.parseLong(timeStamp))); // 时间戳转换成时间
	}
}
