package mibs.asterisk.control.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mibs.asterisk.control.config.AppConfig;
import mibs.asterisk.control.dao.JsConfig;

@RestController
public class CsrfController {
	
	@Autowired
	private AppConfig appConfig;
	
	@RequestMapping("/csrf")
	public CsrfToken csrf(CsrfToken token) {
		return token;
	}
	
	@RequestMapping(value = { "/jsconfig" },method = {RequestMethod.GET})
	public @ResponseBody JsConfig getJsConfig() {
		JsConfig jsconfig = new JsConfig();
		jsconfig.setServerContextPath(appConfig.getServerContextPath());
		return jsconfig;
	}
}