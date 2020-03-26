package mibs.asterisk.control.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.ActionResult;
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
	@RequestMapping(value = { "/dropUser" },method = {RequestMethod.POST})
	public @ResponseBody  ActionResult dropUser(@RequestBody Users us) {
		if (us.getId() == null) return new ActionResult( "USER_NOT_DROPED" );
		Optional<UserEntity> usopt = userRepository.findById(us.getId());
		ActionResult result = null;
		if (usopt.isPresent()) {
			try {
				userRepository.delete(usopt.get());
				result = new ActionResult( "USER_DROPED" );
			}catch(Exception e) {
				result  = new ActionResult( "USER_NOT_DROPED" );
			}
		}else {
			result  = new ActionResult( "USER_NOT_DROPED" );
		}
		return result;
	}
	@RequestMapping(value = { "/saveUser" },method = {RequestMethod.POST})
	public @ResponseBody  ActionResult saveUser(@RequestBody Users us) {
	
		if ( us.getName().length() == 0 ||  us.getPassword().length() == 0 ) return  new ActionResult( "USER_NOT_SAVED" );
		
		if (us.getId() != null) {
			try {
				
				BCryptPasswordEncoder crypt = new BCryptPasswordEncoder(4);
				
				userRepository.updateUser(us.getName(), crypt.encode( us.getPassword() ), us.getRole(), us.getId());
				return new ActionResult( "USER_SAVED" );
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "USER_NOT_SAVED" );
			}
		}else {
			BCryptPasswordEncoder crypt = new BCryptPasswordEncoder(4);
			UserEntity user = new UserEntity();
			user.setName( us.getName() );
			user.setPassword( crypt.encode( us.getPassword() ) );
			user.setRole( us.getRole());
			try {
				userRepository.save( user );
				return new ActionResult( "USER_SAVED" );
			}catch(Exception e) {
				
				logger.error(e.getMessage());
				return new ActionResult( "USER_NOT_SAVED" );
			}
		}
		
	}
	
}
