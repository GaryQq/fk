package com.fengkuang.author.controller;

import com.fengkuang.author.Constants;
import com.fengkuang.author.bean.Article;
import com.fengkuang.author.bean.Author;
import com.fengkuang.author.bean.VipOrder;
import com.fengkuang.author.enums.ResultCode;
import com.fengkuang.author.service.ArticleService;
import com.fengkuang.author.service.VipOrderService;
import com.fengkuang.author.util.CommonUtil;
import com.fengkuang.author.util.ImageUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/article")
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @Resource
    private ArticleService articleService;

    @Resource
    private VipOrderService vipOrderService;

    @RequestMapping("/insert")
    @ResponseBody
    public Map<String, Object> insertArticle(Article article, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = CommonUtil.makeResult(ResultCode.CODE_0000);
        if (null == article) {
            result = CommonUtil.makeResult(ResultCode.CODE_9999);
        } else {
            HttpSession session = request.getSession(true);
            Author author = (Author) session.getAttribute(Constants.SESSION_KEY_USER);
            article.setUserName(author.getUserName());
            try {
                result = articleService.insertArticle(article);
            } catch (Exception e) {
                logger.error("保存文章异常，事务回滚。。。");
                result = CommonUtil.makeResult(ResultCode.CODE_9998);
            }
        }
        return result;
    }

    @RequestMapping("/toList")
    public String toArticleList() {
        return "articleList";
    }

    @RequestMapping("/toDraftList")
    public String toDraftList() {
        return "draftList";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> articleList(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "1") int status, @RequestParam(defaultValue = "") String month) {
        HttpSession session = request.getSession(true);
        Author author = (Author) session.getAttribute(Constants.SESSION_KEY_USER);
        PageInfo<Map<String, Object>> resultList = articleService.articleListByPage(author, pageNum, pageSize, status, month);
        return CommonUtil.makeResult(ResultCode.CODE_0000, resultList);
    }

    @RequestMapping("/draftList")
    @ResponseBody
    public Map<String, Object> draftList(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "1") int status, @RequestParam(defaultValue = "") String month) {
        HttpSession session = request.getSession(true);
        Author author = (Author) session.getAttribute(Constants.SESSION_KEY_USER);
        PageInfo<Map<String, Object>> resultList = articleService.articleListByPage(author, pageNum, pageSize, status, month);
        return CommonUtil.makeResult(ResultCode.CODE_0000, resultList);
    }

    @RequestMapping("/articleMonthes")
    @ResponseBody
    public Map<String, Object> monthesList(HttpServletRequest request, @RequestParam(defaultValue = "1") int status) {
        HttpSession session = request.getSession(true);
        Author author = (Author) session.getAttribute(Constants.SESSION_KEY_USER);
        List<String> monthes = articleService.getMonthesList(author.getUserName(), status);
        return CommonUtil.makeResult(ResultCode.CODE_0000, monthes);
    }

    @RequestMapping("/{id}/detail")
    public ModelAndView articleDetail(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView();
        Article article = articleService.getArticle(id);
        mv.addObject("article", article);
        mv.setViewName("articleDetail");
        return mv;
    }

    @RequestMapping(value = {"/{postId}/edit", "/edit"})
    public ModelAndView articleEdit(@PathVariable(required = false) String postId) {

        ModelAndView mv = new ModelAndView();
        if (StringUtils.isNotBlank(postId)) {
            Article article = articleService.getArticle(postId);
            mv.addObject("article", article);
            mv.addObject("editFlag", 1);
        }
        mv.setViewName("articleEdit");
        return mv;
    }

    @RequestMapping(value = {"/{id}/editDraft"})
    public ModelAndView articleEditById(@PathVariable(required = false) Long id) {
        ModelAndView mv = new ModelAndView();
        if (null != id) {
            Article article = articleService.getArticle(id);
            mv.addObject("article", article);
            mv.addObject("editFlag", 0);
        }
        mv.setViewName("articleEdit");
        return mv;
    }

    @RequestMapping("/{postId}/delete")
    @ResponseBody
    public Map<String, Object> deteteArticle(@PathVariable String postId) {
        Map<String, Object> result = CommonUtil.makeResult(ResultCode.CODE_0000);
        // 判断是否有人订阅
        List<VipOrder> vipOrders = vipOrderService.getVipOrderByPostId(postId);
        if (!CollectionUtils.isEmpty(vipOrders)) {
            result = CommonUtil.makeResult(ResultCode.CODE_0009);
            return result;
        }

        // 删除
        try {
            articleService.deleteArticle(postId);
        } catch (Exception e) {
            e.printStackTrace();
            result = CommonUtil.makeResult(ResultCode.CODE_9999);
        }
        return result;
    }

    @RequestMapping("/{postId}/buyCount")
    @ResponseBody
    public Map<String, Object> getArticleByCount(@PathVariable String postId) {
        List<VipOrder> vipOrders = vipOrderService.getVipOrderByPostId(postId);
        if (CollectionUtils.isEmpty(vipOrders)) {
            return CommonUtil.makeResult(ResultCode.CODE_0000, vipOrders.size());
        } else {
            return CommonUtil.makeResult(ResultCode.CODE_0009, vipOrders.size());
        }
    }


    @RequestMapping("/uploadImage")
    @ResponseBody
    public Map<String, Object> uploadImage(MultipartFile upfile) {
        Map<String, Object> map = new HashMap<>(4);
        try {
            String resultUrl = ImageUtil.uploadFile(upfile.getInputStream(), upfile.getOriginalFilename());
            if (StringUtils.isNotBlank(resultUrl)) {
                map.put("url", resultUrl);
                map.put("state", "SUCCESS");
                map.put("title", "");
                map.put("original", "");
            }
            return map;
        } catch (Exception e) {
            return map;
        }
    }

    @RequestMapping("/toNoticeList")
    public String toNoticeList() {
        return "/noticeList";
    }

    @RequestMapping("/toNoticeDetail")
    public String toNoticeDetail() {
        return "/noticeDetail";
    }

    @RequestMapping("/noticeList")
    @ResponseBody
    public Map<String, Object> noticeList(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<Map<String, Object>> resultList = articleService.noticeListByPage(pageNum, pageSize);
        return CommonUtil.makeResult(ResultCode.CODE_0000, resultList);
    }

}
