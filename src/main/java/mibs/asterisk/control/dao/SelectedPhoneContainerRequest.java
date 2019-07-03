package mibs.asterisk.control.dao;

import java.util.List;

public class SelectedPhoneContainerRequest {
	private String pbxid;
	private String date1;
	private String date2;
	private List<SelectedPhone> phones;
	
	public String getPbxid() {
		return pbxid;
	}
	public void setPbxid(String pbxid) {
		this.pbxid = pbxid;
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
	
	public List<SelectedPhone> getPhones() {
		return phones;
	}
	public void setPhones(List<SelectedPhone> phones) {
		this.phones = phones;
	}
	@Override
	public String toString() {
		return "SelectedPhoneContainerRequest [pbxid=" + pbxid + ", date1=" + date1 + ", date2=" + date2 + ", phones="
				+ phones + "]";
	}
	
}
