package mibs.asterisk.control.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncode implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		
		return new String(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		// TODO Auto-generated method stub
		String password = new String(rawPassword.toString());
		return password.equals(encodedPassword);
	}

}
