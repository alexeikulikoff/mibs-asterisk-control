package mibs.asterisk.control.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.AgentRecord;
import mibs.asterisk.control.dao.AgentReport;
import mibs.asterisk.control.dao.Agents;
import mibs.asterisk.control.dao.CDRQuery;
import mibs.asterisk.control.dao.PayLoadRecord;
import mibs.asterisk.control.dao.PayLoadReport;
import mibs.asterisk.control.dao.Peers;
import mibs.asterisk.control.dao.QueueDetail;
import mibs.asterisk.control.dao.QueueDetailQuery;
import mibs.asterisk.control.dao.QueueQuery;
import mibs.asterisk.control.dao.QueueRecord;
import mibs.asterisk.control.dao.QueueReport;
import mibs.asterisk.control.dao.QueueSpell;
import mibs.asterisk.control.dao.Queues;
import mibs.asterisk.control.dao.QueuesReport;
import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.repository.ConfigurationRepository;

@Controller
public class QueusController  implements ReportController{
	static Logger logger = LoggerFactory.getLogger(QueusController.class);
	
	private static DateTimeFormatter queryFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	//private static DateTimeFormatter mysqlDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	
	private static final String NO_DATA="no_data";
	private static final String DATA_EXIST = "data_exist";
	@Autowired
	private ConfigurationRepository configurationRepository;
	
	private Optional<Agents> getAgent(QueueQuery query, Connection connect) {
		Optional<Agents> result ;
		try ( Statement statement = connect.createStatement())
			{
		       String sql = "select * from agents where id=" + query.getAgentid(); 
			   ResultSet rs = statement.executeQuery( sql );
			   result = rs.first() ? Optional.of(new Agents(rs.getLong("id"),rs.getString("name"), Long.valueOf(query.getPbxid()))) : Optional.empty();
			}catch (Exception e) {
			   result = Optional.empty();
			   logger.error(e.getMessage());
			}
		return result;
	}
	private Optional<Queues> getQueue(QueueQuery query, Connection connect){
		Optional<Queues> result ;
		String sql = "select * from queues where id=" + query.getQueueid();
		try(	
				Statement statement = connect.createStatement();
				ResultSet rs = statement.executeQuery( sql )) 
			{
			   result = rs.first() ? Optional.of(new Queues(rs.getLong("id"),rs.getString("name"), Long.valueOf(query.getPbxid()))) : Optional.empty();
			}catch (Exception e) {
			   result = Optional.empty();
			   logger.error("Error while getting queue for id:" + query.getQueueid() + "  with message:" +  e.getMessage());
			   
			}
		return result;	
	}
	private Optional<Peers> getPeers(Long id, Long pbxid, Connection connect){
		Optional<Peers> result ;
		String sql = "select * from peers where id=" + id;
		try(	
				Statement statement = connect.createStatement();
				ResultSet rs = statement.executeQuery( sql )) 
			{
			   result = rs.first() ? Optional.of(new Peers(rs.getLong("id"),rs.getString("name"), pbxid)) : Optional.empty();
			}catch (Exception e) {
			   result = Optional.empty();
			   logger.error("Error while getting peer for id:" + id + "  with message:" +  e.getMessage());
			   
			}
		return result;	
	}
	private  Optional<List<QueueSpell>> getQuerySpells(QueueQuery query){
		List<QueueSpell> spells = new ArrayList<>();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getPbxid()));
		entity.ifPresent(en->{
			String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
			LocalDate ld1 = LocalDate.parse(query.getDate1(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			LocalDate ld2 = LocalDate.parse(query.getDate2(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			try(
					Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
					Statement statement = connect.createStatement())
					{
						Optional<Agents> optAgents =  getAgent(query, connect);
						Optional<Queues> optQueues =  getQueue(query, connect);
						
						if (!optAgents.isPresent()) { 
							logger.error("Error: Agent is not found!");
							return ;
						}
						if (!optQueues.isPresent()) {
							logger.error("Error: Queue is not found!");
							return ;
						}
						String sql = "select * from members where queueid=" + query.getQueueid() + " and agentid=" + query.getAgentid() + " and time between '" + ld1 + "' and '" + ld2 + "'"; 
						ResultSet rs = statement.executeQuery( sql );
						while (rs.next()) {
							QueueSpell qs = new QueueSpell();
							String event = rs.getString("event");
							if (event.equals("ADDMEMBER")) {
								Optional<Peers> optPeers = getPeers(rs.getLong("peerid"),Long.valueOf(query.getPbxid()),connect) ;	
								if (!optPeers.isPresent()) {
									logger.error("Error: Peer is not found!");
									return ;
								}
								qs.setId(rs.getLong("id"));
								qs.setQueue(optQueues.get().getName());
								qs.setAgent(optAgents.get().getName());
								qs.setPeer( optPeers.get().getName() );
								qs.setAddTime(rs.getString("time"));
								if (rs.next()) {
									event = rs.getString("event");
									Long id = rs.getLong("id");
									if (event.equals("REMOVEMEMBER") & id > qs.getId() ) {
										qs.setRemoveTime(rs.getString("time"));
										spells.add(qs);
										rs.previous();	
									}
								}
						
							}
					  }

					} catch (Exception e) {
						logger.error(e.getMessage());
					}
			
		});
		return  spells.size() > 0 ?  Optional.of(spells) : Optional.empty();
		
	}
	@RequestMapping(value = { "/queueDetail" }, method = { RequestMethod.POST })
	public @ResponseBody QueuesReport showQueueDetail(@RequestBody QueueDetailQuery query){
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getPbxid()));
		if (!entity.isPresent()) return new QueuesReport(null, null);
		String dsURL = "jdbc:mysql://" + entity.get().getDbhost() + ":3306/" + entity.get().getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
		try(
				Connection connect = DriverManager.getConnection(dsURL,  entity.get().getDbuser(),  entity.get().getDbpassword());
				Statement statement = connect.createStatement())
				{
			 		Optional<List<QueueDetail>> r =  prepareQueueDetail(query,statement);
			 		Optional<Integer> p = getPageCount(query, statement);
			 		QueuesReport report = (r.isPresent() && p.isPresent()) ? new QueuesReport(r.get(), getTabs(p.get(),query.getPage())) : new QueuesReport(null, null);
			 		return report;	
				}catch(Exception e) {
					
					logger.error(e.getMessage());
					return  new QueuesReport(null, null);
				}
	}
	private Optional<List<QueueDetail>> prepareQueueDetail(QueueDetailQuery query,Statement statement) throws SQLException{
		List<QueueDetail> queueDetails = new ArrayList<>();
		int page = query.getPage();
		String sql = "select cd.id as id, cd.uniqueid as uniqueid, cd.calldate, cd.src,  cd.duration, ql.agent, ql.queuename from queue_log as ql join cdr as cd on(cd.uniqueid=ql.callid) " + 
				" where ql.queuename='" + query.getQueue() + "'" + 
				" and " + 
				" ql.time between '" + query.getDate1() + "' and '" + query.getDate2() + "'" + 
				" and " + 
				" ql.event='CONNECT'" + 
				" and " + 
				" cd.disposition = 'ANSWERED'"+
				" and" + 
				" ql.agent='" + query.getPeer() + "'" + 
				" group by cd.id limit " + ReportController.LINES_NUMBER  * ( page-1) + "  , " + ReportController.LINES_NUMBER  * page;
	
		ResultSet rs = statement.executeQuery( sql );
		while (rs.next()) {
			queueDetails.add( new QueueDetail(rs.getLong("id"),rs.getString("calldate"), rs.getString("src"), rs.getString("duration"),rs.getString("uniqueid") ) );
		}
		return queueDetails.size() > 0 ? Optional.of(queueDetails) : Optional.empty();
		
	}
	private String zT(long i) {
		return  i > 0 ? ( i < 10 ? "0" + i : i +"") + "" : "00";
	}
	
	
	@RequestMapping(value = { "/showAgentReport" }, method = { RequestMethod.POST })
	public @ResponseBody AgentReport showAgentReport(@RequestBody QueueQuery query) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getPbxid()));
		
		AgentReport agentReport = new AgentReport(DATA_EXIST);
		
		if (!entity.isPresent()) return new AgentReport(NO_DATA);
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
		String sql = "select * from agents";
		
		try(
			Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement();
			ResultSet rs = statement.executeQuery( sql ))
		{
			while(rs.next()) {
				AgentRecord agentRecord = new AgentRecord();
				int id = (int)rs.getLong("id");
				if (id == 23 | id == 24 | id == 25) continue;
				String name = rs.getString("name");
				agentRecord.setId(id);
				agentRecord.setName(name);
				QueueQuery qr = new QueueQuery();
				qr.setAgentid(id);
				qr.setQueueid(query.getQueueid());
				qr.setDate1(query.getDate1());
				qr.setDate2(query.getDate2());
				qr.setPbxid(query.getPbxid());
				Optional<List<QueueSpell>> querySpellsOpt = getQuerySpells( qr );
				if (!querySpellsOpt.isPresent()) {
					agentRecord.setCount(0);
					agentRecord.setDuration("00:00");
					agentRecord.setSecondDuration(0);
					
				}else {
					List<QueueSpell> queueSpells  = querySpellsOpt.get();
					int count = 0;
					Duration d = Duration.ofSeconds(0L);
					
					for(QueueSpell sp : queueSpells) {
						LocalDateTime t1 = LocalDateTime.parse(sp.getAddTime(), formatter);
						LocalDateTime t2 = LocalDateTime.parse(sp.getRemoveTime(), formatter);
						String selectCountSql = "select count(id) as calls from queue_log where time between '"
								+ sp.getAddTime() + "' and '" + sp.getRemoveTime() + "' and queuename='" + sp.getQueue()
								+ "' and agent='" + sp.getPeer() + "' and event ='CONNECT'";
						try (Connection con = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
								Statement state = connect.createStatement();
								ResultSet resultSet = state.executeQuery(selectCountSql)) {
							if (resultSet != null) {
								count += resultSet.next() ? resultSet.getInt("calls") : 0;
								d = d.plus(Duration.between(t1, t2));
							}
						} catch (SQLException e) {
							logger.error("Error while selecting calls count with message^ " + e.getMessage() );
						}
						
					}
					agentRecord.setCount(count);
					agentRecord.setSecondDuration((int) d.toMinutes());
				    
					long hours = d.toHours(); //75
				    long minutes = d.minusHours(hours).toMinutes(); //15
				    agentRecord.setDuration(zT(hours) + ":" + zT(minutes));	
				}
			
				agentReport.add(agentRecord);
				
			}
			agentReport.computeCountDuration();
		
			
		} catch (SQLException e) {
			logger.error("Error while selecting agents with message^ " + e.getMessage() );
			
		}
	
	
		return agentReport;
		
	}
	
	@RequestMapping(value = { "/showQueueReport" }, method = { RequestMethod.POST })
	public @ResponseBody QueueReport showQueueReport(@RequestBody QueueQuery query) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		QueueReport report = new QueueReport();
		Optional<List<QueueSpell>> optSpels = getQuerySpells(query);
		
		if (!optSpels.isPresent()) {
			report.setHaserror("NO_DATA");
			return report;
		}
		List<QueueSpell> spells = optSpels.get();
	
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getPbxid()));
		entity.ifPresent(en->{
			String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
			try(
					Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
					Statement statement = connect.createStatement())
					{
				    report.setAgent(getAgent(query,connect).get().getName());
				    report.setQueue(getQueue(query,connect).get().getName());
					
				    int i=0;
				    int tc = 0;
				    Duration td = Duration.ofHours(0); 
				    for(QueueSpell sp : spells) {
				    	QueueRecord qr = new QueueRecord();
						qr.setId(++i);
						qr.setDate(sp.getAddTime());
						qr.setEnterTime(sp.getAddTime());
						qr.setExitTime(sp.getRemoveTime());
						
						Duration d0 = Duration.between(LocalDateTime.parse(qr.getDate() + " " + qr.getEnterTime(), formatter), LocalDateTime.parse(qr.getDate() + " " + qr.getExitTime(), formatter)); 
						long hours = d0.toHours(); //75
						long minutes = d0.minusHours(hours).toMinutes(); //15
						
						qr.setDuration(zT(hours) + ":" + zT(minutes));
						qr.setPeer(sp.getPeer());
						td = td.plus(d0);
						String sql = "select count(id) as calls from queue_log where time between '" + sp.getAddTime()+"' and '" + sp.getRemoveTime() + "' and queuename='" + sp.getQueue() + "' and agent='" + sp.getPeer()+"' and event ='CONNECT'";
					
						
						try (ResultSet rs = statement.executeQuery( sql )) {
							qr.setCalls( rs.first() ? rs.getInt("calls") : 0 );
							tc += qr.getCalls();
							report.add(qr);
						}catch(Exception e) {
							logger.error(e.getMessage());
						}
				    }
				    report.setTotalcall(tc);
				    long hours = td.toHours(); //75
				    long minutes = td.minusHours(hours).toMinutes(); //15
				    report.setTotalduration(zT(hours) + ":" + zT(minutes));
				}catch(Exception e) {
					logger.error(e.getMessage());
				}
			});
		return report;
	}
	
	@RequestMapping(value = { "/showPayload" }, method = { RequestMethod.POST })
	public @ResponseBody PayLoadReport showPayLoadReport(@RequestBody QueueQuery query) {
	
		PayLoadReport payload = new PayLoadReport();
		
		LocalDate ld1 = LocalDate.parse(query.getDate1(), queryFormatter);
		LocalDate ld2 = LocalDate.parse(query.getDate2(), queryFormatter);
		
		QueueQuery qu = new QueueQuery();
		qu.setDate1(ld1.toString());
		qu.setDate2(ld2.toString());
		qu.setQueueid(query.getQueueid());
		qu.setPbxid(query.getPbxid());
		
		payload.setCallHandleTime(getAverageTime(qu));
		
		Period p = Period.between(ld1, ld2);
	
		try {
			int totalAnswered = 0;
			int totalUnanswered =0;
			for(PayLoadRecord s : payload.getPayload()) {
				int N = p.getDays();
				int an = 0;
				int un = 0;
				int ag = 0;
				double pg = 0;
				for(int i=0; i < N; i++) {
					LocalDate currentDate = ld1.plusDays(i);
					QueueQuery q = new QueueQuery();
					q.setDate1(currentDate + " " + s.getTime1()+":00");
					q.setDate2(currentDate + " " + s.getTime2()+":00");
					q.setQueueid(query.getQueueid());
					q.setPbxid(query.getPbxid());
					an += getAnswersCalls(q) ;
					un += getUnAnswersCalls(q) ;
					ag += getAgets(q) ;
					
				}
				double dan = (double) an /N;
				double dun = (double) un /N;
				double dag = (double) ag / N;
				double per = (dan > 0) ? 100 * dun / dan : 0;
				pg = Math.round((dan + dun) * dag / dan) - dag;
				
				//System.out.println("Answered=" + Math.round(dan) + " Unanswered " + Math.round( dun ) + " Agents " + Math.round( dag ) + " %: " + Math.round( per )  + " D1: " + Math.round(pg));
				totalAnswered += an;
				totalUnanswered += un;

				payload.update(s.getId(),  (int)Math.round(dan),  (int)Math.round(dun), (int)Math.round(pg), (int)Math.round(dag), (int)Math.round(per));
			} 
			payload.setTotalanswered(totalAnswered);
			payload.setTotalunanswered(totalUnanswered);
			int totalpersent = (totalAnswered > 0 ) ? (int) Math.round((double)(100 * totalUnanswered / totalAnswered)) : 0;
			payload.setTotalpersent(totalpersent);
			payload.setStatus("NoError");
		}catch(Exception e) {
			logger.error("Error while creating payload report with message : " + e.getMessage());
			payload.setStatus("Error");
//			e.printStackTrace();
		}
	
		return payload;
	}
	private int getAverageTime(QueueQuery query) {
		int result = 0;
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getPbxid()));
	    if (!entity.isPresent()) return 0;
	    ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
		try(
				Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
				Statement statement = connect.createStatement())
				{
					Optional<Queues> optQueue = getQueue(query,connect);
					if (!optQueue.isPresent()) {
						logger.error("Error! Queue not found for queue id : "  + query.getQueueid());
						return 0;
					}
					String queueName = optQueue.get().getName();
					String sql = "select sum(data2) / count(id)  from queue_log where queuename= '" + queueName + "' and time between '" + query.getDate1() +"' and '" + query.getDate2() + "' and (event = 'COMPLETEAGENT' or event = 'COMPLETECALLER')"; 
					ResultSet rs = statement.executeQuery( sql );
					result = rs.first() ? rs.getInt("answered") : 0;
				} catch (SQLException e) {
					  logger.error("Error while getting answers calls for query:" + query + "  with message:" +  e.getMessage());
				}
			
		return result;
	}
	private int getAnswersCalls(QueueQuery query) {
		int result = 0;
	
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getPbxid()));
	    if (!entity.isPresent()) return 0;
	    ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
		try(
				Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
				Statement statement = connect.createStatement())
				{
					Optional<Queues> optQueue = getQueue(query,connect);
					if (!optQueue.isPresent()) {
						logger.error("Error! Queue not found for queue id : "  + query.getQueueid());
						return 0;
					}
					String queueName = optQueue.get().getName();
					String sql = "SELECT count(id) as answered FROM queue_log where event ='ENTERQUEUE' and agent = 'NONE' and time between  '" + query.getDate1() + "' and '" + query.getDate2() +"' and queuename='" + queueName +"'";
					
					ResultSet rs = statement.executeQuery( sql );
					result = rs.first() ? rs.getInt("answered") : 0;
				} catch (SQLException e) {
					  logger.error("Error while getting answers calls for query:" + query + "  with message:" +  e.getMessage());
				}
			
		return result;
	}
	private int getUnAnswersCalls(QueueQuery query) {
		int result = 0;
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getPbxid()));
	    if (!entity.isPresent()) return 0;
	    ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
		try(
				Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
				Statement statement = connect.createStatement())
				{
					Optional<Queues> optQueue = getQueue(query,connect);
					if (!optQueue.isPresent()) {
						logger.error("Error! Queue not found for queue id : "  + query.getQueueid());
						return 0;
					}
					String queueName = optQueue.get().getName();
					String sql = "SELECT count(id) as unanswered FROM queue_log where event ='ABANDON' and agent = 'NONE' and time between  '" +query.getDate1() + "' and '" + query.getDate2() +"' and queuename='" + queueName +"'";
					
					ResultSet rs = statement.executeQuery( sql );
					result = rs.first() ? rs.getInt("unanswered") : 0;
				} catch (SQLException e) {
					  logger.error("Error while getting unanswers calls for query:" + query + "  with message:" +  e.getMessage());
				}
			
		return result;
	}
	private int getAgets(QueueQuery query) {
		int result = 0;
		
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getPbxid()));
	    if (!entity.isPresent()) return 0;
	    ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
		try(
				Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
				Statement statement = connect.createStatement())
				{
					Optional<Queues> optQueue = getQueue(query,connect);
					if (!optQueue.isPresent()) {
						logger.error("Error! Queue not found for queue id : "  + query.getQueueid());
						return 0;
					}
					String queueName = optQueue.get().getName();
					String sql = "SELECT  count( distinct  agent ) as agents FROM queue_log where agent like 'SIP%'  and time between  '" + query.getDate1() + "' and '" + query.getDate2() +"' and queuename='" + queueName +"'";
					
					ResultSet rs = statement.executeQuery( sql );
					result = rs.first() ? rs.getInt("agents") : 0;
				} catch (SQLException e) {
					  logger.error("Error while getting agents  for query:" + query + "  with message:" +  e.getMessage());
				}
			
		return result;
	}
	private Optional<Integer> getPageCount(QueueDetailQuery query, Statement statement) throws SQLException {
		
		String ld1 = query.getDate1();
		String ld2 = query.getDate2();
		String sql ="select count(cd.id) as total from queue_log as ql join cdr as cd on(cd.uniqueid=ql.callid)  where ql.queuename='" + query.getQueue() +"' " + 
				"and ql.time between '" + ld1 + "' and '" + ld2 + "' and ql.event='CONNECT'  and ql.agent='" + query.getPeer() + "'";  
		ResultSet rs = statement.executeQuery( sql );
		return rs.first() ? Optional.of(rs.getInt("total")/ReportController.LINES_NUMBER) : Optional.empty();
		
	}
	
	
	
}
