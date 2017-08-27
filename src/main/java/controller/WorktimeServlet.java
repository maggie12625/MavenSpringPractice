package controller;

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
import service.ExcelService;
import utils.ValidateUtils;

/**
 * Servlet implementation class WorktimeServlet
 */
public class WorktimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/************************************** 以下陳民錞 ************************************/
	private static final String WRITEWORKTIME_PAGE = "/WEB-INF/views/worktime/writeWorktime.jsp";
	private static final String WRITEWORKTIME_SUB_PAGE = "/WEB-INF/views/worktime/writeWorktimeSub.jsp";
	private static final String MGR_SEARCH_WORKTIME_PAGE = "/WEB-INF/views/manager/mgrSearchWorktime.jsp";
	/************************************** 以上陳民錞 ************************************/

	/************************************** 以下張芷瑄 ************************************/
	private static final String CAllWORKTIME_PAGE = "/WEB-INF/views/manager/callWorktime.jsp";

	/************************************** 以上張芷瑄 ************************************/

	/************************************** 以下彥儒 ************************************/
	private static final String CHECKWORKTIME_PAGE = "/WEB-INF/views/manager/checkWorktime.jsp";

	/************************************** 以上彥儒 ************************************/

	/************************************** 以下吳軒穎 ************************************/
	private static final String SEARCH_WORKTIME_EMP_PAGE = "/WEB-INF/views/employee/searchEmpWorktime.jsp";
	/************************************** 以上吳軒穎 ************************************/

	private WorktimeService worktimeService = new WorktimeService();
	private WorktimeDetialService worktimeDetialService = new WorktimeDetialService();
	private HolidayService holidayService = new HolidayService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		String page = null;

		if (request.getSession().getAttribute("login") == null) {
			request.getRequestDispatcher("./Logout.do").forward(request, response);
			return;
		}
		System.out.println("worktiom_action: " + action);
		switch (action) {

		/************************************** 以下陳民錞 ************************************/
		// 轉交至填寫工時頁面
		case "writeWorktime_page":
			page = doGetEmpWorktime(request);
			break;
		// 轉交至填寫工時子頁面
		case "writeWorktime_sub_page":
			page = doGetEmpWorktimeDetail(request);
			break;
		// 轉交至主管查詢員工工時頁面
		case "mgrSearchWorktime_page":
			try {
				page = doMgrGetEmpWorktime(request);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "saveDetailWorktime":
			page = doSaveDetailWorktime(request);
			break;

		case "exportWorktimeExcel":
			doExporWorktime(request, response);
			return;
		case "exportWorktimeExcelByEmail":
			doExporWorktimeByEmail(request, response);
			return;

		/************************************** 以上陳民錞 ************************************/

		/************************************** 以下張芷瑄 ************************************/
		// 轉交至取得狀態名單
		case "callWorktime_page":
			// 轉交至取得未繳交名單
			page = getUnsubmitEmp(request);
			break;

		case "updateStatus":
			page = checkBoxEvent(request);
			break;
		// 全部催繳
		case "select_all":
			page = selectAllEmp(request);
			break;
		/************************************** 以上張芷瑄 ************************************/

		/************************************** 以下彥儒 ************************************/
		case "checkWorktime_page":
			page = doCheckWorktime(request);
			break;
		case "checkBox_page":

			page = doCheckbox(request);
			break;
		case "searchWorktime_page":
			doSearchWorktime(request);

			page = CHECKWORKTIME_PAGE;
			break;

		/************************************** 以上彥儒 ************************************/
		/************************************** 以下吳軒穎 ************************************/
		// 進入工時查詢(員工)，取得當月當年
		case "enterEmpWorktime":
			System.out.println("enterEmpWorktime");
			page = enterEmpWorktime(request);
			break;
		case "searchEmpWorktime":
			page = doSearchEmpWorktime(request);
			break;

		/************************************** 以上吳軒穎 ************************************/

		}

		request.getRequestDispatcher(page).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/************************************** 以下張芷瑄 ************************************/
	private String getUnsubmitEmp(HttpServletRequest request) {
		Map<String, Object> dataMap = null;
		List<Map<String, String>> UnsubmitEmpList = null;
		Page page = new Page();
		if (request.getParameter("page") != null) {
			page.setNowPage(Integer.parseInt(request.getParameter("page")));
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

	private String checkBoxEvent(HttpServletRequest request) {
		Page page = new Page();
		if (request.getParameter("page") != null) {
			page.setNowPage(Integer.parseInt(request.getParameter("page")));
		}
		String[] datas = request.getParameterValues("emps");
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

		request.setAttribute("page", page);
		request.setAttribute("result", true);
		return "/Worktime.do?action=callWorktime_page";

	}

	private String selectAllEmp(HttpServletRequest request) {

		Map<String, Object> dataMap = null;
		List<Map<String, String>> allMapEmpList = null;
		Page page = new Page();
		if (request.getParameter("page") != null) {
			page.setNowPage(Integer.parseInt(request.getParameter("page")));
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
		System.out.println("全部"+allMapEmpList.size()+"筆要催繳");

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

		request.setAttribute("page", page);
		request.setAttribute("result", true);
		return "/Worktime.do?action=callWorktime_page";

	}

	/************************************** 以上張芷瑄 ************************************/

	/************************************** 以下陳民錞 ************************************/
	// 取得填寫工時資料
	private String doGetEmpWorktime(HttpServletRequest request) {
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Map<String, String> loginInfo = (Map<String, String>) request.getSession().getAttribute("login");
		String empno = loginInfo.get("empno");

		List<Worktime> worktimeList = worktimeService.getWorktimeInfo(empno);

		for (int i = 0; i < worktimeList.size(); i++) {
			holidays = holidayService.getHolidaysByFirstday(worktimeList.get(i).getWeekFirstDay());
			worktimeList.get(i).setHolidays(holidays);
		}

		request.setAttribute("worktimeList", worktimeList);
		return WRITEWORKTIME_PAGE;
	}

	// 取得填寫詳細工時
	private String doGetEmpWorktimeDetail(HttpServletRequest request) {
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Map<String, String> loginInfo = (Map<String, String>) request.getSession().getAttribute("login");
		String empno = loginInfo.get("empno");
		String firstday = request.getParameter("firstday");
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

		request.setAttribute("worktime", worktime);
		return WRITEWORKTIME_SUB_PAGE;
	}

	private String doMgrGetEmpWorktime(HttpServletRequest request) throws ParseException {
		request.setAttribute("hadVisited", true);

		List<Worktime> mgrgetempworktimeList = null;
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Map<String, Object> dataMap = null;
		Page page = new Page();
		if (request.getParameter("page") != null) {
			page.setNowPage(Integer.parseInt(request.getParameter("page")));// 讀取分頁
		}

		String keyword = request.getParameter("keyword");
		String yearMonth = request.getParameter("yearMonth");
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

		request.setAttribute("yearMonth", yearMonth);
		request.setAttribute("keyword", keyword);
		request.setAttribute("page", page);
		request.setAttribute("mgrgetempworktimeList", mgrgetempworktimeList);
		return MGR_SEARCH_WORKTIME_PAGE;
	}

	private String doSaveDetailWorktime(HttpServletRequest request) {
		Map<String, String> loginInfo = (Map<String, String>) request.getSession().getAttribute("login");
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Date firstDate = null;
		String empno = loginInfo.get("empno");
		String doSomething = request.getParameter("do");
		String firstday = request.getParameter("firstDate");
		String workTimeDetailId = empno + (firstday.replaceAll("-", ""));

		System.out.print("員編:" + empno + " 詳細id: " + workTimeDetailId);
		String[] workNames = request.getParameterValues("workNames");

		Worktime worktime = new Worktime();
		worktime.setWorktimeDetailId(workTimeDetailId);
		worktime.setEmpNo(empno);
		List<WorktimeDetail> worktimedetailList = new ArrayList<WorktimeDetail>();

		for (int i = 0; i < workNames.length; i++) {
			WorktimeDetail detail = new WorktimeDetail();
			detail.setDetailId(workTimeDetailId);
			detail.setWorkName(workNames[i]);
			detail.setWorkContent(request.getParameterValues("workContents")[i]);
			if (request.getParameterValues("sunNormal") != null) {
				detail.setSunNormal(Integer.parseInt(request.getParameterValues("sunNormal")[i]));
				detail.setSunOvertime(Integer.parseInt(request.getParameterValues("sunOver")[i]));
			} else {
				detail.setSunNormal(0);
				detail.setSunOvertime(0);
			}

			if (request.getParameterValues("monNormal") != null) {
				detail.setMonNormal(Integer.parseInt(request.getParameterValues("monNormal")[i]));
				detail.setMonOvertime(Integer.parseInt(request.getParameterValues("monOver")[i]));
			} else {
				detail.setMonNormal(0);
				detail.setMonOvertime(0);
			}

			if (request.getParameterValues("tueNormal") != null) {
				detail.setTueNormal(Integer.parseInt(request.getParameterValues("tueNormal")[i]));
				detail.setTueOvertime(Integer.parseInt(request.getParameterValues("tueOver")[i]));
			} else {
				detail.setTueNormal(0);
				detail.setTueOvertime(0);
			}

			if (request.getParameterValues("wedNormal") != null) {
				detail.setWedNormal(Integer.parseInt(request.getParameterValues("wedNormal")[i]));
				detail.setWedOvertime(Integer.parseInt(request.getParameterValues("wedOver")[i]));
			} else {
				detail.setWedNormal(0);
				detail.setWedOvertime(0);
			}

			if (request.getParameterValues("thuNormal") != null) {
				detail.setThuNormal(Integer.parseInt(request.getParameterValues("thuNormal")[i]));
				detail.setThuOvertime(Integer.parseInt(request.getParameterValues("thuOver")[i]));
			} else {
				detail.setThuNormal(0);
				detail.setThuOvertime(0);
			}

			if (request.getParameterValues("friNormal") != null) {
				detail.setFriNormal(Integer.parseInt(request.getParameterValues("friNormal")[i]));
				detail.setFriOvertime(Integer.parseInt(request.getParameterValues("friOver")[i]));
			} else {
				detail.setFriNormal(0);
				detail.setFriOvertime(0);
			}

			if (request.getParameterValues("satNormal") != null) {
				detail.setSatNormal(Integer.parseInt(request.getParameterValues("satNormal")[i]));
				detail.setSatOvertime(Integer.parseInt(request.getParameterValues("satOver")[i]));
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
			firstDate = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(firstday).getTime());
		} catch (ParseException e) {
			// TODO 自動產生的 catch 區塊
			System.err.println("日期轉換錯誤");
			e.printStackTrace();
		}
		worktime.setWeekFirstDay(firstDate);
		worktime.setStatus(worktimeService.getWorktimeStatus(workTimeDetailId));
		holidays = holidayService.getHolidaysByFirstday(firstDate);
		if ("已繳交".equals(worktime.getStatus()) || "已通過".equals(worktime.getStatus())) {
			request.setAttribute("result", "不合法的操作!");
			return WRITEWORKTIME_SUB_PAGE;
		}
		if (ValidateUtils.isLegalWorktime(worktime, holidays)) {
			if ("提交".equals(doSomething)) {
				worktimeService.submitWorktimeDetail(worktime);
			} else if ("暫存".equals(doSomething)) {
				worktimeService.saveWorktimeDetail(worktime);
			} else {
				request.setAttribute("result", "不合法的操作!");
				return WRITEWORKTIME_SUB_PAGE;
			}
			request.setAttribute("action", doSomething);
			request.setAttribute("result", "success");
		} else {
			request.setAttribute("result", "時數不合法，請重填謝謝。");
		}

		// worktime=worktimeService
		worktime.setWorktimeDetailList(worktimedetailList);
		worktime.setHolidays(holidays);
		request.setAttribute("worktime", worktime);

		return WRITEWORKTIME_SUB_PAGE;
	}

	private void doExporWorktime(HttpServletRequest request, HttpServletResponse response) {
		String yearMonth = "";
		String keyword = "";
		if (request.getParameter("yearMonth") != null)
			yearMonth = request.getParameter("yearMonth");
		if (request.getParameter("keyword") != null)
			keyword = request.getParameter("keyword");

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/ms-excel");

		String fileName = (yearMonth.equals("") ? "至今" : yearMonth) + "工時報表";
		try {
			fileName = java.net.URLEncoder.encode(fileName, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO 自動產生的 catch 區塊
			System.err.println("編碼錯誤:" + e1.getMessage());
		}

		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
		try {
			new ExcelService().getExcel(request, yearMonth, keyword, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
		// new excelService().sendExcel(request, yearMonth,
		// keyword,"josjos910052@gmail.com");

	}

	private void doExporWorktimeByEmail(HttpServletRequest request, HttpServletResponse response) {
		String yearMonth = "";
		String keyword = "";
		String email;
		Map<String, String> loginInfo = (Map<String, String>) request.getSession().getAttribute("login");
		email = new EmployeeService().getEmpEmail(loginInfo.get("empno"));

		if (request.getParameter("yearMonth") != null)
			yearMonth = request.getParameter("yearMonth");
		if (request.getParameter("keyword") != null)
			keyword = request.getParameter("keyword");

		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		String fileName = (yearMonth.equals("") ? "至今" : yearMonth) + "工時報表";

		try {
			new ExcelService().sendExcel(request, yearMonth, keyword, email, fileName);
			response.flushBuffer();
		} catch (IOException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}

	}

	/************************************** 以上陳民錞 ************************************/

	/************************************** 以下彥儒 ************************************/
	private String doCheckWorktime(HttpServletRequest request) {
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		List<Worktime> worktime = new ArrayList<Worktime>();
		Page page = new Page();
		if (request.getParameter("page") != null) {
			page.setNowPage(Integer.parseInt(request.getParameter("page")));
		}

		Map<String, Object> dataMap = worktimeService.findWorktime(page);// List<Worktime>
		worktime = (List<Worktime>) dataMap.get("List<Worktime>");

		for (int i = 0; i < worktime.size(); i++) {
			holidays = holidayService.getHolidaysByFirstday(worktime.get(i).getWeekFirstDay());
			worktime.get(i).setHolidays(holidays);
		}
		page = (Page) dataMap.get("page");
		page.setAction(request);

		request.setAttribute("page", page);
		request.setAttribute("empWeekWorktime", worktime);
		return CHECKWORKTIME_PAGE;
	}

	private void doSearchWorktime(HttpServletRequest request) {
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Map<String, Object> dataMap = null;
		Page page = new Page();
		if (request.getParameter("page") != null) {
			page.setNowPage(Integer.parseInt(request.getParameter("page")));
		}
		String searchBy = request.getParameter("by");
		String keyword = request.getParameter("keyword");
		String dateAndWeek = request.getParameter("week");

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

		request.setAttribute("page", page);
		request.setAttribute("empWeekWorktime", worktimeSearch);

	}

	private String doCheckbox(HttpServletRequest request) {
		String[] detailIds = request.getParameterValues("detailId");

		for (int i = 0; i < detailIds.length; i++) {
			System.out.println(detailIds[i]);
			worktimeService.checkWorktime(detailIds[i]);
		}
		
		

		String uri = "./Worktime.do?action=checkWorktime_page";
		String pageAction = request.getParameter("pageAction");
		if (pageAction != null || (!pageAction.equals(""))) {
			uri = pageAction.substring(pageAction.indexOf("/Worktime.do?"));
		}
		
		request.setAttribute("result", "success");
		return uri;
	}

	/************************************** 以上彥儒 ************************************/

	/************************************** 以下吳軒穎 ************************************/
	private String enterEmpWorktime(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Date date = new Date(new java.util.Date().getTime());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		String thisMonth = df.format(date);

		request.setAttribute("thisMonth", thisMonth);
		return SEARCH_WORKTIME_EMP_PAGE;
	}

	private String doSearchEmpWorktime(HttpServletRequest request) {
		request.setAttribute("notfirst", true);
		Map<String, Holiday> holidays = new HashMap<String, Holiday>();
		Map<String, String> loginInfo = (Map<String, String>) request.getSession().getAttribute("login");
		Map<String, Object> dataMap = null;

		String name = loginInfo.get("name");
		String empno = loginInfo.get("empno");
		String searchDate = request.getParameter("date");

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

		request.setAttribute("thisMonth", searchDate);
		request.setAttribute("worktimeList", worktimeList);
		request.setAttribute("name", name);
		request.setAttribute("empno", empno);

		return SEARCH_WORKTIME_EMP_PAGE;
	}

	/************************************** 以上吳軒穎 ************************************/

}
