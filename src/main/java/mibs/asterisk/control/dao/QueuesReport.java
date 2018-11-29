package mibs.asterisk.control.dao;

import java.util.List;

public class QueuesReport {
	private List<QueueDetail> records;
	private List<PageTab> tabs;
	
	public QueuesReport(List<QueueDetail> records, List<PageTab> tabs) {
		super();
		this.records = records;
		this.tabs = tabs;
	}
	public List<QueueDetail> getRecords() {
		return records;
	}

	public List<PageTab> getTabs() {
		return tabs;
	}

}
