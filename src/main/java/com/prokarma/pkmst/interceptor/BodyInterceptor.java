package com.prokarma.pkmst.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This a Body interceptor which extends the HandlerInterceptorAdapter to
 * intercept the request and check if any request body is present inside the
 * request then it should be of proper contentType.
 * 
 * @author rkumar
 *
 */
public class BodyInterceptor extends HandlerInterceptorAdapter {
	private static final String CONTENT_TYPE_MISMATCH = "CONTENT_TYPE_MISMATCH";
	private static String CONTENT_TYPE = "application/json";
	private static final Logger LOG = Logger.getLogger(BodyInterceptor.class.getName());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String contentType = request.getHeader(CONTENT_TYPE);
		LOG.log(Level.INFO, "BodyInterceptor Exectuing");
		if (contentType != null && contentType.startsWith("application/json")) {

			InputStream is = request.getInputStream();
			if (is != null) {
				try {
					if (is.available() != -1) {
						String requestBody = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
						requestBody = requestBody.trim();
						if (!(requestBody.startsWith("{") || requestBody.startsWith("["))) {

							response.setStatus(403, CONTENT_TYPE_MISMATCH);
							return false;
						}
					}
				} catch (IOException e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}

		}

		return true;
	}
}
