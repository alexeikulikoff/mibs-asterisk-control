package mibs.asterisk.control.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table1")
public class Table1 implements Serializable {
	 /**
	 * 
	 */
	 private static final long serialVersionUID = 1L;
	 @Id
	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	 private Long id;
	 private String name;
	
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
}
