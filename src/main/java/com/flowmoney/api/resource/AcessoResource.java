package com.flowmoney.api.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowmoney.api.config.property.FlowMoneyProperty;

@RestController
@RequestMapping("/acessos")
public class AcessoResource {

	@Autowired
	private FlowMoneyProperty flowMoneyProperty;
	
	@DeleteMapping("/logout")
	public void logout(HttpServletRequest req, HttpServletResponse res) {
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(flowMoneyProperty.getSeguranca().isEnableHttps());
		cookie.setPath(req.getContextPath() + "/oauth/token");
		cookie.setMaxAge(0);

		res.addCookie(cookie);
		res.setStatus(HttpStatus.NO_CONTENT.value());
	}
}
