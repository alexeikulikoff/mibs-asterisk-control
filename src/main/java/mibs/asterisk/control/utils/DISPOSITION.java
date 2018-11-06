package mibs.asterisk.control.utils;

public enum DISPOSITION {

	ANSWERED("ANSWERED"), BUSY("BUSY"), NOANSWER("NO ANSWER");
	private String state;
	private DISPOSITION(String state) {
		this.state = state;
	}
	public String getState() {
		return state;
	}
}
