package mibs.asterisk.control.dao;

import java.util.ArrayList;
import java.util.List;

public class QueueReport {

	private String agent;
	private String queue;
	private List<QueueRecord> records;
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
