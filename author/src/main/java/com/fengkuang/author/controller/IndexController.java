package com.fengkuang.author.controller;

import com.fengkuang.author.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 默认启动页
 * @author zhaichong
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        if (null != session.getAttribute(Constants.SESSION_KEY_USER)) {
            return "forward:/author/dispatch";
        }
        return "index";
    }

}
