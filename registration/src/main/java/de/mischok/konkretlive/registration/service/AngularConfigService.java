package de.mischok.konkretlive.registration.service;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Provides communication to angular js
 */
@Controller
public class AngularConfigService {

	@Value("${registration.backendServiceUrl}")
	private String backendServiceUrl;
	
	/**
	 * Returns a javascript file with basic configuration constants
	 * @param response Http Response
	 * @throws Exception Any exception is rethrown
	 */
	@RequestMapping(value = "/conf/constants.js", method = RequestMethod.GET)
	public void getJavascriptConstants(HttpServletResponse response) throws Exception {
		// Override AngularJS default configuration
		String content = "angular.module('registration')\r\n"
								+ "\t.value('backendServiceUrl', '" + this.backendServiceUrl + (this.backendServiceUrl.endsWith("/") ? "" : "/" ) + "')\r\n"
								;
		
		response.getOutputStream().write(content.getBytes(Charset.forName("UTF-8")));
		response.setContentType("application/javascript");
		response.flushBuffer();
	}
}