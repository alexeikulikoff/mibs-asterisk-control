package mibs.asterisk.control.dao;

public class Peers {

	public Long id;
	public String name;
	public Long pbx;
	public Peers(Long id, String name, Long pbx) {
		this.name = name;
		this.id = id;
		this.pbx = pbx;
	}
	public Peers() {};

	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Long getPbx() {
		return pbx;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPbx(Long pbx) {
		this.pbx = pbx;
	}
	
}
