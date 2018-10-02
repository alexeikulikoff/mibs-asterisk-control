package mibs.asterisk.control.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuration")
public class ConfigurationEntity implements Serializable{
	 private static final long serialVersionUID = 1L;
	 @Id
	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	 private Long id;
	 private String astname;
	 private String dbhost;
	 private String dbname;
	 private String dbuser;
	 private String dbpassword;

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
	 public void setId(Long i) {
		 id = i;
	 }
	 public Long getId() {
		 return id;
	 }
}
