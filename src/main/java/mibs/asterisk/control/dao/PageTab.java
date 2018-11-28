package mibs.asterisk.control.dao;

public class PageTab {
	private int p;
	private String cssClass;
	private String caption;
	private String id;
	
	public PageTab(int p, String cssClass, String caption, String id){
		this.p = p;
		this.cssClass = cssClass;
		this.caption = caption;
		this.id = id;
	}
	public void setP(int i){
		this.p = i;
	}
	public int getP(){
		return this.p;
	}
	public String getCssClass(){
		return this.cssClass;
	}
	public void setCssClass(String s){
		this.cssClass = s;
	}
	public String getCaption(){
		return this.caption;
	}
	public void setCaption(String s){
		this.caption = s;
	}
	public String getId(){
		return this.id;
	}
	public void setid(String s){
		this.id = s;
	}
	 
}
