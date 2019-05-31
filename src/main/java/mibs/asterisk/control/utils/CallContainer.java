package mibs.asterisk.control.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class CallContainer {

	private LocalDate date1;
	private LocalDate date2;
	private List<MonthCall> data;
	private int answered;
	private int unanswered;
	private int accepted;
	public CallContainer(String d1, String d2) {
		super();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		date1 = LocalDate.parse(d1,formatter);
		date2 = LocalDate.parse(d2,formatter);
		LinkedHashMap<Integer, List<LocalDate>> mp = new LinkedHashMap<>();
		List<LocalDate> dates  =  new ArrayList<>();
		int m = 0;
		dates.add(date1);
		while(date1.compareTo(date2) < 0) {
			m = date1.getMonthValue();
			date1 = date1.plusDays(1);
			if (m != date1.getMonthValue() ) {
				mp.put(m, dates);
				dates  =  new ArrayList<>();
			}
			dates.add(date1);
		}
		mp.put(m, dates);
		List<MonthCall> monthCalls = new ArrayList<>();
		int j=0;
		for (Map.Entry<Integer, List<LocalDate>> s : mp.entrySet()) {
			MonthCall monthCall = new MonthCall(++j, s.getKey());
			List<Call> calls = new ArrayList<>();
			List<LocalDate> ld = (List<LocalDate>) s.getValue();
			int k=0;
			for(LocalDate d : ld) {
//				String c = d.format( DateTimeFormatter.ofPattern("dd LLLL yyyy"));
				String dc = d.format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				calls.add(new Call(++k, dc + " 00:00:00", dc, d.getDayOfWeek().getValue()));
			}
			monthCall.setCalls(calls);
			monthCalls.add( monthCall );
	    }
		setData( monthCalls );
	
	}
	public void calculate() {
		Optional<Integer> sum1 = (data.stream().map(s -> s.getTotalAnswered())).reduce((u,v)-> u + v);
		answered = sum1.isPresent() ? sum1.get() : 0;
		Optional<Integer> sum2 = (data.stream().map(s -> s.getTotalUnanswered())).reduce((u,v)-> u + v);
		unanswered = sum2.isPresent() ? sum2.get() : 0;
		Optional<Integer> sum3 = (data.stream().map(s -> s.getTotalAccepted())).reduce((u,v)-> u + v);
		accepted = sum3.isPresent() ? sum3.get() : 0;
	}
	public List<MonthCall> getData() {
		return data;
	}
	public void setData(List<MonthCall> data) {
		this.data = data;
	}
	
	public int getAnswered() {
		Optional<Integer> sum = (data.stream().map(s -> s.getTotalAccepted())).reduce((u,v)-> u + v);
		answered = sum.isPresent() ? sum.get() : 0;
		return answered;
	}
	public int getUnanswered() {
		Optional<Integer> sum = (data.stream().map(s -> s.getTotalUnanswered())).reduce((u,v)-> u + v);
		unanswered = sum.isPresent() ? sum.get() : 0;
		return unanswered;
	}
	public int getAccepted() {
		Optional<Integer> sum = (data.stream().map(s -> s.getTotalAccepted())).reduce((u,v)-> u + v);
		accepted = sum.isPresent() ? sum.get() : 0;
		return accepted;
	}
	@Override
	public String toString() {
		return "Wrapper [date1=" + date1 + ", date2=" + date2 + ", data=" + data + "]";
	}
	
	
	
	
}
