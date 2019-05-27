package mibs.asterisk.control.utils;

public class ConsLine {
	private long id;
	private String startDate;
	private String endDate;
	private int answered;
	private int unanswered;
	private int accepted;
	
	public ConsLine(long id, String startDate, String endDate) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
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
		return "ConsLine [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", answered=" + answered
				+ ", unanswered=" + unanswered + ", accepted=" + accepted + "]\n";
	}
	
	
}


