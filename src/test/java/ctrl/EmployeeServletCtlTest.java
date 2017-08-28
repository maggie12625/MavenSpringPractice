package ctrl;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.Before;
import org.junit.Ignore;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import model.Employee;
import model.Page;
import service.EmailService;
import service.EmployeeService;
import service.StartEmpService;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServletCtlTest {

	MockMvc mvcMock;
	@Mock private EmployeeService employeeService;
	@Mock private EmailService service;
	@Mock private StartEmpService startEmpService;
	@InjectMocks
	private EmployeeServletCtl employeeServletCtl;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = 
        		new InternalResourceViewResolver();
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".jsp");
 
        mvcMock = standaloneSetup(employeeServletCtl)
                                 .setViewResolvers(viewResolver)
                                 .build();
    }
    /*
     * info
     * */
	@Test
	public final void testDoFindPersonalInfo() throws Exception {
		Map<String, String> login=new HashMap<String, String>();
		login.put("empno", "000120");
		
		Employee employee=new Employee();
	    when(employeeService.getPersonalInfo("000120")).thenReturn(employee);
		
		RequestBuilder requestBuilder=get("/Employee.do")
				.param("action", "info")
				.param("empno", "000120")
				.sessionAttr("login", login);

	  
		ResultActions actions=mvcMock.perform(requestBuilder);
		actions .andExpect(status().isOk())
		        .andExpect(model().attribute("employee", employee))
		        .andExpect(view().name("/WEB-INF/views/employee/info"))
		        .andExpect(forwardedUrl("/WEB-INF/views/employee/info.jsp"));
	}
	/*
	 *changePwd_page 
	 * */
	@Test
	public final void testDoChangePwd() throws Exception {
		Map<String, String> login=new HashMap<String, String>();
		login.put("empno", "000120");
		
		Employee employee=new Employee();
		employee.setEmpno("000120");
		
	    when(employeeService.getPersonalInfo("000120")).thenReturn(employee);
		when(employeeService.checkAccount(employee)).thenAnswer(new Answer<Boolean>() {

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				if (((Employee) (invocation.getArgument(0))).getEmpno().equals("000120")) {
					return true;
				}
				else {
					return false;
				}
			}
		});
	    
		RequestBuilder requestBuilder=get("/Employee.do")
				.param("action", "changPwd_page")
				.param("old_pw", "a")
				.param("user-pwd-input", "a123456")
				.param("user_pwd_again", "a123456")
				.sessionAttr("login", login);
	  
		ResultActions actions=mvcMock.perform(requestBuilder);
		actions .andExpect(status().isOk())
		        .andExpect(view().name("/WEB-INF/views/employee/changePwd"))
		        .andExpect(forwardedUrl("/WEB-INF/views/employee/changePwd.jsp"));
	}
	@Test
	public final void testDoFindEmpInfo() throws Exception {
		Map<String, String> login=new HashMap<String, String>();
		login.put("empno", "000120");
		
		Page page=new Page();
		
		Map<String, Object> dataMap=new HashMap<String, Object>();
		dataMap.put("page", page);
		
		when(employeeService.findEmpInfo(any(Page.class))).thenReturn(dataMap);
	    
		RequestBuilder requestBuilder=get("/Employee.do")
				.param("action", "mgrSearchEmp_page")
				.sessionAttr("login", login);
	  
		ResultActions actions=mvcMock.perform(requestBuilder);
		actions.andExpect(status().isOk()).andExpect(model().attribute("page", page))
				.andExpect(model().attribute("empInfoList", dataMap.get("List<Employee>")))
				.andExpect(view().name("/WEB-INF/views/manager/mgrSearchEmpInfo"))
				.andExpect(forwardedUrl("/WEB-INF/views/manager/mgrSearchEmpInfo.jsp"));
	}

	@Test
	public final void testDoSearchEmployee() throws Exception {
		Map<String, String> login=new HashMap<String, String>();
		login.put("empno", "000120");
		
		Page page=new Page();
		
		Map<String, Object> dataMap=new HashMap<String, Object>();
		dataMap.put("page", page);

		
		when(employeeService.findByEmpno(anyString(), any(Page.class))).thenReturn(dataMap);
	    
		RequestBuilder requestBuilder=post("/Employee.do")
				.param("action", "search_employee")
				.param("by", "empno")
				.param("keyword","000370")
				.sessionAttr("login", login);
	  
		ResultActions actions=mvcMock.perform(requestBuilder);
		actions.andExpect(status().isOk())
		        .andExpect(model().attribute("page", page))
				.andExpect(model().attribute("empInfoList", dataMap.get("List<Employee>")))
				.andExpect(view().name("/WEB-INF/views/manager/mgrSearchEmpInfo"))
				.andExpect(forwardedUrl("/WEB-INF/views/manager/mgrSearchEmpInfo.jsp"));
	}

	@Test
	public final void testDoAddEmp() throws Exception {
		Map<String, String> login=new HashMap<String, String>();
		login.put("empno", "000120");

		when(employeeService.getMaxEmpNoNext()).thenReturn("999999");
	    
		RequestBuilder requestBuilder=get("/Employee.do")
				.param("action", "addEmp")
				.sessionAttr("login", login);
	  
		ResultActions actions=mvcMock.perform(requestBuilder);
		actions.andExpect(status().isOk())
				.andExpect(view().name("/WEB-INF/views/admin/addEmp"))
				.andExpect(forwardedUrl("/WEB-INF/views/admin/addEmp.jsp"));
	}
	@Test
	public final void testDoValidateInsertEmp() throws Exception {
		Map<String, String> login=new HashMap<String, String>();
		login.put("empno", "000000");
		
		RequestBuilder requestBuilder=post("/Employee.do")
				.sessionAttr("login", login)
				.param("action","validateInsertEmp" )
				.param("name", "杜德偉")
				.param("id", "A123456789")
				.param("positionME", "manager")
		        .param("positionSys","on")
		        .param("email", "abc@fda.com");

		
		mvcMock.perform(requestBuilder)
		        .andExpect(status().isOk())
				.andExpect(view().name("/WEB-INF/views/admin/addEmp1"))
				.andExpect(forwardedUrl("/WEB-INF/views/admin/addEmp1.jsp"));
	}
	@Test
	public final void testDoInsertEmp() throws Exception {
		Map<String, String> login=new HashMap<String, String>();
		login.put("empno", "000000");
		
		when(startEmpService.addEmp(anyString(), any(HttpServletRequest.class)))
		.thenReturn("localhost:8080/bbb?uri=fklflmof");
		
		RequestBuilder requestBuilder=post("/Employee.do")
				.sessionAttr("login", login)
				.param("action","insertEmp" )
				.sessionAttr("maxEmpNo", "999999")
				.sessionAttr("name", "杜德偉")
				.sessionAttr("id", "A123456789")
				.sessionAttr("positionME", "manager")
		        .sessionAttr("positionSys","on")
		        .sessionAttr("email", "abc@fda.com");

		
		mvcMock.perform(requestBuilder)
		        .andExpect(status().isOk())
				.andExpect(view().name("/WEB-INF/views/admin/addEmp"))
				.andExpect(forwardedUrl("/WEB-INF/views/admin/addEmp.jsp"));
		
		
	}
	@Test
	public final void testDoFindUpdateEmp() throws Exception {
		Map<String, String> login=new HashMap<String, String>();
		login.put("empno", "000000");
		
		Page page=new Page();
		
		Map<String, String> updateEmp = new HashMap<String, String>();
		Map<String, Object> dataMap=new HashMap<String, Object>();
		dataMap.put("page", page);
		
		when(employeeService.findUpdateEmpInfo(any(HashMap.class), any(Page.class)))
		.thenReturn(dataMap);
		
		RequestBuilder requestBuilder=post("/Employee.do")
				.sessionAttr("login", login)
				.param("action","FindUpdateEmp" )
				.param("input", "000370")
				.param("by", "empno");

		mvcMock.perform(requestBuilder)
		        .andExpect(status().isOk())
		        .andExpect(model().attribute("page", page))
				.andExpect(view().name("/WEB-INF/views/admin/modifyEmpData"))
				.andExpect(forwardedUrl("/WEB-INF/views/admin/modifyEmpData.jsp"));
	}
	@Test
	public final void testDoFindModifyInfo() throws Exception {
		Map<String, String> login=new HashMap<String, String>();
		login.put("empno", "000000");
		
		when(employeeService.findModifyEmpInfo("000370")).thenReturn(new HashMap<String, String>());
		
		RequestBuilder requestBuilder=get("/Employee.do")
				.sessionAttr("login", login)
				.param("action","updateEmp_page1" )
				.param("modifyempno", "000370");

		mvcMock.perform(requestBuilder)
		        .andExpect(status().isOk())
				.andExpect(view().name("/WEB-INF/views/admin/modifyEmpData1"))
				.andExpect(forwardedUrl("/WEB-INF/views/admin/modifyEmpData1.jsp"));
	}
	@Test
	public final void testDoValidateModifyEmp() throws Exception {
		Map<String, String> login=new HashMap<String, String>();
		login.put("empno", "000000");
		
		RequestBuilder requestBuilder=post("/Employee.do")
				.sessionAttr("login", login)
				.param("action","validateModifyEmp" )
				.param("name", "杜德偉")
				.param("id", "A123456789")
				.param("positionME", "manager")
		        .param("positionSys","on")
		        .param("email", "abc@fda.com")
		        .param("end", "");;

		
		mvcMock.perform(requestBuilder)
		        .andExpect(status().isOk())
				.andExpect(view().name("/WEB-INF/views/admin/modifyEmpData2"))
				.andExpect(forwardedUrl("/WEB-INF/views/admin/modifyEmpData2.jsp"));
	}
	@Test
	public final void testDoUpdateEmpInfo() throws Exception {
		Map<String, String> login=new HashMap<String, String>();
		login.put("empno", "000000");
		
		Map<String,String> modifyInfoList=new HashMap<String,String>();
		modifyInfoList.put("name", "杜德偉");
		modifyInfoList.put("id", "A123456789");
		modifyInfoList.put("positionME", "manager");
		modifyInfoList.put("positionSys","on");
		modifyInfoList.put("email", "abc@fda.com");
		modifyInfoList.put("end", "");
		
		
		RequestBuilder requestBuilder=post("/Employee.do")
				.sessionAttr("login", login)
				.param("action","doUpdateEmpInfo" )
				.sessionAttr("Empno", "000370")
		        .sessionAttr("modifyInfoList", modifyInfoList);
		
		mvcMock.perform(requestBuilder)
		        .andExpect(status().isOk())
		        .andExpect(model().attribute("result", true))
				.andExpect(view().name("/WEB-INF/views/admin/modifyEmpData"))
				.andExpect(forwardedUrl("/WEB-INF/views/admin/modifyEmpData.jsp"));
	}

}
