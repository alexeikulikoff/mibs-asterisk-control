package mibs.asterisk.control.dao;

public class ActionResult {
	 private String message;
	 public ActionResult(String message){
		 this.message = message;
	 }
	 public String getMessage(){
		 return this.message;
	 }
}
