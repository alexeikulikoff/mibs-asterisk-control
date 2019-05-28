package mibs.asterisk.control.utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class DetailData {
	private long id;
	private int month;
	private int totalAnswered;
	private int totalUnanswered;
	private int totalAccepted;
	private List<ConsLine> consLine ;

	BinaryOperator<Integer> adder = (n1, n2) -> n1 + n2;
	
	public DetailData(long id, LocalDate date1, LocalDate date2) {
		super();
		this.id = id;
		Period p = Period.between(date1, date2);
		month = date1.getMonthValue();
		consLine = new ArrayList<>();;
		int N = p.getMonths() == 0 ? p.getDays() : date1.lengthOfMonth();
		for(int i= 0; i < N; i++ ) {
			ConsLine cs =  new ConsLine(i, date1.plusDays(i) + " 00:00:00",  date1.plusDays(i) + " 23:59:59"  );
			consLine.add(cs);
		}
		
	}
	
	public long getId() {
		return id;
	}
	public int getMonth() {
		return month;
	}
	public void calculate() {
		Optional<Integer> sum1 = (consLine.stream().map(s -> s.getAnswered())).reduce((u,v)-> u + v);
		totalAnswered = sum1.isPresent() ? sum1.get() : 0;
		
		Optional<Integer> sum2 = (consLine.stream().map(s -> s.getUnanswered())).reduce((u,v)-> u + v);
		totalUnanswered = sum2.isPresent() ? sum2.get() : 0;
		
		Optional<Integer> sum3 = (consLine.stream().map(s -> s.getAccepted())).reduce((u,v)-> u + v);
		totalAccepted = sum3.isPresent() ? sum3.get() : 0;
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
	
	public void setConsLine(List<ConsLine> consLine) {
		this.consLine = consLine;
	}
	@Override
	public String toString() {
		return "\nDetailData [id=" + id + ", month=" + month + ", totalAnswered=" + getTotalAnswered() + ", totalUnanswered="
				+ getTotalUnanswered() + ", totalAccepted=" + getTotalAccepted() + "\n" +  consLine + "]";
	}
	
	
	
}
