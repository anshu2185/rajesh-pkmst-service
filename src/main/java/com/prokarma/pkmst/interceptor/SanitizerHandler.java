package com.prokarma.pkmst.interceptor;

import java.beans.PropertyEditorSupport;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Every string object coming into your app will go through this code. The
 * Handler cleaner will remove tags like <script>, <div>, etc. It also removes
 * everything inside those tags
 * 
 * @author rkumar
 *
 */
@ControllerAdvice
public class SanitizerHandler {
	static public class StringCleaner extends PropertyEditorSupport {
		@Override
		public void setAsText(String text) {
			if (text == null) {
				setValue(null);
			} else {
				String safe = Jsoup.clean(text, Whitelist.simpleText());
				setValue(safe);
			}
		}

	}

	/**
	 * Such init-binder methods support all arguments that
	 * {@link RequestMapping} supports, except for command/form objects and
	 * corresponding validation result objects. Init-binder methods must not
	 * have a return value; they are usually declared as {@code void}.
	 * 
	 * @param webDataBinder
	 */
	@InitBinder
	public void bindStringCleaner(WebDataBinder webDataBinder) {
		StringCleaner stringCleaner = new StringCleaner();
		webDataBinder.registerCustomEditor(String.class, stringCleaner);
	}

}