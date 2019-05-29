package mibs.asterisk.control.utils;

import java.util.List;
import java.util.Optional;
public class MonthCall {
	private long id;
	private int month;
	private int totalAnswered;
	private int totalUnanswered;
	private int totalAccepted;
	private List<Call> calls ;
	public MonthCall(long id, int month) {
		super();
		this.id = id;
		this.month = month;
	}
	public long getId() {
		return id;
	}
	public int getMonth() {
		return month;
	}
	public void calculate() {
		Optional<Integer> sum1 = (calls.stream().map(s -> s.getAnswered())).reduce((u,v)-> u + v);
		totalAnswered = sum1.isPresent() ? sum1.get() : 0;
		
		Optional<Integer> sum2 = (calls.stream().map(s -> s.getUnanswered())).reduce((u,v)-> u + v);
		totalUnanswered = sum2.isPresent() ? sum2.get() : 0;
		
		Optional<Integer> sum3 = (calls.stream().map(s -> s.getAccepted())).reduce((u,v)-> u + v);
		totalAccepted = sum3.isPresent() ? sum3.get() : 0;
}
	public int getTotalAnswered() {
		
		Optional<Integer> sum = (calls.stream().map(s -> s.getAnswered())).reduce((u,v)-> u + v);
		totalAnswered = sum.isPresent() ? sum.get() : 0;
		return totalAnswered;
	}
	public int getTotalUnanswered() {
		Optional<Integer> sum = (calls.stream().map(s -> s.getUnanswered())).reduce((u,v)-> u + v);
		totalUnanswered = sum.isPresent() ? sum.get() : 0;
		return totalUnanswered;
	}
	public int getTotalAccepted() {
		Optional<Integer> sum = (calls.stream().map(s -> s.getAccepted())).reduce((u,v)-> u + v);
		totalAccepted = sum.isPresent() ? sum.get() : 0;
		return totalAccepted;
	}
	public List<Call> getCalls() {
		return calls;
	}
	public void setCalls(List<Call> c) {
		this.calls = c;
	}
	@Override
	public String toString() {
		return "Calls [id=" + id + ", month=" + month + ", totalAnswered=" + getTotalAnswered() + ", totalUnanswered="
				+ getTotalUnanswered() + ", totalAccepted=" + getTotalAccepted() + "\n" +  calls + "]";
	}
	
	
	
}
