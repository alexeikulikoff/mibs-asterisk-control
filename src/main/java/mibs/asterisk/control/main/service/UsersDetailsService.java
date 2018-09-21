package mibs.asterisk.control.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mibs.asterisk.control.main.entity.User;
import mibs.asterisk.control.main.repository.UserRepository;

@Service
public class UsersDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println( "username "  + username );
		//Users user = repository.findByLogin( username );
		User user = repository.findByName( "admin" );
		System.out.println( user );
		if (user == null) {
			throw new UsernameNotFoundException("User not found for username: [" + username + "]" );
		}
		return new UsersDetails(user);
	}

}
