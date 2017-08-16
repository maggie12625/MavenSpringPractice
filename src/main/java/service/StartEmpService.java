package service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class StartEmpService {
	public static Map<String, String> empMap = new HashMap<String, String>();

	// request.getServerPort()+request.getContextPath()
	public String addEmp(String empNo, HttpServletRequest request) {
		String value = getRandString();
		empMap.put(empNo, value);
		StringBuilder uri = new StringBuilder();
		uri.append(request.getScheme() + "://");
		uri.append(request.getServerName() + ":");
		uri.append(request.getServerPort());
		uri.append(request.getContextPath());
		uri.append("/StartEmployee.do?empno=" + empNo);
		uri.append("&v=" + value);

		return uri.toString();
	}
	
	public String restartEmp(String empNo, HttpServletRequest request) {
		String value = getRandString();
		empMap.replace(empNo, value);
		StringBuilder uri = new StringBuilder();
		uri.append(request.getScheme() + "://");
		uri.append(request.getServerName() + ":");
		uri.append(request.getServerPort());
		uri.append(request.getContextPath());
		uri.append("/StartEmployee.do?empno=" + empNo);
		uri.append("&v=" + value);

		return uri.toString();
	}

	public boolean isMatch(String empNo, String value) {
		if (empNo == null || value == null)
			return false;
		return value.equals(empMap.get(empNo));
	}

	public boolean hasEmpInMap(String empNo) {
		return empMap.get(empNo) != null;
	}



	public void remove(String empNo) {
		empMap.remove(empNo);
	}

	private String getRandString() {
		StringBuilder randomString = new StringBuilder();
		int stringLength = (int) (Math.random() * 10 + 32);
		for (int i = 0; i < stringLength; i++) {
			int which = (int) ((Math.random() * 63) % 3);

			if (which == 1) {
				randomString.append((int) (Math.random() * 10));
			} else if (which == 2) {
				randomString.append((char) ((Math.random() * 26) + 65));
			} else {
				randomString.append(((char) ((Math.random() * 26) + 97)));
			}
		}
		return randomString.toString();
	}

}
