package com.flowmoney.api.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public class UsuarioUtil {

	public static String getUserName(Authentication authentication) {
		String userName = (String) ((Jwt) authentication.getCredentials()).getClaims().get("user_name");
		return userName;
	}
}
