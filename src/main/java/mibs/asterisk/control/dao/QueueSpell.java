package mibs.asterisk.control.dao;

public class QueueSpell {
	private Long id;
	private String agent;
	private String queue;
	private String peer;
	private String addTime;
	private String removeTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getRemoveTime() {
		return removeTime;
	}
	public void setRemoveTime(String removeTime) {
		this.removeTime = removeTime;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getPeer() {
		return peer;
	}
	public void setPeer(String peer) {
		this.peer = peer;
	}
	
}
