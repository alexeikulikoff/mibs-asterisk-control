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
import mibs.asterisk.control.dao.Users;
import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.entity.UserEntity;
import mibs.asterisk.control.exception.SaveAgentsException;
import mibs.asterisk.control.repository.ConfigurationRepository;

@Controller
public class AgentsController {
	static Logger logger = LoggerFactory.getLogger(AgentsController.class);
	@Autowired
	private ConfigurationRepository configurationRepository;
	

	
	@RequestMapping(value = { "/saveAgent" }, method = {RequestMethod.POST})
	public @ResponseBody  ActionResult saveAgent(@RequestBody Agents ag) {
		if ( ag.getName().length() == 0 ||  ag.getExtension().length() == 0 ) return  new ActionResult( "AGENT_NOT_SAVED" );
		Optional<ConfigurationEntity> entity = configurationRepository.findById( ag.getPbxid() );
		if (!entity.isPresent()) return new ActionResult( "AGENT_NOT_SAVED" );
		ConfigurationEntity en = entity.get();
		String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;	
	
		try(Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
			Statement statement = connect.createStatement())
			{
				if (ag.getId() != null) {
					String sql = "update agents set name='" + ag.getName() + "', extension='" + ag.getExtension() + "' where id=" + ag.getId();
				
					statement.executeUpdate(sql);
					return new ActionResult( "AGENT_SAVED" );
				}else {
					String sql = "insert into agents(name,extension) values('" + ag.getName() + "','" + ag.getExtension() + "')";
				
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
					String sql = "select id, name,extension from agents where id =" + id;
					ResultSet rs = statement.executeQuery( sql );
					if (rs.next()) {
						agent.setId(rs.getLong("id"));
						agent.setName(rs.getString("name"));
						agent.setExtension(rs.getString("extension"));
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
				String sql = "select id, name, extension from agents";  
				ResultSet rs = statement.executeQuery( sql );
				while (rs.next()) {
					Agents agent = new Agents();
					agent.setId(rs.getLong("id"));
					agent.setName(rs.getString("name"));
					agent.setExtension(rs.getString("extension"));
					agents.add(agent);
				};
				
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		});
		return agents;
	}
}
