package mibs.asterisk.control.controllers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import mibs.asterisk.control.service.AsteriskQuery;
import mibs.asterisk.control.service.AsteriskResponce;
import mibs.asterisk.control.service.UsersDetails;

@Controller
public class CallBoardController {

	@Autowired
	protected SimpMessagingTemplate simpMessagingTemplate;
	
	@RequestMapping("/callboard")
	public String callboard(Model model, @AuthenticationPrincipal UsersDetails activeUser) {
		model.addAttribute("name", "Pizda");
		return "callboard";
	}
	@MessageMapping("/receiver2")
	@SendTo("/topic/sender2")
	public AsteriskResponce handleMessage(AsteriskQuery query) throws Exception {
	
		System.out.println(query);
		
		AsteriskResponce resp = new AsteriskResponce();
		resp.setContent("1111");
		
		return resp;
		
	/*	
		new Thread(new Runnable() {
			int j = 0;
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(2000);
						resp.setContent(++j+"");
						simpMessagingTemplate.convertAndSend("/topic/sender2", resp);
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
*/		
	}

	
}
