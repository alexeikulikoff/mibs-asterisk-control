package mibs.asterisk.control.service;

public class AsteriskResponce {
	
	private String content;
	public AsteriskResponce() {};
	
	public AsteriskResponce(String s) {
		content = s;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
