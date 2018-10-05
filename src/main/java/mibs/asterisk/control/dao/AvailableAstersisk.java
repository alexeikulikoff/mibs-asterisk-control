package mibs.asterisk.control.dao;

import java.util.ArrayList;
import java.util.List;

public class AvailableAstersisk {
	List<Asterisk> asterisks = new ArrayList<>();
	public void addAsterisk(Asterisk a) {
		asterisks.add( a );
	}
	public List<Asterisk> getAsterisks() {
		return asterisks;
	}
	
}
