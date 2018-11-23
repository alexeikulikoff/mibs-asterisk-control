package mibs.asterisk.control.dao;

public class QueueDetail {
	private Long id;
	private String callTime;
	private String src;
	private String duration;
	private String uniqueid;
	public QueueDetail(Long id, String callTime, String src, String duration, String uniqueid) {
		super();
		this.id = id;
		this.callTime = callTime;
		this.src = src;
		this.duration = duration;
		this.uniqueid = uniqueid;
	}
	public Long getId() {
		return id;
	}
	public String getCallTime() {
		return callTime;
	}
	public String getSrc() {
		return src;
	}
	public String getDuration() {
		return duration;
	}
	public String getUniqueid() {
		return uniqueid;
	}
	
	
	
	
	
}
