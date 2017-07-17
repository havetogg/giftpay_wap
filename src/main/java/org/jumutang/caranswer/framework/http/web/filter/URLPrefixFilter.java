package org.jumutang.caranswer.framework.http.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jumutang.caranswer.framework.x.StringX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取网站根路径
 * 
 * @author yuanyu
 */
public class URLPrefixFilter implements Filter {

	private static final Logger _LOGGER = LoggerFactory.getLogger(URLPrefixFilter.class);

	private String urlPrefix = "urlPrefix";

	public void init(FilterConfig filterConfig) throws ServletException {

		String userConfUrlProfix = filterConfig.getInitParameter("urlPrefix");

		_LOGGER.debug(StringX.stringFormat("初始化服务端域名前缀KEY！配置的取值KEY为：[0]", userConfUrlProfix));

		if (StringX.isNotEmpty(userConfUrlProfix)) {
			this.urlPrefix = userConfUrlProfix;
		}else{
			_LOGGER.debug(StringX.stringFormat("用户没有自定义服务端域名，采用默认配置KEY：[0]", urlPrefix));
		}
		
		_LOGGER.debug(StringX.stringFormat("初始化服务端域名前缀KEY完成，服务端域名前缀为：[0]", this.urlPrefix));
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		_LOGGER.debug("开始拼接服务端域名前缀动作！");
		_LOGGER.debug(StringX.stringFormat("Scheme:[0] , ServerName:[1] , ServerPort:[2] , ContextPath:[3]", 
				req.getScheme(),req.getServerName(),req.getServerPort(),req.getContextPath()));
		
		StringBuffer requestUrlPrefix = new StringBuffer();
		requestUrlPrefix.append(req.getScheme()).append("://").append(req.getServerName()).append(":")
				.append(req.getServerPort()).append(req.getContextPath());
		
		_LOGGER.debug(StringX.stringFormat("服务端域名前缀拼接完毕，拼接结果为：[0]", requestUrlPrefix.toString()));
		
		req.setAttribute(urlPrefix, requestUrlPrefix.toString());
		
		chain.doFilter(request, response);
	}


	public void destroy() {
		_LOGGER.debug("销毁 URLPrefixFilter !");
	}

}
