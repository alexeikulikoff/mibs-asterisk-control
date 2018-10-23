package mibs.asterisk.control.dao;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Units {

	private Long p;
	private String name;
	private Long q;
	public Long getP() {
		return p;
	}
	public void setP(Long p) {
		this.p = p;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getQ() {
		return q;
	}
	public void setQ(Long q) {
		this.q = q;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	private Long pbx;

	public Long getPbx() {
		return pbx;
	}
	public void setPbx(Long pbx) {
		this.pbx = pbx;
	}
	
}
