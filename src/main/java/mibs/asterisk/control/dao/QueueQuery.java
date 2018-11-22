package mibs.asterisk.control.dao;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class QueueQuery {

	private int pbxid;
	private String date1;
	private String date2;
	private int page;
	private int agentid;
	private int queueid;
	
	public int getPbxid() {
		return pbxid;
	}
	public void setPbxid(int pbxid) {
		this.pbxid = pbxid;
	}
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	public String getDate2() {
		return date2;
	}
	public void setDate2(String date2) {
		this.date2 = date2;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getAgentid() {
		return agentid;
	}
	public void setAgentid(int agentid) {
		this.agentid = agentid;
	}
	public int getQueueid() {
		return queueid;
	}
	public void setQueueid(int queueid) {
		this.queueid = queueid;
	}
	@Override
	public String toString() {
		return  ToStringBuilder.reflectionToString(this);
	}
	
	
}
