package mibs.asterisk.control.test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.Filter;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import mibs.asterisk.control.controllers.UsersController;
import mibs.asterisk.control.dao.Users;
import mibs.asterisk.control.entity.UserEntity;
import mibs.asterisk.control.repository.UserRepository;
import mibs.asterisk.control.service.UsersDetailsService;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
public class UserControllerTest {

	//@Autowired
	private MockMvc mvc;
	
	@Autowired
    private WebApplicationContext context;
	
	
	@MockBean
	private UserRepository repository;
	
	@MockBean
	private UsersDetailsService usersDetailsService;
	
	
	
	@Before
	public void setup() {
	    mvc = MockMvcBuilders.webAppContextSetup(context)
	    		.defaultRequest(get("/").with(user("admin").password("123")))
	    		.apply(springSecurity()).build();
	}
	
	@Test
	public void mockMvcLoads() throws Exception {
	        assertThat(mvc).isNotNull();
	}
	@Test
	public void testTest() throws Exception {
		mvc.perform(get("/test")).andDo(print()).andExpect(status().isOk());
		
	}
	@Test
	public void testNotFoundUser() throws Exception {
		Answer<Optional<UserEntity> > answer = new Answer<Optional<UserEntity> >() {
			@Override
			public Optional<UserEntity> answer(InvocationOnMock invocation) throws Throwable {
				UserEntity entity = new UserEntity(null, null, null, null);
				return Optional.of(entity);
			}
		};
		
		when(repository.findById(new Long(11))).thenAnswer(answer);
		
		MvcResult result  = mvc.perform(get("/findUser").param("id", "11")).andDo(print())
							    .andExpect(status().isOk()).andReturn();
		Users user  = new ObjectMapper().readValue( result.getResponse().getContentAsString() , Users.class);
		assertEquals(user.getName(), null);
	}
	@Test
	public void testFindUser() throws Exception {
		Answer<Optional<UserEntity> > answer = new Answer<Optional<UserEntity> >() {
			@Override
			public Optional<UserEntity> answer(InvocationOnMock invocation) throws Throwable {
				UserEntity entity = new UserEntity(1L, "angelina", "123", "USER");
				return Optional.of(entity);
			}
		};
		when(repository.findById(new Long(1))).thenAnswer(answer);
		MvcResult result  = mvc.perform(get("/findUser").param("id", "1")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value("1"))
			.andExpect(jsonPath("$.name").value("angelina"))
			.andReturn();
		String json = result.getResponse().getContentAsString();
		Users user  = new ObjectMapper().readValue(json, Users.class);
		assertEquals(user.getId(), new Long(1));
		
	}
	
}
