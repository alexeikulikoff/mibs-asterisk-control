package mibs.asterisk.control.service;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import mibs.asterisk.control.utils.AnonymousPrincipal;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
	
	@Override
	protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
	
		AnonymousPrincipal principal = (AnonymousPrincipal) request.getPrincipal();     
		if (principal == null) {
		   principal = new AnonymousPrincipal();
		   principal.setName("admin");
		}
		System.out.println("Principal---> " + principal);
	    return principal;
    }
}
