package ctrl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

public class ForgetPwdCtlTest {

	@Test
	public final void testSimple() throws Exception {
		// TODO
		MockMvc mvcMock = standaloneSetup(new ForgetPwdCtl()).build();
		RequestBuilder requestBuilder=post("/ForgetPwd.do")
				.param("empno", "000120")
				.param("id", "a");
	
		
		mvcMock.perform(requestBuilder)
		        .andExpect(status().isOk())
		        .andExpect(view().name("forgetPwd"));
	}

}
