package mibs.asterisk.control.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix="asterisk")
public class AppConfig {

	@Value("${asterisk.configheader}")
	private String configheader;
	@Value("${asterisk.sipconfig}")
	private String sipconfig;
	
	@Value("${asterisk.siptemplate}")
	private String siptemplate;
	
	public String getSiptemplate() {
		return siptemplate;
	}

	public void setSiptemplate(String siptemplate) {
		this.siptemplate = siptemplate;
	}

	public String getConfigheader() {
		return configheader;
	}

	public void setConfigheader(String configheader) {
		this.configheader = configheader;
	}


	public String getSipconfig() {
		return sipconfig;
	}

	public void setSipconfig(String sipconfig) {
		this.sipconfig = sipconfig;
	}

}
