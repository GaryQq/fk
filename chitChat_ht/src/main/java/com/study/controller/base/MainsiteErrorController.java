package com.study.controller.base;

import io.swagger.annotations.Api;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(value = "MainsiteErrorController", description = "错误页面")
@Controller
public class MainsiteErrorController implements ErrorController {

	private static final String ERROR_PATH = "/error";

	@RequestMapping(value = ERROR_PATH, method = { RequestMethod.GET, RequestMethod.POST })
	public String handleError() {
		return "403";
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

}
