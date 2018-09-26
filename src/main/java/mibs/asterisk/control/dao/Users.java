package mibs.asterisk.control.dao;

public class Users {
	 private Long id;
	 protected String name;
	 protected String password;
	 protected String role;
	 public void setId(Long i) {
		 id = i;
	 }
	 public Long getId() {
		 return id;
	 }
	 public void setName(String s) {
		 name = s;
	 }
	 public String getName() {
		 return name;
	 }
	 public void setPassword(String s) {
		 password = s;
	 }
	 public String getPassword() {
		 return password;
	 } 
	 public void setRole(String s) {
		 role = s;
	 }
	 public String getRole() {
		 return role;
	 }
	 @Override
	 public String toString() {
		 return "Name: [" + name + "] password: [" + password + "]"; 
	 }
}
