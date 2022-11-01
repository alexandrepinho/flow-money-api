package com.flowmoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("flowmoney")
@Component
public class FlowMoneyProperty {

	private String origin = "http://localhost:4200";
	private final Seguranca seguranca = new Seguranca();

	public Seguranca getSeguranca() {
		return seguranca;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public static class Seguranca {
		private boolean https;

		public boolean isHttps() {
			return https;
		}

		public void setHttps(boolean https) {
			this.https = https;
		}

	}
}
