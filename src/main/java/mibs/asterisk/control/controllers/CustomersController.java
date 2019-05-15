package mibs.asterisk.control.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mibs.asterisk.control.dao.ActionResult;
import mibs.asterisk.control.dao.Customers;
import mibs.asterisk.control.dao.Users;
import mibs.asterisk.control.entity.CustomersEntity;
import mibs.asterisk.control.entity.UserEntity;
import mibs.asterisk.control.repository.CustomersRepository;

@Controller
public class CustomersController {

	static Logger logger = LoggerFactory.getLogger(CustomersController.class);
	@Autowired
	private CustomersRepository customersRepository;
	
	@RequestMapping(value = { "/findAllCustomers" }, method = { RequestMethod.GET })
	public @ResponseBody List<Customers> findAllCustomers(){
		final List<Customers> customers = new ArrayList<>();
		List<CustomersEntity> entity = customersRepository.findAll();
		if (entity != null && entity.size() > 0) {
			entity.forEach(en -> {
				Customers customer = new Customers();
				customer.setId(en.getId());
				customer.setPhone(en.getPhone());
				customer.setName(en.getName());
				customers.add( customer );
			});
		}
		return customers;
	}
	@RequestMapping(value = { "/getCustomer" },method = {RequestMethod.GET})
	public @ResponseBody Customers getCustomer( @RequestParam(value="id", required = true)  Long id  ) {
		final Customers customer = new Customers();
		CustomersEntity entity = customersRepository.findByid(id);
		if (entity != null) {
			customer.setId(entity.getId());
			customer.setName(entity.getName());
			customer.setPhone(entity.getPhone());
		}
		return customer;
	
	}
	@RequestMapping(value = { "/deleteCustomer" },method = {RequestMethod.POST})
	public @ResponseBody  ActionResult deleteCustomer(@RequestBody Customers cs) {
		if (cs.getId() == null) return new ActionResult( "CUSTOMER_NOT_DROPED" );
		CustomersEntity entity = customersRepository.findByid(cs.getId());
		try {
			  customersRepository.delete(entity);
			  return  new ActionResult( "CUSTOMER_DROPED" );
			}catch(Exception e) {
			  logger.error(e.getMessage());	
			  return new ActionResult( "CUSTOMER_NOT_DROPED" );
			}
		
	}
	@RequestMapping(value = { "/saveCustomer" },method = {RequestMethod.POST})
	public @ResponseBody  ActionResult saveCustomer(@RequestBody Customers cs) {
	
		if ( cs.getName().length() == 0 ||  cs.getPhone().length() == 0 ) return  new ActionResult( "CUSTOMER_SAVE_ERROR" );
		
		if (cs.getId() != null) {
			try {
				customersRepository.updateCustomers(cs.getPhone(), cs.getName(), cs.getId());
				return new ActionResult( "CUSTOMER_SAVED" );
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "CUSTOMER_SAVE_ERROR" );
			}
		}else {
			CustomersEntity customer = new CustomersEntity();
			customer.setName(cs.getName());
			customer.setPhone(cs.getPhone());
			try {
				customersRepository.save( customer );
				return new ActionResult( "CUSTOMER_SAVED" );
			}catch(Exception e) {
				logger.error(e.getMessage());
				return new ActionResult( "CUSTOMER_SAVE_ERROR" );
			}
		}
		
	}
		
	
}
