package mibs.asterisk.control.controllers;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mibs.asterisk.control.entity.User;
import mibs.asterisk.control.service.UsersDetails;

@Controller
public class RootController extends AbstractController{
	//@Autowired
	//private SessionFactory sessionFactory;
	
	@RequestMapping("/")
	public String getRoot(Model model,  @AuthenticationPrincipal UsersDetails activeUser ) {
//		System.out.println("activeUser =" + activeUser.getUsername());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		;
		System.out.println("getName =" + auth.getName());
		System.out.println("getPrincipal =" + auth.getPrincipal());
		System.out.println("auth =" + auth);
		System.out.println("activeUser =" + activeUser.getUsername());
		return "admin/home";
	}
	@RequestMapping("/home")
	public String getHome(Model model,  @AuthenticationPrincipal UsersDetails activeUser ) {
			System.out.println("activeUser =" + activeUser.getUsername());
			User user = userRepository.findByName( activeUser.getUsername() );
			
			//model.addAttribute("userID", user.getId() );
			if (user.getRole().equals("ADMIN")) {
			//	model.addAttribute("pageHeader",messageSource.getMessage("label.explorations", null, locale));
			//	model.addAttribute("patients",messageSource.getMessage("label.patients", null, locale));
			//	model.addAttribute("tableId","tbPatients");
			//	Init(model,activeUser );
				return "admin/home";
			}	
			else if (user.getRole().equals("USER")) {
				//Init(model,activeUser );
				//model.addAttribute("pageHeader",messageSource.getMessage("label.explorations", null, locale));
			
				return "user/home";
			}else {
				return "404";
			}
	}
	
}
