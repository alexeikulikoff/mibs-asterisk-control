package mibs.asterisk.control.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.Units;
import mibs.asterisk.control.entity.UnitsEntity;
import mibs.asterisk.control.repository.UnitsRepository;
import mibs.asterisk.control.service.UsersDetails;
import mibs.asterisk.control.utils.FSContainer;
import mibs.asterisk.control.utils.PNameQ;

@Controller
public class UnitsController extends AbstractController{

	static Logger logger = LoggerFactory.getLogger(UnitsController.class);
	
	@Autowired
	private UnitsRepository unitsRepository;
	
	@Value("${spring.datasource.url}")
	private String datasourceUrl;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	
	private Connection connect = null;
	
/*	@RequestMapping(value = { "/showAllUnits" },method = {RequestMethod.GET})
	public @ResponseBody List<Units> showUnits(Model model) {
		List<Units> units = new ArrayList<>();
		List<UnitsEntity> entity = unitsRepository.findAll();
		if (entity != null && entity.size() > 0 ) {
			entity.forEach(en->{
				Units unit = new Units();
				unit.setP(en.getId());
				unit.setName(en.getUnit());
				unit.setQ(en.getQ());
				units.add( unit );
			});
		}
		return units;
	}
*/	
	@RequestMapping(value = { "/showAllUnits" },method = {RequestMethod.GET})
	public @ResponseBody FSContainer showFSContainer(Model model) {
		FSContainer rootFS = new FSContainer(new PNameQ(0,"ROOT",0));
		try {
			 connect = DriverManager.getConnection( datasourceUrl, username, password );
			 fillSQL( new Long(0),rootFS);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
		}
		return rootFS;
		
	}
	
	private void fill(Long p,  FSContainer fs) {
		List<UnitsEntity> resultSet = unitsRepository.findByQ( new Long(p));
		resultSet.forEach(rs->{
			Long p1 = rs.getId();
			 FSContainer tempFs = new FSContainer(new PNameQ( p1 ,rs.getUnit(),rs.getQ()));
			 fs.addContainer(tempFs);
		     fill(p1,tempFs);
		});
	}
	private void fillSQL(Long p,  FSContainer fs) throws SQLException {
		Statement statement =  connect.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from units where q=" + p + " order by unit");
		while (resultSet.next()) {
		     long p1 = resultSet.getLong("p");
		     long q = resultSet.getLong("q");
		     String unit = resultSet.getString("unit");
		     FSContainer tempFs = new FSContainer(new PNameQ(p1,unit,q));
		     fs.addContainer(tempFs);
		     fillSQL(p1,tempFs);
		 }
		statement.close();
	}
	
}
