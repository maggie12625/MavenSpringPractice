package ctrl;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutCtl {
	@RequestMapping(value="/Logout.do")
	public String simple(HttpSession session) {

		if (session.getAttribute("login") != null) {
			session.removeAttribute("login");
			session.invalidate();
		}
		return "login";

	}

}
