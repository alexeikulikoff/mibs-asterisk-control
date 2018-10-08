package mibs.asterisk.control.utils;

import java.util.ArrayList;
import java.util.List;

import mibs.asterisk.control.dao.Equipments;

public class PNameQ {
	private long p;
	private String name;
	private long q;
	List<Equipments> equipments;
	public PNameQ(long p, String name, long q) {
		this.p = p;
		this.name = name;
		this.q = q;
		equipments = new ArrayList<>();
	}

	public void addEquipments(Equipments e) {
		equipments.add(e);
	}
	public List<Equipments> getEquipments(){
		return equipments;
	}
	public long getP() {
		return p;
	}

	public String getName() {
		return name;
	}

	public long getQ() {
		return q;
	}

	@Override
	public String toString() {
		return "p=" + p + "  name=" + name + " q=" + q;
	}

}
