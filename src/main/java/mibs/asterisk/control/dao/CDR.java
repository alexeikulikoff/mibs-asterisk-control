package mibs.asterisk.control.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CDR {
	private int id;
	private String calldate;
	private String clid;
	private String src;
	private String dst;
	private String dcontext;
	private String lastapp;
	private String lastdata;
	private String duration;
	private String billsec;
	private String disposition;
	private String channel;
	private String dstchannel;
	private String amaflags;
	private String accountcode;
	private String uniqueid;
	private String userfield;
	private String answer;
	private String end;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCalldate() {
		
		return  LocalDateTime.parse(calldate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")).format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) ;
		
	}
	public void setCalldate(String calldate) {
		this.calldate = calldate;
	}
	public String getClid() {
		return clid;
	}
	public void setClid(String clid) {
		this.clid = clid;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getDst() {
		return dst;
	}
	public void setDst(String dst) {
		this.dst = dst;
	}
	public String getDcontext() {
		return dcontext;
	}
	public void setDcontext(String dcontext) {
		this.dcontext = dcontext;
	}
	public String getLastapp() {
		return lastapp;
	}
	public void setLastapp(String lastapp) {
		this.lastapp = lastapp;
	}
	public String getLastdata() {
		return lastdata;
	}
	public void setLastdata(String lastdata) {
		this.lastdata = lastdata;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getBillsec() {
		return billsec;
	}
	public void setBillsec(String billsec) {
		this.billsec = billsec;
	}
	public String getDisposition() {
		return disposition;
	}
	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		
		this.channel = (channel != null ) ?  channel.lastIndexOf("-") > -1  ? channel.substring(0, channel.lastIndexOf("-")) : channel : " ";
	}
	public String getDstchannel() {
		
		return dstchannel;
	}
	public void setDstchannel(String dstchannel) {
		
		this.dstchannel = (dstchannel != null ) ?  dstchannel.lastIndexOf("-") > -1  ? dstchannel.substring(0, dstchannel.lastIndexOf("-")) : dstchannel : " "; 

	}
	public String getAmaflags() {
		return amaflags;
	}
	public void setAmaflags(String amaflags) {
		this.amaflags = amaflags;
	}
	public String getAccountcode() {
		return accountcode;
	}
	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}
	public String getUniqueid() {
		return uniqueid;
	}
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	public String getUserfield() {
		return userfield;
	}
	public void setUserfield(String userfield) {
		this.userfield = userfield;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	@Override
	public String toString() {
		return "CDR [id=" + id + ", calldate=" + calldate + ", clid=" + clid + ", src=" + src + ", dst=" + dst
				+ ", dcontext=" + dcontext + ", lastapp=" + lastapp + ", lastdata=" + lastdata + ", duration="
				+ duration + ", billsec=" + billsec + ", disposition=" + disposition + ", channel=" + channel
				+ ", dstchannel=" + dstchannel + ", amaflags=" + amaflags + ", accountcode=" + accountcode
				+ ", uniqueid=" + uniqueid + ", userfield=" + userfield + ", answer=" + answer + ", end=" + end + "]";
	}
	
}
