package mibs.asterisk.control.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetailDataWrapper {

	private LocalDate date1;
	private LocalDate date2;
	private List<DetailData> data;
	private int answered;
	private int unanswered;
	private int accepted;
	
	public DetailDataWrapper(String d1, String d2) {
		super();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		data = new ArrayList<>();
		date1 = LocalDate.parse(d1,formatter);
		date2 = LocalDate.parse(d2,formatter);
		Period p = Period.between(date1,date2);
		
		int i = 0;
		for( i=0 ; i < p.getMonths(); i++) {
			data.add( new DetailData(i , date1.plusMonths(i), date1.plusMonths(i+1) ));
		}
		if (p.getDays() > 0) {
			data.add( new DetailData(i+1 , date1.plusMonths(i), date2 ));
		}
		
	}
	public List<DetailData> getData() {
		return data;
	}
	public void setData(List<DetailData> data) {
		this.data = data;
	}
	public void calculate() {
		Optional<Integer> sum1 = (data.stream().map(s -> s.getTotalAnswered())).reduce((u,v)-> u + v);
		answered = sum1.isPresent() ? sum1.get() : 0;
		Optional<Integer> sum2 = (data.stream().map(s -> s.getTotalUnanswered())).reduce((u,v)-> u + v);
		unanswered = sum2.isPresent() ? sum2.get() : 0;
		Optional<Integer> sum3 = (data.stream().map(s -> s.getTotalAccepted())).reduce((u,v)-> u + v);
		accepted = sum3.isPresent() ? sum3.get() : 0;
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
	@Override
	public String toString() {
		return "DetailDataWrapper [date1=" + date1 + ", date2=" + date2 + ", data=" + data + "]";
	}
	
	
	
	
}
