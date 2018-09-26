package mibs.asterisk.control.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import mibs.asterisk.control.controllers.RootController;
import mibs.asterisk.control.controllers.UsersController;


//@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MVCTest {
	 
	// @Autowired
	 private MockMvc mockMvc;

	  @Before
	  public void setup() {
	       // this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	        this.mockMvc = MockMvcBuilders.standaloneSetup(new RootController()).build();
	  }
	  @Test
	   public void testSimpleMVC() throws Exception {
		  this.mockMvc.perform(get("/test")).andDo(print()).andExpect(status().isOk());
          
		  //assertEquals(2, 1 + 1);
	  }
	    
}
