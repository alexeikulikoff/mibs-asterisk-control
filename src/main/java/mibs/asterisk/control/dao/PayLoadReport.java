package mibs.asterisk.control.dao;


import java.util.TreeSet;

public class PayLoadReport {
	
	private String status;
	TreeSet<PayLoadRecord> payload;
	private int callHandleTime; // Average time of call handling;
	
	private int totalanswered;
	private int totalunanswered;
	private int totalpersent;
	
	
	public PayLoadReport() {
		payload = new TreeSet<>();
		payload.add( new PayLoadRecord(1, "08:00","08:59") );
		payload.add( new PayLoadRecord(2, "09:00","09:59") );
		payload.add( new PayLoadRecord(3, "10:00","10:59") );
		payload.add( new PayLoadRecord(4, "11:00","11:59") );
		payload.add( new PayLoadRecord(5, "12:00","12:59") );
		payload.add( new PayLoadRecord(6, "13:00","13:59") );
		payload.add( new PayLoadRecord(7, "14:00","14:59") );
		payload.add( new PayLoadRecord(8, "15:00","15:59") );
		payload.add( new PayLoadRecord(9, "16:00","16:59") );
		payload.add( new PayLoadRecord(10,"17:00","17:59") );
		payload.add( new PayLoadRecord(11,"18:00","18:59") );
		payload.add( new PayLoadRecord(12,"19:00","19:59") );
		payload.add( new PayLoadRecord(13,"20:00","20:59") );
		payload.add( new PayLoadRecord(14,"21:00","21:59") );
		payload.add( new PayLoadRecord(15,"22:00","22:59") );
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TreeSet<PayLoadRecord> getPayload() {
		return payload;
	}
	public void update(int id, int answered, int unanswered, int prediction, int agents, int persent ) {
		payload.forEach(s->{
			if (s.getId() == id) {
				s.setAnswered(answered);
				s.setUnanswered(unanswered);
				s.setPrediction(prediction);
				s.setAgents(agents);
				s.setPersent(persent);
			}
		});
	}

	public int getCallHandleTime() {
		return callHandleTime;
	}

	public void setCallHandleTime(int callHandleTime) {
		this.callHandleTime = callHandleTime;
	}
	public int getTotalanswered() {
		return totalanswered;
	}
	public void setTotalanswered(int totalanswered) {
		this.totalanswered = totalanswered;
	}
	public int getTotalunanswered() {
		return totalunanswered;
	}
	public void setTotalunanswered(int totalunanswered) {
		this.totalunanswered = totalunanswered;
	}
	public void setPayload(TreeSet<PayLoadRecord> payload) {
		this.payload = payload;
	}
	
	public int getTotalpersent() {
		return totalpersent;
	}
	public void setTotalpersent(int totalpersent) {
		this.totalpersent = totalpersent;
	}
	@Override
	public String toString() {
		return "PayLoadReport [status=" + status + ", payload=" + payload + ", callHandleTime=" + callHandleTime
				+ ", totalanswered=" + totalanswered + ", totalunanswered=" + totalunanswered + "]";
	}
	

	
	
	
	
}
