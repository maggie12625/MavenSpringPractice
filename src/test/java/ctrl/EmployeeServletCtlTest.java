package ctrl;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Date;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

public class EmployeeServletCtlTest {

	@Test
	public final void testDoPost() throws Exception {
		// TODO
		MockMvc mvcMock = standaloneSetup(new EmployeeServletCtl()).build();
		
		String text = mvcMock.perform(get("/Employee.do").
				sessionAttr("login", new Date())).andReturn()
				.getResponse().getContentAsString();

		System.out.print("text:"+text);
	}
	

	@Test
	public final void testDoGet() throws Exception {
		// TODO
		MockMvc mvcMock = standaloneSetup(new EmployeeServletCtl()).build();
		
		String text = mvcMock.perform(get("/Employee.do").
				sessionAttr("login", new Date())).andReturn()
				.getResponse().getContentAsString();

		System.out.print("text:"+text);
	}

	@Test
	public final void testDoFindPersonalInfo() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public final void testDoChangePwd() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public final void testDoFindEmpInfo() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public final void testDoSearchEmployee() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public final void testDoAddEmp() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public final void testDoValidateInsertEmp() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public final void testDoInsertEmp() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public final void testDoFindUpdateEmp() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public final void testDoFindModifyInfo() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public final void testDoValidateModifyEmp() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

	@Test
	public final void testDoUpdateEmpInfo() throws Exception {
		// TODO
		throw new RuntimeException("not yet implemented");
	}

}
