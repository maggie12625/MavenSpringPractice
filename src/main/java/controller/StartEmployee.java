package controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Employee;
import service.EmailService;
import service.EmployeeService;
import service.StartEmpService;

/**
 * Servlet implementation class StartEmployee
 * 
 * uri:/StartEmployee.do
 */
public class StartEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String START = "/startEmp.jsp";

	private EmployeeService employeeService = new EmployeeService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		StartEmpService startEmpServicece = new StartEmpService();
		String empno = request.getParameter("empno");
		String value = request.getParameter("v");
		String action = request.getParameter("action");
		// System.out.println(new StartEmpService().isMatch(empno, value));
		System.out.println("啟動頁面" + empno + "  " + action);
		// Entry<String,String> set=(Entry<String, String>)
		// service.empMap.entrySet();
		System.out.println("現在有:" + startEmpServicece.empMap.size());

		if (action != null && "restart".equals(action)) {
			// 重新啟動帳號
			System.out.println("重新啟動");

			if (employeeService.hasEmp(empno)) {
				// 有此員工
				if (employeeService.isStartedEmp(empno)) {
					// 已啟動帳號
					
					response.getWriter().println("{\"result\":\"帳號已經啟動，不需再器啟動。\"}");
				} else {
					// 尚未啟動帳號
					String uri=null;
					String email=employeeService.getEmpEmail(empno);
					if (startEmpServicece.hasEmpInMap(empno)) {
						// 已存在map中
						uri=startEmpServicece.restartEmp(empno, request);
					} else {
						uri=startEmpServicece.addEmp(empno, request);
					}
					sendEmail(email,uri);
					response.getWriter().println("{\"result\":\"success\"}");
				}
			} else {
				// 沒有此員工
				response.getWriter().println("{\"result\":\"查無此員編。\"}");
			}

			response.flushBuffer();
			return;
		}
		// 啟動帳號驗證
		if (startEmpServicece.isMatch(empno, value)) {
			changePassword(empno);
			startEmpServicece.remove(empno);
			request.setAttribute("result", "success");
		} else {
			request.setAttribute("empno", empno);
			request.setAttribute("result", "error");
		}

		request.getRequestDispatcher(START).forward(request, response);
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
	
	
	private void sendEmail(String email,String uri){
		EmailService emailService = new EmailService();
		StringBuilder html = new StringBuilder();
		html.append("<center><h1>工時系統-重新啟動</h1><hr>");
		html.append("<h2>您的帳號已被建立，請點選以下連結來啟用您的帳號</h2>");
		html.append("<h3><a href="+uri+">點我啟動帳號</a></h3>歡迎加入!<hr>");
		html.append("<h5>若是連結無效，請複製下列連結:</h5>");
		html.append("<p>"+uri+"</p>");
		html.append("<span style='position: relative;left:30%;'>系統管理員 敬上</span></center>");
		String Title = "工時系統-重新啟動";
		try {
			emailService.sendHtmlMail(email, Title, html.toString());
		} catch (AddressException e) {
			// TODO 自動產生的 catch 區塊
			System.err.println("email地址錯誤: " + e.getMessage());
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO 自動產生的 catch 區塊
			System.err.println("email寄送錯誤: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void changePassword(String empno){
		Employee employee=employeeService.getPersonalInfo(empno);
		employee.setPassword(employee.getId());
		employeeService.update(employee);
	}

}
