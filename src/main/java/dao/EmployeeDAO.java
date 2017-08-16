package dao;

import java.util.List;
import java.util.Map;

import model.Employee;
import model.Page;

public interface EmployeeDAO {

	boolean hasEmp(String empno);

	void insert(Employee employee);

	void update(Employee employee);

	boolean checkAccount(Employee employee);
	
	boolean isStartedEmp(String empno);

	Employee findByEmpNo(String Empno);
	
	String getEmpEmail(String Empno);
	
	Map<String, String> getLoginInfoByEmpno(String empNo);

	List<Employee> findAll();
	
	
	/**********************************以下彥儒**************************************/
	Map<String , Object> findEmpInfo(Page page);
	
	Map<String , Object> findByEmpno(String empno,Page page);
	
	Map<String , Object> findByName(String name,Page page);
	
	/**********************************以上彥儒***************************************/
	
	
	/**********************************以下吳軒穎*****************************************/	
	String findMaxEmpNoNext();
	/**********************************以上吳軒穎*****************************************/
	
	/**********************************以下張芷瑄*****************************************/

	public Map<String, Object> findUpdateEmpInfo(Map<String,String> updateEmp,Page page) ;
	public Map<String,String>  findModifyEmpInfo(String Selectempno) ;
	public void UpdateEmpInfo(Employee Emp);
	
	/**********************************以上張芷瑄*****************************************/
}
