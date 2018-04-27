package com.prokarma.pkmst.interceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.prokarma.pkmst.interceptor.utils.SimpleRateLimiter;

/**
 * This a Rate Limit interceptor which extends the HandlerInterceptorAdapter to
 * limit the requests created by a client. Deafult limit to 10/min.
 * 
 * there isn’t a “canonical” way to implement rate-limiting (in Java). It should
 * be implemented in gateway
 * 
 * @author rkumar
 *
 */
public class RateLimitInterceptor extends HandlerInterceptorAdapter {
	private static int permits = 10;
	private boolean enabled = true;
	private static final Logger LOG = Logger.getLogger(AuditInterceptor.class.getName());
	private int hourlyLimit = 10;

	private Map<String, SimpleRateLimiter> limiters = new ConcurrentHashMap<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LOG.log(Level.INFO, "RateLimitInterceptor is Exectuing");
		if (!enabled) {
			return true;
		}
		String clientId = request.getHeader("Client-Id");

		// let non-API requests pass
		if (clientId == null) {
			return true;
		}
		SimpleRateLimiter rateLimiter = getRateLimiter(clientId);
		boolean allowRequest = rateLimiter.tryAcquire();

		if (!allowRequest) {
			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
		}
		response.addHeader("X-RateLimit-Limit", String.valueOf(hourlyLimit));
		return allowRequest;
	}

	private SimpleRateLimiter getRateLimiter(String clientId) {
		if (limiters.containsKey(clientId)) {
			return limiters.get(clientId);
		} else {
			synchronized (clientId.intern()) {
				// double-checked locking to avoid multiple-reinitializations
				if (limiters.containsKey(clientId)) {
					return limiters.get(clientId);
				}

				SimpleRateLimiter rateLimiter = SimpleRateLimiter.create(permits, TimeUnit.MINUTES);

				limiters.put(clientId, rateLimiter);
				return rateLimiter;
			}
		}
	}
}