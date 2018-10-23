package mibs.asterisk.control.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * CREATE TABLE `configuration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `astname` varchar(100) COLLATE utf8_bin NOT NULL,
  `dbhost` varchar(200) COLLATE utf8_bin NOT NULL,
  `dbname` varchar(200) COLLATE utf8_bin NOT NULL,
  `dbuser` varchar(200) COLLATE utf8_bin NOT NULL,
  `dbpassword` varchar(200) COLLATE utf8_bin NOT NULL,
  `sshlogin` varchar(250) COLLATE utf8_bin NOT NULL DEFAULT 'admin',
  `sshpassword` varchar(250) COLLATE utf8_bin NOT NULL DEFAULT 'admin',
  `asthost` varchar(99) COLLATE utf8_bin NOT NULL DEFAULT 'localhost',
  `astuser` varchar(99) COLLATE utf8_bin NOT NULL DEFAULT 'admin',
  `astpassword` varchar(99) COLLATE utf8_bin NOT NULL DEFAULT 'admin',
  PRIMARY KEY (`id`),
  UNIQUE KEY `astname` (`astname`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin
 * 
 * 
 * 
 */

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
	 private String sshlogin;
	 private String sshpassword;
	 private String asthost;
	 private String astuser;
	 private String astpassword;

	public String getAsthost() {
		return asthost;
	}
	public void setAsthost(String asthost) {
		this.asthost = asthost;
	}
	public String getAstuser() {
		return astuser;
	}
	public void setAstuser(String astuser) {
		this.astuser = astuser;
	}
	public String getAstpassword() {
		return astpassword;
	}
	public void setAstpassword(String astpassword) {
		this.astpassword = astpassword;
	}
	public String getSshlogin() {
		return sshlogin;
	}
	public void setSshlogin(String sshlogin) {
		this.sshlogin = sshlogin;
	}
	public String getSshpassword() {
		return sshpassword;
	}
	public void setSshpassword(String sshpassword) {
		this.sshpassword = sshpassword;
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
	 public void setId(Long i) {
		 id = i;
	 }
	 public Long getId() {
		 return id;
	 }
}
