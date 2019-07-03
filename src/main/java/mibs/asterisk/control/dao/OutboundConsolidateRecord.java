package mibs.asterisk.control.dao;

public class OutboundConsolidateRecord {

	private String phone;
	private long calls;
	private float duration;
	private String duration2;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public long getCalls() {
		return calls;
	}
	public void setCalls(long calls) {
		this.calls = calls;
	}
	public float getDuration() {
		return duration;
	}
	public void setDuration(float duration) {
		this.duration = duration;
	}
	public String getDuration2() {
		return duration2;
	}
	public void setDuration2(String duration2) {
		this.duration2 = duration2;
	}
	
}
