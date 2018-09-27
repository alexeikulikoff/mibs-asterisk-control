package mibs.asterisk.control.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.Users;
import mibs.asterisk.control.entity.UserEntity;
import mibs.asterisk.control.repository.UserRepository;
import static mibs.asterisk.control.utils.Const.NULL;;

@Controller
public class UsersController extends AbstractController{
	@Autowired
	protected UserRepository userRepository;
	
	
	static Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	@RequestMapping("/test")
	public String getTest(Model model) {
		return "admin/home";
		
	}
	@RequestMapping("/testQunit")
	public String testQunit(Model model) {
		return "testQunit";
		
	}
	
	@RequestMapping(value = { "/findAllUsers" },method = {RequestMethod.GET})
	public @ResponseBody List<Users> findAllUsers(Model model){
		final List<Users> users  = new ArrayList<>();
		List<UserEntity> euserEntity = userRepository.findAll();
		if ( euserEntity != null && euserEntity.size() > 0 )  {
			euserEntity.forEach(us-> {
				Users user = new Users();
				user.setId(us.getId());
				user.setName(us.getName());
				user.setPassword(us.getPassword());
				user.setRole(us.getRole());
				users.add( user );
			});
		}
		return users;
	}
	@RequestMapping(value = { "/findUser" },method = {RequestMethod.GET})
	public @ResponseBody Users findUser( @RequestParam(value="id", required = true)  Long id  ) {
		final Users user = new Users();
		//initUsers();
		Optional<UserEntity> euserEntity = userRepository.findById(id);
		euserEntity.ifPresent(e->{
			user.setId(e.getId());
			user.setName(e.getName());
			user.setPassword(e.getPassword());
			user.setRole(e.getRole());
		});
		
		return user;
	}
	
}
