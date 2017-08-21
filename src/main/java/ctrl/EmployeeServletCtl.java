package ctrl;

import static utils.ValidateUtils.isBlank;
import static utils.ValidateUtils.isLegalPassWord;
import static utils.ValidateUtils.isValidEmail;
import static utils.ValidateUtils.isValidPID;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import model.Employee;
import model.Page;
import service.EmailService;
import service.EmployeeService;
import service.StartEmpService;

@Controller
public class EmployeeServletCtl {
	private static final String INFO_PAGE = "/WEB-INF/views/employee/info";
	private static final String CHANGE_PASSWORD_PAGE = "/WEB-INF/views/employee/changePwd";

	/************************************* 以下彥儒 ***************************************/
	private static final String MGR_SEARCH_PAGE = "/WEB-INF/views/manager/mgrSearch";
	private static final String MGR_SEARCH_EMP_PAGE = "/WEB-INF/views/manager/mgrSearchEmpInfo";
	private static final String MGR_SEARCH_WORKTIME_PAGE = "/WEB-INF/views/manager/mgrSearchWorktime";
	private static final String MGR_MANAGE_WORKTIME_PAGE = "/WEB-INF/views/manager/mgrManageWorktime";
	private static final String MGR_CALL_WORKTIME_PAGE = "/WEB-INF/views/manager/callWorktime";
	/************************************* 以上彥儒 ***************************************/

	/********************************** 以下吳軒穎 *****************************************/

	private static final String EMP_MANAGE_PAGE = "/WEB-INF/views/admin/empManage";
	private static final String ADD_EMP_PAGE = "/WEB-INF/views/admin/addEmp";
	private static final String ADD_EMP_PAGE1 = "/WEB-INF/views/admin/addEmp1";

	/********************************** 以上吳軒穎 *****************************************/

	/********************************** 以下張芷瑄 *****************************************/
	private static final String UPDATE_EMP_PAGE = "/WEB-INF/views/admin/modifyEmpData";
	private static final String UPDATE_EMP_PAGE1 = "/WEB-INF/views/admin/modifyEmpData1";
	private static final String UPDATE_EMP_PAGE2 = "/WEB-INF/views/admin/modifyEmpData2";

	/********************************** 以上張芷瑄 *****************************************/

	private EmployeeService employeeService = new EmployeeService();

	@RequestMapping(value = "/Employee.do")
	public String simple(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam("action") String action) {
		// request.setCharacterEncoding("utf-8");
		// String action =request.getParameter("action");
		String doSomething = null;

		if (session.getAttribute("login") == null) {
			return "forward:/Logout.do";
		}
		System.out.println("emp_action: " + action);
		switch (action) {
		case "info":
			// 轉交至個人資料畫面
			// page = doFindPersonalInfo(request);
			// break;
			doSomething = "forward:/Employee.doFindPersonalInfo";
			break;
		// 轉交至變更密碼頁面
		case "changPwd_page":
			// page = CHANGE_PASSWORD_PAGE;
			// break;
			doSomething = CHANGE_PASSWORD_PAGE;
			break;
		// 進行變更密碼
		case "changPwd":
			// page = doChangePwd(request);
			// break;
			doSomething = "forward:/Employee.doChangePwd";
			break;
		/************************************ 底下彥儒 **********************************/
		// 轉交至主管查詢頁面
		case "mgrSearch_page":
			// page = MGR_SEARCH_PAGE;
			// break;
			doSomething = MGR_SEARCH_PAGE;
			break;
		// 轉交至主管查詢員工資料頁面 ，剛進去
		case "mgrSearchEmp_page":
			// 這邊要改 page = MGR_SEARCH_EMP_PAGE;
			// page = doFindEmpInfo(request);
			// break;
			doSomething = "forward:/Employee.doFindEmpInfo";
			break;
		// 轉交至主管查詢工時頁面
		case "mgrSearchWorktime_page":
			// page = MGR_SEARCH_WORKTIME_PAGE;
			// break;
			doSomething = MGR_SEARCH_WORKTIME_PAGE;
			break;
		// 轉交至(主要)工時審核頁面
		case "mgrManageWorktime_page":
			// page = MGR_MANAGE_WORKTIME_PAGE;
			// break;
			doSomething = MGR_MANAGE_WORKTIME_PAGE;
			break;
		// 轉交至工時催繳頁面
		case "callWorktime_page":
			// page = MGR_CALL_WORKTIME_PAGE;
			// break;
			doSomething = MGR_CALL_WORKTIME_PAGE;
			break;
		// 搜尋員工
		case "search_employee":
			// doSearchEmployee(request);
			// page = MGR_SEARCH_EMP_PAGE;
			// break;
			doSomething = "forward:/Employee.doSearchEmployee";
			break;
		/********************** 以上彥儒 **********************************/

		/********************************** 以下吳軒穎 *****************************************/
		// 轉交至員工管理頁面
		case "empManage_page":
			// page = EMP_MANAGE_PAGE;
			// break;
			doSomething = EMP_MANAGE_PAGE;
			break;
		case "addEmp":
			// page = doAddEmp(request);
			// break;
			doSomething = "forward:/Employee.doAddEmp";
			break;
		case "validateInsertEmp":
			// page = doValidateInsertEmp(request);
			// break;
			doSomething = "forward:/Employee.doValidateInsertEmp";
			break;
		case "insertEmp":
			// page = doInsertEmp(request);
			// break;
			doSomething = "forward:/Employee.doInsertEmp";
			break;
		/********************************** 以上吳軒穎 *****************************************/

		/********************************** 以下張芷瑄 *****************************************/
		// 轉到修改員工頁面
		case "updateEmp_page":
			// page = UPDATE_EMP_PAGE;
			// break;
			doSomething = UPDATE_EMP_PAGE;
			break;
		// 取得被修改的人
		case "FindUpdateEmp":
			// page = doFindUpdateEmp(request);
			// break;
			doSomething = "forward:/Employee.doFindUpdateEmp";
			break;
		// 動作：取得確定修改的人的資訊
		// 目的：修改
		case "updateEmp_page1":
			// page = doFindModifyInfo(request);
			// break;
			doSomething = "forward:/Employee.doFindModifyInfo";
			break;
		// 驗證修改資料
		case "validateModifyEmp":
			// page = doValidateModifyEmp(request);
			// break;
			doSomething = "forward:/Employee.doValidateModifyEmp";
			break;
		// 取消
		case "cancel":
			// page = UPDATE_EMP_PAGE1;
			// break;
			doSomething = UPDATE_EMP_PAGE1;
			break;
		// 更新修改資料
		case "doUpdateEmpInfo":
			// page = doUpdateEmpInfo(request);
			// break;
			doSomething = "forward:/Employee.doUpdateEmpInfo";
			break;
		/********************************** 以上張芷瑄 *****************************************/
		default:
			System.out.println("error");
			doSomething = "./main";
		}

		return doSomething;
	}

	@RequestMapping("Employee.doFindPersonalInfo")
	private String doFindPersonalInfo(Model model, HttpSession session) {
		Map<String, String> loginInfo = (Map<String, String>) session.getAttribute("login");
		String empNo = loginInfo.get("empno");
		Employee empVO = employeeService.getPersonalInfo(empNo);
		model.addAttribute("employee", empVO);
		return INFO_PAGE;
	}

	@RequestMapping(value = "Employee.doChangePwd", method = RequestMethod.POST)
	private String doChangePwd(HttpSession session, Model model, @RequestParam("old_pw") String old_password,
			@RequestParam("user-pwd-input") String new_password,
			@RequestParam("user_pwd_again") String new_again_password) {
		// request.removeAttribute("user-pwd-input");
		Map<String, String> errorMsgs = new HashMap<>();
		Map<String, String> loginInfo = (Map<String, String>) session.getAttribute("login");
		String empNo = loginInfo.get("empno");
		Employee empVO = employeeService.getPersonalInfo(empNo);

		// String old_password = request.getParameter("old_pw");
		// String new_password = request.getParameter("user-pwd-input");
		// String new_again_password = request.getParameter("user_pwd_again");
		System.out.println("舊密碼: " + old_password + " 新密碼: " + new_password + " 第2次新密碼: " + new_again_password);
		empVO.setPassword(old_password);

		if (isBlank(old_password)) {
			errorMsgs.put("old_pw", "請輸入舊密碼");
		}
		if (isBlank(new_password)) {
			errorMsgs.put("new_password", "請輸入新密碼");
		}
		if (isBlank(new_again_password)) {
			errorMsgs.put("new_again_password", "請再次輸入新密碼");
		}
		if (!errorMsgs.isEmpty()) {
			model.addAttribute("errorMsgs", errorMsgs);
			return CHANGE_PASSWORD_PAGE;
		}
		if (!employeeService.checkAccount(empVO)) {
			System.out.println();
			errorMsgs.put("old_pw", "舊密碼錯誤");
			model.addAttribute("errorMsgs", errorMsgs);
		} else {
			if (!isLegalPassWord(new_password)) {
				errorMsgs.put("new_password", "格式錯誤。請輸入長度6~16位的英文或數字");
			}
			if (!isLegalPassWord(new_again_password)) {
				errorMsgs.put("new_again_password", "格式錯誤。請輸入長度6~16位的英文或數字");
			}
			if (!new_password.equals(new_again_password)) {
				errorMsgs.put("new_again_password", "與新密碼不相符。");
			}

			if (!errorMsgs.isEmpty()) {
				model.addAttribute("errorMsgs", errorMsgs);
				return CHANGE_PASSWORD_PAGE;
			} else {

				empVO.setPassword(new_password);
				model.addAttribute("result", true);
				employeeService.update(empVO);
			}

		}

		return CHANGE_PASSWORD_PAGE;
	}

	/********************************** 以下彥儒 ************************************/
	@RequestMapping("Employee.doFindEmpInfo")
	private String doFindEmpInfo(@RequestParam(value = "page", required = false) String pageNum,
			HttpServletRequest request, Model model,HttpSession session) {
		if (session.getAttribute("login") == null) {
			return "forward:/Logout.do";
		}
		Page page = new Page();
		if (pageNum != null) {
			page.setNowPage(Integer.parseInt(pageNum));
		}

		Map<String, Object> dataMap = employeeService.findEmpInfo(page);// List<Employee>
		List<Employee> empInfo = (List<Employee>) dataMap.get("List<Employee>");

		page = (Page) dataMap.get("page");
		page.setAction(request);

		model.addAttribute("page", page);
		model.addAttribute("empInfoList", empInfo);

		return MGR_SEARCH_EMP_PAGE;
	}

	@RequestMapping(value = "Employee.doSearchEmployee")
	private String doSearchEmployee(Model model, HttpServletRequest request,
			@RequestParam(value = "page", required = false) String pageNum, @RequestParam("by") String searchBy,
			@RequestParam("keyword") String keyword) {

		Page page = new Page();
		if (pageNum != null) {
			page.setNowPage(Integer.parseInt(pageNum));
		}

		// String searchBy = request.getParameter("by");
		// String keyword = request.getParameter("keyword");
		Map<String, Object> dataMap;// List<Employee>
		List<Employee> empSearch = null;

		if (searchBy.equals("empno")) {
			dataMap = employeeService.findByEmpno(keyword, page);
			empSearch = (List<Employee>) dataMap.get("List<Employee>");
		} else {
			dataMap = employeeService.findByName(keyword, page);
			empSearch = (List<Employee>) dataMap.get("List<Employee>");
		}

		page = (Page) dataMap.get("page");
		page.setAction(request);

		model.addAttribute("page", page);
		model.addAttribute("empInfoList", empSearch);

		return MGR_SEARCH_EMP_PAGE;
		

	}

	/********************************** 以上彥儒 ************************************/

	/********************************** 以下吳軒穎 *****************************************/
	@RequestMapping("Employee.doAddEmp")
	private String doAddEmp(HttpSession session) {
		// TODO Auto-generated method stub
		String maxEmpNo = employeeService.getMaxEmpNoNext();
		session.setAttribute("maxEmpNo", maxEmpNo);

		return ADD_EMP_PAGE;
	}

	@RequestMapping(value = "Employee.doValidateInsertEmp", method = RequestMethod.POST)
	private String doValidateInsertEmp(HttpSession session, Model model, @RequestParam("name") String name,
			@RequestParam("id") String id, @RequestParam("positionME") String position,
			@RequestParam(value = "positionSys", required = false) String positionS,
			@RequestParam("email") String email) {
		// TODO Auto-generated method stub
		// request.removeAttribute("result");

		Map<String, String> errorMsgs = new HashMap<>();
		// String name, id, position, positionS, email;
		// name = request.getParameter("name");
		//
		// id = request.getParameter("id");
		// position = request.getParameter("positionME");
		// positionS = request.getParameter("positionSys");
		// email = request.getParameter("email");

		session.setAttribute("name", name.trim());
		session.setAttribute("id", id);
		session.setAttribute("positionME", position);
		session.setAttribute("positionSys", positionS);
		session.setAttribute("email", email);

		if (isBlank(name)) {
			errorMsgs.put("name", "請輸入名字");
		}
		if (isBlank(id)) {
			errorMsgs.put("pid", "請輸入身分證");
		}
		if (position == null) {
			errorMsgs.put("position", "請選擇職位");
		}
		if (isBlank(email)) {
			errorMsgs.put("email", "請輸入信箱");
		}

		if (!errorMsgs.isEmpty()) {
			model.addAttribute("errorMsgs", errorMsgs);
			return ADD_EMP_PAGE;
		}

		if (!isValidPID(id)) {
			errorMsgs.put("id", "身分證格式錯誤");
		}

		if (!isValidEmail(email)) {
			errorMsgs.put("email", "email格式錯誤");
		}

		if (!errorMsgs.isEmpty()) {
			model.addAttribute("errorMsgs", errorMsgs);
			return ADD_EMP_PAGE;
		}
		System.out.println("validateOK!");

		return ADD_EMP_PAGE1;
	}

	@RequestMapping(value = "Employee.doInsertEmp", method = RequestMethod.POST)
	private String doInsertEmp(HttpSession session, HttpServletRequest request, Model model) {
		// TODO Auto-generated method stub
		Employee emp = new Employee();

		emp.setEmpno(session.getAttribute("maxEmpNo").toString());
		emp.setName(session.getAttribute("name").toString());
		StringBuilder position = new StringBuilder();

		// Employee or Manager
		if (session.getAttribute("positionME").toString().equals("manager")) {
			position.append("主管");
		} else {
			position.append("員工");
		}
		if (session.getAttribute("positionSys") != null
				&& session.getAttribute("positionSys").toString().equals("on")) {
			position.append("-系統管理員");
		}
		//
		emp.setPosition(position.toString());
		emp.setPassword(null);
		emp.setEmail(session.getAttribute("email").toString());
		emp.setId(session.getAttribute("id").toString().toUpperCase());

		// 新增員工
		employeeService.insert(emp);

		// 發送email
		EmailService service = new EmailService();
		StringBuilder html = new StringBuilder();
		String uri = new StartEmpService().addEmp(emp.getEmpno(), request);
		html.append("<style>p{margin: 0 0;}p span{margin: 0 10px;}</style>");
		html.append("<center><h1>工時系統-啟動帳號</h1><hr>");
		html.append("<h2>您的帳號已被建立，請點選以下連結來啟用您的帳號</h2>");
		html.append("<p><span>員工編號:</span><span>" + emp.getEmpno() + "</span></p>");
		html.append("<p><span>姓名:</span><span>" + emp.getName() + "</span></p>");
		html.append("<p><span>職位:</span><span>" + emp.getPosition() + "</span></p>");
		html.append("<h3><a href='" + uri + "'>點我啟動帳號</a></h3>歡迎加入!<hr>");
		html.append("<h5>若是連結無效，請複製下列連結:</h5>");
		html.append("<p>" + uri + "</p>");
		html.append("<span style='position: relative;left:30%;'>系統管理員 敬上</span></center>");
		String Title = "新增員工成功囉!!";
		try {
			service.sendHtmlMail(emp.getEmail(), Title, html.toString());
		} catch (AddressException e) {
			// TODO 自動產生的 catch 區塊
			System.out.println("email地址錯誤: " + e.getMessage());
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO 自動產生的 catch 區塊
			System.out.println("email寄送錯誤: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Email送出!!");

		// remove Session attribute
		session.removeAttribute("maxEmpNo");
		session.removeAttribute("positionME");
		session.removeAttribute("positionSys");
		session.removeAttribute("email");
		session.removeAttribute("id");
		session.removeAttribute("name");

		model.addAttribute("result", true);
		return "forward:/Employee.doAddEmp";
	}

	/********************************** 以上吳軒穎 *****************************************/

	/********************************** 以下張芷瑄 *****************************************/
	@RequestMapping(value = "Employee.doFindUpdateEmp")
	private String doFindUpdateEmp(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam("input") String input, @RequestParam(value = "page", required = false) String pageNum,
			@RequestParam("by") String by) {
		if (session.getAttribute("login") == null) {
			return "forward:/Logout.do";
		}

		model.addAttribute("NotFirst", true);
		model.addAttribute("Search", input);

		Page page = new Page();
		if (pageNum != null) {
			page.setNowPage(Integer.parseInt(pageNum));
		}

		Map<String, String> updateEmp = new HashMap<String, String>();
		updateEmp.put("by", by);
		updateEmp.put("input", input);

		Map<String, Object> dataMap = employeeService.findUpdateEmpInfo(updateEmp, page);// List<Employee>
		List<Employee> empInfo = (List<Employee>) dataMap.get("List<Employee>");// 回傳被修改的人的資訊

		page = (Page) dataMap.get("page");
		page.setAction(request);

		model.addAttribute("page", page);
		session.setAttribute("UpdateEmpInfoList", empInfo);
		return UPDATE_EMP_PAGE;
	}

	@RequestMapping("Employee.doFindModifyInfo")
	private String doFindModifyInfo(HttpSession session, @RequestParam("modifyempno") String modifyempno) {
		session.removeAttribute("UpdateEmpInfoList");
		// Selectempno放確定修改的人的empno
		// String Selectempno = request.getParameter("modifyempno");
		employeeService.findModifyEmpInfo(modifyempno);
		session.setAttribute("Empno", modifyempno);

		Map<String, String> modifyInfo = employeeService.findModifyEmpInfo(modifyempno);

		session.setAttribute("modifyInfoList", modifyInfo);
		return UPDATE_EMP_PAGE1;

	}

	//
	// private void cancel(HttpServletRequest request) {
	// // TODO Auto-generated method stub
	//
	// }
	@RequestMapping(value = "Employee.doValidateModifyEmp", method = RequestMethod.POST)
	private String doValidateModifyEmp(HttpSession session, Model model, @RequestParam("name") String name,
			@RequestParam("id") String id, @RequestParam("positionME") String position,
			@RequestParam(value = "positionSys", required = false) String positionS,
			@RequestParam("email") String email, @RequestParam("end") String end) {
		// TODO Auto-generated method stub

		Map<String, String> errorMsgs = new HashMap<>();

		// 先暫存
		// String name, id, position, positionS, email, end;
		// name = request.getParameter("name");
		// id = request.getParameter("id");
		// position = request.getParameter("positionME");
		// positionS = request.getParameter("positionSys");
		// email = request.getParameter("email");
		// end = request.getParameter("end");

		Map<String, String> modifyInfo = new HashMap<>();
		modifyInfo.put("name", name.trim());
		modifyInfo.put("id", id);
		modifyInfo.put("positionME", position);
		modifyInfo.put("positionSys", positionS);
		modifyInfo.put("email", email);
		modifyInfo.put("end", end);
		session.setAttribute("modifyInfoList", modifyInfo);

		// 驗證
		if (isBlank(name)) {
			errorMsgs.put("name", "請輸入名字");
		}
		if (isBlank(id)) {
			errorMsgs.put("pid", "請輸入身分證");
		}
		if (position == null) {
			errorMsgs.put("position", "請選擇職位");
		}
		if (isBlank(email)) {
			errorMsgs.put("email", "請輸入信箱");
		}

		if (!errorMsgs.isEmpty()) {
			model.addAttribute("errorMsgs", errorMsgs);
			return UPDATE_EMP_PAGE1; // 若以上4個有錯，即返修改畫面
		}

		if (!isValidPID(id)) {
			errorMsgs.put("id", "身分證格式錯誤");
		}

		if (!isValidEmail(email)) {
			errorMsgs.put("email", "email格式錯誤");
		}

		if (!errorMsgs.isEmpty()) {
			model.addAttribute("errorMsgs", errorMsgs);
			return UPDATE_EMP_PAGE1;
		}

		System.out.println("validateSuccess");

		return UPDATE_EMP_PAGE2; // 不能修改的畫面
	}

	@RequestMapping(value = "Employee.doUpdateEmpInfo", method = RequestMethod.POST)
	private String doUpdateEmpInfo(HttpSession session, Model model) {

		Employee emp = new Employee();

		Map<String, String> modifyInfo = (Map<String, String>) session.getAttribute("modifyInfoList");

		emp.setEmpno(session.getAttribute("Empno").toString());
		emp.setName(modifyInfo.get("name"));
		StringBuilder position = new StringBuilder();
		StringBuilder end = new StringBuilder();

		// 系統日期
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// Employee or Manager
		if (modifyInfo.get("positionME").equals("manager")) {
			position.append("主管");
		} else {
			position.append("員工");
		}
		if (modifyInfo.get("positionSys") != null && modifyInfo.get("positionSys").toString().equals("on")) {
			position.append("-系統管理員");
		}

		// 離職或未離職
		if (modifyInfo.get("end").equals("End")) {
			end.append(dateFormat.format(date));
		} else {
			end.append(date = null);
		}
		System.out.println(end);
		// 字串轉日期

		try {
			if (date != null) {
				date = dateFormat.parse(end.toString());
				System.out.println(date);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		emp.setPosition(position.toString());
		emp.setEmail(modifyInfo.get("email"));
		emp.setId(modifyInfo.get("id").toUpperCase()); // toUpperCase()轉大寫
		emp.setEnd(date);

		// insert EMPLOYEE
		employeeService.UpdateEmpInfo(emp);
		session.removeAttribute("UpdateEmpInfoList");
		session.removeAttribute("positionME");
		session.removeAttribute("positionSys");
		session.removeAttribute("emp");

		model.addAttribute("result", true);
		return UPDATE_EMP_PAGE;

	}
	/********************************** 以上張芷瑄 *****************************************/

}
