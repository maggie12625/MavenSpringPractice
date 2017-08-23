package ctrl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dao.ConnectionHelper;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;


public class LoginCtlTest {
	@Before
	public void contextSetup() throws Exception {		
//	    try {
//
//	        // Create initial context
//	        System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
//	            "org.apache.naming.java.javaURLContextFactory");
//	        System.setProperty(Context.URL_PKG_PREFIXES, 
//	            "org.apache.naming");            
//	        InitialContext ic = new InitialContext();
//
//	        ic.createSubcontext("java:");
//	        ic.createSubcontext("java:/comp");
//	        ic.createSubcontext("java:/comp/env");
//	        ic.createSubcontext("java:/comp/env/jdbc");
//
//	        // Construct DataSource
//	        OracleConnectionPoolDataSource ds = new OracleConnectionPoolDataSource();
//	        ds.setURL("jdbc:oracle:thin:@//200.200.200.200:49161/xe"); // <--insert url to database here
//	        ds.setUser("system"); //<-- self explanatory
//	        ds.setPassword("oracle"); //<-- self explanatory
//	        
//            ic.bind("java:comp/env/jdbc/fjut5_3_wt_ds", ds); //<--insert name of binding here
//            
//	    } catch (NamingException ex) {
//	        ex.printStackTrace();
//	    } catch (SQLException ex){
//	        ex.printStackTrace();
//	    }
		
//		Context initContext = new InitialContext();
//		Context envContext = (Context) initContext.lookup("java:/comp/env");
//		DataSource dataSource = (DataSource) envContext.lookup("jdbc/fjut5_3_wt_ds");
//		SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
//		builder.bind("java:comp/env/jdbc/fjut5_3_wt_ds", dataSource);
//		builder.activate();
	}
	//登入成功
	@Test
	public void testSimple() throws Exception {
		
		MockMvc mvcMock = standaloneSetup(new LoginCtl()).build();
		RequestBuilder requestBuilder=post("/Login.do")
				.param("id", "000000")
				.param("password", "a");
		
		mvcMock.perform(requestBuilder)
		        .andExpect(status().isOk())
		        .andExpect(view().name("main"));
	}
	//登入錯誤
	@Test
	public void testSimple1() throws Exception {
		
		MockMvc mvcMock = standaloneSetup(new LoginCtl()).build();
		RequestBuilder requestBuilder=post("/Login.do")
				.param("id", "000000")
				.param("password", "a");
		
		String text=mvcMock.perform(requestBuilder)
		        .andExpect(status().isOk())
		        .andExpect(view().name("login"))
		        .andReturn().getResponse().getContentAsString();
	
		System.out.println("text:"+text);
	}

}
