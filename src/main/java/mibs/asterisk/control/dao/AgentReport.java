package mibs.asterisk.control.dao;

import java.util.TreeSet;

public class AgentReport {
	private TreeSet<AgentRecord> agentreport = new TreeSet<AgentRecord>();

	public TreeSet<AgentRecord> getAgentreport() {
		return agentreport;
	}

	public void setAgentreport(TreeSet<AgentRecord> agentreport) {
		this.agentreport = agentreport;
	}
	public void add(AgentRecord a) {
		agentreport.add(a);
	}
	
	
}
