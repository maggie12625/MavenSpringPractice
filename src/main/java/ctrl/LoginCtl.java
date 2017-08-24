package ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import model.Employee;
import service.EmployeeService;

@Controller
public class LoginCtl {
	
	private EmployeeService employeeService = new EmployeeService();
	private static final String SUCCUSS_PAGE = "main";
	private static final String ERROR_PAGE = "login";
	
	@RequestMapping(value="/Login.do",method=RequestMethod.POST)
	public   String simple(@RequestParam(value = "id") String id,
			@RequestParam(value = "password") String password ,Model model,HttpSession session) {
		List<String> errorMsgs = new ArrayList<String>();
		Employee employee = new Employee();
		employee.setEmpno(id);
		employee.setPassword(password);
		String path = ERROR_PAGE;
		System.out.println("id: '" + employee.getEmpno() + "' ,pw: '" + employee.getPassword() + "'");
		if (employee.getEmpno().equals("")) {
			errorMsgs.add("請輸入帳號!");
		}
		if (employee.getPassword().equals("")) {
			errorMsgs.add("請輸入密碼!");
		}
		if (!errorMsgs.isEmpty()) {
			model.addAttribute("errorMsgs", errorMsgs);
			return ERROR_PAGE;
		}
		if (employeeService.hasEmp(employee.getEmpno())) {
			if (employeeService.checkAccount(employee)) {
				Map<String, String> login=employeeService.getLoginInfo(employee.getEmpno());
				model.addAttribute("login",login);
				 
				session.setAttribute("login",login);
				if(login.get("end")==null){
					path = SUCCUSS_PAGE;
				}else{
					errorMsgs.add("你已離職，無法登入。");
					model.addAttribute("errorMsgs", errorMsgs);
					model.addAttribute("employee", employee);
				}
					
			} else {
				if(employeeService.isStartedEmp(employee.getEmpno())){
					errorMsgs.add("帳號或密碼錯誤");
					
				}else{
					errorMsgs.add("帳號尚未啟動");
				}
				model.addAttribute("errorMsgs", errorMsgs);
				model.addAttribute("employee", employee);
			}

		} else {
			errorMsgs.add("查無此帳號喔!!");
			model.addAttribute("errorMsgs", errorMsgs);
			model.addAttribute("employee", employee);
		}
		 
		return path;
	}

}