package com.study.controller.management;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.study.service.management.LotterytieUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "LotterytieUser", description = "疯狂caibo库用户信息API")
@RestController
@RequestMapping("/lotterytieUser")
public class LotterytieUserController {

	private static Logger logger = LoggerFactory.getLogger(LotterytieUserController.class);

	@Autowired
	private LotterytieUserService lotterytieUserService;

	@ApiOperation(value = "根据昵称查询用户", notes = "根据昵称查询用户API")
	@RequestMapping(value = "/searchNickName", method = { RequestMethod.POST })
	public String searchNickName(HttpServletRequest request) {
		try {
			String nick_name = request.getParameter("nick_name");
			String user_name = request.getParameter("user_name");
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("nick_name", nick_name);
			param.put("user_name", user_name);
			Map<String, Object> user_map = lotterytieUserService.findUserEntity(param);

			Map<String, Object> res_map = new HashMap<String, Object>();
			if (!user_map.isEmpty()) {
				res_map.put("code", "success");
				res_map.put("data", user_map);
			}
			JSONObject json = new JSONObject(res_map);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
	
}
