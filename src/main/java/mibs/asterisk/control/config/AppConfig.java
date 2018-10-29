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
	
	@Value("${asterisk.sshport}")
	private String sshport;
	
	@Value("${asterisk.known_hosts}")
	private String known_hosts;
	
	@Value("${asterisk.sipcongigremote}")
	private String sipcongigremote;
	
	@Value("${server.servlet.context-path}")
	private String serverContextPath;
	
	@Value("${spring.datasource.url}")
	private String datasourceUrl;
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	public String getDatasourceUrl() {
		return datasourceUrl;
	}
	public void setDatasourceUrl(String datasourceUrl) {
		this.datasourceUrl = datasourceUrl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Value("${asterisk.ami_port}")
	private String ami_port;
	
	public String getAmi_port() {
		return ami_port;
	}
	public void setAmi_port(String ami_port) {
		this.ami_port = ami_port;
	}
	public String getServerContextPath() {
		return serverContextPath;
	}
	public void setServerContextPath(String serverContextPath) {
		this.serverContextPath = serverContextPath;
	}
	public String getSipcongigremote() {
		return sipcongigremote;
	}
	public void setSipcongigremote(String sipcongigremote) {
		this.sipcongigremote = sipcongigremote;
	}
	public String getKnown_hosts() {
		return known_hosts;
	}

	public void setKnown_hosts(String known_hosts) {
		this.known_hosts = known_hosts;
	}
	public String getSshport() {
		return sshport;
	}
	public void setSshport(String sshport) {
		this.sshport = sshport;
	}
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
