package mibs.asterisk.control.utils;

public class PNameQ {

	/* ADD YOUR CODE HERE */

	private long p;
	private String name;
	private long q;

	public PNameQ(long p, String name, long q) {
		this.p = p;
		this.name = name;
		this.q = q;
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
