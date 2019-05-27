package mibs.asterisk.control.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ConsolidateQuery {

	
	private int pbxid;
	private String date1;
	private String date2;
	private String queuename;
	private String d1;
	private String d2;
	
	public String getD1() {
		return LocalDate.parse(date1,formatter).toString();
	}
	public String getD2() {
		return LocalDate.parse(date2,formatter).toString();
	}
	public DateTimeFormatter getFormatter() {
		return formatter;
	}
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
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
	public String getQueuename() {
		return queuename;
	}
	public void setQueuename(String queuename) {
		this.queuename = queuename;
	}
	@Override
	public String toString() {
		return "ConsolidateQuery [pbxid=" + pbxid + ", date1=" + date1 + ", date2=" + date2 + ", queuename=" + queuename
				+ ", d1=" + d1 + ", d2=" + d2 + ", formatter=" + formatter + "]";
	}
	
	
	
}
