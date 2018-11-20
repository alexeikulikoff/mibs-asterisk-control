package mibs.asterisk.control.dao;

public class QueueQuery {
	private int id;
	private String date1;
	private String date2;
	private int page;
	private int agentid;
	private int peerid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getPeerid() {
		return peerid;
	}
	public void setPeerid(int peerid) {
		this.peerid = peerid;
	}
	
}
