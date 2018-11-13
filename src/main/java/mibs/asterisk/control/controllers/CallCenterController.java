package mibs.asterisk.control.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.ActionResult;
import mibs.asterisk.control.dao.Agents;
import mibs.asterisk.control.dao.Peers;
import mibs.asterisk.control.dao.Queues;
import mibs.asterisk.control.dao.Users;
import mibs.asterisk.control.dao.Сentconf;
import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.entity.UserEntity;
import mibs.asterisk.control.exception.SaveAgentsException;
import mibs.asterisk.control.repository.ConfigurationRepository;

@Controller
public class CallCenterController {
	static Logger logger = LoggerFactory.getLogger(CallCenterController.class);
	@Autowired
	private ConfigurationRepository configurationRepository;
	

	@RequestMapping(value = { "/dropAgent" }, method = {RequestMethod.POST})
	public @ResponseBody  ActionResult dropAgent(@RequestBody Agents ag) {
		Optional<ConfigurationEntity> entity = configurationRepository.findById( ag.getPbxid() );
		if (!entity.isPresent()) return new ActionResult( "AGENT_NOT_DROPED" );
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;	
		try(Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement())
			{
				if (ag.getId() != null) {
					String sql = "delete from agents where id=" + ag.getId();
					statement.executeUpdate(sql);
					return new ActionResult( "AGENT_DROPED" );
				}else {
					return new ActionResult( "AGENT_NOT_DROPED" );
				}
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "AGENT_NOT_DROPED" );
			}
	}
	@RequestMapping(value = { "/dropPeer" }, method = {RequestMethod.POST})
	public @ResponseBody  ActionResult dropPeer(@RequestBody Peers ag) {
		Optional<ConfigurationEntity> entity = configurationRepository.findById( ag.getPbx() );
		if (!entity.isPresent()) return new ActionResult( "PEER_NOT_DROPED" );
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;	
		try(Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement())
			{
				if (ag.getId() != null) {
					String sql = "delete from peers where id=" + ag.getId();
					statement.executeUpdate(sql);
					return new ActionResult( "PEER_DROPED" );
				}else {
					return new ActionResult( "PEER_NOT_DROPED" );
				}
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "PEER_NOT_DROPED" );
			}
	}
	@RequestMapping(value = { "/dropQueue" }, method = {RequestMethod.POST})
	public @ResponseBody  ActionResult dropQueue(@RequestBody Queues ag) {
		Optional<ConfigurationEntity> entity = configurationRepository.findById( ag.getPbx() );
		if (!entity.isPresent()) return new ActionResult( "QUEUE_NOT_DROPED" );
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;	
		try(Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement())
			{
				if (ag.getId() != null) {
					String sql = "delete from queues where id=" + ag.getId();
					statement.executeUpdate(sql);
					return new ActionResult( "QUEUE_DROPED" );
				}else {
					return new ActionResult( "QUEUE_NOT_DROPED" );
				}
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "QUEUE_NOT_DROPED" );
			}
	}
	@RequestMapping(value = { "/saveAgent" }, method = {RequestMethod.POST})
	public @ResponseBody  ActionResult saveAgent(@RequestBody Agents ag) {
		if ( ag.getName().length() == 0  ) return  new ActionResult( "AGENT_NOT_SAVED" );
		Optional<ConfigurationEntity> entity = configurationRepository.findById( ag.getPbxid() );
		if (!entity.isPresent()) return new ActionResult( "AGENT_NOT_SAVED" );
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;	
	
		try(Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement())
			{
				if (ag.getId() != null) {
					String sql = "update agents set name='" + ag.getName() + "' where id=" + ag.getId();
				
					statement.executeUpdate(sql);
					return new ActionResult( "AGENT_SAVED" );
				}else {
					String sql = "insert into agents(name) values('" + ag.getName() +  "')";
				
					statement.executeUpdate(sql);
					return new ActionResult( "AGENT_SAVED" );
				}
		
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "AGENT_NOT_SAVED" );
			}
	}
	@RequestMapping(value = { "/findAgent" },method = {RequestMethod.GET})
	public @ResponseBody Agents findAgent( @RequestParam(value="id", required = true)  Long id , @RequestParam(value="pbxid", required = true)  Long pbxid  ) {
		Agents agent = new Agents();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(pbxid);
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;	
	
		try(Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement())
			{
					String sql = "select id, name from agents where id =" + id;
					ResultSet rs = statement.executeQuery( sql );
					if (rs.next()) {
						agent.setId(rs.getLong("id"));
						agent.setName(rs.getString("name"));
						agent.setPbxid(pbxid);
					}
				
			}catch(Exception e) {
				logger.error(e.getMessage());
			}
		return agent;
		
	}
	@RequestMapping(value = { "/findAllAgents" },method = {RequestMethod.GET})
	public @ResponseBody List<Agents> findAllAgents( @RequestParam(value="id", required = true)  Long id  ) {
		List<Agents> agents = new ArrayList<>();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(id));
		entity.ifPresent(en -> {
			String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
			try(
				Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
				Statement statement = connect.createStatement())
				{
				String sql = "select id, name from agents";  
				ResultSet rs = statement.executeQuery( sql );
				while (rs.next()) {
					Agents agent = new Agents();
					agent.setId(rs.getLong("id"));
					agent.setName(rs.getString("name"));
					agents.add(agent);
				};
				
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		});
		return agents;
	}
	@RequestMapping(value = { "/findAllPeers" },method = {RequestMethod.GET})
	public @ResponseBody List<Peers> findAllPeers( @RequestParam(value="id", required = true)  Long id  ) {
		List<Peers> peers = new ArrayList<>();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(id));
		entity.ifPresent(en -> {
			String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
			try(
				Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
				Statement statement = connect.createStatement())
				{
				String sql = "select id, name from peers";  
				ResultSet rs = statement.executeQuery( sql );
				while (rs.next()) {
					Peers peer = new Peers();
					peer.setId(rs.getLong("id"));
					peer.setName(rs.getString("name"));
					peers.add(peer);
				};
				
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		});
		return peers;
	}
	@RequestMapping(value = { "/savePeer" }, method = {RequestMethod.POST})
	public @ResponseBody  ActionResult savePeer(@RequestBody Peers ag) {
		if ( ag.getName().length() == 0  ) return  new ActionResult( "PEER_NOT_SAVED" );
		Optional<ConfigurationEntity> entity = configurationRepository.findById( ag.getPbx() );
		if (!entity.isPresent()) return new ActionResult( "PEER_NOT_SAVED" );
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;	
	
		try(Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement())
			{
				if (ag.getId() != null) {
					String sql = "update peers set name='" + ag.getName() + "' where id=" + ag.getId();
					statement.executeUpdate(sql);
					return new ActionResult( "PEER_SAVED" );
				}else {
					String sql = "insert into peers(name) values('" + ag.getName() + "')";
					statement.executeUpdate(sql);
					return new ActionResult( "PEER_SAVED" );
				}
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "PEER_NOT_SAVED" );
			}
	}
	@RequestMapping(value = { "/findPeer" },method = {RequestMethod.GET})
	public @ResponseBody Peers findPeer( @RequestParam(value="id", required = true)  Long id , @RequestParam(value="pbxid", required = true)  Long pbxid  ) {
		Peers peer = new Peers();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(pbxid);
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;	
	
		try(Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement())
			{
					String sql = "select id, name from peers where id =" + id;
					ResultSet rs = statement.executeQuery( sql );
					if (rs.next()) {
						peer.setId(rs.getLong("id"));
						peer.setName(rs.getString("name"));
						peer.setPbx(pbxid);
					}
				
			}catch(Exception e) {
				logger.error(e.getMessage());
			}
		return peer;
		
	}
	@RequestMapping(value = { "/findAllQueues" },method = {RequestMethod.GET})
	public @ResponseBody List<Queues> findAllQueues( @RequestParam(value="id", required = true)  Long id  ) {
		List<Queues> queues = new ArrayList<>();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(id));
		entity.ifPresent(en -> {
			String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
			try(
				Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
				Statement statement = connect.createStatement())
				{
				String sql = "select id, name from queues";  
				ResultSet rs = statement.executeQuery( sql );
				while (rs.next()) {
					Queues queue = new Queues();
					queue.setId(rs.getLong("id"));
					queue.setName(rs.getString("name"));
					queues.add( queue );
				};
				
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		});
		return queues;
	}
	@RequestMapping(value = { "/findQueue" },method = {RequestMethod.GET})
	public @ResponseBody Queues findQueue( @RequestParam(value="id", required = true)  Long id , @RequestParam(value="pbxid", required = true)  Long pbxid  ) {
		Queues queue = new Queues();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(pbxid);
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;	
	
		try(Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement())
			{
					String sql = "select id, name from queues where id =" + id;
					ResultSet rs = statement.executeQuery( sql );
					if (rs.next()) {
						queue.setId(rs.getLong("id"));
						queue.setName(rs.getString("name"));
						queue.setPbx(pbxid);
					}
				
			}catch(Exception e) {
				logger.error(e.getMessage());
			}
		return queue;
		
	}
	@RequestMapping(value = { "/saveQueue" }, method = {RequestMethod.POST})
	public @ResponseBody  ActionResult saveQueue(@RequestBody Queues ag) {
		if ( ag.getName().length() == 0  ) return  new ActionResult( "QUEUE_NOT_SAVED" );
		Optional<ConfigurationEntity> entity = configurationRepository.findById( ag.getPbx() );
		if (!entity.isPresent()) return new ActionResult( "QUEUE_NOT_SAVED" );
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;	
	
		try(Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement())
			{
				if (ag.getId() != null) {
					String sql = "update queues set name='" + ag.getName() + "' where id=" + ag.getId();
					statement.executeUpdate(sql);
					return new ActionResult( "QUEUE_SAVED" );
				}else {
					String sql = "insert into queues(name) values('" + ag.getName() + "')";
					statement.executeUpdate(sql);
					return new ActionResult( "QUEUE_SAVED" );
				}
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "QUEUE_NOT_SAVED" );
			}
	}
	@RequestMapping(value = { "/findAllCentconf" },method = {RequestMethod.GET})
	public @ResponseBody List<Сentconf> findAllCentconf( @RequestParam(value="id", required = true)  Long id  ) {
		List<Сentconf> centconfs = new ArrayList<>();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(id));
		entity.ifPresent(en -> {
			String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
			try(
				Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
				Statement statement = connect.createStatement())
				{
				String sql = "select cf.id as id, q.name as queuename, a.name as agentname,  extension, penalty, queueid, agentid from centerconfig as cf join agents as a on (a.id=agentid) join queues as q on (q.id=queueid) order by queuename";  
				ResultSet rs = statement.executeQuery( sql );
				while (rs.next()) {
					Сentconf centconf = new Сentconf();
					centconf.setId(rs.getLong("id"));
					centconf.setAgentid(rs.getLong("agentid"));
					centconf.setQueueid(rs.getLong("queueid"));
					centconf.setAgentname(rs.getString("agentname"));
					centconf.setQueuename(rs.getString("queuename"));
					centconf.setExtention(rs.getString("extension"));
					centconf.setPenalty(rs.getInt("penalty"));
					centconfs.add(centconf);
				};
				
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		});
		return centconfs;
	}
	
	@RequestMapping(value = { "/saveСentconf" }, method = {RequestMethod.POST})
	public @ResponseBody  ActionResult saveСentconf(@RequestBody Сentconf ag) {
		if ( ag.getExtention().length() == 0  ) return  new ActionResult( "CONFIG_NOT_SAVED" );
		Optional<ConfigurationEntity> entity = configurationRepository.findById( ag.getPbx() );
		if (!entity.isPresent()) return new ActionResult( "CONFIG_NOT_SAVED" );
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;	
	
		try(Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement())
			{
				if (ag.getId() != null) {
					String sql = "update centerconfig set agentid=" +ag.getAgentid() +", queueid=" + ag.getQueueid() + ",extension='" + ag.getExtention() + "', penalty=" + ag.getPenalty() + " where id=" + ag.getId();
					statement.executeUpdate(sql);
					return new ActionResult( "CONFIG_SAVED" );
				}else {
					String sql = "insert into centerconfig(agentid, queueid, extension, penalty) values(" + ag.getAgentid() + "," + ag.getQueueid() + ",'" + ag.getExtention() + "'," + ag.getPenalty() + ")";
					System.out.println(sql);
					statement.executeUpdate(sql);
					return new ActionResult( "CONFIG_SAVED" );
				}
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "CONFIG_NOT_SAVED" );
			}
	}

}
