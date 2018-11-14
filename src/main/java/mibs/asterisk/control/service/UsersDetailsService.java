package mibs.asterisk.control.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mibs.asterisk.control.entity.UserEntity;
import mibs.asterisk.control.repository.UserRepository;

@Service
public class UsersDetailsService implements UserDetailsService{

	@Autowired
	
	private UserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = repository.findByName( username );
		if (user == null) {
			throw new UsernameNotFoundException("User not found for username: [" + username + "]" );
		}
		return new UsersDetails(user);
	}

}
