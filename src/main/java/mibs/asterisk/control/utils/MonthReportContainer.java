package mibs.asterisk.control.utils;

public class MonthReportContainer {
	 
	 private MonthCell[][] cells; 
	 public MonthReportContainer() {
		 cells = new MonthCell[17][32];
		 for(int i=0; i < 17; i++) {
			 for(int j=0; j < 32; j++) {
				 cells[i][j] = new MonthCell();
			 }
		 }
	 }
	public MonthCell[][] getCells() {
		return cells;
	}
	public void setCells(int i, int j , MonthCell cell ) {
		cells[i][j] = cell;
	}
}
