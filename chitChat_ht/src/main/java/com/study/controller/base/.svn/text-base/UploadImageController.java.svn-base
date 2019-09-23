package com.study.controller.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.study.util.tool.PortraitImgUtil;
import com.study.util.upload.ImageUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "UploadImageController", description = "上传图片访问地址")
@RestController
@RequestMapping("/upload")
public class UploadImageController {

	private static Logger logger = LoggerFactory.getLogger(UploadImageController.class);

	@ApiOperation(value = "公共上传图片方法", notes = "公共上传图片方法")
	@RequestMapping(value = "/uploadImage", method = { RequestMethod.POST })
	public String uploadImage(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		Map<String, Object> res_map = new HashMap<String, Object>();
		String url = "";
		String code = "0000";
		try {
			url = ImageUtil.uploadFile(file.getInputStream(), file.getOriginalFilename());
		} catch (ParseException | IOException e) {
			e.printStackTrace();
			code = "0099";
		}

		res_map.put("code", code);
		res_map.put("filePath", url.equals("") ? "" : PortraitImgUtil.getPortraitImgUtil(url, "1"));
		JSONObject json = new JSONObject(res_map);
		return json.toJSONString();
	}
}
