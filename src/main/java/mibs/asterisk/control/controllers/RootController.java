package mibs.asterisk.control.controllers;

import java.util.HashMap;
import java.util.List;
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
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
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
import mibs.asterisk.control.entity.Table1;
import mibs.asterisk.control.entity.UserEntity;
import mibs.asterisk.control.repository.ConfigurationRepository;
import mibs.asterisk.control.repository.UserRepository;
import mibs.asterisk.control.service.UsersDetails;

@Controller
public class RootController extends AbstractController{
	
	@Autowired
	protected UserRepository userRepository;
	@Autowired
	private ConfigurationRepository configurationRepository;
	
	@RequestMapping("/")
	public String getRoot(Model model,  @AuthenticationPrincipal UsersDetails activeUser ) {
		System.out.println("activeUser =" + activeUser);
		System.out.println("role =" + activeUser.getRole());
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
		UserEntity user = userRepository.findByName( activeUser.getUsername() );
		if (user == null) return "error-404";
		model.addAttribute("user_role", user.getRole());
		
		return user.getRole().equals("ADMIN") ? "admin/setting" : "error-401";
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
	
	
	@RequestMapping("/home")
	public String getHome(Model model, @AuthenticationPrincipal UsersDetails activeUser) {
		System.out.println("activeUser =" + activeUser.getUsername());

/*		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(Table1.class);
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		configuration.setProperty("hibernate.connection.url", "jdbc:mysql://172.16.255.6:3306/asterisk_1");
		configuration.setProperty("hibernate.connection.username", "asterisk-admin");
		configuration.setProperty("hibernate.connection.password", "456852");
		configuration.setProperty("hibernate.connection.autoReconnect", "true");
	
		
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

		Table1 t1 = new Table1();
		t1.setName(UUID.randomUUID().toString());
	    SessionFactory sessionFactory = configuration.buildSessionFactory(sr);
	    Session session  = sessionFactory.openSession();
	    session.getTransaction().begin();
	    session.persist( t1 );
	    session.getTransaction().commit();
	    session.clear();
	    
	   // session.getTransaction().begin();
	  
	    
	    List<Object[]>  rows = session.createNativeQuery("select id, name from table1").getResultList();
	    
	    rows.forEach(s->System.out.println((String)s[1]));
	    
	    @SuppressWarnings("unchecked")
		List<Table1> rows2 = session.createNativeQuery("select id, name from table1").addEntity(Table1.class).list();
	    
	    rows2.forEach(s->System.out.println(s.getId() + "  "  + s.getName()));
	    
	    System.out.println("sessionFactory =" + sessionFactory);
	
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	    dataSource.setUrl("jdbc:mysql://172.16.255.6:3306/asterisk_control");
	    dataSource.setUsername("asterisk-admin");
	    dataSource.setPassword("456852");
	    JdbcOperations template = new JdbcTemplate(dataSource);
	    
	    int rowCount = template.queryForObject("select count(name) from users", Integer.class);
		
	    System.out.println("rowCount =" + rowCount);
*/		
		UserEntity user = userRepository.findByName(activeUser.getUsername());
		if (user == null)
			return "404";
		return user.getRole().equals("ADMIN") ? "admin/home" : "user/home";
	}
	
}
