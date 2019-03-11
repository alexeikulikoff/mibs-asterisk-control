package mibs.asterisk.control.dao;

import java.util.ArrayList;
import java.util.List;

public class QueueReport {

	private String agent;
	private String queue;
	private String totalduration;
	private int totalcall;
	private String haserror;

	public String getHaserror() {
		return haserror;
	}
	public void setHaserror(String haserror) {
		this.haserror = haserror;
	}
	private List<QueueRecord> records;
	
	public String getTotalduration() {
		return totalduration;
	}
	public void setTotalduration(String totalduration) {
		this.totalduration = totalduration;
	}
	public int getTotalcall() {
		return totalcall;
	}
	public void setTotalcall(int totalcall) {
		this.totalcall = totalcall;
	}
	public QueueReport() {
		records = new ArrayList<>();
	}
	public void add(QueueRecord r) {
		records.add(r);
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
	public List<QueueRecord> getRecords() {
		return records;
	}
	public void setRecords(List<QueueRecord> records) {
		this.records = records;
	}
	
}
