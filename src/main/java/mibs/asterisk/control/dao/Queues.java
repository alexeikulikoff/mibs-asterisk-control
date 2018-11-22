package mibs.asterisk.control.dao;

public class Queues {

	private Long id;
	private String name;
	private Long pbx;
	
	public Queues(Long i , String n, Long p) {
		id =i;
		name = n;
		pbx = p;
	}
	public Queues() {}
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
