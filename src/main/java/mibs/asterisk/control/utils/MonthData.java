package mibs.asterisk.control.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonthData {
	private long id;
	private int month;
	private int totalAnswered;
	private int totalUnanswered;
	private int totalAccepted;
	private List<ConsLine> consLine ;
	public MonthData(long id, int month) {
		super();
		this.id = id;
		this.month = month;
		this.consLine = new ArrayList<>();;
		LocalDate ld1 =  LocalDate.of(LocalDate.now().getYear(), this.month , 1); 
		int monthLength = ld1.lengthOfMonth();
		for(int i= 0; i < monthLength; i++ ) {
			consLine.add( new ConsLine(i, ld1.plusDays(i) + " 00:00:01",  ld1.plusDays(i) + " 23:59:59"  ));
		}
		
	}
	public long getId() {
		return id;
	}
	public int getMonth() {
		return month;
	}
	public int getTotalAnswered() {
		return totalAnswered;
	}
	public int getTotalUnanswered() {
		return totalUnanswered;
	}
	public int getTotalAccepted() {
		return totalAccepted;
	}
	public List<ConsLine> getConsLine() {
		return consLine;
	}
	public void setTotalAnswered(int totalAnswered) {
		this.totalAnswered = totalAnswered;
	}
	public void setTotalUnanswered(int totalUnanswered) {
		this.totalUnanswered = totalUnanswered;
	}
	public void setTotalAccepted(int totalAccepted) {
		this.totalAccepted = totalAccepted;
	}
	public void setConsLine(List<ConsLine> consLine) {
		this.consLine = consLine;
	}
	@Override
	public String toString() {
		return "MonthData [id=" + id + ", month=" + month + ", totalAnswered=" + totalAnswered + ", totalUnanswered="
				+ totalUnanswered + ", totalAccepted=" + totalAccepted + "\n" + "consLine=" + consLine + "]";
	}
	
	
	
}
