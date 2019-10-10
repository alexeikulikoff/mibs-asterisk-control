package mibs.asterisk.control.dao;

public class PerHoursQuery {

	private int pbxid;
	private String month1;
	private String month2;
	private String queuename;
	private String year;
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getPbxid() {
		return pbxid;
	}
	public void setPbxid(int pbxid) {
		this.pbxid = pbxid;
	}
	public String getMonth1() {
		return month1;
	}
	public void setMonth1(String month1) {
		this.month1 = month1;
	}
	public String getMonth2() {
		return month2;
	}
	public void setMonth2(String month2) {
		this.month2 = month2;
	}
	public String getQueuename() {
		return queuename;
	}
	public void setQueuename(String queuename) {
		this.queuename = queuename;
	}
	


}
