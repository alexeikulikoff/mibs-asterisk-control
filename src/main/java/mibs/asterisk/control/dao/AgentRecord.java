package mibs.asterisk.control.dao;

public class AgentRecord implements Comparable<AgentRecord> {

	private int id;
	private String name;
	private String duration;
	private int count;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "AgentRecord [id=" + id + ", name=" + name + ", duration=" + duration + ", count=" + count + "]";
	}
	@Override
	public int compareTo(AgentRecord arg0) {
		
		return id - arg0.id;
	}
	
	
	
}
