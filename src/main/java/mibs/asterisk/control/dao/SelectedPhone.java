package mibs.asterisk.control.dao;

public class SelectedPhone {
	private String id;
	private String phone;
	private String office;
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	@Override
	public String toString() {
		return "SelectedPhone [id=" + id + ", phone=" + phone + ", office=" + office + "]";
	}
	
	
	
}
