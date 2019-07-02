package mibs.asterisk.control.controllers;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.AgentReport;
import mibs.asterisk.control.dao.Equipments;
import mibs.asterisk.control.dao.Pbx;
import mibs.asterisk.control.dao.QueueQuery;
import mibs.asterisk.control.entity.EquipmentsEntity;
import mibs.asterisk.control.repository.EquipmentsRepository;

@Controller
public class OutboundController {

	static Logger logger = LoggerFactory.getLogger(OutboundController.class);
	private static DateTimeFormatter queryFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	@Autowired
	private EquipmentsRepository equipmentsRepository;
	
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
