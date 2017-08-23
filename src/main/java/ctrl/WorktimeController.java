package ctrl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import model.Employee;
import model.Holiday;
import model.Page;
import model.Worktime;
import model.WorktimeDetail;
import service.EmailService;
import service.EmployeeService;
import service.HolidayService;
import service.WorktimeDetialService;
import service.WorktimeService;
import service.excelService;
import utils.ValidateUtils;

/**
 * Servlet implementation class WorktimeServlet
 */
@Controller

public class WorktimeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String ORIGIN_PAGE = "login";
	/************************************** 以下陳民錞 ************************************/
	private static final String WRITEWORKTIME_PAGE = "/WEB-INF/views/worktime/writeWorktime";
	private static final String WRITEWORKTIME_SUB_PAGE = "/WEB-INF/views/worktime/writeWorktimeSub";
	private static final String MGR_SEARCH_WORKTIME_PAGE = "/WEB-INF/views/manager/mgrSearchWorktime";
	/************************************** 以上陳民錞 ************************************/

	/************************************** 以下張芷瑄 ************************************/
	private static final String CAllWORKTIME_PAGE = "/WEB-INF/views/manager/callWorktime";

	/************************************** 以上張芷瑄 ************************************/

	/************************************** 以下彥儒 ************************************/
	private static final String CHECKWORKTIME_PAGE = "/WEB-INF/views/manager/checkWorktime";

	/************************************** 以上彥儒 ************************************/

	/************************************** 以下吳軒穎 ************************************/
	private static final String SEARCH_WORKTIME_EMP_PAGE = "/WEB-INF/views/employee/searchEmpWorktime";
	/************************************** 以上吳軒穎 ************************************/

	private WorktimeService worktimeService = new WorktimeService();
	private WorktimeDetialService worktimeDetialService = new WorktimeDetialService();
	private HolidayService holidayService = new HolidayService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@RequestMapping(value = "Worktime.do", method = RequestMethod.POST)
	public String WorktimePost(Model model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("action") String action,
			@RequestParam(required = false, value = "page") String pageNum,
			@RequestParam(required = false, value = "firstday") String firstday,
			@RequestParam(required = false, value = "keyword") String keyword,
			@RequestParam(required = false, value = "yearmon") String yearMonth,
			@RequestParam(required = false, value = "do") String doSomething,
			@RequestParam(required = false, value = "date") String searchDate,
			@RequestParam(required = false, value = "pageAction") String pageAction,
			@RequestParam(required = false, value = "detailId") String detailId,
			@RequestParam(required = false, value = "by") String searchBy,
			@RequestParam(required = false, value = "week") String dateAndWeek,
			@RequestParam(required = false, value = "emps") String[] datas,
			@RequestParam(required = false, value = "workNames") String[] workNames,
			@RequestParam(required = false, value = "workContents") String[] workContents,
			@RequestParam(required = false, value = "sunNormal") String sunNormal,
			@RequestParam(required = false, value = "sunOver") String sunOver,
			@RequestParam(required = false, value = "monNormal") String monNormal,
			@RequestParam(required = false, value = "monOver") String monOver,
			@RequestParam(required = false, value = "tueNormal") String tueNormal,
			@RequestParam(required = false, value = "tueOver") String tueOver,
			@RequestParam(required = false, value = "wedNormal") String wedNormal,
			@RequestParam(required = false, value = "wedOver") String wedOver,
			@RequestParam(required = false, value = "thuNormal") String thuNormal,
			@RequestParam(required = false, value = "thuOver") String thuOver,
			@RequestParam(required = false, value = "friNormal") String friNormal,
			@RequestParam(required = false, value = "friOver") String friOver,
			@RequestParam(required = false, value = "satNormal") String satNormal,
			@RequestParam(required = false, value = "satOver") String satOver,
			@RequestParam(required = false, value = "detailIds") String detailIds

	) {

		String page = null;
		if (session.getAttribute("login") == null) {
			return ORIGIN_PAGE;
		}
		System.out.println("worktiom_action: " + action);
		switch (action) {

		case "saveDetailWorktime":
			page = doSaveDetailWorktime(workNames, sunNormal, sunOver, monNormal, monOver, tueNormal, tueOver,
					wedNormal, satOver, satNormal, friOver, wedOver, thuNormal, thuOver, friNormal, workContents,
					request, doSomething, firstday, model, session);
			break;

		case "searchEmpWorktime":
			page = doSearchEmpWorktime(model, session, searchDate);
			break;
		case "updateStatus":
			page = checkBoxEvent(datas, request, model, pageNum);
			break;
		// 轉交至主管查詢員工工時頁面
		case "mgrSearchWorktime_page":
			try {
				page = doMgrGetEmpWorktime(request, model, keyword, yearMonth, pageNum);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		}
		return page;
	}

	@RequestMapping(value = "Worktime.do", method = RequestMethod.GET)
	public String WorktimeGet(Model model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response, @RequestParam("action") String action,
			@RequestParam(required = false, value = "page") String pageNum,
			@RequestParam(required = false, value = "firstday") String firstday,
			@RequestParam(required = false, value = "keyword") String keyword,
			@RequestParam(required = false, value = "yearmon") String yearMonth,
			@RequestParam(required = false, value = "do") String doSomething,
			@RequestParam(required = false, value = "date") String searchDate,
			@RequestParam(required = false, value = "pageAction") String pageAction,
			@RequestParam(required = false, value = "detailId") String detailId,
			@RequestParam(required = false, value = "by") String searchBy,
			@RequestParam(required = false, value = "week") String dateAndWeek,
			@RequestParam(required = false, value = "emps") String[] datas,
			@RequestParam(required = false, value = "workNames") String[] workNames,
			@RequestParam(required = false, value = "workContents") String[] workContents,
			@RequestParam(required = false, value = "sunNormal") String sunNormal,
			@RequestParam(required = false, value = "sunOver") String sunOver,
			@RequestParam(required = false, value = "monNormal") String monNormal,
			@RequestParam(required = false, value = "monOver") String monOver,
			@RequestParam(required = false, value = "tueNormal") String tueNormal,
			@RequestParam(required = false, value = "tueOver") String tueOver,
			@RequestParam(required = false, value = "wedNormal") String wedNormal,
			@RequestParam(required = false, value = "wedOver") String wedOver,
			@RequestParam(required = false, value = "thuNormal") String thuNormal,
			@RequestParam(required = false, value = "thuOver") String thuOver,
			@RequestParam(required = false, value = "friNormal") String friNormal,
			@RequestParam(required = false, value = "friOver") String friOver,
			@RequestParam(required = false, value = "satNormal") String satNormal,
			@RequestParam(required = false, value = "satOver") String satOver,
			@RequestParam(required = false, value = "detailIds") String detailIds

	)

	{
		String page = null;
		if (session.getAttribute("login") == null) {
			return ORIGIN_PAGE;
		}
		System.out.println("worktiom_action: " + action);
		switch (action) {

		/************************************** 以下陳民錞 ************************************/
		// 轉交至填寫工時頁面
		case "writeWorktime_page":
			page = doGetEmpWorktime(model, session);
			// page = WRITEWORKTIME_PAGE;
			break;
		// 轉交至填寫工時子頁面
		case "writeWorktime_sub_page":
			page = doGetEmpWorktimeDetail(session, model, firstday);
			break;

		case "exportWorktimeExcel":
			doExporWorktime(yearMonth, keyword, request, response);
			return page;

		case "exportWorktimeExcelByEmail":
			doExporWorktimeByEmail(response, session, yearMonth, keyword, request);
			return page;

		/************************************** 以上陳民錞 ************************************/

		/************************************** 以下張芷瑄 ************************************/
		// 轉交至取得狀態名單
		case "callWorktime_page":
			// 轉交至取得未繳交名單
			page = getUnsubmitEmp(request, pageNum);
			break;

		// 全部催繳
		case "select_all":
			page = selectAllEmp(model, request, pageNum);
			break;
		/************************************** 以上張芷瑄 ************************************/

		/************************************** 以下彥儒 ************************************/
		case "checkWorktime_page":
			page = doCheckWorktime(pageNum, model, request);
			break;
		case "checkBox_page":

			page = doCheckbox(request, pageAction, model);
			break;
		case "searchWorktime_page":
			doSearchWorktime(request, model, page, searchBy, keyword, dateAndWeek);

			page = CHECKWORKTIME_PAGE;
			break;

		/************************************** 以上彥儒 ************************************/
		/************************************** 以下吳軒穎 ************************************/
		// 進入工時查詢(員工)，取得當月當年
		case "enterEmpWorktime":
			System.out.println("enterEmpWorktime");
			page = enterEmpWorktime(model);
			break;

		/************************************** 以上吳軒穎 ************************************/

		}

		return page;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	/************************************** 以下張芷瑄 ************************************/
	protected String getUnsubmitEmp(HttpServletRequest request, String pageNum) {
		Map<String, Object> dataMap = null;
		List<Map<String, String>> UnsubmitEmpList = null;
		Page page = new Page();
		if (pageNum != null) {
			page.setNowPage(Integer.parseInt(pageNum));
		}

		dataMap = worktimeService.getUnsubmitEmpList(page);
		UnsubmitEmpList = (List<Map<String, String>>) dataMap.get("List<Map<String, String>>");

		// 設定分頁
		page = (Page) dataMap.get("page");
		page.setAction(request);

		request.setAttribute("page", page);
		request.setAttribute("UnsubmitEmpList", UnsubmitEmpList);
		return CAllWORKTIME_PAGE;
	}

	protected String checkBoxEvent(String[] datas, HttpServletRequest request, Model model, String pageNum) {
		Page page = new Page();
		if (pageNum != null) {
			page.setNowPage(Integer.parseInt(pageNum));
		}
		// String[] datas = request.getParameterValues("emps");
		String[] emps = new String[datas.length];
		String[] date = new String[datas.length];
		String[] email = new String[datas.length];
		String[] name = new String[datas.length];
		// System.out.println(datas[0]);

		for (int i = 0; i < datas.length; i++) {
			// System.out.println("分割："+datas[i].split("~").length);
			// System.out.println("emp:"+datas[i].split("~")[0]+" date:
			// "+datas[i].split("~")[1]);
			emps[i] = datas[i].split("~")[0];
			date[i] = datas[i].split("~")[1];
			email[i] = datas[i].split("~")[2];
			name[i] = datas[i].split("~")[3];
		}
		worktimeService.updateStatus(emps, date);
		worktimeService.addCalltimes(emps, date);
		// 發送email
		EmailService emailService = new EmailService();

		for (int i = 0; i < datas.length; i++) {
			StringBuilder html = new StringBuilder();
			html.append("<style>p{margin: 0 0;}p span{margin: 0 10px;}</style>");
			html.append("<center><h1>工時系統-工時尚未交</h1><hr>");
			html.append("<h2>" + name[i] + "(先生/小姐)<br>您" + date[i] + "週的工時尚未繳交，請盡速完成。</h2>");

			html.append("<span style='position: relative;left:30%;'>主管 敬上</span></center>");
			String Title = "催交工時";
			try {
				emailService.sendHtmlMail(email[i], Title, html.toString());
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

		page.setAction(request);
		model.addAttribute("page", page);
		model.addAttribute("result", true);

		return "/Worktime.do?action=callWorktime_page";

	}

	protected String selectAllEmp(Model model, HttpServletRequest request, String pageNum) {

		Map<String, Object> dataMap = null;
		List<Map<String, String>> allMapEmpList = null;
		Page page = new Page();
		if (pageNum != null) {
			page.setNowPage(Integer.parseInt(pageNum));
		}
		page.setPageSize(99999);
		dataMap = worktimeService.getUnsubmitEmpList(page);
		allMapEmpList = (List<Map<String, String>>) dataMap.get("List<Map<String, String>>");

		String[] emps = new String[allMapEmpList.size()];
		String[] date = new String[allMapEmpList.size()];
		String[] email = new String[allMapEmpList.size()];
		String[] name = new String[allMapEmpList.size()];

		for (int i = 0; i < allMapEmpList.size(); i++) {

			Map<String, String> worktimeMap = allMapEmpList.get(i);
			emps[i] = worktimeMap.get("empno");
			date[i] = worktimeMap.get("firstday");
			email[i] = worktimeMap.get("email");
			name[i] = worktimeMap.get("name");
		}
		System.out.println("全部" + allMapEmpList.size() + "筆要催繳");

		worktimeService.updateStatus(emps, date);
		worktimeService.addCalltimes(emps, date);

		EmailService emailService = new EmailService();

		for (int i = 0; i < allMapEmpList.size(); i++) {
			StringBuilder html = new StringBuilder();
			html.append("<style>p{margin: 0 0;}p span{margin: 0 10px;}</style>");
			html.append("<center><h1>工時系統-工時尚未交</h1><hr>");
			html.append("<h2>" + name[i] + "(先生/小姐)<br>您" + date[i] + "週的工時尚未繳交，請盡速完成。</h2>");

			html.append("<span style='position: relative;left:30%;'>主管 敬上</span></center>");
			String Title = "催交工時";
			try {
				emailService.sendHtmlMail(email[i], Title, html.toString());
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
		page.setAction(request);

		model.addAttribute("page", page);
		model.addAttribute("result", true);
		return "/Worktime.do?action=callWorktime_page";

	}

	/************************************** 以上張芷瑄 ************************************/

	/************************************** 以下陳民錞 ************************************/
	// 取得填寫工時資料
	protected String doGetEmpWorktime(Model model, HttpSession session) {
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Map<String, String> loginInfo = (Map<String, String>) session.getAttribute("login");
		String empno = loginInfo.get("empno");

		List<Worktime> worktimeList = worktimeService.getWorktimeInfo(empno);

		for (int i = 0; i < worktimeList.size(); i++) {
			holidays = holidayService.getHolidaysByFirstday(worktimeList.get(i).getWeekFirstDay());
			worktimeList.get(i).setHolidays(holidays);
		}

		model.addAttribute("worktimeList", worktimeList);
		return WRITEWORKTIME_PAGE;
	}

	// 取得填寫詳細工時
	protected String doGetEmpWorktimeDetail(HttpSession session, Model model, String firstday) {
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Map<String, String> attribute = (Map<String, String>) session.getAttribute("login");
		Map<String, String> loginInfo = attribute;
		String empno = loginInfo.get("empno");
		Worktime worktime = new Worktime();
		worktime.setStatus("未繳交");
		Date firstDateOfWeek = null;
		try {
			firstDateOfWeek = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(firstday).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.err.println("日期轉換錯誤");
		}
		holidays = holidayService.getHolidaysByFirstday(firstDateOfWeek);
		worktime = worktimeDetialService.searchDetailWorktim(empno, firstday);
		worktime.setHolidays(holidays);
		worktime.setWeekFirstDay(firstDateOfWeek);
		if (worktime.getStatus() == null)
			worktime.setStatus("未繳交");

		model.addAttribute("worktime", worktime);
		return WRITEWORKTIME_SUB_PAGE;
	}

	protected String doMgrGetEmpWorktime(HttpServletRequest request, Model model, String keyword, String yearMonth,
			String pageNum) throws ParseException {
		model.addAttribute("hadVisited", true);

		List<Worktime> mgrgetempworktimeList = null;
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Map<String, Object> dataMap = null;
		Page page = new Page();
		if (pageNum != null) {
			page.setNowPage(Integer.parseInt(pageNum));// 讀取分頁
		}

		String searchBy = "name";
		if (keyword.matches("\\d*")) {
			searchBy = "id";
		}

		System.out.println("主管查員工工時-查詢日期: " + yearMonth + " 查 " + searchBy + " :" + keyword + (keyword == ""));
		if (searchBy.equals("name")) {
			// 由名子搜尋
			dataMap = worktimeService.getWorktimeByNameInfo(keyword, yearMonth, page);
			mgrgetempworktimeList = (List<Worktime>) dataMap.get("List<Worktime>");
		} else {
			// 由原編搜尋
			dataMap = worktimeService.getWorktimeByEmpnoInfo(keyword, yearMonth, page);
			mgrgetempworktimeList = (List<Worktime>) dataMap.get("List<Worktime>");
		}

		// 設定假日
		for (int i = 0; i < mgrgetempworktimeList.size(); i++) {
			holidays = holidayService.getHolidaysByFirstday(mgrgetempworktimeList.get(i).getWeekFirstDay());
			mgrgetempworktimeList.get(i).setHolidays(holidays);
		}

		// 設定分頁
		page = (Page) dataMap.get("page");
		page.setAction(request);

		model.addAttribute("yearMonth", yearMonth);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("mgrgetempworktimeList", mgrgetempworktimeList);
		return MGR_SEARCH_WORKTIME_PAGE;
	}

	protected String doSaveDetailWorktime(String[] workNames, String sunNormal, String sunOver, String monNormal,
			String monOver, String tueNormal, String tueOver, String wedNormal, String wedOver, String thuNormal,
			String thuOver, String friOver, String friNormal, String satOver, String satNormal, String[] workContents,
			HttpServletRequest request, String doSomething, String firstday, Model model, HttpSession session) {

		Map<String, String> loginInfo = (Map<String, String>) session.getAttribute("login");
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Date firstDate = null;
		String empno = loginInfo.get("empno");
		String first = request.getParameter("firstDate");
		String workTimeDetailId = empno + (first.replaceAll("-", ""));
		System.out.print("員編:" + empno + " 詳細id: " + workTimeDetailId);
		Worktime worktime = new Worktime();
		worktime.setWorktimeDetailId(workTimeDetailId);
		worktime.setEmpNo(empno);
		List<WorktimeDetail> worktimedetailList = new ArrayList<WorktimeDetail>();

		for (int i = 0; i < workNames.length; i++) {
			WorktimeDetail detail = new WorktimeDetail();
			detail.setDetailId(workTimeDetailId);
			detail.setWorkName(workNames[i]);
			detail.setWorkContent(workContents[i]);
			if (sunNormal != null) {
				detail.setSunNormal(Integer.parseInt(sunNormal));
				detail.setSunOvertime(Integer.parseInt(sunOver));
			} else {
				detail.setSunNormal(0);
				detail.setSunOvertime(0);
			}

			if (monNormal != null) {
				detail.setMonNormal(Integer.parseInt(monNormal));
				detail.setMonOvertime(Integer.parseInt(monOver));
			} else {
				detail.setMonNormal(0);
				detail.setMonOvertime(0);
			}

			if (tueNormal != null) {
				detail.setTueNormal(Integer.parseInt(tueNormal));
				detail.setTueOvertime(Integer.parseInt(tueOver));
			} else {
				detail.setTueNormal(0);
				detail.setTueOvertime(0);
			}

			if (wedNormal != null) {
				detail.setWedNormal(Integer.parseInt(wedNormal));
				detail.setWedOvertime(Integer.parseInt(wedOver));
			} else {
				detail.setWedNormal(0);
				detail.setWedOvertime(0);
			}

			if (thuNormal != null) {
				detail.setThuNormal(Integer.parseInt(thuNormal));
				detail.setThuOvertime(Integer.parseInt(thuOver));
			} else {
				detail.setThuNormal(0);
				detail.setThuOvertime(0);
			}

			if (friNormal != null) {
				detail.setFriNormal(Integer.parseInt(friNormal));
				detail.setFriOvertime(Integer.parseInt(friOver));
			} else {
				detail.setFriNormal(0);
				detail.setFriOvertime(0);
			}

			if (satNormal != null) {
				detail.setSatNormal(Integer.parseInt(satNormal));
				detail.setSatOvertime(Integer.parseInt(satOver));
			} else {
				detail.setSatNormal(0);
				detail.setSatOvertime(0);
			}

			worktimedetailList.add(detail);
		}
		System.out.print(" 詳細工時編號:" + workTimeDetailId + " 進行工時資料的  " + doSomething);
		System.out.println(" 共" + worktimedetailList.size() + " 筆專案");
		worktime.setWorktimeDetailList(worktimedetailList);

		worktime = worktimeService.calculateWorktime_time(worktime);
		try {
			firstDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(first).getTime());
		} catch (ParseException e) {
			// TODO 自動產生的 catch 區塊
			System.err.println("日期轉換錯誤");
			e.printStackTrace();
		}
		worktime.setWeekFirstDay(firstDate);
		worktime.setStatus(worktimeService.getWorktimeStatus(workTimeDetailId));
		holidays = holidayService.getHolidaysByFirstday(firstDate);
		if ("已繳交".equals(worktime.getStatus()) || "已通過".equals(worktime.getStatus())) {
			model.addAttribute("result", "不合法的操作!");
			return WRITEWORKTIME_SUB_PAGE;
		}
		if (ValidateUtils.isLegalWorktime(worktime, holidays)) {
			if ("提交".equals(doSomething)) {
				worktimeService.submitWorktimeDetail(worktime);
			} else if ("暫存".equals(doSomething)) {
				worktimeService.saveWorktimeDetail(worktime);
			} else {
				model.addAttribute("result", "不合法的操作!");
				return WRITEWORKTIME_SUB_PAGE;
			}
			model.addAttribute("action", doSomething);
			model.addAttribute("result", "success");
		} else {
			model.addAttribute("result", "時數不合法，請重填謝謝。");
		}

		// worktime=worktimeService
		worktime.setWorktimeDetailList(worktimedetailList);
		worktime.setHolidays(holidays);
		model.addAttribute("worktime", worktime);

		return WRITEWORKTIME_SUB_PAGE;
	}

	protected void doExporWorktime(String yearMonth, String keyword, HttpServletRequest request,
			HttpServletResponse response) {

		// String yearMonth = "";
		// String keyword = "";
		// if (request.getParameter("yearMonth") != null)
		// yearMonth = request.getParameter("yearMonth");
		// if (request.getParameter("keyword") != null)
		// keyword = request.getParameter("keyword");

//		response.setCharacterEncoding("utf-8");
//		response.setContentType("application/ms-excel");

		String fileName = (yearMonth.equals("") ? "至今" : yearMonth) + "工時報表";
		try {
			fileName = java.net.URLEncoder.encode(fileName, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO 自動產生的 catch 區塊
			System.err.println("編碼錯誤:" + e1.getMessage());
		}

		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");

		try {
			new excelService().getExcel(request, yearMonth, keyword, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
		new excelService().sendExcel(request, yearMonth, keyword, "josjos910052@gmail.com", fileName);
		return;

	}

	protected void doExporWorktimeByEmail(HttpServletResponse response, HttpSession session, String yearMonth,
			String keyword, HttpServletRequest request) {
		// String yearMonth = "";
		// String keyword = "";
		String email;
		Map<String, String> loginInfo = (Map<String, String>) session.getAttribute("login");
		email = new EmployeeService().getEmpEmail(loginInfo.get("empno"));

		// if (request.getParameter("yearMonth") != null)
		// yearMonth = request.getParameter("yearMonth");
		// if (request.getParameter("keyword") != null)
		// keyword = request.getParameter("keyword");

		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		String fileName = (yearMonth.equals("") ? "至今" : yearMonth) + "工時報表";

		try {
			new excelService().sendExcel(request, yearMonth, keyword, email, fileName);
			response.flushBuffer();
		} catch (IOException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}

	}

	/************************************** 以上陳民錞 ************************************/

	/************************************** 以下彥儒 ************************************/
	protected String doCheckWorktime(String pageNum, Model model, HttpServletRequest request) {
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		List<Worktime> worktime = new ArrayList<Worktime>();
		Page page = new Page();
		if (pageNum != null) {
			page.setNowPage(Integer.parseInt(pageNum));
		}

		Map<String, Object> dataMap = worktimeService.findWorktime(page);// List<Worktime>
		worktime = (List<Worktime>) dataMap.get("List<Worktime>");

		for (int i = 0; i < worktime.size(); i++) {
			holidays = holidayService.getHolidaysByFirstday(worktime.get(i).getWeekFirstDay());
			worktime.get(i).setHolidays(holidays);
		}
		page = (Page) dataMap.get("page");
		page.setAction(request);

		model.addAttribute("page", page);
		model.addAttribute("empWeekWorktime", worktime);
		return CHECKWORKTIME_PAGE;
	}

	protected void doSearchWorktime(HttpServletRequest request, Model model, String pageNum, String searchBy,
			String keyword, String dateAndWeek) {
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Map<String, Object> dataMap = null;
		Page page = new Page();
		if (pageNum != null) {
			page.setNowPage(Integer.parseInt(pageNum));
		}

		System.out.println("週數:'" + dateAndWeek + "' 搜尋:'" + keyword + "' by " + searchBy);

		List<Worktime> worktimeSearch = null;
		if (!dateAndWeek.isEmpty() || !keyword.isEmpty()) {
			if (searchBy.equals("empno")) {
				dataMap = worktimeService.findByEmpno(dateAndWeek, keyword, page);
				worktimeSearch = (List<Worktime>) dataMap.get("List<Worktime>");
			} else {
				dataMap = worktimeService.findByName(dateAndWeek, keyword, page);
				worktimeSearch = (List<Worktime>) dataMap.get("List<Worktime>");
			}
		} else {
			dataMap = worktimeService.findWorktime(page);// List<Worktime>
			worktimeSearch = (List<Worktime>) dataMap.get("List<Worktime>");
		}
		// 設定假日
		for (int i = 0; i < worktimeSearch.size(); i++) {
			holidays = holidayService.getHolidaysByFirstday(worktimeSearch.get(i).getWeekFirstDay());
			worktimeSearch.get(i).setHolidays(holidays);
		}
		// 設定分頁
		page = (Page) dataMap.get("page");
		page.setAction(request);

		model.addAttribute("page", page);
		model.addAttribute("empWeekWorktime", worktimeSearch);

	}

	protected String doCheckbox(HttpServletRequest request, String pageAction, Model model) {
		String[] detailIds = request.getParameterValues("detailId");

		for (int i = 0; i < detailIds.length; i++) {
			System.out.println(detailIds[i]);
			worktimeService.checkWorktime(detailIds[i]);
		}

		String uri = "./Worktime.do?action=checkWorktime_page";
		if (pageAction != null || (!pageAction.equals(""))) {
			uri = pageAction.substring(pageAction.indexOf("/Worktime.do?"));
		}

		model.addAttribute("result", "success");
		return uri;
	}

	/************************************** 以上彥儒 ************************************/

	/************************************** 以下吳軒穎 ************************************/
	protected String enterEmpWorktime(Model model) {
		// TODO Auto-generated method stub
		Date date = new Date(new java.util.Date().getTime());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		String thisMonth = df.format(date);

		model.addAttribute("thisMonth", thisMonth);
		return SEARCH_WORKTIME_EMP_PAGE;
	}

	protected String doSearchEmpWorktime(Model model, HttpSession session, String searchDate) {
		model.addAttribute("notfirst", true);
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Map<String, String> loginInfo = (Map<String, String>) session.getAttribute("login");
		Map<String, Object> dataMap = null;

		String name = loginInfo.get("name");
		String empno = loginInfo.get("empno");

		// searchWorktimeEmp
		List<Worktime> worktimeList = null;
		try {
			dataMap = worktimeService.getWorktimeByEmpnoInfo(empno, searchDate, new Page());
			worktimeList = (List<Worktime>) dataMap.get("List<Worktime>");
		} catch (ParseException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}

		// 查詢假日
		for (int i = 0; i < worktimeList.size(); i++) {
			holidays = holidayService.getHolidaysByFirstday(worktimeList.get(i).getWeekFirstDay());
			worktimeList.get(i).setHolidays(holidays);
		}

		model.addAttribute("thisMonth", searchDate);
		model.addAttribute("worktimeList", worktimeList);
		model.addAttribute("name", name);
		model.addAttribute("empno", empno);

		return SEARCH_WORKTIME_EMP_PAGE;
	}

	/************************************** 以上吳軒穎 ************************************/

}
