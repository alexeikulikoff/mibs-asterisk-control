package mibs.asterisk.control.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import mibs.asterisk.control.service.UsersDetails;

@Controller
public class UnitsController extends AbstractController{

	@RequestMapping("/units")
	public String showUnits(Model model, @AuthenticationPrincipal UsersDetails activeUser) {
		//UserEntity user = userRepository.findByName( activeUser.getUsername() );
		//if (user == null) return "error-404";
		//model.addAttribute("user_role", user.getRole());
		
		//return user.getRole().equals("ADMIN") ? "admin/setting" : "error-401";
		return "admin/units";
	}
	
}
