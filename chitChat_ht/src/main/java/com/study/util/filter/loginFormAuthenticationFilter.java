package com.study.util.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * 重写登陆成功后的验证过滤器
 *
 */
public class loginFormAuthenticationFilter extends FormAuthenticationFilter {
	/**
	 * 登陆成功后重定向方法
	 */
	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		/* WebUtils.getAndClearSavedRequest(request); */
		WebUtils.issueRedirect(request, response, "/index", null, true);
	}
}
