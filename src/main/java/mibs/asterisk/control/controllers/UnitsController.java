package mibs.asterisk.control.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.ActionResult;
import mibs.asterisk.control.dao.Equipments;
import mibs.asterisk.control.dao.Units;
import mibs.asterisk.control.dao.Users;
import mibs.asterisk.control.entity.EquipmentsEntity;
import mibs.asterisk.control.entity.UnitsEntity;
import mibs.asterisk.control.entity.UserEntity;
import mibs.asterisk.control.repository.EquipmentsRepository;
import mibs.asterisk.control.repository.UnitsRepository;
import mibs.asterisk.control.service.UsersDetails;
import mibs.asterisk.control.utils.FSContainer;
import mibs.asterisk.control.utils.PNameQ;

@Controller
public class UnitsController extends AbstractController{

	static Logger logger = LoggerFactory.getLogger(UnitsController.class);
	

	@Value("${spring.datasource.url}")
	private String datasourceUrl;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	
	private Connection connect = null;

	@Autowired
	private UnitsRepository unitsRepository;
	@Autowired
	private EquipmentsRepository equipmentsRepository;
	
	@RequestMapping(value = { "/dropUnit" },method = {RequestMethod.POST})
	public @ResponseBody  ActionResult dropUnit(@RequestBody Units un) {

		if (un.getP() == null) return new ActionResult( "UNIT_NOT_DROPED" );
		Optional<UnitsEntity> unitOps = unitsRepository.findById(un.getP());
		
		if (!unitOps.isPresent()) return new ActionResult( "UNIT_NOT_DROPED" );
		List<UnitsEntity> units = unitsRepository.findByQ( unitOps.get().getId());
		if (units.size() > 0) return new ActionResult( "UNIT_NOT_DROPED" );
		try {
			unitsRepository.delete(unitOps.get());
			return new ActionResult( "UNIT_DROPED" );
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ActionResult( "UNIT_NOT_DROPED" );
		}
	}
	
	@RequestMapping(value = { "/findUnit" },method = {RequestMethod.GET})
	public @ResponseBody Units findUnit( @RequestParam(value="id", required = true)  Long id  ) {
		final Units unit = new Units();
		Optional<UnitsEntity> entity = unitsRepository.findById(id);
		entity.ifPresent(en->{
			unit.setP(en.getId());
			unit.setName(en.getUnit());
			unit.setQ(en.getQ());
		});
		return unit;
	}
	
	@RequestMapping(value = { "/saveUnit" },method = {RequestMethod.POST})
	public @ResponseBody  ActionResult saveUnit(@RequestBody Units un) {
		if (un.getName().length() == 0) return  new ActionResult( "UNIT_NOT_SAVED" );
	
		if (un.getP() != null) {
			try {
				unitsRepository.updateUnit(un.getName(), un.getP());
				return new ActionResult( "UNIT_SAVED" );
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "UNIT_NOT_SAVED" );
			}
		}else {
			UnitsEntity unit = new UnitsEntity();
			unit.setUnit( un.getName());
			unit.setQ( new Long(0));
			try {
				unitsRepository.save(unit);
				return new ActionResult( "UNIT_SAVED" );
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "UNIT_NOT_SAVED" );
			}
		}
	}
	
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

		     
		     PNameQ pnameq = new PNameQ(p1,unit,q);
		     List<EquipmentsEntity> eqs = equipmentsRepository.findByP(p1);
		     if (eqs != null && eqs.size() > 0) {
		    	 eqs.forEach(e->{
		    		 Equipments equipment = new Equipments();
		    		 equipment.setId( e.getId() );
		    		 equipment.setPhone( e.getPhone() );
		    		 equipment.setIpaddress(e.getIpaddress());
		    		 equipment.setOffice(e.getOffice());
		    		 equipment.setNetmask(e.getNetmask());
		    		 equipment.setGateway(e.getGateway());
		    		 equipment.setP(e.getP());
		    		 equipment.setPerson(e.getPerson());
		    		 equipment.setPassword(e.getPassword());
		    		 
		    		 pnameq.addEquipments( equipment );
		    	 }); 
		     }
		     
		     FSContainer tempFs = new FSContainer( pnameq );

		     fs.addContainer(tempFs);
		     fillSQL(p1,tempFs);
		 }
		statement.close();
	}	
}
