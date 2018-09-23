package mibs.asterisk.control.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

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

import mibs.asterisk.control.entity.Table1;
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
	public String getHome(Model model, @AuthenticationPrincipal UsersDetails activeUser) {
		System.out.println("activeUser =" + activeUser.getUsername());

		Properties c = new Properties();
		c.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		c.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		c.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db1");
		c.setProperty("hibernate.connection.username", "asterisk-admin");
		c.setProperty("hibernate.connection.password", "456852");
		c.setProperty("hibernate.connection.autoReconnect", "true");

	/*	c.setProperty("connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");
		c.setProperty("c3p0.min_size", "5");
		c.setProperty("c3p0.max_size", "20");
		c.setProperty("c3p0.timeout", "1800");
		c.setProperty("c3p0.max_statements", "100");
		c.setProperty("hibernate.c3p0.testConnectionOnCheckout", "true");
*/		
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(Table1.class);
		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db1");
		configuration.setProperty("hibernate.connection.username", "asterisk-admin");
		configuration.setProperty("hibernate.connection.password", "456852");
		configuration.setProperty("hibernate.connection.autoReconnect", "true");
	
		
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

		Table1 t1 = new Table1();
		t1.setName("Procs1");
	    SessionFactory sessionFactory = configuration.buildSessionFactory(sr);
	    Session session  = sessionFactory.openSession();
	    session.getTransaction().begin();
	    session.persist( t1 );
	    session.getTransaction().commit();
	    session.clear();
	    
	   // session.getTransaction().begin();
	  
	    
	    List<Object[]>  rows = session.createNativeQuery("select id, name from tb1").getResultList();
	    
	    rows.forEach(s->System.out.println((String)s[1]));
	    
	    List<Table1> rows2 = session.createNativeQuery("select id, name from tb1").addEntity(Table1.class).list();
	    
	    rows2.forEach(s->System.out.println(s.getId() + "  "  + s.getName()));
	    
	    System.out.println("sessionFactory =" + sessionFactory);
	
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	    dataSource.setUrl("jdbc:mysql://localhost:3306/db1");
	    dataSource.setUsername("asterisk-admin");
	    dataSource.setPassword("456852");
	    JdbcOperations template = new JdbcTemplate(dataSource);
	    
	    int rowCount = template.queryForObject("select count(name) from tb1", Integer.class);
		
	    System.out.println("rowCount =" + rowCount);
		
		User user = userRepository.findByName(activeUser.getUsername());
		if (user == null)
			return "404";
		return user.getRole().equals("ADMIN") ? "admin/home" : "user/home";
	}
	
}
