package ctrl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.hamcrest.core.IsNot;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import model.Employee;
import service.EmailService;
import service.EmployeeService;

@RunWith(MockitoJUnitRunner.class)
public class ForgetPwdCtlTest {
	
	MockMvc mvcMock;
	@Mock
	private EmployeeService employeeService;
	@Mock
	private EmailService service;
	@InjectMocks
	private ForgetPwdCtl forgetPwdCtl;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = 
        		new InternalResourceViewResolver();
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".jsp");
 
        mvcMock = standaloneSetup(forgetPwdCtl)
                                 .setViewResolvers(viewResolver)
                                 .build();
    }
    /**
     *忘記密碼成功 
     * */
	@Test
	public final void testSimple() throws Exception {
		Employee employee=new Employee();
		employee.setId("A123456789");
		
	    when(employeeService.hasEmp("000120")).thenReturn(true);
	    when(employeeService.isStartedEmp("000120")).thenReturn(true);
	    when(employeeService.getPersonalInfo("000120")).thenReturn(employee);

		RequestBuilder requestBuilder=post("/ForgetPwd.do")
				.param("empno", "000120")
				.param("id", "A123456789");	    
		
		mvcMock.perform(requestBuilder)
		        .andExpect(status().isOk())
		        .andExpect(model().attribute("result", true))
		        .andExpect(view().name("forgetPwd"))
		        .andExpect(forwardedUrl("forgetPwd.jsp"));
	}
	
    /**
     *忘記密碼失敗 
     * */
	@Test
	public final void testSimple1() throws Exception {
		Employee employee=new Employee();
		employee.setId("A123456789");
		
	    when(employeeService.hasEmp("000120")).thenReturn(true);
	    when(employeeService.isStartedEmp("000120")).thenReturn(true);
	    when(employeeService.getPersonalInfo("000120")).thenReturn(employee);

		RequestBuilder requestBuilder=post("/ForgetPwd.do")
				.param("empno", "000120")
				.param("id", "A12345678");
	    
		
		ResultActions resultActions=mvcMock.perform(requestBuilder);
		 MockHttpServletResponse mockHttpServletResponse = resultActions .andExpect(status().isOk())
		        .andExpect(model().attribute("result",false))
		        .andExpect(view().name("forgetPwd"))
		        .andExpect(forwardedUrl("forgetPwd.jsp")).andReturn().getResponse();
		 String string=mockHttpServletResponse.toString();
	}

}
