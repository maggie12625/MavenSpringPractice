package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Employee;
import model.Page;
import utils.PageResultSet;

public class EmployeeJDBCDAO implements EmployeeDAO {
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public EmployeeJDBCDAO() {
		// TODO 自動產生的建構子 Stub
	}

	@Override
	public void insert(Employee employee) {
		// TODO 自動產生的方法 Stub
		StringBuilder builder = new StringBuilder();
		builder.append("insert into EMPLOYEE (empno,NAME , POSITION ,PASSWORD ,ID ,EMAIL )");
		builder.append("values (?,?,?,?,?,?)");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());

			pstmt.setString(1, employee.getEmpno());
			pstmt.setString(2, employee.getName());
			pstmt.setString(3, employee.getPosition());
			pstmt.setString(4, employee.getPassword());
			pstmt.setString(5, employee.getId());
			pstmt.setString(6, employee.getEmail());

			int i = pstmt.executeUpdate();
			System.out.println("新增 " + i + " 筆員工");

		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	@Override
	public void update(Employee employee) {
		// TODO 自動產生的方法 Stub
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE EMPLOYEE SET ");
		// 名子
		if (employee.getName() != null) {
			sql.append("NAME='" + employee.getName() + "' ");
		}
		// 職位
		if (employee.getPosition() != null) {
			sql.append(",POSITION='" + employee.getPosition() + "' ");
		}
		// 密碼
		if (employee.getPosition() != null) {
			sql.append(",PASSWORD='" + employee.getPassword() + "' ");
		}

		// 身份證
		if (employee.getId() != null) {
			sql.append(",ID='" + employee.getId() + "' ");
		}
		// email
		if (employee.getEmail() != null) {
			sql.append(",EMAIL='" + employee.getEmail() + "' ");
		}
		// 離職
		if (employee.getEnd() != null) {
			sql.append(",END=to_date('" +new SimpleDateFormat("yyyy-MM-dd").format(employee.getEnd()) + "','yyyy-MM-dd') ");
		}
		sql.append("WHERE EMPNO = '" + employee.getEmpno() + "' ");

		try {
			this.conn = ConnectionHelper.getConnection();
			stmt = conn.createStatement();
			int successCount = stmt.executeUpdate(sql.toString());

			System.out.println("更新 " + successCount + " 筆員工資料");

		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}

	}

	@Override
	public boolean hasEmp(String empno) {
		// TODO 自動產生的方法 Stub
		StringBuilder builder = new StringBuilder();
		builder.append("select count(empno) ");
		builder.append("from employee ");
		builder.append("where Empno = ? ");
		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());
			pstmt.setString(1, empno);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				int count = rs.getInt(1);
				if (count == 1) {
					return true;
				}
			}
			return false;

		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} catch (Exception e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		} finally {
			close();
		}
		return false;

	}

	@Override
	public boolean checkAccount(Employee employee) {
		// TODO 自動產生的方法 Stub
		StringBuilder builder = new StringBuilder();
		builder.append("select password ");
		builder.append("from employee ");
		builder.append("where Empno = ? ");
		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());

			pstmt.setString(1, employee.getEmpno());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				if(rs.getString("password")==null){
					return false;
				}
				if (rs.getString("password").equals(employee.getPassword())) {
					return true;
				}
			}

			return false;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	@Override
	public boolean isStartedEmp(String empno) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) ");
		sql.append("from employee ");
		sql.append("where Empno = ? ");
		sql.append("and password is null ");
		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, empno);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt("count(*)") == 1) {
					return false;
				}
			}

			return true;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	@Override
	public Employee findByEmpNo(String Empno) {
		// TODO 自動產生的方法 Stub
		StringBuilder builder = new StringBuilder();
		builder.append("select * ");
		builder.append("from employee ");
		builder.append("where Empno = ? ");
		builder.append("order by 1 ");
		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());

			pstmt.setString(1, Empno);

			rs = pstmt.executeQuery();
			Employee employee = createEmployee(rs);

			return employee;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	@Override
	public String getEmpEmail(String Empno) {
		String email = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select email ");
		sql.append("from employee ");
		sql.append("where Empno = ? ");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, Empno);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				email = rs.getString("email");
			}

			return email;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	@Override
	public Map<String, String> getLoginInfoByEmpno(String empNo) {
		// TODO 自動產生的方法 Stub
		Map<String, String> loginInfo = new HashMap<String, String>();
		StringBuilder builder = new StringBuilder();
		builder.append("select name ,position,end ");
		builder.append("from employee ");
		builder.append("where Empno = ? ");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());

			pstmt.setString(1, empNo);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				loginInfo.put("empno", empNo);
				loginInfo.put("name", rs.getString("name"));
				loginInfo.put("position", rs.getString("position"));
				if(rs.getDate("end")!=null){
					loginInfo.put("end", new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("end")));
				}
				
				

				return loginInfo;
			}

			return null;

		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	@Override
	public List<Employee> findAll() {
		// TODO 自動產生的方法 Stub
		return null;
	}

	public Employee createEmployee(ResultSet rs) {
		Employee employee = new Employee();
		try {
			if (rs.next()) {
				employee.setEmpno(rs.getString("empno"));
				employee.setName(rs.getString("name"));
				employee.setPosition(rs.getString("position"));
				employee.setPassword(rs.getString("password"));
				employee.setId(rs.getString("id"));
				employee.setEmail(rs.getString("email"));
				employee.setBegin(rs.getDate("begin"));
				employee.setEnd(rs.getDate("end"));
			}

		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		}

		return employee;
	}

	private void close() {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace(System.err);
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace(System.err);
			}
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace(System.err);
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace(System.err);
			}
	}

	public void insertWorktime(Employee emp) {
		Date date = new Date();

		try {
			String findbegindate = "select BEGIN from EMPLOYEE where empno=?"; // get
																				// begin
																				// of
																				// the
																				// emp
																				// table

			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(findbegindate);

			pstmt.setString(1, emp.getEmpno());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				date = rs.getDate("begin");
			}
			/////////////////////////////////////////////////////////////////////////////////////

			DateFormat dateFormat = new SimpleDateFormat("yyyy");

			Calendar cal = new GregorianCalendar((Integer.parseInt(dateFormat.format(date)) + 1), (0), 1);
			Date lastday = cal.getTime();

			Calendar firstDay = Calendar.getInstance();
			firstDay.setFirstDayOfWeek(Calendar.SUNDAY);
			firstDay.setTime(date);
			firstDay.set(Calendar.DAY_OF_WEEK, firstDay.getFirstDayOfWeek());

			date = firstDay.getTime();

			int lastweek = getWeekOfYear(lastday);
			int beginweek = getWeekOfYear(date);

			String insertworktime = "insert into worktime(empno, WEEK_FIRST_DAY)values(?,?)"; // insert
																								// worktime
																								// table
																								// of
																								// the
																								// emp
			pstmt = conn.prepareStatement(insertworktime);
			while (lastday.after(date)) {
				pstmt.setString(1, emp.getEmpno());
				pstmt.setDate(2, new java.sql.Date(date.getTime()));

				pstmt.executeUpdate();

				date.setDate(date.getDate() + 7);
			}

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			close();
		}
	}

	public int getWeekOfYear(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		int woy = cal.get(Calendar.WEEK_OF_YEAR);
		return woy;

	}

	/***************************** 以下彥儒 ******************************/
	@Override
	public Map<String, Object> findByEmpno(String empno, Page page) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Employee> list = new ArrayList<Employee>();

		StringBuilder builder = new StringBuilder();
		builder.append("select * ");
		builder.append("from employee ");
		builder.append("where Empno like ? ");
		builder.append("and position like '%員工%' ");
		builder.append("order by 1 ");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			pstmt.setString(1, "%" + empno + "%");

			PageResultSet pageResultSet=new PageResultSet(pstmt.executeQuery(), page);

			rs = pageResultSet.getResultSet();
			for (int i = 0; i < pageResultSet.getPageRowsCount(); i++) {
				// empVO 也稱為 Domain objects
				Employee emp = new Employee();
				emp.setEmpno(rs.getString("empno"));
				emp.setName(rs.getString("name"));
				emp.setPosition(rs.getString("position"));
				emp.setId(rs.getString("id"));
				emp.setEmail(rs.getString("email"));

				list.add(emp); // Store the row in the list
				rs.next();
			}
			System.out.println(list.size());
			dataMap.put("List<Employee>", list);
			dataMap.put("page", new Page(pageResultSet));
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}

		return dataMap;
	}

	@Override
	public Map<String, Object> findByName(String name, Page page) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Employee> list = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		builder.append("select * ");
		builder.append("from employee ");
		builder.append("where UPPER(name) like UPPER(?) ");
		builder.append("and position like '%員工%' ");
		builder.append("order by 1 ");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, "%" + name + "%");

			PageResultSet pageResultSet=new PageResultSet(pstmt.executeQuery(), page);

			rs = pageResultSet.getResultSet();
			for (int i = 0; i < pageResultSet.getPageRowsCount(); i++) {
				// empVO 也稱為 Domain objects
				Employee emp = new Employee();
				emp.setEmpno(rs.getString("empno"));
				emp.setName(rs.getString("name"));
				emp.setPosition(rs.getString("position"));
				emp.setId(rs.getString("id"));
				emp.setEmail(rs.getString("email"));

				list.add(emp); // Store the row in the list
				rs.next();
			}
			dataMap.put("List<Employee>", list);
			dataMap.put("page", new Page(pageResultSet));
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊

			e.printStackTrace();

			// throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}

		return dataMap;
	}

	@Override
	public Map<String, Object> findEmpInfo(Page page) {
		// TODO 自動產生的方法 Stub
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Employee> list = new ArrayList<>();

		StringBuilder builder = new StringBuilder();
		builder.append("select EMPNO,NAME,POSITION,ID,EMAIL ");
		builder.append("from employee ");
		builder.append("where position like '%員工%' ");
		builder.append("order by 1 ");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			// 分頁
			PageResultSet pageResultSet=new PageResultSet(pstmt.executeQuery(), page);

			rs = pageResultSet.getResultSet();
			for (int i = 0; i < pageResultSet.getPageRowsCount(); i++) {
				Employee emp = new Employee();
				emp.setEmpno(rs.getString("empno"));
				emp.setName(rs.getString("name"));
				emp.setPosition(rs.getString("position"));
				emp.setId(rs.getString("id"));
				emp.setEmail(rs.getString("email"));

				list.add(emp); // Store the row in the list
				rs.next();
			}
			dataMap.put("List<Employee>", list);
			dataMap.put("page", new Page(pageResultSet));

		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}

		return dataMap;
	}

	/***************************** 以上彥儒 ******************************/

	/********************************** 以下吳軒穎 *****************************************/
	@Override
	public String findMaxEmpNoNext() {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		builder.append("select max(empno) ");
		builder.append("from employee ");
		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());

			rs = pstmt.executeQuery();
			String maxEmpNo = null;
			if (rs.next()) {
				maxEmpNo = rs.getString("max(empno)");
			}
			if (maxEmpNo == null) {
				maxEmpNo = "0";
			}
			String maxEmpNoNext = String.format("%06d", Integer.parseInt(maxEmpNo) + 1); // get
																							// maxEmpNoNext
			System.out.println("maxEmpNoNext:" + maxEmpNoNext);

			return maxEmpNoNext;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}

	}

	/********************************** 以上吳軒穎 *****************************************/

	/********************************** 以下張芷瑄 *****************************************/
	// 取的被修改的人的資訊
	@Override
	public Map<String, Object> findUpdateEmpInfo(Map<String,String> updateEmp,Page page) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Employee> list = new ArrayList<>();

		StringBuilder builder = new StringBuilder();
		builder.append("select EMPNO,NAME,POSITION,ID,EMAIL,END ");
		builder.append("from employee ");
		builder.append("where upper(");
		builder.append(updateEmp.get("by"));
		builder.append(") like upper(?) ");
		builder.append("order by 1 ");

		try {

			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, "%" + updateEmp.get("input") + "%");
			
			PageResultSet pageResultSet=new PageResultSet(pstmt.executeQuery(), page);
			
			rs = pageResultSet.getResultSet();
			for (int i = 0; i < pageResultSet.getPageRowsCount(); i++) {
				Employee emp = new Employee();
				emp.setEmpno(rs.getString("empno"));
				emp.setName(rs.getString("name"));
				emp.setPosition(rs.getString("position"));
				emp.setId(rs.getString("id"));
				emp.setEmail(rs.getString("email"));
				emp.setEnd(rs.getDate("end"));

				list.add(emp); // Store the row in the list
				rs.next();
			}
			dataMap.put("List<Employee>", list);
			dataMap.put("page", new Page(pageResultSet));

		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}

		return dataMap;
	}

	// 取得確定修改的人的資訊
	@Override
	public Map<String, String> findModifyEmpInfo(String Selectempno) {
		// TODO 自動產生的方法 Stub
		Map<String, String> EmpMap = new HashMap<>();

		StringBuilder builder = new StringBuilder();
		builder.append("select EMPNO,NAME,POSITION,ID,EMAIL,END ");
		builder.append("from employee ");
		builder.append("where empno=? ");

		System.out.println(builder.toString());
		try {

			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());
			pstmt.setString(1, Selectempno);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// 判斷職位
				String position = rs.getString("position");
				String positionME = null, positionSys = null;
				if (position.contains("員工")) {
					positionME = "employee";
				} else {
					positionME = "manager";
				}

				if (position.contains("系統管理員")) {
					positionSys = "on";
				}

				if (position.contains("員工")) {
					positionME = "employee";
				} else {
					positionME = "manager";
				}

				Date dropoff = rs.getDate("end");
				String end;
				if (dropoff == null) {
					end = "unEnd";
				} else {
					end = "End";
				}

				System.out.println(positionSys);
				EmpMap.put("empno", rs.getString("empno"));
				EmpMap.put("name", rs.getString("name"));
				EmpMap.put("positionME", positionME);
				EmpMap.put("positionSys", positionSys);
				EmpMap.put("id", rs.getString("id"));
				EmpMap.put("email", rs.getString("email"));
				EmpMap.put("end", end);

				// Store the row in the list
			}

		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}

		return EmpMap;
	}
	
	@Override
	public void UpdateEmpInfo(Employee Emp) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder builder = new StringBuilder();
		builder.append("update employee ");
		builder.append("set NAME=?,POSITION=?,ID=?,EMAIL=? ");
		if (Emp.getEnd() != null) {
			builder.append(",END=to_date(?,'yyyy-MM-dd') ");
		}else{
			builder.append(",END=? ");
		}
		builder.append("where empno=? ");
		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());
			pstmt.setString(1, Emp.getName());
			pstmt.setString(2, Emp.getPosition());
			pstmt.setString(3, Emp.getId());
			pstmt.setString(4, Emp.getEmail());
			if (Emp.getEnd() != null) {
				pstmt.setString(5, dateFormat.format(Emp.getEnd()));
				pstmt.setString(6, Emp.getEmpno());
			}else{
				pstmt.setString(5, null);
			    pstmt.setString(6, Emp.getEmpno());}

			pstmt.executeUpdate();
			System.out.println("UpdateSuccess");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}


	}

	/********************************** 以上張芷瑄 *****************************************/

	public static void main(String[] args) {
		EmployeeDAO dao = new EmployeeJDBCDAO();

		// Employee emp = dao.findByEmpNo("1");
		// System.out.println("emp id:" + emp.getId());
		// System.out.println("emp name:" + emp.getName());
		//
		// Employee emp1 = new Employee();
		// emp1.setEmpno("000002");
		// emp1.setName("王曉明");
		// emp1.setId("F123456789");
		// emp1.setPosition("主管(系統管理員");
		// emp1.setPassword("manager");
		// emp1.setEmail("asd@asf.dfsd");
	}

}
