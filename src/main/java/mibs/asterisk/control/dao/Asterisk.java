package mibs.asterisk.control.dao;

public class Asterisk {
	private Long id;
	private String name;
	public Asterisk(Long l, String s) {
		id = l;
		name = s;
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
}
