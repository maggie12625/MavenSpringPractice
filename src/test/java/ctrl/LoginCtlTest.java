package ctrl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.web.servlet.view.InternalResourceViewResolver;

import service.EmployeeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

@RunWith(MockitoJUnitRunner.class)
public class LoginCtlTest {
	
	MockMvc mvcMock;
	@Mock
	private EmployeeService employeeService;
	@InjectMocks
	private LoginCtl loginCtl;

	
    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = 
        		new InternalResourceViewResolver();
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".jsp");
 
        mvcMock = standaloneSetup(loginCtl)
                                 .setViewResolvers(viewResolver)
                                 .build();
    }
	/**
	 * 登入成功
	 * */
	@Test
	public void testSimple() throws Exception {

		RequestBuilder requestBuilder=post("/Login.do")
				.param("id", "000000")
				.param("password", "a");
		
		when(employeeService.hasEmp(anyString())).thenReturn(true);
		when(employeeService.checkAccount(any())).thenReturn(true);
		when(employeeService.getLoginInfo(anyString())).thenReturn(anyMap());

        mvcMock.perform(requestBuilder)
		       .andExpect(status().isOk())
		       .andExpect(view().name("main"))
		       .andExpect(forwardedUrl("main.jsp"));
	}
	/**
	 * 登入錯誤
	 * */
	@Test
	public void testSimple1() throws Exception {
		
		RequestBuilder requestBuilder=post("/Login.do")
				.param("id", "abcde")
				.param("password", "a");
		
		when(employeeService.hasEmp(anyString())).thenReturn(false);
		
        mvcMock.perform(requestBuilder)
		        .andExpect(status().isOk())
		        .andExpect(view().name("login"))
		        .andExpect(forwardedUrl("login.jsp"));
	
	}

}
