package mibs.asterisk.control.utils;

public class MonthCell {
	
	private int abandon;
	private int eneter;
	private int connect;
	private int agents;
	private int payload;
	private int value1;
	public int getValue1() {
		return value1;
	}
	public void setValue1(int value1) {
		this.value1 = value1;
	}
	public int getPayload() {
		return payload;
	}
	public void setPayload(int payload) {
		this.payload = payload;
	}
	public int getAbandon() {
		return abandon;
	}
	public void setAbandon(int abandon) {
		this.abandon = abandon;
	}
	public int getEneter() {
		return eneter;
	}
	public void setEneter(int eneter) {
		this.eneter = eneter;
	}
	public int getConnect() {
		return connect;
	}
	public void setConnect(int connect) {
		this.connect = connect;
	}
	public int getAgents() {
		return agents;
	}
	public void setAgents(int agents) {
		this.agents = agents;
	}
	@Override
	public String toString() {
		return "MonthCell [abandon=" + abandon + ", eneter=" + eneter + ", connect=" + connect + ", agents=" + agents
				+ "]";
	}
	
	
}
