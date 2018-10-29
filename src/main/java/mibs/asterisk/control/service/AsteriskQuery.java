package mibs.asterisk.control.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AsteriskQuery implements AsteriskMessage{

	private int id;
	private String command;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
