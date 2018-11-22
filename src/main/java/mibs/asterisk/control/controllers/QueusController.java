package mibs.asterisk.control.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

import mibs.asterisk.control.dao.Agents;
import mibs.asterisk.control.dao.CDRQuery;
import mibs.asterisk.control.dao.Peers;
import mibs.asterisk.control.dao.QueueQuery;
import mibs.asterisk.control.dao.QueueReport;
import mibs.asterisk.control.dao.QueueSpell;
import mibs.asterisk.control.dao.Queues;
import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.repository.ConfigurationRepository;

@Controller
public class QueusController {
	static Logger logger = LoggerFactory.getLogger(QueusController.class);
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
		try(	
				Statement statement = connect.createStatement()) 
			{
			   
		       String sql = "select * from queues where id=" + query.getQueueid();
			   ResultSet rs = statement.executeQuery( sql );
			   result = rs.first() ? Optional.of(new Queues(rs.getLong("id"),rs.getString("name"), Long.valueOf(query.getPbxid()))) : Optional.empty();
			}catch (Exception e) {
			   result = Optional.empty();
			   logger.error(e.getMessage());
			   
			}
		return result;	
	}
	private Optional<Peers> getPeers(Long id, Long pbxid, Connection connect){
		Optional<Peers> result ;
		try(	
				Statement statement = connect.createStatement()) 
			{
		       String sql = "select * from peers where id=" + id;
			   ResultSet rs = statement.executeQuery( sql );
			   result = rs.first() ? Optional.of(new Peers(rs.getLong("id"),rs.getString("name"), pbxid)) : Optional.empty();
			}catch (Exception e) {
			   result = Optional.empty();
			   logger.error(e.getMessage());
			   
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
	@RequestMapping(value = { "/showQueueReport" }, method = { RequestMethod.POST })
	public @ResponseBody List<QueueReport> showQueueReport(@RequestBody QueueQuery query) {
	
		List<QueueReport> reports = new ArrayList<>();
		Optional<List<QueueSpell>> optSpels = getQuerySpells(query);
		List<QueueSpell> spells = optSpels.get();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getPbxid()));
		entity.ifPresent(en->{
			String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
			try(
					Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
					Statement statement = connect.createStatement())
					{
					spells.forEach(sp->{
						QueueReport qr = new QueueReport();
						qr.setDate(sp.getAddTime());
						qr.setEnterTime(sp.getAddTime());
						qr.setExitTime(sp.getRemoveTime());
						qr.setPeer(sp.getPeer());
						String sql = "select count(id) as calls from queue_log where time between '" + sp.getAddTime()+"' and '" + sp.getRemoveTime() + "' and queuename='" + sp.getQueue() + "' and agent='" + sp.getPeer()+"' and event ='CONNECT'";
						System.out.println(sql);
						try {
							ResultSet rs = statement.executeQuery( sql );
							qr.setCalls( rs.first() ? rs.getInt("calls") : 0 );
							reports.add(qr);
						}catch(Exception e) {
							logger.error(e.getMessage());
						}
					});
					}catch(Exception e) {
						logger.error(e.getMessage());
					}
			});
		return reports;
	}
	
}
