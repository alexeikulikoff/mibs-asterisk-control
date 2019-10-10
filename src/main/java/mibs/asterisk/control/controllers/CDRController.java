package mibs.asterisk.control.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import mibs.asterisk.control.dao.CDR;
import mibs.asterisk.control.dao.CDRQuery;
import mibs.asterisk.control.dao.CDRReport;
import mibs.asterisk.control.dao.ConsolidateQuery;
import mibs.asterisk.control.dao.PerHoursQuery;
import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.repository.ConfigurationRepository;
import mibs.asterisk.control.utils.MonthCall;
import mibs.asterisk.control.utils.MonthCell;
import mibs.asterisk.control.utils.MonthReportContainer;
import mibs.asterisk.control.utils.CallContainer;
import mibs.asterisk.control.utils.Call;




@Controller
public class CDRController implements ReportController{

	
	static Logger logger = LoggerFactory.getLogger(CDRController.class);
	@Autowired
	private ConfigurationRepository configurationRepository;
	
	private Optional<Integer> getAnswered(String queue, String d1, String d2, Connection connect) {
		Optional<Integer> result ;
		String sql = "select count(id) as a1  from queue_log where queuename ='" + queue +  "' and time between '" + d1 + "' and '" + d2 + "' and event='CONNECT'";
		try(	
				Statement statement = connect.createStatement();
				ResultSet rs = statement.executeQuery( sql )) 
			{
			   result = rs.first() ? Optional.of(new Integer(rs.getInt("a1"))) : Optional.empty();
			}catch (Exception e) {
			   result = Optional.empty();
			   logger.error("Error while getting getAnswered for sql: " + sql);
			}
		return result;	
	}
	private Optional<Integer> getUnanswered(String queue, String d1, String d2, Connection connect) {
		Optional<Integer> result ;
		String sql = "select count(id) as a1  from queue_log where queuename ='" + queue +  "' and time between '" + d1 + "' and '" + d2 + "' and event='ABANDON'";
		//logger.info("getUnanswered " + sql);
		try(	
				Statement statement = connect.createStatement();
				ResultSet rs = statement.executeQuery( sql )) 
			{
			   result = rs.first() ? Optional.of(new Integer(rs.getInt("a1"))) : Optional.empty();
			}catch (Exception e) {
			   result = Optional.empty();
			   logger.error("Error while getting getUnanswered for sql: " + sql);
			   
			}
		return result;	
	}
	private Optional<Integer> getEnter(String queue, String d1, String d2, Connection connect) {
		Optional<Integer> result ;
		String sql = "select count(id) as a1  from queue_log where queuename ='" + queue +  "' and time between '" + d1 + "' and '" + d2 + "' and event='ENTERQUEUE' and agent='NONE'";
		try(	
				Statement statement = connect.createStatement();
				ResultSet rs = statement.executeQuery( sql )) 
			{
			   result = rs.first() ? Optional.of(new Integer(rs.getInt("a1"))) : Optional.empty();
			}catch (Exception e) {
			   result = Optional.empty();
			   logger.error("Error while getting getEnter for sql: " + sql);
			   
			}
		return result;	
	}
	private Optional<String> queueName(int id, Connection connect) {
		Optional<String> result ;
		String sql = "select name from queues where id = " + id;
		try(	
				Statement statement = connect.createStatement();
				ResultSet rs = statement.executeQuery( sql )) 
			{
			   result = rs.first() ? Optional.of(new String(rs.getString("name"))) : Optional.empty();
			}catch (Exception e) {
			   result = Optional.empty();
			   logger.error("Error while getting queue for id:" +id + "  with message:" +  e.getMessage());
			   
			}
		return result;	
	}
	
	
	@RequestMapping(value = { "/showPerhours" }, method = { RequestMethod.POST })
	public @ResponseBody MonthReportContainer showPerhours(@RequestBody PerHoursQuery query ) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		logger.info("ConsolidateQuery:" + query);
		MonthReportContainer container  = null;
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getPbxid()));
		if(!entity.isPresent()) return null;
		
		String dsURL = "jdbc:mysql://" + entity.get().getDbhost() + ":3306/" + entity.get().getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
		try(
			Connection connect = DriverManager.getConnection(dsURL, entity.get().getDbuser(), entity.get().getDbpassword());
			Statement statement = connect.createStatement())
			{
			Optional<String> qopt = queueName(Integer.parseInt(query.getQueuename()),connect);
			if (qopt.isPresent()) {
				String queue = qopt.get();

				int year = Integer.valueOf(query.getYear());
				int month1 = Integer.valueOf( query.getMonth1() );
				
				LocalDate d0 = LocalDate.of(year, month1, 1);
				int monthL = d0.lengthOfMonth();
				
				LocalDateTime EndMonth = LocalDateTime.of(year, month1, monthL , 23, 59);
			
				LocalDateTime currDay =  LocalDateTime.of(year, month1, 1, 7, 0);
				int j = 0;
				container = new MonthReportContainer();
				while(currDay.isBefore(EndMonth)) {
					LocalDateTime EndHours = LocalDateTime.of(year, month1, currDay.getDayOfMonth() , 23, 59);
					LocalDateTime currHour = LocalDateTime.of(year, month1,  currDay.getDayOfMonth(), 7, 0);
				
					
					int ha = 0;
					int hu = 0;
					int he = 0;
					int i=0;
					while(currHour.isBefore(EndHours)) {
						currHour = currHour.plusHours(1);
						String d2 = currHour.plusMinutes(59).plusSeconds(59).format(formatter);
						String d1 = currHour.format(formatter);
						String sql = "select * from consolidate where calldate between '" + d1 + "' and '" + d2 + "' and queue='" + queue + "'";
						ResultSet rs = statement.executeQuery( sql ) ;
						MonthCell mc = new MonthCell();
						if ( rs.first()) {
							int abandon = rs.getInt("abandon");
							mc.setAbandon( abandon );
							int enter = rs.getInt("eneter");
							mc.setEneter(enter);
							int answered = rs.getInt("connect");
							mc.setConnect(answered);
							double agents = (enter > 0) ? (double) (rs.getInt("agents")/enter) : 0;
						
							mc.setAgents( (int)agents );
							double payload = answered > 0 ? Math.round((answered + abandon) * agents/answered) - agents : 0;
						
							mc.setPayload((int)payload);
							 
							ha += answered;
							hu += abandon;
							he += enter;
 						}else {
							mc.setAbandon(0);
							mc.setEneter(0);
							mc.setConnect(0);
							mc.setAgents(0);
						}
						container.setCells(i, j, mc);
						i++;
					}
					
					MonthCell mc = new MonthCell();
					mc.setAbandon( hu );
					mc.setEneter( he );
					mc.setConnect( ha );
					
					int c = (he > 0) ? (int) Math.round(100 * (double) hu/ (double)he ) : 0 ;
					mc.setValue1(c);
					container.setCells(i++, j, mc);
					j++;
					currDay = currDay.plusDays(1);
				}
			}	
			}catch (Exception e) {
				 logger.error("Error in showConsolidate  with message:" +  e.getMessage());
			}
		
		for(int i=0; i < 17 ;i++) {
			int a = 0;
			int u = 0;
			int e = 0;
			for (int j = 0; j  < 31; j ++) {
				a = a + container.getCells()[i][j].getConnect();
				u = u + container.getCells()[i][j].getAbandon();
				e = e + container.getCells()[i][j].getEneter();
			}
			MonthCell mc = new MonthCell();
			mc.setAbandon(u);
			mc.setEneter(e);
			mc.setConnect(a);
			int c = (e > 0) ? (int) Math.round(100 * (double) u/ (double)e ) : 0 ;
			mc.setValue1(c);
			container.setCells(i, 31, mc);
		}
		int a = 0;
		int u = 0;
		int e = 0;
		int v = 0;
		int count = 0;
		for(int i=0; i < 17; i++) {
			a = a + container.getCells()[i][31].getConnect();
			u = u + container.getCells()[i][31].getAbandon();
			e = e + container.getCells()[i][31].getEneter();
			v = v + container.getCells()[i][31].getValue1();
			count += container.getCells()[i][31].getValue1() > 0 ? 1 : 0;
		}
		v = count > 0 ? v / count : 0;
		MonthCell mc = new MonthCell();
		mc.setAbandon(u);
		mc.setEneter(e);
		mc.setConnect(a);
		mc.setValue1(v);
		container.setCells(17, 31, mc);
		return container;
	}
	@RequestMapping(value = { "/showConsolidate" }, method = { RequestMethod.POST })
	public @ResponseBody CallContainer showConsolidate(@RequestBody ConsolidateQuery query ) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		logger.info("ConsolidateQuery:" + query);
		CallContainer container = null;
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getPbxid()));
		if(!entity.isPresent()) return null;
		String dsURL = "jdbc:mysql://" + entity.get().getDbhost() + ":3306/" + entity.get().getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
		try(
			Connection connect = DriverManager.getConnection(dsURL, entity.get().getDbuser(), entity.get().getDbpassword());
			Statement statement = connect.createStatement()
				)
			{
			Optional<String> qopt = queueName(Integer.parseInt(query.getQueuename()),connect);
			if (qopt.isPresent()) {
				String queue = qopt.get();
				container = new CallContainer(query.getDate1(), query.getDate2());
				for(MonthCall d : container.getData()) {
					
					for(Call c : d.getCalls()) {
						
						LocalDateTime lt1 = LocalDateTime.parse(c.getStartDate(),formatter);
						LocalDateTime lt2 = lt1.plusHours(23).plusMinutes(59).plusSeconds(59);
						
						String sql = "select sum(eneter) as e, sum(connect) as c, sum(abandon) as a from consolidate where calldate between '" + lt1.format(formatter) + "' and '" + lt2.format(formatter) +  "' and queue='" + queue + "'" ;
						ResultSet rs = statement.executeQuery( sql ) ;
						if ( rs.first()) {
							c.setAccepted( rs.getInt("e"));
							c.setAnswered( rs.getInt("c") );
							c.setUnanswered(rs.getInt("a"));
						}else {
							c.setAccepted( 0);
							c.setAnswered( 0 );
							c.setUnanswered(0);
						}
					}
					d.calculate();
				}
				container.calculate();
			}else {
				 logger.error("Error Queue is not found for  id:" + query.getQueuename());
			}
			
		}catch (Exception e) {
			 logger.error("Error in showConsolidate  with message:" +  e.getMessage());
		}
		return container;
		
	}
	
	@RequestMapping(value = { "/showCDR" }, method = { RequestMethod.POST })
	public @ResponseBody CDRReport showCDR(@RequestBody CDRQuery query) {
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getId()));
		if(!entity.isPresent()) return new CDRReport(null, null);
		String dsURL = "jdbc:mysql://" + entity.get().getDbhost() + ":3306/" + entity.get().getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
		try(
			Connection connect = DriverManager.getConnection(dsURL, entity.get().getDbuser(), entity.get().getDbpassword());
			Statement statement = connect.createStatement())
			{
				Optional<List<CDR>> r = prepareCDR(query, statement);
				Optional<Integer> p = getPageCount(query, statement);
				CDRReport result  = (r.isPresent() && p.isPresent()) ?  new CDRReport(r.get(), getTabs(p.get(),query.getPage())) : new CDRReport(null, null) ;
				return result;	
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new CDRReport(null, null);
			}
	}
	private Optional<Integer> getPageCount(CDRQuery query, Statement statement) throws SQLException {
		LocalDate ld1 = LocalDate.parse(query.getDate1(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		LocalDate ld2 = LocalDate.parse(query.getDate2(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		String sql = "select count(id) as total from cdr where disposition='" + query.getDisposition() + "' and calldate between '" + ld1 +"' and '" + ld2 + "' and " + query.getDirection() + "  like '" + query.getPhone() + "'";  
		ResultSet rs = statement.executeQuery( sql );
		return rs.first() ? Optional.of(rs.getInt("total")/ReportController.LINES_NUMBER) : Optional.empty();
		
	}
	private Optional<List<CDR>> prepareCDR( CDRQuery query, Statement statement) throws SQLException{
		List<CDR> cdrs = new ArrayList<>();
		LocalDate ld1 = LocalDate.parse(query.getDate1(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		LocalDate ld2 = LocalDate.parse(query.getDate2(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		int page = query.getPage();
		String sql = "select id, calldate, clid, src, dst, duration, disposition, uniqueid, channel, dstchannel from cdr where disposition='" + query.getDisposition() + "' and calldate between '" + ld1 +"' and '" + ld2 + "' and " + query.getDirection() + "  like '" + query.getPhone() + "'  order by id limit " + ReportController.LINES_NUMBER  * ( page-1) + "  , " + ReportController.LINES_NUMBER  * page;  
		ResultSet rs = statement.executeQuery( sql );
		while (rs.next()) {
			CDR cdr = new CDR();
			cdr.setId(rs.getInt("id"));
			cdr.setCalldate(rs.getString("calldate"));
			cdr.setClid(rs.getString("clid"));
			cdr.setSrc(rs.getString("src"));
			cdr.setDst(rs.getString("dst"));
			cdr.setDuration(rs.getString("duration"));
			cdr.setDisposition(rs.getString("disposition"));
			cdr.setUniqueid(rs.getString("uniqueid"));
			cdr.setChannel(rs.getString("channel"));
			cdr.setDstchannel(rs.getString("dstchannel"));
			cdrs.add(cdr);
		};
		return cdrs.size() > 0 ? Optional.of(cdrs) : Optional.empty() ;
	}
	
}
