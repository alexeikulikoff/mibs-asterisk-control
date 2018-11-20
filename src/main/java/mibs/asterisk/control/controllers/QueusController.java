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


import mibs.asterisk.control.dao.CDRQuery;
import mibs.asterisk.control.dao.QueueQuery;
import mibs.asterisk.control.dao.QueueSpell;
import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.repository.ConfigurationRepository;

@Controller
public class QueusController {
	static Logger logger = LoggerFactory.getLogger(QueusController.class);
	@Autowired
	private ConfigurationRepository configurationRepository;
	
	@RequestMapping(value = { "/showQueueSpell" }, method = { RequestMethod.POST })
	public List<QueueSpell> showQueueSpell(@RequestBody QueueQuery query) {
		List<QueueSpell> spells = new ArrayList<>();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getId()));
		 entity.ifPresent(en->{
			String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
			LocalDate ld1 = LocalDate.parse(query.getDate1(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			LocalDate ld2 = LocalDate.parse(query.getDate2(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			int page = query.getPage();
			try(
					Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
					Statement statement = connect.createStatement())
					{
				
				    String sql = "select * from members where time between '" + ld1 + "' and '" + ld2 + "'"; 
					
					ResultSet rs = statement.executeQuery( sql );
					while (rs.next()) {
						QueueSpell qs = new QueueSpell();
						String event = rs.getString("event");
						if (event.equals("ADDMEMBER")) {
							qs.setId(rs.getLong("id"));
							qs.setAgentid(rs.getInt("agentid"));
							qs.setPeerid(rs.getInt("peerid"));
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
						e.printStackTrace();
					}
		});	
	
		return spells;
		
	}
	
	
	
}
