package mibs.asterisk.control.dao;

public class CDRQuery {
	
	private int id;
	private String date1;
	private String date2;
	private String phone;
	private int page;
	private String disposition;
	private String direction;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDirection() {
		return direction.equals("IN") ? "dst" : "src";
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getDisposition() {
		return disposition;
	}
	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	public String getDate2() {
		return date2;
	}
	public void setDate2(String date2) {
		this.date2 = date2;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
}
