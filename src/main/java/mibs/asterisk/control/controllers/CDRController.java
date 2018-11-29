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
import org.springframework.web.bind.annotation.ResponseBody;
import mibs.asterisk.control.dao.CDR;
import mibs.asterisk.control.dao.CDRQuery;
import mibs.asterisk.control.dao.CDRReport;
import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.repository.ConfigurationRepository;


@Controller
public class CDRController implements ReportController{

	
	static Logger logger = LoggerFactory.getLogger(CDRController.class);
	@Autowired
	private ConfigurationRepository configurationRepository;
	
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
