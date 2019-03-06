package mibs.asterisk.control.dao;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueueSpell {
	private Long id;
	private String agent;
	private String queue;
	private String peer;
	private String addTime;
	private String removeTime;
	private long duration;
	private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public long getDuration() {
		
	    LocalDateTime ld1 = LocalDateTime.parse( addTime , fmt);
	    LocalDateTime ld2 = LocalDateTime.parse( removeTime , fmt);
	    Duration d =  Duration.between(ld1, ld2);
	    return d.toMinutes();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getRemoveTime() {
		return removeTime;
	}
	public void setRemoveTime(String removeTime) {
		this.removeTime = removeTime;
		
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getPeer() {
		return peer;
	}
	public void setPeer(String peer) {
		this.peer = peer;
	}
	@Override
	public String toString() {
		return "QueueSpell [id=" + id + ", agent=" + agent + ", queue=" + queue + ", peer=" + peer + ", addTime="
				+ addTime + ", removeTime=" + removeTime + ", duration=" + duration + ", fmt=" + fmt + "]";
	}
	
}
