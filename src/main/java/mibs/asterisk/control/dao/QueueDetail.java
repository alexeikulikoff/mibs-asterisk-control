package mibs.asterisk.control.dao;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueueDetail {
	private Long id;
	private String callTime;
	private String src;
	private String duration;
	private String uniqueid;
	public QueueDetail(Long id, String callTime, String src, String duration, String uniqueid) {
		super();
		this.id = id;
		this.callTime = LocalDateTime.parse(callTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")).format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy "));
		this.src = src;
		String sd = duration.contains(".")? duration.substring(0, duration.lastIndexOf(".")) : duration;
		Integer s = Integer.parseInt(sd);
		this.duration = String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
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
