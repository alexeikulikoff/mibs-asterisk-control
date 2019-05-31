package mibs.asterisk.control.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Call {
	private long id;
	private String mDate;
	private String startDate;
	private String endDate;
	private int answered;
	private int unanswered;
	private int accepted;
	private int dayofweek;
	
	public Call(long id, String startDate, String endDate,  int dayofweek) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dayofweek = dayofweek;
		
		mDate = LocalDate.parse(startDate.split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")).format( DateTimeFormatter.ofPattern("dd LLLL yyyy")); 
	}
	public int getDayofweek() {
		return dayofweek;
	}
	public String getmDate() {
		return mDate;
	}

	public long getId() {
		return id;
	}
	public int getAnswered() {
		return answered;
	}
	public int getUnanswered() {
		return unanswered;
	}
	public int getAccepted() {
		return accepted;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setAnswered(int answered) {
		this.answered = answered;
	}
	public void setUnanswered(int unanswered) {
		this.unanswered = unanswered;
	}
	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}
	public String getEndDate() {
		return endDate;
	}
	@Override
	public String toString() {
		return "Call [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", answered=" + answered
				+ ", unanswered=" + unanswered + ", accepted=" + accepted + "]\n";
	}
	
	
}


