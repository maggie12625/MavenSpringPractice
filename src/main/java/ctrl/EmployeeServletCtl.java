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
			@RequestParam(required = false, value = "action") String action,
			@RequestParam(required = false, value = "name") String name,
			@RequestParam(required = false, value = "id") String id,
			@RequestParam(required = false, value = "positionME") String position,
			@RequestParam(required = false, value = "positionSys") String positionS,
			@RequestParam(required = false, value = "email") String email,
			@RequestParam(required = false, value = "end") String end,
			@RequestParam(required = false, value = "old_pw") String old_password,
			@RequestParam(required = false, value = "user-pwd-input") String new_password,
			@RequestParam(required = false, value = "user_pwd_again") String new_again_password,
			@RequestParam(required = false, value = "page") String pageNum,
			@RequestParam(required = false, value = "by") String searchBy,
			@RequestParam(required = false, value = "keyword") String keyword,
			@RequestParam(required = false, value = "input") String input,
			@RequestParam(required = false, value = "modifyempno") String modifyempno) {

		String page = null;

		if (session.getAttribute("login") == null) {
			return "forward:/Logout.do";
		}
		System.out.println("emp_action: " + action);
		switch (action) {
		case "info":
			// 轉交至個人資料畫面
			page = doFindPersonalInfo(model, session);
			break;
		// 轉交至變更密碼頁面
		case "changPwd_page":
			page = CHANGE_PASSWORD_PAGE;
			break;
		// 進行變更密碼
		case "changPwd":
			page = doChangePwd(session, model, old_password, new_password, new_again_password);
			break;
		/************************************ 底下彥儒 **********************************/
		// 轉交至主管查詢頁面
		case "mgrSearch_page":
			page = MGR_SEARCH_PAGE;
			break;
		// 轉交至主管查詢員工資料頁面 ，剛進去
		case "mgrSearchEmp_page":
			// 這邊要改 page = MGR_SEARCH_EMP_PAGE;
			page = doFindEmpInfo(pageNum,request,model,session);
			break;
		// 轉交至主管查詢工時頁面
		case "mgrSearchWorktime_page":
			page = MGR_SEARCH_WORKTIME_PAGE;
			break;
		// 轉交至(主要)工時審核頁面
		case "mgrManageWorktime_page":
			page = MGR_MANAGE_WORKTIME_PAGE;
			break;
		// 轉交至工時催繳頁面
		case "callWorktime_page":
			page = MGR_CALL_WORKTIME_PAGE;
			break;
		// 搜尋員工
		case "search_employee":
			doSearchEmployee(model, request, pageNum, searchBy, keyword);
			page = MGR_SEARCH_EMP_PAGE;
			break;
		/********************** 以上彥儒 **********************************/

		/********************************** 以下吳軒穎 *****************************************/
		// 轉交至員工管理頁面
		case "empManage_page":
			page = EMP_MANAGE_PAGE;
			break;
		case "addEmp":
			page = doAddEmp(session);
			break;
		case "validateInsertEmp":
			page = doValidateInsertEmp(session, model, name, id, position, positionS, email);
			break;
		case "insertEmp":
			page = doInsertEmp(session, request, model);
			break;
		/********************************** 以上吳軒穎 *****************************************/

		/********************************** 以下張芷瑄 *****************************************/
		// 轉到修改員工頁面
		case "updateEmp_page":
			page = UPDATE_EMP_PAGE;
			break;
		// 取得被修改的人
		case "FindUpdateEmp":
			page = doFindUpdateEmp(model, session, request, input, pageNum, searchBy);
			break;
		// 動作：取得確定修改的人的資訊
		// 目的：修改
		case "updateEmp_page1":
			page = doFindModifyInfo(session, modifyempno);
			break;
		// 驗證修改資料
		case "validateModifyEmp":
			page = doValidateModifyEmp(session, model, name, id, position, positionS, email, end);
			break;
		// 取消
		case "cancel":
			page = UPDATE_EMP_PAGE1;
			break;
		// 更新修改資料
		case "doUpdateEmpInfo":
			page = doUpdateEmpInfo(session, model);
			break;
		/********************************** 以上張芷瑄 *****************************************/
		default:
			System.out.println("error");
			page = "./main";
		}
		return page;
	}

	private String doFindPersonalInfo(Model model, HttpSession session) {
		Map<String, String> loginInfo = (Map<String, String>) session.getAttribute("login");
		String empNo = loginInfo.get("empno");
		Employee empVO = employeeService.getPersonalInfo(empNo);
		model.addAttribute("employee", empVO);
		return INFO_PAGE;
	}


	private String doChangePwd(HttpSession session, Model model, String old_password, String new_password,
			String new_again_password) {
		Map<String, String> errorMsgs = new HashMap<>();
		Map<String, String> loginInfo = (Map<String, String>) session.getAttribute("login");
		String empNo = loginInfo.get("empno");
		Employee empVO = employeeService.getPersonalInfo(empNo);

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

	private String doFindEmpInfo(String pageNum,HttpServletRequest request,Model model,
			HttpSession session) {
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


	private String doSearchEmployee(Model model, HttpServletRequest request, String pageNum, String searchBy,
			String keyword) {
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

	private String doAddEmp(HttpSession session) {
		// TODO Auto-generated method stub
		String maxEmpNo = employeeService.getMaxEmpNoNext();
		session.setAttribute("maxEmpNo", maxEmpNo);

		return ADD_EMP_PAGE;
	}


	private String doValidateInsertEmp(HttpSession session, Model model, String name, String id, String position,
			String positionS, String email) {
		// TODO Auto-generated method stub
		// request.removeAttribute("result");

		Map<String, String> errorMsgs = new HashMap<>();

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
		return doAddEmp(session);
	}

	/********************************** 以上吳軒穎 *****************************************/

	/********************************** 以下張芷瑄 *****************************************/

	private String doFindUpdateEmp(Model model, HttpSession session, HttpServletRequest request, String input,
			String pageNum, String by) {
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


	private String doFindModifyInfo(HttpSession session,String modifyempno) {
		session.removeAttribute("UpdateEmpInfoList");
		// Selectempno放確定修改的人的empno

		employeeService.findModifyEmpInfo(modifyempno);
		session.setAttribute("Empno", modifyempno);

		Map<String, String> modifyInfo = employeeService.findModifyEmpInfo(modifyempno);

		session.setAttribute("modifyInfoList", modifyInfo);
		return UPDATE_EMP_PAGE1;

	}

	
	private String doValidateModifyEmp(HttpSession session, Model model, String name, String id, String position,
			String positionS, String email, String end) {
		// TODO Auto-generated method stub

		Map<String, String> errorMsgs = new HashMap<>();

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
