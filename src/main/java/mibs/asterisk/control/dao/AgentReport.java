package mibs.asterisk.control.dao;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.TreeSet;

public class AgentReport {
	private String status;
	private TreeSet<AgentRecord> agentrecords = new TreeSet<AgentRecord>();
	private int tcd ;  // Total Count Duration 
	private double rc;
	private static DecimalFormat f = new DecimalFormat("##");
	public AgentReport(String status) {
		super();
		this.status = status;
		tcd = 0;
	}
	public AgentReport() {}
	public TreeSet<AgentRecord> getAgentrecords() {
		return agentrecords;
	}
	
	public void setAgentrecords(TreeSet<AgentRecord> r) {
		this.agentrecords = r;
	}
	public void add(AgentRecord a) {
		agentrecords.add(a);
		tcd += a.getSecondDuration() * a.getCount();
	}
	public void computeCountDuration() {
		
		agentrecords.forEach(s->{
			double u = 0;
			if (tcd > 0) {
				u =  Math.round(100.0 * s.getSecondDuration() * s.getCount() /tcd);
			}
			
			if (u > 0 &  u < 1  ) {
				s.setCountduration("0" + f.format(u));	
			}else if ( u < 0) {
				s.setCountduration("0");	
			}else {
				 s.setCountduration(u > 0 ? f.format(u) : "0");
			}
			
			
		});
	}
	public String getStatus() {
		return status;
	}
	public int size() {
		return agentrecords.size();
	}
	public int getTcd() {
		return tcd;
	}
	public void setTcd(int tcd) {
		this.tcd = tcd;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "AgentReport [status=" + status + ", agentrecords=" + agentrecords + ", tcd=" + tcd + "]";
	}
	
	

	
	
}
