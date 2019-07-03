package mibs.asterisk.control.dao;

import java.util.List;

public class SelectedPhoneContainer {

	private List<SelectedPhone> container;

	public List<SelectedPhone> getContainer() {
		return container;
	}

	public void setContainer(List<SelectedPhone> container) {
		this.container = container;
	}

	@Override
	public String toString() {
		return "SelectedPhoneContainer [container=" + container + "]";
	}
	
}
