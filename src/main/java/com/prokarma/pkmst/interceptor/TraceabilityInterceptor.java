package com.prokarma.pkmst.interceptor;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This a audit interceptor which extends the HandlerInterceptorAdapter.An id is
 * passed in from client and will be unique with an application context. The id
 * will be passed into the backend and return to the consumer for transaction
 * tracing.
 * 
 * @author rkumar
 *
 */
public class TraceabilityInterceptor extends HandlerInterceptorAdapter {
	private static String TRACEABILITY_ID = "trace-id";
	private static final Logger LOG = Logger.getLogger(TraceabilityInterceptor.class.getName());

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		LOG.log(Level.INFO, "TraceabilityInterceptor is Exectuing");
		String tracebilityId = request.getHeader(TRACEABILITY_ID);
		if (tracebilityId != null) {
			response.addHeader(TRACEABILITY_ID, tracebilityId);
		}
		return true;
	}

}
