package service;

import dao.WorktimeDetailDAO;
import dao.WorktimeDetailJDBCDAO;
import model.Worktime;

public class WorktimeDetialService {

	private WorktimeDetailDAO wdDAO=new WorktimeDetailJDBCDAO();
	
	public Worktime searchDetailWorktim(String searchEmpno,String firstDateOfWeek){
		return wdDAO.searchDetailWorktim(searchEmpno,firstDateOfWeek);
	}
	
	
	
	/****************************以下彥儒***************************/
	public void passWorktime(String id, String date){
		wdDAO.passWorktime(id, date);
	};
	public String notPassWorktime(String id, String date, String reason){
		return wdDAO.notPassWorktime(id, date, reason);
	};

	
	/****************************以上彥儒***************************/
	

}
