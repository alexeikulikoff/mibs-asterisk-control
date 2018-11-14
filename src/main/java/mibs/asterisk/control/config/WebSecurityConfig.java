package mibs.asterisk.control.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import mibs.asterisk.control.service.UsersDetailsService;
import mibs.asterisk.control.utils.CustomPasswordEncode;

@Configuration
@EnableWebSecurity
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 700)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
   
	@Autowired
	private UsersDetailsService usersDetailsService;
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/callboard").antMatchers("/ws2");
	}
	@Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
    			.csrf().ignoringAntMatchers("/ws/**").and()
    			.authorizeRequests()
    			.antMatchers("/ws/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/home").permitAll()
                .antMatchers("/dashboard").permitAll()
                .antMatchers("/restore").permitAll()
   			 	.antMatchers("/forgot_password").permitAll()
   			 	.antMatchers("/verifyMail").permitAll()
   			 	.antMatchers("/css/**").permitAll()
   			 	.antMatchers("/font-awesome/**").permitAll()
   			 	.antMatchers("/js/**").permitAll() 	
   			 	.antMatchers("/img/**").permitAll()
   			 	.antMatchers("/fragments/**").permitAll()
   			 	.antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login").failureUrl("/login-error").defaultSuccessUrl("/")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }
	@Bean
	public BCryptPasswordEncoder passwordEncoder(int n){
	    return new BCryptPasswordEncoder(n);
	}
	@Bean
	public CustomPasswordEncode customPasswordEncoder() {
		return new CustomPasswordEncode();
	}
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(usersDetailsService);
      
        auth.setPasswordEncoder(passwordEncoder(4));
      //  auth.setPasswordEncoder(customPasswordEncoder());
        return auth;
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, UsersDetailsService usersDetailsService) throws Exception {
    	auth.userDetailsService(usersDetailsService).passwordEncoder(passwordEncoder(4));
    	//auth.userDetailsService(usersDetailsService).passwordEncoder( customPasswordEncoder() );
	}
   
	

}