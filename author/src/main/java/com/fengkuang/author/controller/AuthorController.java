package com.fengkuang.author.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengkuang.author.Constants;
import com.fengkuang.author.bean.Author;
import com.fengkuang.author.enums.ResultCode;
import com.fengkuang.author.service.AuthorService;
import com.fengkuang.author.util.CommonUtil;
import com.fengkuang.author.util.HttpRequestProxy;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/author")
public class AuthorController {

    @Value("${h5.login.url}")
    private String loginUrl;

    @Value("${h5.authCode.url}")
    private String authCodeUrl;

    @Value("${h5.fastLogin.url}")
    private String fastLoginUrl;

    @Resource
    private AuthorService authorService;

    @RequestMapping("/apply")
    @ResponseBody
    public Map<String, Object> applyForAuthor(HttpServletRequest request, Author author, String authCode) {

        // 判断作者名称是否可用
        int count = authorService.getAuthorNameCount(author);
        if (count > 0) {
            return CommonUtil.makeResult(ResultCode.CODE_0014);
        }
        HttpSession session = request.getSession(true);
        /**
         * 验证码不为空，说明是未登录用户申请作者
         * 先调用快速登录接口，判断验证码正确性，再获取用户userName，判断该用户是否申请过作者
         */
        if (StringUtils.isNotBlank(authCode)) {
            String mobile = author.getMobile();
            Map<String, String> param = new HashMap<>(3);
            param.put("phone", mobile);
            param.put("code", authCode);
            param.put("sid", Constants.SID);
            String loginRtn = HttpRequestProxy.doGet(fastLoginUrl, param, Constants.ENCODE_UTF_8, Constants.ENCODE_UTF_8);
            if (StringUtils.isNotBlank(loginRtn)) {
                System.out.println(loginRtn);
                JSONObject rtn = JSON.parseObject(loginRtn);
                String status = rtn.getString("status");
                if (StringUtils.equals("0000", status)) {
                    String userName = rtn.getString("userName");
                    Author authorReal = authorService.getAuthorByUserName(userName);
                    // 申请被拒绝当做未申请过
                    if (null != authorReal && -1 != authorReal.getStatus()) {
                        authorReal.setImageUrl(StringUtils.isNotBlank(authorReal.getImageUrl()) ? Constants.IMAGE_DOMAIN + authorReal.getImageUrl() : "");
                        session.setAttribute(Constants.SESSION_KEY_USER, authorReal);
                        if (authorReal.getStatus() == 1) {
                            return CommonUtil.makeResult(ResultCode.CODE_0013);
                        } else if (authorReal.getStatus() == 0) {
                            return CommonUtil.makeResult(ResultCode.CODE_0012);
                        }
                    }
                    author.setUserName(userName);
                } else if (StringUtils.equals("0142", status)) {
                    return CommonUtil.makeResult(ResultCode.CODE_0011);
                } else {
                    return CommonUtil.makeResult(ResultCode.CODE_9999);
                }
            } else {
                return CommonUtil.makeResult(ResultCode.CODE_9999);
            }
        }

        author.setStatus(0);

        if (null == session.getAttribute(Constants.SESSION_KEY_USER)) {
            session.setAttribute(Constants.SESSION_KEY_USER, author);
        }

        if (StringUtils.isNotBlank(author.getImageUrl())) {
            author.setImageUrl(author.getImageUrl().replace(Constants.IMAGE_DOMAIN, ""));
        }
        authorService.saveAuthorApply(author);
        return CommonUtil.makeResult(ResultCode.CODE_0000);
    }

    @RequestMapping("/myHome")
    public ModelAndView toMyHome() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("authorHome");
        return mv;
    }

    @RequestMapping("/summary")
    @ResponseBody
    public Map<String, Object> authorSummary(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Author author = (Author) session.getAttribute(Constants.SESSION_KEY_USER);
        Map<String, Object> summary = authorService.authorSummary(author.getUserName());
        return CommonUtil.makeResult(ResultCode.CODE_0000, summary);
    }

    @RequestMapping("/toIncome")
    public String toIncome() {
        return "authorIncome";
    }

    @RequestMapping("/income")
    @ResponseBody
    public Map<String, Object> authorIncome(HttpServletRequest request, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        HttpSession session = request.getSession(true);
        Author author = (Author) session.getAttribute(Constants.SESSION_KEY_USER);
        PageInfo<Map<String, Object>> pageInfo = authorService.incomeListByPage(author.getUserName(), pageNum, pageSize);
        return CommonUtil.makeResult(ResultCode.CODE_0000, pageInfo);
    }

    @RequestMapping("/totalIncome")
    @ResponseBody
    public Map<String, Object> authorTotalIncome(HttpServletRequest request, String month) {
        HttpSession session = request.getSession(true);
        Author author = (Author) session.getAttribute(Constants.SESSION_KEY_USER);
        String totalIncome = authorService.authorTotalIncome(author.getUserName(), month);
        return CommonUtil.makeResult(ResultCode.CODE_0000, totalIncome);
    }

    @RequestMapping("/dispatch")
    public ModelAndView dispatch(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        HttpSession session = request.getSession(true);
        Object authorObj = session.getAttribute(Constants.SESSION_KEY_USER);
        if (null == authorObj) {
            mv.setViewName("/");
        } else {
            Author author = (Author) authorObj;
            // 判断是否是作者，如果是作者，跳转到个人中心，如果不是作者，跳转作者申请
            Author authorReal = authorService.getAuthorByUserName(author.getUserName());
            if (null != authorReal && -1 != authorReal.getStatus()) {
                authorReal.setImageUrl(StringUtils.isNotBlank(authorReal.getImageUrl()) ? Constants.IMAGE_DOMAIN + authorReal.getImageUrl() : "");
                session.setAttribute(Constants.SESSION_KEY_USER, authorReal);
                author = authorReal;
            }
            if (null != authorReal && authorReal.getStatus() == 1) {
                mv.setViewName("redirect:/author/myHome");
            } else {
                mv.setViewName("applyForAuthor");
                mv.addObject("author", author);
            }
        }
        return mv;
    }

    @RequestMapping("/login")
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = CommonUtil.makeResult(ResultCode.CODE_0000);

        String loginType = request.getParameter("loginTypeFlag");
        String passName = request.getParameter("passName");
        String password = request.getParameter("password");

        if (CommonUtil.hasNullParams(loginType, passName, password)) {
            return CommonUtil.makeResult(ResultCode.CODE_0001);
        }

        // 登录，区分是手机号验证码登录还是账号密码登录
        if (StringUtils.equals("0", loginType)) {
            // 账号密码
            Map<String, String> param = new HashMap<>(3);
            param.put("userName", passName);
            param.put("password", password);
            param.put("sid", Constants.SID);
            String loginRtn = HttpRequestProxy.doGet(loginUrl, param, Constants.ENCODE_UTF_8, Constants.ENCODE_UTF_8);
            if (StringUtils.isNotBlank(loginRtn)) {
                JSONObject rtn = JSON.parseObject(loginRtn);
                String status = rtn.getString("status");
                if (StringUtils.equals("0000", status)) {
                    String userName = rtn.getString("userName");
                    String nickName = rtn.getString("nick_name");
                    String imageUrl = rtn.getString("mid_image");
                    if (StringUtils.isBlank(nickName)) {
                        nickName = userName;
                    }

                    Author author = new Author(userName, nickName, imageUrl);
                    HttpSession session = request.getSession(true);
                    session.setAttribute(Constants.SESSION_KEY_USER, author);
                    return CommonUtil.makeResult(ResultCode.CODE_0000, author);
                } else {
                    return CommonUtil.makeResult(ResultCode.CODE_0010);
                }
            } else {
                return CommonUtil.makeResult(ResultCode.CODE_9999);
            }
        } else if (StringUtils.equals("1", loginType)) {
            // 手机号验证码
            Map<String, String> param = new HashMap<>(3);
            param.put("phone", passName);
            param.put("code", password);
            param.put("sid", Constants.SID);
            String loginRtn = HttpRequestProxy.doGet(fastLoginUrl, param, Constants.ENCODE_UTF_8, Constants.ENCODE_UTF_8);
            if (StringUtils.isNotBlank(loginRtn)) {
                System.out.println(loginRtn);
                JSONObject rtn = JSON.parseObject(loginRtn);
                String status = rtn.getString("status");
                if (StringUtils.equals("0000", status)) {
                    String userName = rtn.getString("userName");
                    String nickName = rtn.getString("nick_name");
                    String imageUrl = rtn.getString("mid_image");
                    if (StringUtils.isBlank(nickName)) {
                        nickName = userName;
                    }
                    Author author = new Author(userName, nickName, imageUrl);
                    HttpSession session = request.getSession(true);
                    session.setAttribute(Constants.SESSION_KEY_USER, author);
                    return CommonUtil.makeResult(ResultCode.CODE_0000, author);
                } else if (StringUtils.equals("0142", status)) {
                    return CommonUtil.makeResult(ResultCode.CODE_0011);
                } else {
                    return CommonUtil.makeResult(ResultCode.CODE_9999);
                }
            } else {
                return CommonUtil.makeResult(ResultCode.CODE_9999);
            }
        }
        return CommonUtil.makeResult(ResultCode.CODE_0000);
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Map<String, Object> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.removeAttribute(Constants.SESSION_KEY_USER);
        return CommonUtil.makeResult(ResultCode.CODE_0000);
    }

    @RequestMapping("/toApply")
    public String toApply() {
        return "applyForAuthor";
    }

    @RequestMapping("/authCode")
    @ResponseBody
    public Map<String, Object> sendAuthCode(@RequestParam String phoneNum) {
        Map<String, String> param = new HashMap<>(2);
        param.put("phone", phoneNum);
        param.put("sid", Constants.SID);
        String rtn = HttpRequestProxy.doGet(authCodeUrl, param, Constants.ENCODE_UTF_8, Constants.ENCODE_UTF_8);
        if (StringUtils.isNotBlank(rtn)) {
            JSONObject jsonObject = JSON.parseObject(rtn);
            String status = jsonObject.getString("status");
            if (StringUtils.equals("0000", status)) {
                return CommonUtil.makeResult(ResultCode.CODE_0000);
            }
        }
        return CommonUtil.makeResult(ResultCode.CODE_9999);
    }
}
