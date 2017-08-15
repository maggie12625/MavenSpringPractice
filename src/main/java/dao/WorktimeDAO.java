package dao;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.Employee;
import model.Page;
import model.Worktime;
import model.WorktimeDetail;

public interface WorktimeDAO {
	
	/*************************以下陳民錞*******************************/
	public List<Worktime> getWorktime(String empno);

	public boolean hasWorktimeDetail(WorktimeDetail detailid);
	
	public boolean hasWorktime(String detailid);
	
	public void saveWorktimeDetail(Worktime worktime);
	
	public void submitWorktimeDetail(Worktime worktime);
	
	public String getWorktimeStatus(String detailId);
	
	public Map<String, Object> getWorktimeByName(String name,String year_month, Page page)throws ParseException;
	
	public Map<String, Object> getWorktimeByEmpno(String empno,String year_month, Page page)throws ParseException;
	
	public Worktime calculateWorktime_time(Worktime worktime);
	
	public List<String[]> getExcel(String yearMonth, String keyword );
	
	/*************************以上陳民錞*******************************/
	
	/*************************以下吳軒穎*******************************/
	
	public int getWeekOfYear(Date date);
	/*************************以上吳軒穎*******************************/
	
	
	/*************************以下張芷瑄*******************************/
	
	Map<String, Object> getUnsubmitEmpList(Page page); 
	public void updateStatus (String[] emps,String[] date );
	public void addCalltimes (String[] emps,String[] date );
	
	/*************************以上張芷瑄*******************************/
	
	/************************************** 以下彥儒 ************************************/
	
	public Map<String, Object> findWorktime(Page page);
	public Map<String, Object> findByEmpno(String dateAndWeek,String empno,Page page);
	public Map<String, Object> findByName(String dateAndWeek,String name,Page page);
	public void checkWorktime(String detailId);


	/************************************** 以上彥儒 ************************************/
	
	
	
}
