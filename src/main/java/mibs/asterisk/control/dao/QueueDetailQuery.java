package mibs.asterisk.control.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueueDetailQuery {

	private String date1;
	private String date2;
	private String peer;
	private String queue;
	private Long pbxid;
	
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = LocalDateTime.parse(date1, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")) ;;
	}
	public String getDate2() {
		return date2;
	}
	public void setDate2(String date2) {
		this.date2 = LocalDateTime.parse(date2, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")) ;;
	}
	public String getPeer() {
		return peer;
	}
	public void setPeer(String peer) {
		this.peer = peer;
	}
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public Long getPbxid() {
		return pbxid;
	}
	public void setPbxid(Long pbxid) {
		this.pbxid = pbxid;
	}
	@Override
	public String toString() {
		return "QueueDetailQuery [date1=" + date1 + ", date2=" + date2 + ", peer=" + peer + ", queue=" + queue
				+ ", pbxid=" + pbxid + "]";
	}
	
}
