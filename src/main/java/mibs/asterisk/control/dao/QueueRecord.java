package mibs.asterisk.control.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueueRecord {

	public String date;
	public String enterTime;
	public String exitTime;
	public String peer;
	public int calls;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) ;;
	}
	public String getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(String enterTime) {
		this.enterTime = LocalDateTime.parse(enterTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")).format(DateTimeFormatter.ofPattern("HH:mm")) ;
	}
	public String getExitTime() {
		return exitTime;
	
	}
	public void setExitTime(String exitTime) {
		this.exitTime = LocalDateTime.parse(exitTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")).format(DateTimeFormatter.ofPattern("HH:mm")) ;;
	}
	public String getPeer() {
		return peer;
	}
	public void setPeer(String peer) {
		this.peer = peer;
	}
	public int getCalls() {
		return calls;
	}
	public void setCalls(int calls) {
		this.calls = calls;
	}
	
	
}
