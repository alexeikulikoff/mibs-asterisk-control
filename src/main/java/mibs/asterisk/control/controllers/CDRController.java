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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.CDR;
import mibs.asterisk.control.dao.CDRQuery;
import mibs.asterisk.control.dao.Pbx;
import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.repository.ConfigurationRepository;
import mibs.asterisk.control.utils.DISPOSITION;

@Controller
public class CDRController {

	static final int LINES_NUMBER = 50;
	static Logger logger = LoggerFactory.getLogger(CDRController.class);
	@Autowired
	private ConfigurationRepository configurationRepository;
	
	@RequestMapping(value = { "/showCDR" }, method = { RequestMethod.POST })
	public @ResponseBody List<CDR> showCDR(@RequestBody CDRQuery query) {
	
		List<CDR> cdrs = prepareCDR(query);
		return cdrs;
		
	}
	private List<CDR> prepareCDR( CDRQuery query){
		List<CDR> cdrs = new ArrayList<>();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf(query.getId()));
		entity.ifPresent(en -> {
			String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
			LocalDate ld1 = LocalDate.parse(query.getDate1(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			LocalDate ld2 = LocalDate.parse(query.getDate2(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			int page = query.getPage();
			try(
				Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
				Statement statement = connect.createStatement())
				{
				String sql = "select id, calldate, clid, src, dst, duration, disposition, uniqueid, channel, dstchannel from cdr where disposition='" + query.getDisposition() + "' and calldate between '" + ld1 +"' and '" + ld2 + "' and " + query.getDirection() + "  like '" + query.getPhone() + "'  order by id limit " + LINES_NUMBER  * ( page-1) + "  , " + LINES_NUMBER  * page;  
				System.out.println( sql );
				ResultSet rs = statement
							.executeQuery( sql );
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
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		return cdrs;
	}
	
}
