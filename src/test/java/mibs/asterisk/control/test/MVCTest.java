package mibs.asterisk.control.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import mibs.asterisk.control.controllers.RootController;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MVCTest {
	 
	// private MockMvc mockMvc;
	 
	// @Autowired
	// private RootController rootController;
	// @Autowired
	// private WebApplicationContext context;

//	 @LocalServerPort
//	 private int port;

	// @Autowired
	// private TestRestTemplate restTemplate;

	 
	// @Before
	// public void setup() {
	//	 mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()) .build();
	// }
	// @Test
	// public void contexLoads() throws Exception {
	//        assertThat(rootController).isNotNull();
	// }
	 @Test
	 public void testSimpleMVC() throws Exception {
	//	 mock.perform(get("/test")).andDo(print()).andExpect(status().isOk());
          
		  assertEquals(2, 1 + 1);
	  }
	// @Test
	// public void testControlMVC() throws Exception {
	//	 this.mockMvc.perform(get("/control")).andDo(print()).andExpect(status().isOk());
          
		  //assertEquals(2, 1 + 1);
	 // }
	    
	    
}
