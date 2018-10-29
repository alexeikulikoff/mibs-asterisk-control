package mibs.asterisk.control.utils;

import java.util.ArrayList;
import java.util.List;

public class FSContainer {
	private PNameQ tr;
	private int level;

	private List<FSContainer> fs;
	
	public FSContainer(PNameQ r) {
		tr = r;
		fs = new ArrayList<>();
	}
	public int getSize() {
		return fs.size();
	}
	public List<FSContainer> getContainers(){
		return fs;
	}
	public PNameQ getPNameQ() {
		return tr;
	}
	public void addContainer(FSContainer f) {
		fs.add(f);
	}
	@Override
	public String toString() {
		String rs = "\n";
		for(int i=0; i < fs.size(); i++) {
			rs += fs.get(i) + " " + "\n";
		}
		return "level: " + level + "  " +   tr.toString() +  rs ;
	}
	public FSContainer getContainerByP(int p) {
		return (p < fs.size())? fs.get(p) : null;
	}
	public List<FSContainer> getContainersByQ(int q){
		List<FSContainer> result = new ArrayList<>();
		for(int i=0; i < fs.size(); i++) {
			if (fs.get(i).tr.getQ() == q) result.add(fs.get(i));
		}
		return result.size() > 0 ? result: null;

}

	}
	


