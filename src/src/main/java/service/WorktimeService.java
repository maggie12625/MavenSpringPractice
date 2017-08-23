
package service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import dao.WorktimeDAO;
import dao.WorktimeJDBCDAO;
import model.Employee;
import model.Page;
import model.Worktime;
import model.WorktimeDetail;

public class WorktimeService {

	private WorktimeDAO wDao = new WorktimeJDBCDAO();

	/************************************* 以下陳民錞 *****************************/
	public List<Worktime> getWorktimeInfo(String empno) {
		return wDao.getWorktime(empno);
	}

	public void saveWorktimeDetail(Worktime worktime) {
		wDao.saveWorktimeDetail(worktime);
	}

	public void submitWorktimeDetail(Worktime worktime) {
		wDao.submitWorktimeDetail(worktime);
	}

	public String getWorktimeStatus(String detailId) {
		return wDao.getWorktimeStatus(detailId);
	}

	public Map<String, Object> getWorktimeByEmpnoInfo(String empno, String year_month, Page page)
			throws ParseException {
		return wDao.getWorktimeByEmpno(empno, year_month, page);
	}

	public Map<String, Object> getWorktimeByNameInfo(String name, String year_month, Page page) throws ParseException {
		return wDao.getWorktimeByName(name, year_month, page);
	}

	public Worktime calculateWorktime_time(Worktime worktime) {
		return wDao.calculateWorktime_time(worktime);
	}

	/************************************* 以上陳民錞 *****************************/

	/************************************* 以下張芷瑄 *****************************/
	public Map<String, Object> getUnsubmitEmpList(Page page) {
		return wDao.getUnsubmitEmpList(page);
	}

	public void updateStatus(String[] emps, String[] date) {
		wDao.updateStatus(emps, date);// 不用return 因為回傳值為void
	};

	public void addCalltimes(String[] emps, String[] date) {
		wDao.addCalltimes(emps, date);// 不用return 因為回傳值為void
	};

	/************************************* 以上張芷瑄 *****************************/

	/************************************** 以下彥儒 ************************************/
	public Map<String, Object> findWorktime(Page page) {
		return wDao.findWorktime(page);
	}

	public Map<String, Object> findByEmpno(String dateAndWeek, String empno, Page page) {
		return wDao.findByEmpno(dateAndWeek, empno, page);
	}

	public Map<String, Object> findByName(String dateAndWeek, String name, Page page) {
		return wDao.findByName(dateAndWeek, name, page);
	}

	public void checkWorktime(String detailId) {
		// TODO Auto-generated method stub
		wDao.checkWorktime(detailId);
	}

	/************************************** 以上彥儒 ************************************/

	/********************************** 以下吳軒穎 *****************************************/

	/********************************** 以上吳軒穎 *****************************************/

}
