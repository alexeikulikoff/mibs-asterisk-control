package mibs.asterisk.control.dao;

public class Agents {
	
	private Long id;
	private String name;
	private Long pbxid;
	public Agents() {}
	
	public Agents(Long i, String n, Long p) {
		id = i;
		name = n;
		pbxid = p;
	}
	public Long getPbxid() {
		return pbxid;
	}
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPbxid(Long pbxid) {
		this.pbxid = pbxid;
	}

	
}
