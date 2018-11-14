package mibs.asterisk.control.config;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.sockjs.transport.handler.SockJsWebSocketHandler;

import mibs.asterisk.control.service.CustomHandshakeHandler;

//@DependsOn("AppConfig") 
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<Session>{

	@Autowired
	private AppConfig appConfig;
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		 Message preSend(Message message,  MessageChannel channel) {
		        StompHeaderAccessor accessor = StompHeaderAccessor.wrap((org.springframework.messaging.Message<?>) message);

		        List<String> tokenList = accessor.getNativeHeader("X-Authorization");
		        
		        System.out.println( tokenList);
		        accessor.removeNativeHeader("X-Authorization");

		        String token = null;
		        if(tokenList != null && tokenList.size() > 0) {
		          token = (String) tokenList.get(0);
		        }

		        // validate and convert to a Principal based on your own requirements e.g.
		        // authenticationManager.authenticate(JwtAuthentication(token))
		      //  Principal yourAuth = token == null ? null : "...";
		 };
	
	}
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.enableSimpleBroker("/topic2");
        config.setApplicationDestinationPrefixes( appConfig.getServerContextPath() );
    }

	@Override
	protected void configureStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").withSockJS();
		registry.addEndpoint("/ws2").setHandshakeHandler(new CustomHandshakeHandler()).withSockJS();
		
		
	}

	

}