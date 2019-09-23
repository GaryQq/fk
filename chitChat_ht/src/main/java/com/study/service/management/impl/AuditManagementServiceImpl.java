package com.study.service.management.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.study.mapper.management.AuditManagementMapper;
import com.study.service.lottery.LotteryService;
import com.study.service.management.AuditManagementService;
import com.study.util.EsUserUtil;
import com.study.util.bean.PageBean;
import com.study.util.permissions.PageBeanUtil;
import com.study.util.tool.ESLotteryUtil;
import com.study.util.tool.PortraitImgUtil;
import com.study.util.tool.StringUtil;

@Service("auditManagementService")
public class AuditManagementServiceImpl implements AuditManagementService {

	private static Logger logger = LoggerFactory.getLogger(AuditManagementServiceImpl.class);

	@Autowired
	private AuditManagementMapper auditManagementMapper;

	@Autowired
	private LotteryService lotteryService;

	@Autowired
	private MongoTemplate mongoTemplate;

	/** 声明全局变量返回容器 */
	private List<Map<String, Object>> res_list;

	/** 声明全局变量返回容器 */
	private Map<String, Object> res_map;

	@Override
	public List<Map<String, Object>> findAuditManagementView(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		res_list = auditManagementMapper.findAuditManagementView(param);
		return res_list;
	}

	@Override
	public void enableUserAgentEntity(Map<String, Object> param) {
		auditManagementMapper.enableUserAgentEntity(param);
	}

	@Override
	public Map<String, Object> findAuditManagementEntity(String user_name) {
		Map<String, Object> map = auditManagementMapper.findAuditManagementEntity(user_name);

		logger.info("findAuditManagementEntity=======>map:" + map);

		String iPhone = StringUtil.nullBlank(map.get("IPHONE"));
		String eMail = StringUtil.nullBlank(map.get("EMAIL"));
		String info = StringUtil.nullBlank(map.get("INFO"));
		String nickName = StringUtil.nullBlank(map.get("NICK_NAME"));
		String userImg = PortraitImgUtil.getPortraitImgUtil(StringUtil.nullBlank(map.get("MID_IMAGE")),
				StringUtil.nullBlank(map.get("TYPE")));
		String education = StringUtil.nullBlank(map.get("EDUCATION"));
		String school = StringUtil.nullBlank(map.get("SCHOOL"));
		String birthday = StringUtil.nullBlank(map.get("BIRTHDAY"));
		String userName = StringUtil.nullBlank(map.get("USER_NAME"));
		String userLevel = StringUtil.nullBlank(map.get("USER_LEVEL"));
		String operator = StringUtil.nullBlank(map.get("OPERATOR"));
		String create_time = StringUtil.nullBlank(map.get("CREATE_TIME"));

		List<Map<String, Object>> img_list = new ArrayList<Map<String, Object>>();

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
				img_list.add(ret1);
			}
		}
		res_map = new HashMap<>();
		res_map.put("iPhone", iPhone);
		res_map.put("eMail", eMail);
		res_map.put("info", info);
		res_map.put("nickName", nickName);
		res_map.put("userImg", userImg);
		res_map.put("education", education);
		res_map.put("school", school);
		res_map.put("birthday", birthday);
		res_map.put("userLevel", userLevel);
		res_map.put("operator", operator);
		res_map.put("create_time", create_time);
		res_map.put("img_list", img_list);
		return res_map;
	}

	@Override
	public List<Map<String, Object>> findAuditView(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		List<Map<String, Object>> res_list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = auditManagementMapper.findAuditView(param);
		EsUserUtil esUserUtil = new EsUserUtil();
		if (!list.isEmpty()) {
			for (Map<String, Object> map : list) {
				String audit_id = StringUtil.nullBlank(map.get("AUDIT_ID"));
				String user_id = StringUtil.nullBlank(map.get("USER_ID"));
				String user_name = StringUtil.nullBlank(map.get("USER_NAME"));
				String nick_name = StringUtil.nullBlank(map.get("NICK_NAME"));
				String region = StringUtil.nullBlank(map.get("REGION"));// 代理地区
				String language = StringUtil.nullBlank(map.get("LANGUAGE"));// 母语
				String masterLanguage = StringUtil.nullBlank(map.get("MASTERLANGUAGE"));// 精通语言
				String create_date = StringUtil.nullBlank(map.get("DATE_CREATED"));// 注册时间
				String members = StringUtil.nullBlank(map.get("COUN_USER_NAME")); // 成员数
				String video_odds = StringUtil.nullBlank(map.get("VIDEO_ODDS")); // 视频接通率
				String voice_odds = StringUtil.nullBlank(map.get("VOICE_ODDS"));// 语音接通率
				String average_score = StringUtil.nullBlank(map.get("AVERAGE_SCORE"));// 评分
				String user_level = StringUtil.nullBlank(map.get("USER_LEVEL"));// 评级
				String del_status = StringUtil.nullBlank(map.get("DEL_STATUS"));// 代理状态
				String audit_time = StringUtil.nullBlank(map.get("AUDIT_TIME"));// 操作时间
				String audit_name = StringUtil.nullBlank(map.get("AUDIT_NAME")); // 操作人

				Map<String, Object> es_map = esUserUtil.getUserInfo(user_id);
				String last_date = "";
				if (!"".equals(StringUtil.nullBlank(es_map))) {
					last_date = StringUtil.nullBlank(es_map.get("create_date"));// 最后登录时间
				}

				DBCollection collection = mongoTemplate.getCollection("yt_live_tipoff_user");
				BasicDBList condList = new BasicDBList();
				condList.add(new BasicDBObject("tipoff_user_name", user_name));
				BasicDBObject searchCond = new BasicDBObject();
				searchCond.put("$and", condList);
				DBCursor cursor = collection.find(searchCond).sort(new BasicDBObject("create_time", -1));
				String report = StringUtil.nullBlank(cursor.length());// 被举报次数

				Map<String, Object> audit_map = new HashMap<String, Object>();
				audit_map.put("audit_id", audit_id);
				audit_map.put("user_id", user_id);
				audit_map.put("user_name", user_name);
				audit_map.put("nick_name", nick_name);
				audit_map.put("region", region);
				audit_map.put("language", language);
				audit_map.put("masterLanguage", masterLanguage);
				audit_map.put("create_date", create_date);
				audit_map.put("members", members);
				audit_map.put("video_odds", video_odds);
				audit_map.put("voice_odds", voice_odds);
				audit_map.put("average_score", average_score);
				audit_map.put("user_level", user_level);
				audit_map.put("del_status", del_status);
				audit_map.put("audit_time", audit_time);
				audit_map.put("audit_name", audit_name);
				audit_map.put("last_date", last_date);
				audit_map.put("report", report);

				res_list.add(audit_map);
			}
		}
		return res_list;
	}

	@Override
	public Map<String, Object> findAuditEntity(String user_name) {
		Map<String, Object> map = auditManagementMapper.findAuditEntity(user_name);
		logger.info("findAuditEntity===================>map:" + map);
		String user_id = StringUtil.nullBlank(map.get("USER_ID"));
		String userName = StringUtil.nullBlank(map.get("USER_NAME"));
		String nick_name = StringUtil.nullBlank(map.get("NICK_NAME"));// 昵称
		String userImg = PortraitImgUtil.getPortraitImgUtil(StringUtil.nullBlank(map.get("MID_IMAGE")),
				StringUtil.nullBlank(map.get("TYPE")));// 头像
		String birthday = StringUtil.nullBlank(map.get("BIRTHDAY"));// 生日
		String sex = StringUtil.nullBlank(map.get("SEX"));

		String education = StringUtil.nullBlank(map.get("EDUCATION"));// 学历
		String school = StringUtil.nullBlank(map.get("SCHOOL"));// 学校
		String region = StringUtil.nullBlank(map.get("REGION"));// 地区
		String language = StringUtil.nullBlank(map.get("LANGUAGE"));// 母语
		String masterLanguage = StringUtil.nullBlank(map.get("MASTERLANGUAGE")); // 精通语言
		String invitation_name = "".equals(StringUtil.nullBlank(map.get("INVITATION_NAME"))) ? "无代理"
				: StringUtil.nullBlank(map.get("INVITATION_NAME"));// 代理

		/** 查询caipiao库用户手机号 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("user_name", user_name);
		Map<String, Object> user_map = lotteryService.findLotteryUsers(param);
		String mobile = StringUtil.nullBlank(user_map.get("user_mobile"));// 手机

		String loginsource = StringUtil.nullBlank(map.get("LOGINSOURCE"));
        String loginsource1 = StringUtil.nullBlank(map.get("LOGINSOURCE1"));
		String wx_bound = "10".equals(loginsource) ? "已绑定" : "未绑定";
		String qq_bound = "3".equals(loginsource1) ? "已绑定" : "未绑定";
		String true_name = StringUtil.nullBlank(map.get("TRUE_NAME"));// 真实姓名
		String account_new = "".equals(StringUtil.nullBlank(map.get("ACCOUNT_NEW"))) ? "已绑定" : "未绑定"; // 提现账号
		String video_price = StringUtil.nullBlank(map.get("VIDEO_PRICE")) + "金币/每分钟"; // 视频价格
		String voice_price = StringUtil.nullBlank(map.get("VOICE_PRICE")) + "金币/每分钟"; // 音频价格
		String revice_rate = StringUtil.nullBlank(map.get("REVICE_RATE"));// 接通率
		String average_score = StringUtil.nullBlank(map.get("AVERAGE_SCORE"));// 评分
		String info = StringUtil.nullBlank(map.get("INFO"));// 个人介绍
		/** 个人相册 */
		List<Map<String, Object>> img_list = new ArrayList<Map<String, Object>>();
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
				img_list.add(ret1);
			}
		}

		// 语伴个性签名从mongo里面查询
		String autograph = "";// 个人签名
		DBCollection autograph_collection = mongoTemplate.getCollection("yb_liveuserhome_autograph");
		BasicDBList paramList = new BasicDBList();
		paramList.add(new BasicDBObject("user_name", userName));
		BasicDBObject searchBasic = new BasicDBObject();
		searchBasic.put("$and", paramList);
		DBCursor autograph_cursor = autograph_collection.find(searchCond).sort(new BasicDBObject("create_time", -1));
		while (autograph_cursor.hasNext()) {
			Map map1 = (Map) autograph_cursor.next();
			autograph = StringUtil.nullBlank(map1.get("autograph"));
		}
		res_map = new HashMap<>();
		res_map.put("user_id", user_id);
		res_map.put("user_name", userName);
		res_map.put("nick_name", nick_name);
		res_map.put("userImg", userImg);
		res_map.put("birthday", birthday);
		res_map.put("sex", sex);
		res_map.put("autograph", autograph);
		res_map.put("education", education);
		res_map.put("school", school);
		res_map.put("region", region);
		res_map.put("language", language);
		res_map.put("masterLanguage", masterLanguage);
		res_map.put("invitation_name", invitation_name);
		res_map.put("mobile", mobile);
		res_map.put("loginsource", loginsource);
		res_map.put("wx_bound", wx_bound);
		res_map.put("qq_bound", qq_bound);
		res_map.put("true_name", true_name);
		res_map.put("account_new", account_new);
		res_map.put("video_price", video_price);
		res_map.put("voice_price", voice_price);
		res_map.put("revice_rate", revice_rate);
		res_map.put("average_score", average_score);
		res_map.put("info", info);
		res_map.put("img_list", img_list);
		return res_map;
	}

	@Override
	public void insertAuditEntity(Map<String, Object> param) {
		auditManagementMapper.insertAuditEntity(param);
	}

	@Override
	public Map<String, Object> findAuditByIdEntity(Integer audit_id) {
		Map<String, Object> map = auditManagementMapper.findAuditByIdEntity(audit_id);
		String id = StringUtil.nullBlank(map.get("ID"));
		String user_name = StringUtil.nullBlank(map.get("USER_NAME"));
		String region = StringUtil.nullBlank(map.get("REGION"));
		String masterLanguage = StringUtil.nullBlank(map.get("MASTERLANGUAGE"));
		String agent_name = StringUtil.nullBlank(map.get("AGENT_NAME"));
		String area_code = StringUtil.nullBlank(map.get("AREA_CODE"));
		String iphone = StringUtil.nullBlank(map.get("IPHONE"));
		String email = StringUtil.nullBlank(map.get("EMAIL"));
		String info = StringUtil.nullBlank(map.get("INFO"));
		res_map = new HashMap<>();
		res_map.put("id", id);
		res_map.put("user_name", user_name);
		res_map.put("region", region);
		res_map.put("masterLanguage", masterLanguage);
		res_map.put("agent_name", agent_name);
		res_map.put("area_code", area_code);
		res_map.put("iphone", iphone);
		res_map.put("email", email);
		res_map.put("info", info);
		return res_map;
	}

	@Override
	public void updateAuditEntity(Map<String, Object> param) {
		auditManagementMapper.updateAuditEntity(param);
	}

	@Override
	public List<Map<String, Object>> findAuditTeacherView(Map<String, Object> param, PageBean bean) {
		if (PageBeanUtil.pageBeanIsNotEmpty(bean)) {
			PageHelper.startPage(bean.getPage(), bean.getRows());
		}
		List<Map<String, Object>> res_list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = auditManagementMapper.findAuditTeacherView(param);
		ESLotteryUtil esLotteryUtil = new ESLotteryUtil();
		if (!list.isEmpty()) {
			for (Map<String, Object> map : list) {
				String audit_id = StringUtil.nullBlank(map.get("AUDIT_ID"));
				String user_name = StringUtil.nullBlank(map.get("USER_NAME"));
				String nick_name = StringUtil.nullBlank(map.get("NICK_NAME"));
				String sex = StringUtil.nullBlank(map.get("SEX"));
				String region = StringUtil.nullBlank(map.get("REGION"));// 地区
				String language = StringUtil.nullBlank(map.get("LANGUAGE"));// 母语
				String agency_nick_name = StringUtil.nullBlank(map.get("AGENCY_NICK_NAME"));// 代理人
				String create_date = StringUtil.nullBlank(map.get("DATE_CREATED"));// 注册时间
				String create_time = StringUtil.nullBlank(map.get("CREATE_TIME"));// 申请时间
				String audit_time = StringUtil.nullBlank(map.get("AUDIT_TIME"));// 审核时间
				String status = StringUtil.nullBlank(map.get("STATUS"));// 老师审核状态
				String audit_name = StringUtil.nullBlank(map.get("AUDIT_NAME"));// 审核人
				Map<String, Object> lottery_user = esLotteryUtil.findLotteryUserInfo(user_name);
				String phone = StringUtil.nullBlank(lottery_user.get("user_mobile"));
				String content = StringUtil.nullBlank(map.get("CONTENT"));// 审核备注
				Map<String, Object> teacher_map = new HashMap<String, Object>();
				teacher_map.put("audit_id", audit_id);
				teacher_map.put("user_name", user_name);
				teacher_map.put("nick_name", nick_name);
				teacher_map.put("sex", sex);
				teacher_map.put("region", region);
				teacher_map.put("language", language);
				teacher_map.put("agency_nick_name", agency_nick_name);
				teacher_map.put("create_date", create_date);
				teacher_map.put("create_time", create_time);
				teacher_map.put("audit_time", audit_time);
				teacher_map.put("status", status);
				teacher_map.put("audit_name", audit_name);
				teacher_map.put("phone", phone);
				teacher_map.put("content", content);
				teacher_map.put("status1", status);
				res_list.add(teacher_map);
			}
		}
		return res_list;
	}

}
