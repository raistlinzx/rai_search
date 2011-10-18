package com.rai.search.web.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class EncodingFilter implements Filter {
	private String targetEncoding = "utf-8";

	public void destroy() {

	}

	public void doFilter(ServletRequest sreq, ServletResponse sres,
			FilterChain fc) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)sreq;
        request.setCharacterEncoding(targetEncoding);
        fc.doFilter(sreq, sres);
	}

	public void init(FilterConfig config) throws ServletException {
		this.targetEncoding=config.getInitParameter("encoding");
	}

}
