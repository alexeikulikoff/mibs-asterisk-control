package mibs.asterisk.control.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mibs.asterisk.control.dao.CDRQuery;
import mibs.asterisk.control.dao.PageTab;

public interface ReportController {
	static final int LINES_NUMBER = 50;
	
	default  List<PageTab> getTabs(int PageCount, int activePage){
		List<PageTab> tabs = new ArrayList<PageTab>();
		if (activePage==1){
			tabs.add(new PageTab(activePage-1,"paginate_button previous disabled","Previous",""));
		}
		else{
			tabs.add(new PageTab(activePage-1,"paginate_button previous","Previous",""));
		}
		if (PageCount <= 7){
			for(int i=1; i <= PageCount; i++){
				if (i == activePage ){
					tabs.add(new PageTab(i,"paginate_button active",""+i,""));
					
				}else{
					tabs.add(new PageTab(i,"paginate_button",""+i,""));
				}
			}
		}else{
			if(activePage >=1 && activePage <= 4){
				for(int i=1; i <= 5; i++){
					if (i == activePage ){
						tabs.add(new PageTab(i,"paginate_button active",""+i,""));
						
					}else{
						tabs.add(new PageTab(i,"paginate_button",""+i,""));
					}
				}
				tabs.add(new PageTab(6,"paginate_button disabled","...",""));
				tabs.add(new PageTab(PageCount,"paginate_button","" + PageCount,""));
			}
			if(activePage > 4 && activePage <= PageCount - 4){
				tabs.add(new PageTab(1,"paginate_button","1",""));
				tabs.add(new PageTab(2,"paginate_button disabled","...",""));
				tabs.add(new PageTab(activePage-1,"paginate_button","" + (activePage-1),""));
				tabs.add(new PageTab(activePage,"paginate_button active","" + activePage,""));
				tabs.add(new PageTab(activePage+1,"paginate_button","" + (activePage+1),""));
				tabs.add(new PageTab(activePage+2,"paginate_button disabled","...",""));
				tabs.add(new PageTab(PageCount,"paginate_button","" + PageCount,""));
				
			}
			if(activePage > (PageCount - 4) && (PageCount - 4) > 0){
				tabs.add(new PageTab(1,"paginate_button","1",""));
				tabs.add(new PageTab(2,"paginate_button disabled","...",""));
				for(int i= PageCount-4; i <= PageCount; i++){
					if (i == activePage){
						tabs.add(new PageTab(i,"paginate_button active",""+i,""));
						
					}else{
						tabs.add(new PageTab(i,"paginate_button",""+i,""));
					}
				}
			}
		}	
		if (activePage==PageCount){
			tabs.add(new PageTab(activePage + 1,"paginate_button next disabled","Next",""));
		}
		else{
			tabs.add(new PageTab(activePage + 1,"paginate_button next","Next",""));
		}
		return tabs;
	}
}
