package mibs.asterisk.control.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "units")
public class UnitsEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "p")
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Long getQ() {
		return q;
	}
	public void setQ(Long q) {
		this.q = q;
	}
	private String unit;
	private Long q;
	private Long pbx;

	public Long getPbx() {
		return pbx;
	}
	public void setPbx(Long pbx) {
		this.pbx = pbx;
	}
	
	
}
