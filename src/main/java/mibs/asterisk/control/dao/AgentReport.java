package mibs.asterisk.control.dao;

import java.util.TreeSet;

public class AgentReport {
	private String status;
	private TreeSet<AgentRecord> agentreport = new TreeSet<AgentRecord>();
	public AgentReport(String status) {
		super();
		this.status = status;
	}
	public AgentReport() {}
	public TreeSet<AgentRecord> getAgentreport() {
		return agentreport;
	}

	public void setAgentreport(TreeSet<AgentRecord> agentreport) {
		this.agentreport = agentreport;
	}
	public void add(AgentRecord a) {
		agentreport.add(a);
	}
	public String getStatus() {
		return status;
	}
	@Override
	public String toString() {
		return "AgentReport [status=" + status + ", agentreport=" + agentreport + "]";
	}

	
	
}
