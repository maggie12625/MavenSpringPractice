package ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.HolidayService;
import service.WorktimeDetialService;

@Controller
public class WorktimeDetailServlet {
	private static final String SEARCH_DETAIL_WORKTIME = "/WEB-INF/views/worktime/detailWorktime.jsp";
	private static final String CHECK_WORKTIME_DETAIL_PAGE = "/WEB-INF/views/manager/checkWorktimeDetail.jsp";

	private WorktimeDetialService worktimeDetialService = new WorktimeDetialService();
	private HolidayService holidayService = new HolidayService();

	@RequestMapping(value="/simple")
	public @ResponseBody String simple() {
		return "Hello world revisited!";
	}

}
