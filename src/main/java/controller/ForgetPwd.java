package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Employee;
import service.EmailService;
import service.EmployeeService;

import static utils.ValidateUtils.isLegalPassWord;

/**
 * Servlet implementation class ForgetPwd
 */
public class ForgetPwd extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MAXPASSWORD = 6;
	private static final String FORGET_PWD_PAGE = "/forgetPwd.jsp";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher(FORGET_PWD_PAGE).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.removeAttribute("result");
		Boolean result = false;

		String path = FORGET_PWD_PAGE;

		Map<String, String> errorMsgs = new HashMap<>();
		String empno = request.getParameter("empno");
		String id = request.getParameter("id").toUpperCase();
		request.setAttribute("empno", empno);
		request.setAttribute("id", id);
		EmployeeService employeeService = new EmployeeService();

		// 是否有此員編
		if (!employeeService.hasEmp(empno)) {
			errorMsgs.put("error_empno", "員編不存在");
			request.setAttribute("errorMsgs", errorMsgs);
			request.getRequestDispatcher(path).forward(request, response);
			return;
		}
		// 是否此員編已啟用
		if (!employeeService.isStartedEmp(empno)) {
			errorMsgs.put("error_empno", "此員編未啟用");
			request.setAttribute("errorMsgs", errorMsgs);
			request.getRequestDispatcher(path).forward(request, response);
			return;
		}

		Employee emp = employeeService.getPersonalInfo(empno);
		// 是否身分證相符
		if (!emp.getId().equals(id)) {
			errorMsgs.put("error_id", "身分證不相符");
			request.setAttribute("errorMsgs", errorMsgs);

			request.getRequestDispatcher(path).forward(request, response);
			return;
		}

		String[] PwdContent = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G",
				"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b",
				"c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
				"x", "y", "z" };
		StringBuilder new_password = new StringBuilder();
		// 產生隨機密碼並驗證此隨機密碼是否合法
		do {
			new_password = new StringBuilder();
			for (int i = 1; i <= MAXPASSWORD; i++) {
				System.out.println(i);
				new_password.append(PwdContent[(int) (Math.random() * PwdContent.length)]);
			}
			System.out.println(new_password.toString());
		} while (!isLegalPassWord(new_password.toString()));
		// 更改密碼
		emp.setPassword(new_password.toString());
		employeeService.update(emp);
		// 送出email
		EmailService service = new EmailService();
		StringBuilder Text = new StringBuilder();
		// 忘記密碼時間
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
		String forgetDate = df.format(date);

		Text.append("<style>p{font-size: 17px;margin: 0 10px;}p span{margin: 0 10px;font-weight: bold;}");
		Text.append("div{margin-top:10px; }div p{font-weight: bold;color: red;}</style>");
		Text.append("<center><h1>工時系統-忘記密碼</h1><hr>");
		Text.append("<p><span>您在 " + forgetDate + " 使用忘記密碼服務</span></p>");
		Text.append("<div></div>");
		Text.append("<p>員工編號為:" + emp.getEmpno() + "</p>");
		Text.append("<p>您的新密碼為:" + new_password + "</p>");
		Text.append("<div>");
		Text.append("<p>如果非您本人執行此項服務</p>");
		Text.append("<p>請務必保持Email為您自己的信箱!</p>");
		Text.append("<p>以確保您帳號之安全</p>");
		Text.append("</div>");
		Text.append("<h2>系統管理員 敬上</h2></center>");
		String Title = "工時系統忘記密碼!!";
		try {
			service.sendHtmlMail(emp.getEmail(), Title, Text.toString());
			System.out.println("已送出忘記密碼信件");
		} catch (AddressException e) {
			// TODO 自動產生的 catch 區塊
			System.out.println("email地址錯誤: " + e.getMessage());
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO 自動產生的 catch 區塊
			System.out.println("email寄送錯誤: " + e.getMessage());
			e.printStackTrace();
		}
		result = true;
		request.setAttribute("result", result);
		request.removeAttribute("empno");
		request.removeAttribute("id");
		request.getRequestDispatcher(FORGET_PWD_PAGE).forward(request, response);
	}

}
