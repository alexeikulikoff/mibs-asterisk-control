package mibs.asterisk.control.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import mibs.asterisk.control.service.UsersDetailsService;

@Configuration
@EnableWebSecurity
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 700)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
            .authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/restore").permitAll()
   			 	.antMatchers("/forgot_password").permitAll()
   			 	.antMatchers("/verifyMail").permitAll()
   			 	.antMatchers("/css/**").permitAll().anyRequest().permitAll()
   			 	.antMatchers("/font-awesome/**").permitAll().anyRequest().permitAll()
   			 	.antMatchers("/js/**").permitAll().anyRequest().permitAll()   			 	
   			 	.antMatchers("/img/**").permitAll()
   			 	.antMatchers("/fragments/**").permitAll()
   			 	.antMatchers("/admin/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login").failureUrl("/login-error").defaultSuccessUrl("/")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, UsersDetailsService usersDetailsService) throws Exception {
    	auth.userDetailsService(usersDetailsService).passwordEncoder(new BCryptPasswordEncoder( 4 ));
	}
}