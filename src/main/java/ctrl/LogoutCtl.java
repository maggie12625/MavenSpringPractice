package ctrl;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogoutCtl {
	
//	@ResponseBody
	@RequestMapping("/Logout.do")
	public String simple(HttpSession session) {
//		StringBuilder logout = new StringBuilder();
//		logout.append("<script>");
//		logout.append("window.parent.parent.location.href = 'login.jsp';");
//		logout.append("</script>");

		if (session.getAttribute("login") != null) {
			session.removeAttribute("login");
			session.invalidate();
		}

		return "login";

//		 return logout.toString();
		
		// response.getWriter().write(logout.toString());
		// response.flushBuffer();
	}

}
