package mibs.asterisk.control.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import mibs.asterisk.control.service.Message;

@Controller
public class AsteriskController {

	  @MessageMapping("/receiver")
	  @SendTo("/topic/sender")
	  public Message sendMessage(Message message) throws Exception {
	        Thread.sleep(1000); // simulated delay
	        return new Message("Hello, " + HtmlUtils.htmlEscape(message.getContent()) + "!");
	  }
	  
	  
}
