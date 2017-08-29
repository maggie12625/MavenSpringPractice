package ctrl;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


public class LogoutCtlTest {

	MockMvc mvcMock;

	private LogoutCtl logoutCtl=new LogoutCtl();
	
    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".jsp");
 
        mvcMock = standaloneSetup(logoutCtl)
                                 .setViewResolvers(viewResolver)
                                 .build();
    }
	/**
	 * 登入過登出
	 * */
	@Test
	public void testSimple() throws Exception {

		RequestBuilder requestBuilder=post("/Logout.do")
				.sessionAttr("login",new Object());

        mvcMock.perform(requestBuilder)
		       .andExpect(status().isOk())
		       .andExpect(view().name("login"))
		       .andExpect(forwardedUrl("login.jsp"));
	}
	
	/**
	 * 未登入過登出
	 * */
	@Test
	public void testSimple1() throws Exception {

		RequestBuilder requestBuilder=post("/Logout.do");

        mvcMock.perform(requestBuilder)
		       .andExpect(status().isOk())
		       .andExpect(view().name("login"))
		       .andExpect(forwardedUrl("login.jsp"));
	}

}
