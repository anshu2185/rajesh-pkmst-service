package com.prokarma.pkmst.interceptor;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * This a audit interceptor which extends the HandlerInterceptorAdapter.It
 * generates a UUID in the first API/service and pass it to all other
 * APIs/services in the call tree for tracking purpose.
 * 
 * @author rkumar
 *
 */
public class CorrelationInterceptor implements ClientHttpRequestInterceptor {
	private static String CORRELATION_ID = "correaltion-id";
	private static final Logger LOG = Logger.getLogger(CorrelationInterceptor.class.getName());

	/* (non-Javadoc)
	 * @see org.springframework.http.client.ClientHttpRequestInterceptor#intercept(org.springframework.http.HttpRequest, byte[], org.springframework.http.client.ClientHttpRequestExecution)
	 */
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		String correlationId = request.getHeaders().get(CORRELATION_ID).get(0);
		LOG.log(Level.INFO, "CorrelationInterceptor Exectuing");

		if (correlationId == null) {
			correlationId = UUID.randomUUID().toString();
		}
		request.getHeaders().set(CORRELATION_ID, correlationId);
		return execution.execute(request, body);
	}
}
