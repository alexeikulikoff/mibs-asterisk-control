package mibs.asterisk.control.dao;

import java.util.List;

public class CDRReportWrapper {
	private List<CDR> records;
	private List<PageTab> tabs;
	
	public CDRReportWrapper(List<CDR> records, List<PageTab> tabs) {
		super();
		this.records = records;
		this.tabs = tabs;
	}
	public List<CDR> getRecords() {
		return records;
	}

	public List<PageTab> getTabs() {
		return tabs;
	}

}
