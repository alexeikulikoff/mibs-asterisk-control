package mibs.asterisk.control.dao;

public class Agents {
	
	private Long id;
	private String name;
	private String extension;
	private Long pbxid;
	public Long getPbxid() {
		return pbxid;
	}
	public void setPbxid(Long pbxid) {
		this.pbxid = pbxid;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
}
