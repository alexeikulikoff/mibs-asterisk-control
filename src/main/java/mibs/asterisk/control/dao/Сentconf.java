package mibs.asterisk.control.dao;

public class Сentconf {

	private Long id;
	private Long agentid;
	private Long queueid;
	private String agentname;
	private String queuename;
	private String extention;
	private int penalty;
	private Long pbx;
	
	public Long getPbx() {
		return pbx;
	}
	public void setPbx(Long pbx) {
		this.pbx = pbx;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAgentid() {
		return agentid;
	}
	public void setAgentid(Long agentid) {
		this.agentid = agentid;
	}
	public Long getQueueid() {
		return queueid;
	}
	public void setQueueid(Long queueid) {
		this.queueid = queueid;
	}
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	public String getQueuename() {
		return queuename;
	}
	public void setQueuename(String queuename) {
		this.queuename = queuename;
	}
	public String getExtention() {
		return extention;
	}
	public void setExtention(String extention) {
		this.extention = extention;
	}
	public int getPenalty() {
		return penalty;
	}
	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}
	
}
