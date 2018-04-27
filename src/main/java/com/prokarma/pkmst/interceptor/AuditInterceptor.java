package com.prokarma.pkmst.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.prokarma.pkmst.controller.ApplicationAudit;

/**
 * This a audit interceptor which extends the HandlerInterceptorAdapter to
 * calculate the time taken by searchbyids request
 * 
 * @author rkumar
 *
 */
public class AuditInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOG = Logger.getLogger(AuditInterceptor.class.getName());

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LOG.log(Level.INFO, "Pre-handle");
		HandlerMethod hm = (HandlerMethod) handler;
		Method method = hm.getMethod();
		if (method.isAnnotationPresent((Class<? extends Annotation>) ApplicationAudit.class)) {
			LOG.log(Level.INFO, method.getAnnotation(ApplicationAudit.class).value());
			request.setAttribute("STARTTIME", System.currentTimeMillis());
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		LOG.log(Level.INFO, "Post-handle");
		HandlerMethod hm = (HandlerMethod) handler;
		Method method = hm.getMethod();

		if (method.isAnnotationPresent(ApplicationAudit.class)) {
			LOG.log(Level.INFO, method.getAnnotation(ApplicationAudit.class).value());
			request.setAttribute("ENDTIME", System.currentTimeMillis());
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		LOG.log(Level.INFO, "After completion handle");
		HandlerMethod hm = (HandlerMethod) handler;
		Method method = hm.getMethod();
		if (method.isAnnotationPresent(ApplicationAudit.class)) {
			LOG.log(Level.INFO, method.getAnnotation(ApplicationAudit.class).value());
			LOG.log(Level.INFO, "Total time take for request:"
					+ ((Long) request.getAttribute("ENDTIME") - (Long) request.getAttribute("STARTTIME")));
		}
	}
}
