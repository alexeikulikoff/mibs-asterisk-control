package mibs.asterisk.control.controllers;

import java.util.Optional;

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
			Optional<User> user = userRepository.findByName( activeUser.getUsername() );
			if (!user.isPresent()) return "404";
			return user.get().getRole().equals("ADMIN")? "admin/home": "user/home";
	}
	
}
