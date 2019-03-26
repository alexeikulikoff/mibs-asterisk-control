package mibs.asterisk.control.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "equipments")
public class EquipmentsEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;  
	private String phone; 
	private String office; 
	private Long p; 
	private String ipaddress; 
	private String netmask; 
	private String gateway; 
	private String password;
	private String person;
	@Column(name = "recordin")
	private String recordIn;
	@Column(name = "recordout")
	private String recordOut;
	private String external;
	
	@OneToOne
	@JoinColumn(name = "templateid")
	private TemplateEntity template;
	
	public TemplateEntity getTemplate() {
		return template;
	}
	public void setTemplate(TemplateEntity template) {
		this.template = template;
	}
	public String getRecordIn() {
		return recordIn;
	}
	public void setRecordIn(String recordIn) {
		this.recordIn = recordIn;
	}
	public String getRecordOut() {
		return recordOut;
	}
	public void setRecordOut(String recordOut) {
		this.recordOut = recordOut;
	}
	public String getExternal() {
		return external;
	}
	public void setExternal(String external) {
		this.external = external;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public Long getP() {
		return p;
	}
	public void setP(Long p) {
		this.p = p;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getNetmask() {
		return netmask;
	}
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	@Override
	public String toString() {
		return "EquipmentsEntity [id=" + id + ", phone=" + phone + ", office=" + office + ", p=" + p + ", ipaddress="
				+ ipaddress + ", netmask=" + netmask + ", gateway=" + gateway + ", password=" + password + ", person="
				+ person + ", recordIn=" + recordIn + ", recordOut=" + recordOut + ", external=" + external + "]";
	}  

	  
	  
}
