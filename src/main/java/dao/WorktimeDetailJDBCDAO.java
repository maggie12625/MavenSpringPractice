package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.Employee;
import model.Worktime;
import model.WorktimeDetail;

public class WorktimeDetailJDBCDAO implements WorktimeDetailDAO {

	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public WorktimeDetailJDBCDAO() {
		// TODO 自動產生的建構子 Stub
	}

	@Override
	public Worktime searchDetailWorktim(String searchEmpno, String firstDateOfWeek) {
		Worktime searchData = new Worktime();
		List<WorktimeDetail> worktimeDetailList = new ArrayList<WorktimeDetail>();

		StringBuilder sql = new StringBuilder();
		sql.append("select e.name,wt.*,wtd.* ");
		sql.append("from EMPLOYEE e,WORKTIME_DETAIL wtd,worktime wt ");
		sql.append("where wt.DETAIL_ID = wtd.DETAIL_ID (+) AND  wt.empno = e.empno ");
		sql.append("AND wt.DETAIL_ID = ? ");
		sql.append("ORDER BY wtd.WORK_NAME ");

		StringBuilder DETAIL_ID = new StringBuilder(searchEmpno.trim());
		DETAIL_ID.append(firstDateOfWeek.replaceAll("-", ""));
		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, DETAIL_ID.toString());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				Employee employee = new Employee();
				employee.setName(rs.getString("name"));
				searchData.setEmployee(employee);
				searchData.setEmpNo(rs.getString("EmpNo"));
				searchData.setWeekFirstDay(rs.getDate("WEEK_FIRST_DAY"));
				searchData.setStatus(rs.getString("STATUS"));
				searchData.setSunNormal(rs.getInt("SUN_NORMAL"));
				searchData.setSunOvertime(rs.getInt("SUN_OVERTIME"));
				searchData.setMonNormal(rs.getInt("MON_NORMAL"));
				searchData.setMonOvertime(rs.getInt("MON_OVERTIME"));
				searchData.setTueNormal(rs.getInt("TUE_NORMAL"));
				searchData.setTueOvertime(rs.getInt("TUE_OVERTIME"));
				searchData.setWedNormal(rs.getInt("WED_NORMAL"));
				searchData.setWedOvertime(rs.getInt("WED_OVERTIME"));
				searchData.setThuNormal(rs.getInt("THU_NORMAL"));
				searchData.setThuOvertime(rs.getInt("THU_OVERTIME"));
				searchData.setFriNormal(rs.getInt("FRI_NORMAL"));
				searchData.setFriOvertime(rs.getInt("FRI_OVERTIME"));
				searchData.setSatNormal(rs.getInt("SAT_NORMAL"));
				searchData.setSatOvertime(rs.getInt("SAT_OVERTIME"));
				searchData.setNotPassReason(rs.getString("NOT_PASS_REASON"));
				searchData.setWorktimeDetailId(rs.getString("DETAIL_ID"));
				// 存入 第一筆詳細工時資料
				WorktimeDetail detail = createWorktimeDetail(rs);
				if (detail != null)
					worktimeDetailList.add(detail);
			}
			// 存入 剩下的詳細工時資料
			while (rs.next()) {
				WorktimeDetail detail = createWorktimeDetail(rs);
				if (detail != null)
					worktimeDetailList.add(detail);
			}
			searchData.setWorktimeDetailList(worktimeDetailList);
			System.out.println("DetailId: " + DETAIL_ID.toString() + " 總共 " + worktimeDetailList.size() + " 筆詳細工時資料");
			return searchData;

		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} catch (Exception e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	private WorktimeDetail createWorktimeDetail(ResultSet rs) {
		WorktimeDetail worktimeDetail = new WorktimeDetail();
		try {
			if (rs.getString(21) != null) {
				worktimeDetail.setDetailId(rs.getString(21));
				worktimeDetail.setWorkName(rs.getString("WORK_NAME"));
				worktimeDetail.setWorkContent(rs.getString("WORK_CONTENT"));
				worktimeDetail.setSunNormal(rs.getInt(24));
				worktimeDetail.setSunOvertime(rs.getInt(25));
				worktimeDetail.setMonNormal(rs.getInt(26));
				worktimeDetail.setMonOvertime(rs.getInt(27));
				worktimeDetail.setTueNormal(rs.getInt(28));
				worktimeDetail.setTueOvertime(rs.getInt(29));
				worktimeDetail.setWedNormal(rs.getInt(30));
				worktimeDetail.setWedOvertime(rs.getInt(31));
				worktimeDetail.setThuNormal(rs.getInt(32));
				worktimeDetail.setThuOvertime(rs.getInt(33));
				worktimeDetail.setFriNormal(rs.getInt(34));
				worktimeDetail.setFriOvertime(rs.getInt(35));
				worktimeDetail.setSatNormal(rs.getInt(36));
				worktimeDetail.setSatOvertime(rs.getInt(37));

				return worktimeDetail;
			}

			return null;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		}
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
	
	
	/****************************以下彥儒***************************/
	public void passWorktime(String id, String date){
		StringBuilder builder = new StringBuilder();
		builder.append("update worktime ");
		builder.append("set status = '通過' ");
		builder.append("where empno = ? ");
		builder.append("and WEEK_FIRST_DAY = TO_DATE(?,'YYYY-MM-DD') ");
		

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, date);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	};
	public String notPassWorktime(String id, String date, String reason){
		StringBuilder update_builder = new StringBuilder();
		StringBuilder insert_builder = new StringBuilder();
		StringBuilder select_builder = new StringBuilder();
		
		update_builder.append("update worktime ");
		update_builder.append("set status = '未通過' ");
		update_builder.append(",NOT_PASS_REASON =?  ");
		update_builder.append("where empno = ? ");
		update_builder.append("and WEEK_FIRST_DAY = TO_DATE(?,'YYYY-MM-DD') ");
		
		insert_builder.append("Insert into callWorktime ");
		insert_builder.append("values ( ? ,TO_DATE(?,'YYYY-MM-DD'),0)");
		
		select_builder.append("select email ");
		select_builder.append("from employee " );
		select_builder.append("where empno= ? ");
		
		this.conn = ConnectionHelper.getConnection();
		try {
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(update_builder.toString());
			pstmt.setString(1, reason);
			pstmt.setString(2, id);
			pstmt.setString(3, date);
			pstmt.executeUpdate();
			
			pstmt= conn.prepareStatement(insert_builder.toString());
			pstmt.setString(1, id);
			pstmt.setString(2,date);
			pstmt.executeUpdate();
			
			pstmt= conn.prepareStatement(select_builder.toString());
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			String email = null;
			while(rs.next()){
				email = rs.getString("EMAIL");
			}
			conn.commit();
			return email;
		} catch (SQLException e) {
			
			try {
				// 失敗則 回覆操作
				if (conn != null) {
					
					conn.rollback();
				}

			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		
		} finally {
			close();
		}
		
	};



	
	/****************************以上彥儒***************************/

	public static void main(String[] args) {
		WorktimeDetailJDBCDAO detailJDBCDAO = new WorktimeDetailJDBCDAO();
		detailJDBCDAO.searchDetailWorktim("000002", "2017-12-05");
	}

}
