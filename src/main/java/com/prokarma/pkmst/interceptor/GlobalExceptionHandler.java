package com.prokarma.pkmst.interceptor;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This is a global exception handler to handle the exceptions which are not
 * caught
 * 
 * @author rkumar
 *
 */
@ControllerAdvice

public class GlobalExceptionHandler {

	private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class.getName());

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public void handleAll(Exception e) {
		LOG.log(Level.SEVERE, "Unhandled exception occurred");

	}
}
