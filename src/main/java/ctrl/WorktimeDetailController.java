package ctrl;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import model.Holiday;
import model.Page;
import model.Worktime;
import service.EmailService;
import service.HolidayService;
import service.WorktimeDetialService;

@Controller
public class WorktimeDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ERROR_PAGE = "login";
	private static final String SEARCH_DETAIL_WORKTIME = "/WEB-INF/views/worktime/detailWorktime";
	private static final String CHECK_WORKTIME_DETAIL_PAGE = "/WEB-INF/views/manager/checkWorktimeDetail";

	private WorktimeDetialService worktimeDetialService = new WorktimeDetialService();
	private HolidayService holidayService = new HolidayService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@RequestMapping("WorktimeDetail.do")
	public String worktimDetail(@RequestParam("action") String action,
			@RequestParam("id") String id,
			@RequestParam("date") String date,
			@RequestParam(required=false ,value ="pageAction") String pageAction,
			@RequestParam(required=false ,value ="firstday")String firstDay ,
			@RequestParam(required=false ,value ="status")String status ,
			@RequestParam(required=false ,value ="reason")String reason ,
			@RequestParam(required=false ,value ="k")String kind ,
			HttpSession session,Model model){ 
		
		if (session.getAttribute("login") == null) {
			return ERROR_PAGE;
		}
		String page = null;
		System.out.println("worktimeDetail_action: " + action);
		switch (action) {

		case "searchDetailWorktime":
			// 轉交至詳細工時資料
			Map<String, String> loginInfo = (Map<String, String>) session.getAttribute("login");
			page = doSearchDetailWorktime(loginInfo,date ,id,model);
			break;

		case "checkWorktimeDetail_page":
			page = doShowcheckWorktimeDetail(id,date,pageAction,model);
			break;

		/**************************** 以下彥儒 ***************************/
		case "checkDetail_page":
			page = doCheckWorktime( id,  firstDay,  
					status, reason,  kind,pageAction,model);
			break;

		/**************************** 以上彥儒 ***************************/

		}
		return page;
	}

	private String doSearchDetailWorktime(Map<String, String> loginInfo ,
			String firstDateOfWeek ,String searchEmpno,Model model) {
		 Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		String position = loginInfo.get("position");
		String empno = loginInfo.get("empno"); 
		Worktime searchData = null;

		System.out.println("搜尋詳細工時- 搜尋員編:" + searchEmpno + " 搜尋日: " + firstDateOfWeek);
		if (position.contains("主管")) {
			searchData = worktimeDetialService.searchDetailWorktim(searchEmpno, firstDateOfWeek);

			if (searchData.getWorktimeDetailList().size() == 0)
				model.addAttribute("errorMsgs", "很抱歉!查無資料。");
			model.addAttribute("DetailWorktime", searchData);

		} else if (position.contains("員工")) {
			searchData = worktimeDetialService.searchDetailWorktim(empno, firstDateOfWeek);

			if (searchData.getWorktimeDetailList().size() == 0)
				model.addAttribute("errorMsgs", "很抱歉!查無資料。");
			model.addAttribute("DetailWorktime", searchData);
		} else {
			model.addAttribute("errorMsgs", "很抱歉!你無權訪問。");
		}
		try {
			holidays = holidayService.getHolidaysByFirstday(
					new Date(new SimpleDateFormat("yyyy-MM-dd").parse(firstDateOfWeek).getTime()));
			model.addAttribute("holidays", holidays);
		} catch (ParseException e) {
			// TODO 自動產生的 catch 區塊
			System.err.println("日期轉換錯誤");
			e.printStackTrace();
		}

		return SEARCH_DETAIL_WORKTIME;
	}

	private String doShowcheckWorktimeDetail(
			String id, String date, String pageAction, Model model) {
		 
		System.out.println("~~~~action:"+pageAction);
		model.addAttribute("id", id);
		model.addAttribute("firstDateOfWeek", date);
		model.addAttribute("pageAction", pageAction);
		return CHECK_WORKTIME_DETAIL_PAGE;
	}

	/**************************** 以下彥儒 ***************************/

	private String doCheckWorktime(
			String id,
			String date, String status,
           String reason, 
			String kind,String pageAction,Model model) {

		String worktimeEmail = null;

		if (status.equals("ok")) {
			worktimeDetialService.passWorktime(id, date);
		} else {
			worktimeEmail = worktimeDetialService.notPassWorktime(id, date, reason);

			EmailService service = new EmailService();

			StringBuilder html = new StringBuilder();

			html.append("<style>p{margin: 0 0;}p span{margin: 0 10px;}</style>");
			html.append("<center><h1>工時系統-工時未通過</h1><hr>");
			html.append("<h2>您的工時已被退回，請重新填寫工時內容</h2>");
			html.append("<p><span>員工編號:</span><span>" + id + "</span></p>");
			html.append("<p><span>周別:</span><span>" + date + "</span></p>");

			html.append("<span style='position: relative;left:30%;'>主管 敬上</span></center>");
			String Title = "工時未通過!";
			try {

				service.sendHtmlMail(worktimeEmail, Title, html.toString());

			} catch (AddressException e) {
				// TODO 自動產生的 catch 區塊
				System.out.println("email地址錯誤: " + e.getMessage());
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO 自動產生的 catch 區塊
				System.out.println("email寄送錯誤: " + e.getMessage());
				e.printStackTrace();
			}

		}
		model.addAttribute("result", "success");

		String uri = "./Worktime.do?action=checkWorktime_page";
		System.out.println();
		if (pageAction != null || (!pageAction.equals(""))) {
			uri = pageAction.substring(pageAction.indexOf("/Worktime.do?"));
		}
		if (!"simple".equals(kind)) {
			uri = "./WorktimeDetail.do?action=checkWorktimeDetail_page&id=" + id + "&date=" + date;
		}

		return uri;
	}

	/**************************** 以上彥儒 ***************************/

}
