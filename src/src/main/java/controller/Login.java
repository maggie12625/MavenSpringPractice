package controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jws.Oneway;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Employee;
import service.EmailService;
import service.EmployeeService;
import service.excelService;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String SUCCUSS_PAGE = "main.jsp";
	private static final String ERROR_PAGE = "login.jsp";

	private EmployeeService employeeService = new EmployeeService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 如果用get傳送則導回 ERROR_PAGE
		request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub	
		
		String path = ERROR_PAGE;
		List<String> errorMsgs = new ArrayList<String>();
		Employee employee = new Employee();
		employee.setEmpno(request.getParameter("id"));
		employee.setPassword(request.getParameter("password"));

		System.out.println("id: '" + employee.getEmpno() + "' ,pw: '" + employee.getPassword() + "'");
		if (employee.getEmpno().equals("")) {
			errorMsgs.add("請輸入帳號!");
		}
		if (employee.getPassword().equals("")) {
			errorMsgs.add("請輸入密碼!");
		}
		if (!errorMsgs.isEmpty()) {
			request.setAttribute("errorMsgs", errorMsgs);
			request.getRequestDispatcher(path).forward(request, response);
		}
		
		if (employeeService.hasEmp(employee.getEmpno())) {
			if (employeeService.checkAccount(employee)) {
				Map<String, String> login=employeeService.getLoginInfo(employee.getEmpno());
				request.getSession().setAttribute("login",login);
				if(login.get("end")==null){
					path = SUCCUSS_PAGE;
				}else{
					errorMsgs.add("你已離職，無法登入。");
					request.setAttribute("errorMsgs", errorMsgs);
					request.setAttribute("employee", employee);
				}
					
			} else {
				if(employeeService.isStartedEmp(employee.getEmpno())){
					errorMsgs.add("帳號或密碼錯誤");
					
				}else{
					errorMsgs.add("帳號尚未啟動");
				}
				request.setAttribute("errorMsgs", errorMsgs);
				request.setAttribute("employee", employee);
			}

		} else {
			errorMsgs.add("查無此帳號喔!!");
			request.setAttribute("errorMsgs", errorMsgs);
			request.setAttribute("employee", employee);
		}
		
		request.getRequestDispatcher(path).forward(request, response);

	}

}
