package mibs.asterisk.control.dao;

public class PayLoadRecord implements Comparable<PayLoadRecord>{

	private int id;
	private String time1;
	private String time2;
	private int answered;
	private int unanswered;
	private int prediction;
	private int agents;  // Agents per queue 
	private int persent;
	
	public PayLoadRecord(int id, String time1, String time2) {
		super();
		this.id = id;
		this.time1 = time1;
		this.time2 = time2;
		
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getPersent() {
		return persent;
	}


	public void setPersent(int persent) {
		this.persent = persent;
	}


	public String getTime1() {
		return time1;
	}
	public void setTime1(String time1) {
		this.time1 = time1;
	}
	public String getTime2() {
		return time2;
	}
	public void setTime2(String time2) {
		this.time2 = time2;
	}
	public int getAnswered() {
		return answered;
	}
	public void setAnswered(int answered) {
		this.answered = answered;
	}
	public int getUnanswered() {
		return unanswered;
	}


	public void setUnanswered(int unanswered) {
		this.unanswered = unanswered;
	}


	public int getPrediction() {
		return prediction;
	}


	public void setPrediction(int prediction) {
		this.prediction = prediction;
	}
	public int getAgents() {
		return agents;
	}

	public void setAgents(int agents) {
		this.agents = agents;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PayLoadRecord other = (PayLoadRecord) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "PayLoadRecord [id=" + id + ", time1=" + time1 + ", time2=" + time2 + ", answered=" + answered
				+ ", unanswered=" + unanswered + ", prediction=" + prediction + ", agents=" + agents + ", persent="
				+ persent + "]";
	}


	@Override
	public int compareTo(PayLoadRecord o) {
		return id - o.id;
	}
	
	
	
	
	
}
