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
import org.mockito.runners.MockitoJUnitRunner;
//import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.Before;
import org.junit.Ignore;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import model.Employee;
import model.Holiday;
import model.Page;
import model.Worktime;
import service.EmailService;
import service.EmployeeService;
import service.HolidayService;
import service.StartEmpService;
import service.WorktimeDetialService;
import service.WorktimeService;

@RunWith(MockitoJUnitRunner.class)
public class WorktimeCtlTest {
	MockMvc mockMvc;
	@Mock private WorktimeService worktimeService;
	@Mock private WorktimeDetialService worktimeDetailService;
	@Mock private HolidayService holidayService;
	@InjectMocks
	private WorktimeController worktimeCtl;
	
@Before
public void setup() {
    InternalResourceViewResolver viewResolver = 
    		new InternalResourceViewResolver();
    viewResolver.setPrefix("");
    viewResolver.setSuffix(".jsp");
    
    mockMvc = standaloneSetup(worktimeCtl)
    		.setViewResolvers(viewResolver)
            .build();

}

@Test
public final void testDoGetEmpWorktime() throws Exception {
	Map<String, String> login=new HashMap<String, String>();
	login.put("empno", "000369");
	
	Map<String, Holiday> holidays = new HashMap<String, Holiday>();
	List<Worktime> worktimeList = new ArrayList<Worktime>();
	
	when(worktimeService.getWorktimeInfo("000369")).thenReturn(worktimeList);
	
	for (int i = 0; i < worktimeList.size(); i++) {
	when(holidayService.getHolidaysByFirstday(worktimeList.get(i).getWeekFirstDay()))
			.thenReturn(holidays);
	
	}
	RequestBuilder requestBuilder=get("/Worktime.do")
			.param("action", "writeWorktime_page")
			.param("empno", "000396")
			.sessionAttr("login", login);

  
	ResultActions actions=mockMvc.perform(requestBuilder);
	actions .andExpect(status().isOk())
	        .andExpect(model().attribute("worktimeList", worktimeList))
	        .andExpect(view().name("/WEB-INF/views/worktime/writeWorktime"))
	        .andExpect(forwardedUrl("/WEB-INF/views/worktime/writeWorktime.jsp"));
	
}
}


