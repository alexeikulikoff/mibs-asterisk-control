package mibs.asterisk.control.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.Asterisk;
import mibs.asterisk.control.dao.AvailableAstersisk;
import mibs.asterisk.control.entity.ConfigurationEntity;

import mibs.asterisk.control.entity.UserEntity;
import mibs.asterisk.control.repository.ConfigurationRepository;
import mibs.asterisk.control.repository.UserRepository;
import mibs.asterisk.control.service.AsteriskQuery;
import mibs.asterisk.control.service.AsteriskResponce;
import mibs.asterisk.control.service.UsersDetails;

@Controller
public class RootController extends AbstractController{
	
	private Locale locale = Locale.getDefault();
	
	@Autowired
	private MessageSource messageSource;
	

	
	@Autowired
	protected UserRepository userRepository;
	@Autowired
	private ConfigurationRepository configurationRepository;
	
	@RequestMapping("/")
	public String getRoot(Model model,  @AuthenticationPrincipal UsersDetails activeUser ) {
		
		UserEntity user = userRepository.findByName( activeUser.getUsername() );
		if (user == null) return "error-404";
		model.addAttribute("user_role", user.getRole());
		return  activeUser.getRole().equals("ADMIN") ? "redirect:/setting" : "redirect:/start";
	}
	@RequestMapping("/start")
	public String showStart(Model model, @AuthenticationPrincipal UsersDetails activeUser) {
		UserEntity user = userRepository.findByName( activeUser.getUsername() );
		if (user == null) return "error-404";
		model.addAttribute("user_role", user.getRole());
		
		return user.getRole().equals("USER") ? "user/start" : "error-401";
	}
	
	@RequestMapping("/setting")
	public String showSetting(Model model, @AuthenticationPrincipal UsersDetails activeUser) {
		
		model.addAttribute("menu0",messageSource.getMessage("label.setting", null, locale) );
		model.addAttribute("menu1",messageSource.getMessage("label.users", null, locale) );
		
		UserEntity user = userRepository.findByName( activeUser.getUsername() );
		if (user == null) return "error-404";
		model.addAttribute("user_role", user.getRole());
		
		return user.getRole().equals("ADMIN") ? "admin/setting" : "error-401";
	}
	@RequestMapping("/units")
	public String showUnits(Model model, @AuthenticationPrincipal UsersDetails activeUser) {
		
		model.addAttribute("menu0",messageSource.getMessage("label.menu", null, locale) );
		model.addAttribute("menu1",messageSource.getMessage("label.units", null, locale) );
		
		UserEntity user = userRepository.findByName( activeUser.getUsername() );
		if (user == null) return "error-404";
		model.addAttribute("user_role", user.getRole());
		
		return user.getRole().equals("ADMIN") ? "admin/units" : "user/units";
	}
	@RequestMapping("/callcenter")
	public String showAgents(Model model, @AuthenticationPrincipal UsersDetails activeUser) {
		model.addAttribute("menu0",messageSource.getMessage("label.setting", null, locale) );
		model.addAttribute("menu1",messageSource.getMessage("label.callcenter", null, locale) );
		UserEntity user = userRepository.findByName( activeUser.getUsername() );
		if (user == null) return "error-404";
		model.addAttribute("user_role", user.getRole());
		
		return user.getRole().equals("ADMIN") ? "admin/callcenter" : "user/callcenter";
	}
	@RequestMapping(value = { "/showAvailableAstersisk" },method = {RequestMethod.GET})
	public @ResponseBody AvailableAstersisk showAvailableAstersisk( Model model) {
		
		AvailableAstersisk asterisks = new AvailableAstersisk();
		List<ConfigurationEntity> entity =  configurationRepository.findAll();
		if (entity!=null && entity.size()>0) {
			entity.forEach(en->{
				asterisks.addAsterisk(new Asterisk(en.getId(), en.getAstname()));
			});
		}
		return asterisks;
	}
	
	@RequestMapping(value = { "/cdr" }, method = { RequestMethod.GET })
	public String showCDR(Model model) {
		model.addAttribute("menu0",messageSource.getMessage("label.reports", null, locale) );
		model.addAttribute("menu1",messageSource.getMessage("label.cdr", null, locale) );
		return "admin/cdr";
	}
	@RequestMapping(value = { "/queues" }, method = { RequestMethod.GET })
	public String showQueues(Model model) {
		model.addAttribute("menu0",messageSource.getMessage("label.reports", null, locale) );
		model.addAttribute("menu1",messageSource.getMessage("label.queues", null, locale) );
		return "admin/queues";
	}
	
	
}
