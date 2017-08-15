package service;

import java.util.List;
import java.util.Map;

import dao.EmployeeDAO;
import dao.EmployeeJDBCDAO;
import model.Employee;
import model.Page;

public class EmployeeService {
	
	private EmployeeDAO eDao=new EmployeeJDBCDAO();
	
	public EmployeeService() {
		// TODO 自動產生的建構子 Stub
	}
	
	public void update(Employee employee){
		eDao.update(employee);
	}
	
	public Employee getPersonalInfo(String empNo){
		return eDao.findByEmpNo(empNo);
	}
	
	public String getEmpEmail(String empno){
		return eDao.getEmpEmail(empno);
	}
	
	/**
	 * 檢查傳入的empNo是否存在
	 * @param empNo
	 * 員工編號
	 * @return boolean
	 */
	public boolean hasEmp(String empNo){
		return eDao.hasEmp(empNo);
	}
	
	/**
	 * 檢查傳入的empNo是否啟動
	 * @param empNo
	 * 員工編號
	 * @return boolean
	 */
	public boolean isStartedEmp(String empNo){
		return eDao.isStartedEmp(empNo);
	}
	
	
	/**
	 * 檢查傳入的employee是否可登入
	 * @param employee
	 * 員工
	 * @return boolean
	 */
	public boolean checkAccount(Employee employee){
		return eDao.checkAccount(employee);
	}
	
	/**
	 * 由傳入的員工編號，取得登入資訊。包括員編、姓名與職位。
	 * @param empNo
	 * @return Map<String,String>
	 */
	public Map<String,String> getLoginInfo(String empNo){
		return eDao.getLoginInfoByEmpno(empNo);
	}
	
	
	/*******************以下彥儒**************************/
	public Map<String , Object> findEmpInfo(Page page) {
	    return this.eDao.findEmpInfo(page); 
	}
	public Map<String , Object> findByEmpno(String empno,Page page){
		return this.eDao.findByEmpno(empno,page);
	}
	public Map<String , Object> findByName(String name,Page page){
		return this.eDao.findByName(name,page);
	}

	
	/*******************以上彥儒**************************/
	
	/**********************************以下吳軒穎*****************************************/
	public String getMaxEmpNoNext(){		
		return eDao.findMaxEmpNoNext();
	}
	public void insert(Employee employee){
		eDao.insert(employee);
	}
	/**********************************以上吳軒穎*****************************************/
	
	/**********************************以下張芷瑄*****************************************/
	public Map<String, Object> findUpdateEmpInfo(Map<String,String> updateEmp,Page page) {
		return eDao.findUpdateEmpInfo(updateEmp,page);
		 
	}
	
	public Map<String,String> findModifyEmpInfo(String Selectempno) {
		return eDao.findModifyEmpInfo(Selectempno);
		 
	}
	
	public void UpdateEmpInfo(Employee emp){
		eDao.UpdateEmpInfo(emp);

	}
	/**********************************以上張芷瑄*****************************************/

}
