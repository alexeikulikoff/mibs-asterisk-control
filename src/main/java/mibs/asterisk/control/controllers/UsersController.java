package mibs.asterisk.control.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.Users;
import mibs.asterisk.control.entity.UserEntity;

@Controller
public class UsersController extends AbstractController{

	static Logger logger = LoggerFactory.getLogger(UsersController.class);
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
	
}
