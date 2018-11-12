package mibs.asterisk.control.dao;

public class Peers {

	public Long id;
	public String name;
	public Long pbx;
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
	public Long getPbx() {
		return pbx;
	}
	public void setPbx(Long pbx) {
		this.pbx = pbx;
	}
}
