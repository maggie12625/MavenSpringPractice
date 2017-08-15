package dao;

import java.sql.SQLException;

import model.Worktime;

public interface WorktimeDetailDAO {
	Worktime searchDetailWorktim(String searchEmpno,String firstDateOfWeek);
	
	
	/****************************以下彥儒***************************/
	public void passWorktime(String id, String date);
	public String notPassWorktime(String id, String date, String reason);

	
	/****************************以上彥儒***************************/
}
