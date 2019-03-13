package mibs.asterisk.control.dao;

public class Equipments {
	private Long id;  
	private String phone; 
	private String office; 
	private Long p; 
	private String ipaddress; 
	private String netmask; 
	private String gateway; 
	private String password;
	private String person;
	private String recordIn;
	private String recordOut;
	private String external;
	private Long templateid;
	private String templatename;
	
	public Long getTemplateid() {
		return templateid;
	}
	public void setTemplateid(Long templateid) {
		this.templateid = templateid;
	}
	public String getTemplatename() {
		return templatename;
	}
	public void setTemplatename(String templatename) {
		this.templatename = templatename;
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
	
}
