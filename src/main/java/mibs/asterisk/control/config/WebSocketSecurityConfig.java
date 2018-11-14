package mibs.asterisk.control.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer{

	  @Override
	   protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
	    	messages.simpMessageDestMatchers("/receiver/**", "/topic/**").permitAll()
	    			.simpMessageDestMatchers("/receiver2/**", "/topic2/**").anonymous()
					.simpSubscribeDestMatchers("/topic/**", "/receiver/**" ).permitAll()
					.simpSubscribeDestMatchers("/topic2/**", "/receiver2/**" ).anonymous()
					.anyMessage().permitAll();
	}
	
}
