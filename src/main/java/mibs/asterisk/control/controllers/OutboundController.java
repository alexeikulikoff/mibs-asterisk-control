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

import mibs.asterisk.control.dao.ActionResult;
import mibs.asterisk.control.dao.AgentReport;
import mibs.asterisk.control.dao.Agents;
import mibs.asterisk.control.dao.Equipments;
import mibs.asterisk.control.dao.OutboundConsolidateRecord;
import mibs.asterisk.control.dao.OutboundConsolidateReport;
import mibs.asterisk.control.dao.Pbx;
import mibs.asterisk.control.dao.Peers;
import mibs.asterisk.control.dao.QueueQuery;
import mibs.asterisk.control.dao.QueueSpell;
import mibs.asterisk.control.dao.Queues;
import mibs.asterisk.control.dao.SelectedPhone;
import mibs.asterisk.control.dao.SelectedPhoneContainer;
import mibs.asterisk.control.dao.SelectedPhoneContainerRequest;
import mibs.asterisk.control.entity.ConfigurationEntity;
import mibs.asterisk.control.entity.EquipmentsEntity;
import mibs.asterisk.control.entity.SelectedPhoneEntity;
import mibs.asterisk.control.repository.ConfigurationRepository;
import mibs.asterisk.control.repository.EquipmentsRepository;
import mibs.asterisk.control.repository.SelectedPhoneRepository;

@Controller
public class OutboundController {

	static Logger logger = LoggerFactory.getLogger(OutboundController.class);
	private static DateTimeFormatter queryFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	@Autowired
	private EquipmentsRepository equipmentsRepository;
	@Autowired
	private SelectedPhoneRepository selectedPhoneRepository;
	@Autowired
	private ConfigurationRepository configurationRepository;
	
	private Optional<OutboundConsolidateRecord> getOutboundConsolidateRecord(String phone, String pbxId, String d1, String d2) {
		OutboundConsolidateRecord record  = new OutboundConsolidateRecord();
		Optional<ConfigurationEntity> entity = configurationRepository.findById(Long.valueOf( pbxId ));
		entity.ifPresent(en->{
			String dsURL = "jdbc:mysql://" + en.getDbhost() + ":3306/" + en.getDbname() + "?useUnicode=yes&characterEncoding=UTF-8"	;
			LocalDate ld1 = LocalDate.parse(d1, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			LocalDate ld2 = LocalDate.parse(d2, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			try(
					Connection connect = DriverManager.getConnection(dsURL, en.getDbuser(), en.getDbpassword());
					Statement statement = connect.createStatement())
					{
				        String sql = "select count(id) as calls, sum(duration) as duration from cdr where channel like 'SIP/" + phone + "-%' and dstchannel like 'DAHDI%' and calldate between '" + ld1 + "' and '" + ld2 + "'";
				        ResultSet rs = statement.executeQuery( sql );

				        if (rs!=null) {
				        	rs.next(); 
				        	float fd = rs.getFloat("duration");
							record.setPhone(phone);
							record.setCalls(rs.getLong("calls"));
							record.setDuration(fd);
							int s = (int) fd;
							String duration2 = String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
							record.setDuration2(duration2);
							
						}
					  }
					 catch (Exception e) {
						logger.error("Error while getting outbound consolidate record with message: " + e.getMessage());
					}
				});
				return (record.getPhone().length() > 0) ? Optional.of(record) : Optional.empty();
		
	}
	@RequestMapping(value = { "/createOutboundConsolidateReport" }, method = { RequestMethod.POST })
	public @ResponseBody  OutboundConsolidateReport createOutboundConsolidateReport(@RequestBody SelectedPhoneContainerRequest request) {
		
		OutboundConsolidateReport report = new  OutboundConsolidateReport();
		List<OutboundConsolidateRecord> records = new ArrayList<>();
		request.getPhones().forEach((s)->{
			Optional<OutboundConsolidateRecord> op = getOutboundConsolidateRecord(s.getPhone(), request.getPbxid(), request.getDate1(), request.getDate2());
			if (op.isPresent()) {
				OutboundConsolidateRecord record = new OutboundConsolidateRecord();
				record.setPhone(op.get().getPhone());
				record.setCalls(op.get().getCalls());
				record.setDuration(op.get().getDuration());
				record.setDuration2(op.get().getDuration2());
				records.add(record);
			}
		});
	
		report.setRecords(records);
		report.setTotalCalls(records.stream().mapToLong(s->s.getCalls()).sum());
		report.setTotalDuration(records.stream().mapToDouble( s->s.getDuration()).sum());
		return report;
	
	}
	
	@RequestMapping(value = { "/saveSelectedPhones" }, method = { RequestMethod.POST })
	public @ResponseBody  ActionResult saveSelectedPhones(@RequestBody SelectedPhoneContainer container) {
		try {
			selectedPhoneRepository.deleteAll();
			
			container.getContainer().forEach((co)->{
				SelectedPhoneEntity e = new SelectedPhoneEntity();
				e.setIdd(co.getId());
				e.setPhone(co.getPhone());
				e.setOffice(co.getOffice());
				selectedPhoneRepository.save(e);
			});	
			return new ActionResult("PHONES_SAVE_SUCCESS");
		}catch(Exception e) {
			logger.error("Error while saved selected phones with message : " + e.getMessage());
			return new ActionResult("PHONES_SAVE_ERROR");
		}
	}
	@RequestMapping(value = { "/getSavedPhones" }, method = { RequestMethod.POST })
	public @ResponseBody List<SelectedPhone> getSavedPhones() {
		
		List<SelectedPhone> result = new ArrayList<>();
		List<SelectedPhoneEntity> entity = selectedPhoneRepository.findAll();
		entity.forEach(s->{
			SelectedPhone sp = new SelectedPhone();
			sp.setId(s.getIdd());
			sp.setPhone(s.getPhone());
			sp.setOffice(s.getOffice());
			result.add(sp);
		});
		return result;
	}
	@RequestMapping(value = { "/getAllEquipments" }, method = { RequestMethod.POST })
	public @ResponseBody List<Equipments> getAllEquipments() {
		
		 List<EquipmentsEntity> equipments = equipmentsRepository.findAll();
		 List<Equipments> result = new ArrayList<>();
		 equipments.forEach(s-> {
			 Equipments e = new Equipments();
			 e.setId(s.getId());
			 e.setPhone(s.getPhone());
			 e.setOffice(s.getOffice());
			 result.add(e);
		 });
		 
		 return result;
	}
	
		
}
