package mibs.asterisk.control.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class UnitsController extends AbstractController{

	@Autowired
	private UnitsRepository unitsRepository;
	
	@RequestMapping(value = { "/showAllUnits" },method = {RequestMethod.GET})
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
	
}
