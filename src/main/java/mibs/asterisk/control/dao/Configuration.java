package mibs.asterisk.control.dao;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Configuration {
	private Long id;
	private String astname;
	private String dbhost;
	private String dbname;
	private String dbuser;
	private String dbpassword;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAstname() {
		return astname;
	}
	public void setAstname(String astname) {
		this.astname = astname;
	}
	
	public String getDbhost() {
		return dbhost;
	}
	public void setDbhost(String dbhost) {
		this.dbhost = dbhost;
	}
	public String getDbname() {
		return dbname;
	}
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	public String getDbuser() {
		return dbuser;
	}
	public void setDbuser(String dbuser) {
		this.dbuser = dbuser;
	}
	public String getDbpassword() {
		return dbpassword;
	}
	public void setDbpassword(String dbpassword) {
		this.dbpassword = dbpassword;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	 
}
