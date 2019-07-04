package mibs.asterisk.control.dao;

import java.util.List;

public class OutboundConsolidateReport {

	private List<OutboundConsolidateRecord> records;
	private long totalCalls;
	private double totalDuration;
	private String totalduration2;
	
	public String getTotalduration2() {
		return totalduration2;
	}
	public void setTotalduration2(String totalduration2) {
		this.totalduration2 = totalduration2;
	}
	public List<OutboundConsolidateRecord> getRecords() {
		return records;
	}
	public void setRecords(List<OutboundConsolidateRecord> records) {
		this.records = records;
	}
	public long getTotalCalls() {
		return totalCalls;
	}
	public void setTotalCalls(long totalCalls) {
		this.totalCalls = totalCalls;
	}
	public double getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(double d) {
		this.totalDuration = d;
	}
	
  
}
