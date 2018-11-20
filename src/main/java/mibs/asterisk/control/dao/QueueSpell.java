package mibs.asterisk.control.dao;

public class QueueSpell {
	private Long id;
	private int agentid;
	private int peerid;
	private int queueid;
	private String addTime;
	private String removeTime;
	
	public Long getId() {
		return id;
	}
	public int getAgentid() {
		return agentid;
	}
	public void setAgentid(int agentid) {
		this.agentid = agentid;
	}
	public int getPeerid() {
		return peerid;
	}
	public void setPeerid(int peerid) {
		this.peerid = peerid;
	}
	public int getQueueid() {
		return queueid;
	}
	public void setQueueid(int queueid) {
		this.queueid = queueid;
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
}
