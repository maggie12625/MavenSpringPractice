package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;
import javax.servlet.http.HttpServletRequest;

import model.Employee;
import model.Page;
import model.Worktime;
import model.WorktimeDetail;
import utils.PageResultSet;

public class WorktimeJDBCDAO implements WorktimeDAO {
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	/************************************* 以下陳民錞 *****************************************/
	@Override
	public List<Worktime> getWorktime(String empno) {
		// 造出工時javaBean物件
		List<Worktime> worktimeList = new ArrayList<Worktime>();
		// SQL語法找出符合條件的工時資料
		StringBuilder sql = new StringBuilder();
		sql.append("select * ");
		sql.append("from worktime ");
		sql.append("where empno=? ");
		sql.append("and status in ('未繳交','未通過','已催繳') ");
		sql.append("and week_first_day<=sysdate ");
		sql.append("order by 1 ");
		this.conn = ConnectionHelper.getConnection();
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, empno);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				worktimeList.add(createWorktime(rs));
			}
			// 測試用
		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}

		String firstDateOfWeek = new SimpleDateFormat("yyyy-MM-dd").format(calWeekFirstDate(new Date()));
		String detailId = empno + (firstDateOfWeek.replaceAll("-", ""));
		System.out.println(detailId + " 有資料嗎? " + hasWorktime(detailId));
		if (!hasWorktime(detailId)) {
			Worktime worktime = new Worktime();
			worktime.setEmpNo(empno);
			worktime.setStatus("未繳交");
			worktime.setWeekFirstDay(new java.sql.Date(parseDate(firstDateOfWeek).getTime()));
			worktime.setWorktimeDetailId(empno + (firstDateOfWeek.replaceAll("-", "")));
			worktime.setSunNormal(0);
			worktime.setSunOvertime(0);
			worktime.setMonNormal(0);
			worktime.setMonOvertime(0);
			worktime.setTueNormal(0);
			worktime.setTueOvertime(0);
			worktime.setWedNormal(0);
			worktime.setWedOvertime(0);
			worktime.setThuNormal(0);
			worktime.setThuOvertime(0);
			worktime.setFriNormal(0);
			worktime.setFriOvertime(0);
			worktime.setSatNormal(0);
			worktime.setSatOvertime(0);

			worktimeList.add(worktime);
		}

		return worktimeList;
	}

	public Map<String, Object> getWorktimeByName(String name, String year_month, Page page) throws ParseException {
		// 造出工時javaBean物件
		Map<String, Object> dataMap = new HashMap<String, Object>();
		System.out.println("姓名:" + name);
		Date month = null;
		Date firstDate;
		Date endDate;
		Date lastDayOfMonth;
		List<Worktime> worktimeList = new ArrayList<Worktime>();
		// SQL語法找出符合條件的工時資料
		StringBuilder sql = new StringBuilder();
		sql.append("select a.*,b.name ");
		sql.append("from worktime a,employee b ");
		sql.append("where a.empno=b.empno ");
		sql.append("and b.name like ? ");
		sql.append("and to_char(a.week_first_day,'yyyy-mm-dd') between ? and ? ");
		sql.append("and status='已通過'");
		sql.append("order by a.WEEK_FIRST_DAY,b.empno ");

		this.conn = ConnectionHelper.getConnection();
		try {
			pstmt = conn.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			month = new Date(new SimpleDateFormat("yyyy-MM").parse(year_month).getTime());
			System.out.print("查詢月份: " + new SimpleDateFormat("yyyy-MM").format(month));

			Calendar cal = Calendar.getInstance();
			firstDate = new Date(month.getTime());
			cal.setFirstDayOfWeek(Calendar.SUNDAY);
			cal.setTime(firstDate);
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
			firstDate = new Date(cal.getTime().getTime());

			lastDayOfMonth = new java.util.Date(month.getTime());
			lastDayOfMonth.setMonth(lastDayOfMonth.getMonth() + 1);
			lastDayOfMonth.setDate(lastDayOfMonth.getDate() - 1);
			cal.setTime(lastDayOfMonth);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			endDate = new Date(cal.getTime().getTime());

			pstmt.setString(1, "%" + name + "%");
			pstmt.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(firstDate));
			pstmt.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(endDate));

			PageResultSet pageResultSet = new PageResultSet(pstmt.executeQuery(), page);

			rs = pageResultSet.getResultSet();
			for (int i = 0; i < pageResultSet.getPageRowsCount(); i++) {
				Employee employee = new Employee();
				employee.setName(rs.getString("name"));
				Worktime worktime = createWorktime(rs);
				worktime.setEmployee(employee);
				worktimeList.add(worktime);
				rs.next();
			}

			dataMap.put("List<Worktime>", worktimeList);
			dataMap.put("page", new Page(pageResultSet));
		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
		return dataMap;
	}

	public Map<String, Object> getWorktimeByEmpno(String empno, String year_month, Page page) throws ParseException {
		// 造出工時javaBean物件
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Date month = null;
		Date firstDate;
		Date endDate;
		Date lastDayOfMonth;
		List<Worktime> worktimeList = new ArrayList<Worktime>();
		// SQL語法找出符合條件的工時資料
		StringBuilder sql = new StringBuilder();
		sql.append("select a.*,b.name ");
		sql.append("from worktime a,employee b ");
		sql.append("where a.empno=b.empno ");
		sql.append("and a.empno like ? ");
		sql.append("and to_char(a.week_first_day,'yyyy-mm-dd') between ? and ? ");
		sql.append("and status='已通過'");
		sql.append("order by a.WEEK_FIRST_DAY ");

		this.conn = ConnectionHelper.getConnection();
		try {
			pstmt = conn.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			month = new Date(new SimpleDateFormat("yyyy-MM").parse(year_month).getTime());
			System.out.print("查詢月份: " + new SimpleDateFormat("yyyy-MM").format(month));

			Calendar cal = Calendar.getInstance();

			firstDate = new Date(month.getTime());
			cal.setFirstDayOfWeek(Calendar.SUNDAY);
			cal.setTime(firstDate);
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
			firstDate = new Date(cal.getTime().getTime());

			lastDayOfMonth = new java.util.Date(month.getTime());
			lastDayOfMonth.setMonth(lastDayOfMonth.getMonth() + 1);
			lastDayOfMonth.setDate(lastDayOfMonth.getDate() - 1);
			cal.setTime(lastDayOfMonth);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			endDate = new Date(cal.getTime().getTime());

			pstmt.setString(1, "%" + empno + "%");
			pstmt.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(firstDate));
			pstmt.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(endDate));

			PageResultSet pageResultSet = new PageResultSet(pstmt.executeQuery(), page);

			rs = pageResultSet.getResultSet();
			for (int i = 0; i < pageResultSet.getPageRowsCount(); i++) {
				Employee employee = new Employee();
				employee.setName(rs.getString("name"));
				Worktime worktime = createWorktime(rs);
				worktime.setEmployee(employee);
				worktimeList.add(worktime);
				rs.next();
			}

			dataMap.put("List<Worktime>", worktimeList);
			dataMap.put("page", new Page(pageResultSet));
		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
		return dataMap;
	}

	public Worktime createWorktime(ResultSet rs) {
		Worktime worktime = new Worktime();
		try {
			worktime.setEmpNo(rs.getString("EmpNo"));
			worktime.setWeekFirstDay(rs.getDate("WEEK_FIRST_DAY"));
			worktime.setStatus(rs.getString("STATUS"));
			worktime.setSunNormal(rs.getInt("SUN_NORMAL"));
			worktime.setSunOvertime(rs.getInt("SUN_OVERTIME"));
			worktime.setMonNormal(rs.getInt("MON_NORMAL"));
			worktime.setMonOvertime(rs.getInt("MON_OVERTIME"));
			worktime.setTueNormal(rs.getInt("TUE_NORMAL"));
			worktime.setTueOvertime(rs.getInt("TUE_OVERTIME"));
			worktime.setWedNormal(rs.getInt("WED_NORMAL"));
			worktime.setWedOvertime(rs.getInt("WED_OVERTIME"));
			worktime.setThuNormal(rs.getInt("THU_NORMAL"));
			worktime.setThuOvertime(rs.getInt("THU_OVERTIME"));
			worktime.setFriNormal(rs.getInt("FRI_NORMAL"));
			worktime.setFriOvertime(rs.getInt("FRI_OVERTIME"));
			worktime.setSatNormal(rs.getInt("SAT_NORMAL"));
			worktime.setSatOvertime(rs.getInt("SAT_OVERTIME"));
			worktime.setNotPassReason(rs.getString("NOT_PASS_REASON"));
			worktime.setWorktimeDetailId(rs.getString("DETAIL_ID"));
			return worktime;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		}
	}

	@Override
	public boolean hasWorktimeDetail(WorktimeDetail detailid) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(detail_id) ");
		sql.append("from worktime_detail ");
		sql.append("where detail_id=? ");

		this.conn = ConnectionHelper.getConnection();
		try {
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, detailid.getDetailId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if ((rs.getInt("count(detail_id)")) > 0) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	@Override
	public boolean hasWorktime(String detailId) {
		Connection private_conn = null;
		PreparedStatement private_pstmt = null;
		ResultSet private_rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append("select count(detail_id) ");
		sql.append("from worktime ");
		sql.append("where detail_id=? ");

		private_conn = ConnectionHelper.getConnection();
		try {
			private_pstmt = private_conn.prepareStatement(sql.toString());
			private_pstmt.setString(1, detailId);
			private_rs = private_pstmt.executeQuery();
			if (private_rs.next()) {
				if ((private_rs.getInt("count(detail_id)")) > 0) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {

			try {
				if (private_rs != null)
					private_rs.close();
				if (private_pstmt != null)
					private_pstmt.close();
				if (private_conn != null)
					private_conn.close();
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}
	}

	@Override
	public Worktime calculateWorktime_time(Worktime worktime) {
		worktime.setSunNormal(0);
		worktime.setSunOvertime(0);
		worktime.setMonNormal(0);
		worktime.setMonOvertime(0);
		worktime.setTueNormal(0);
		worktime.setTueOvertime(0);
		worktime.setWedNormal(0);
		worktime.setWedOvertime(0);
		worktime.setThuNormal(0);
		worktime.setThuOvertime(0);
		worktime.setFriNormal(0);
		worktime.setFriOvertime(0);
		worktime.setSatNormal(0);
		worktime.setSatOvertime(0);
		for (WorktimeDetail wd : worktime.getWorktimeDetailList()) {
			if (worktime.getWorktimeDetailId().equals(wd.getDetailId())) {
				if (wd.getSunNormal() != null) {
					worktime.setSunNormal(worktime.getSunNormal() + wd.getSunNormal());
				}
				if (wd.getSunOvertime() != null) {
					worktime.setSunOvertime(worktime.getSunOvertime() + wd.getSunOvertime());
				}
				if (wd.getMonOvertime() != null) {
					worktime.setMonNormal(worktime.getMonNormal() + wd.getMonNormal());
				}
				if (wd.getMonOvertime() != null) {
					worktime.setMonOvertime(worktime.getMonOvertime() + wd.getMonOvertime());
				}
				if (wd.getTueOvertime() != null) {
					worktime.setTueNormal(worktime.getTueNormal() + wd.getTueNormal());
				}
				if (wd.getTueOvertime() != null) {
					worktime.setTueOvertime(worktime.getTueOvertime() + wd.getTueOvertime());
				}
				if (wd.getWedOvertime() != null) {
					worktime.setWedNormal(worktime.getWedNormal() + wd.getWedNormal());
				}
				if (wd.getWedOvertime() != null) {
					worktime.setWedOvertime(worktime.getWedOvertime() + wd.getWedOvertime());
				}
				if (wd.getThuOvertime() != null) {
					worktime.setThuNormal(worktime.getThuNormal() + wd.getThuNormal());
				}
				if (wd.getThuOvertime() != null) {
					worktime.setThuOvertime(worktime.getThuOvertime() + wd.getThuOvertime());
				}
				if (wd.getFriOvertime() != null) {
					worktime.setFriNormal(worktime.getFriNormal() + wd.getFriNormal());
				}
				if (wd.getFriOvertime() != null) {
					worktime.setFriOvertime(worktime.getFriOvertime() + wd.getFriOvertime());
				}
				if (wd.getSatOvertime() != null) {
					worktime.setSatNormal(worktime.getSatNormal() + wd.getSatNormal());
				}
				if (wd.getSatOvertime() != null) {
					worktime.setSatOvertime(worktime.getSatOvertime() + wd.getSatOvertime());
				}
			}
		}
		return worktime;
	};

	@Override
	public void saveWorktimeDetail(Worktime worktime) {
		StringBuilder sql_insert_worktime = new StringBuilder();
		StringBuilder sql_update_worktime = new StringBuilder();
		StringBuilder sql_delete_WorktimeDetail = new StringBuilder();
		StringBuilder sql_insert_WorktimeDetail = new StringBuilder();

		sql_insert_worktime.append("insert into worktime ");
		sql_insert_worktime.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

		sql_update_worktime.append("update worktime set ");
		sql_update_worktime.append("SUN_NORMAL=?,SUN_OVERTIME=?,MON_NORMAL=?,MON_OVERTIME=?,");
		sql_update_worktime.append("TUE_NORMAL=?,TUE_OVERTIME=?,WED_NORMAL=?,WED_OVERTIME=?,");
		sql_update_worktime.append("THU_NORMAL=?,THU_OVERTIME=?,FRI_NORMAL=?,FRI_OVERTIME=?,");
		sql_update_worktime.append("SAT_NORMAL=?,SAT_OVERTIME=? ");
		sql_update_worktime.append("where detail_id=? ");

		sql_delete_WorktimeDetail.append("delete from worktime_detail ");
		sql_delete_WorktimeDetail.append("where detail_id=? ");

		sql_insert_WorktimeDetail.append("insert into worktime_detail ");
		sql_insert_WorktimeDetail.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		this.conn = ConnectionHelper.getConnection();
		try {
			conn.setAutoCommit(false);

			if (!hasWorktime(worktime.getWorktimeDetailId())) {
				pstmt = conn.prepareStatement(sql_insert_worktime.toString());
				pstmt.setString(1, worktime.getEmpNo());
				pstmt.setDate(2, worktime.getWeekFirstDay());
				pstmt.setString(3, "未繳交");
				pstmt.setInt(4, worktime.getSunNormal());
				pstmt.setInt(5, worktime.getSunOvertime());
				pstmt.setInt(6, worktime.getMonNormal());
				pstmt.setInt(7, worktime.getMonOvertime());
				pstmt.setInt(8, worktime.getTueNormal());
				pstmt.setInt(9, worktime.getTueOvertime());
				pstmt.setInt(10, worktime.getWedNormal());
				pstmt.setInt(11, worktime.getWedOvertime());
				pstmt.setInt(12, worktime.getThuNormal());
				pstmt.setInt(13, worktime.getThuOvertime());
				pstmt.setInt(14, worktime.getFriNormal());
				pstmt.setInt(15, worktime.getFriOvertime());
				pstmt.setInt(16, worktime.getSatNormal());
				pstmt.setInt(17, worktime.getSatOvertime());
				pstmt.setString(18, worktime.getNotPassReason());
				pstmt.setString(19, worktime.getWorktimeDetailId());
			} else {
				pstmt = conn.prepareStatement(sql_update_worktime.toString());
				pstmt.setInt(1, worktime.getSunNormal());
				pstmt.setInt(2, worktime.getSunOvertime());
				pstmt.setInt(3, worktime.getMonNormal());
				pstmt.setInt(4, worktime.getMonOvertime());
				pstmt.setInt(5, worktime.getTueNormal());
				pstmt.setInt(6, worktime.getTueOvertime());
				pstmt.setInt(7, worktime.getWedNormal());
				pstmt.setInt(8, worktime.getWedOvertime());
				pstmt.setInt(9, worktime.getThuNormal());
				pstmt.setInt(10, worktime.getThuOvertime());
				pstmt.setInt(11, worktime.getFriNormal());
				pstmt.setInt(12, worktime.getFriOvertime());
				pstmt.setInt(13, worktime.getSatNormal());
				pstmt.setInt(14, worktime.getSatOvertime());
				pstmt.setString(15, worktime.getWorktimeDetailId());
			}
			pstmt.executeUpdate();

			pstmt = conn.prepareStatement(sql_delete_WorktimeDetail.toString());
			pstmt.setString(1, worktime.getWorktimeDetailId());
			pstmt.executeUpdate();

			pstmt = conn.prepareStatement(sql_insert_WorktimeDetail.toString());
			for (WorktimeDetail wd : worktime.getWorktimeDetailList()) {
				pstmt.setString(1, wd.getDetailId());
				pstmt.setString(2, wd.getWorkName());
				pstmt.setString(3, wd.getWorkContent());
				pstmt.setInt(4, wd.getSunNormal());
				pstmt.setInt(5, wd.getSunOvertime());
				pstmt.setInt(6, wd.getMonNormal());
				pstmt.setInt(7, wd.getMonOvertime());
				pstmt.setInt(8, wd.getTueNormal());
				pstmt.setInt(9, wd.getTueOvertime());
				pstmt.setInt(10, wd.getWedNormal());
				pstmt.setInt(11, wd.getWedOvertime());
				pstmt.setInt(12, wd.getThuNormal());
				pstmt.setInt(13, wd.getThuOvertime());
				pstmt.setInt(14, wd.getFriNormal());
				pstmt.setInt(15, wd.getFriOvertime());
				pstmt.setInt(16, wd.getSatNormal());
				pstmt.setInt(17, wd.getSatOvertime());
				pstmt.executeUpdate();
				pstmt.clearParameters();
			}
			conn.commit();
			System.out.println("完成工時暫存");
		} catch (SQLException e) {
			System.err.println("暫存工時失敗! " + e.getMessage());
			try {
				// 失敗則 回覆操作
				if (conn != null) {
					System.out.println("回復操作");
					conn.rollback();
				}

			} catch (SQLException e1) {
				System.err.println("資料庫錯誤. " + e1.getMessage());
				e1.printStackTrace();
			}
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			try {
				// 調回自動 COMMIT
				if (conn != null)
					conn.setAutoCommit(true);
			} catch (SQLException e) {
				System.err.println("資料庫錯誤. " + e.getMessage());
				e.printStackTrace();
			}
			close();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO 自動產生的 catch 區塊
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public void submitWorktimeDetail(Worktime worktime) {
		StringBuilder sql_insert_worktime = new StringBuilder();
		StringBuilder sql_update_worktime = new StringBuilder();
		StringBuilder sql_delete_WorktimeDetail = new StringBuilder();
		StringBuilder sql_insert_WorktimeDetail = new StringBuilder();
		StringBuilder sql_delete_Callworktime = new StringBuilder();

		sql_delete_Callworktime.append("delete from callworktime ");
		sql_delete_Callworktime.append("where empno=? and week_first_day=?");

		sql_insert_worktime.append("insert into worktime ");
		sql_insert_worktime.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

		sql_update_worktime.append("update worktime set ");
		sql_update_worktime.append("SUN_NORMAL=?,SUN_OVERTIME=?,MON_NORMAL=?,MON_OVERTIME=?,");
		sql_update_worktime.append("TUE_NORMAL=?,TUE_OVERTIME=?,WED_NORMAL=?,WED_OVERTIME=?,");
		sql_update_worktime.append("THU_NORMAL=?,THU_OVERTIME=?,FRI_NORMAL=?,FRI_OVERTIME=?,");
		sql_update_worktime.append("SAT_NORMAL=?,SAT_OVERTIME=?,STATUS=?  ");
		sql_update_worktime.append("where detail_id=? ");

		sql_delete_WorktimeDetail.append("delete from worktime_detail ");
		sql_delete_WorktimeDetail.append("where detail_id=? ");

		sql_insert_WorktimeDetail.append("insert into worktime_detail ");
		sql_insert_WorktimeDetail.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		this.conn = ConnectionHelper.getConnection();
		try {
			conn.setAutoCommit(false);
			System.out.println("asdasdasdasdasd" + getWorktimeStatus(worktime.getWorktimeDetailId()));
			// deletecallworktime
			String status = getWorktimeStatus(worktime.getWorktimeDetailId());
			if (status != null && (status.equals("已催繳") || status.equals("未通過"))) {
				pstmt = conn.prepareStatement(sql_delete_Callworktime.toString());
				pstmt.setString(1, worktime.getEmpNo());
				pstmt.setDate(2, worktime.getWeekFirstDay());

				pstmt.executeUpdate();
			}

			if (!hasWorktime(worktime.getWorktimeDetailId())) {
				pstmt = conn.prepareStatement(sql_insert_worktime.toString());
				pstmt.setString(1, worktime.getEmpNo());
				pstmt.setDate(2, worktime.getWeekFirstDay());
				pstmt.setString(3, "已繳交");
				pstmt.setInt(4, worktime.getSunNormal());
				pstmt.setInt(5, worktime.getSunOvertime());
				pstmt.setInt(6, worktime.getMonNormal());
				pstmt.setInt(7, worktime.getMonOvertime());
				pstmt.setInt(8, worktime.getTueNormal());
				pstmt.setInt(9, worktime.getTueOvertime());
				pstmt.setInt(10, worktime.getWedNormal());
				pstmt.setInt(11, worktime.getWedOvertime());
				pstmt.setInt(12, worktime.getThuNormal());
				pstmt.setInt(13, worktime.getThuOvertime());
				pstmt.setInt(14, worktime.getFriNormal());
				pstmt.setInt(15, worktime.getFriOvertime());
				pstmt.setInt(16, worktime.getSatNormal());
				pstmt.setInt(17, worktime.getSatOvertime());
				pstmt.setString(18, worktime.getNotPassReason());
				pstmt.setString(19, worktime.getWorktimeDetailId());
			} else {
				pstmt = conn.prepareStatement(sql_update_worktime.toString());
				pstmt.setInt(1, worktime.getSunNormal());
				pstmt.setInt(2, worktime.getSunOvertime());
				pstmt.setInt(3, worktime.getMonNormal());
				pstmt.setInt(4, worktime.getMonOvertime());
				pstmt.setInt(5, worktime.getTueNormal());
				pstmt.setInt(6, worktime.getTueOvertime());
				pstmt.setInt(7, worktime.getWedNormal());
				pstmt.setInt(8, worktime.getWedOvertime());
				pstmt.setInt(9, worktime.getThuNormal());
				pstmt.setInt(10, worktime.getThuOvertime());
				pstmt.setInt(11, worktime.getFriNormal());
				pstmt.setInt(12, worktime.getFriOvertime());
				pstmt.setInt(13, worktime.getSatNormal());
				pstmt.setInt(14, worktime.getSatOvertime());
				pstmt.setString(15, "已繳交");
				pstmt.setString(16, worktime.getWorktimeDetailId());
			}

			pstmt.executeUpdate();

			// delete
			pstmt = conn.prepareStatement(sql_delete_WorktimeDetail.toString());
			pstmt.setString(1, worktime.getWorktimeDetailId());
			pstmt.executeUpdate();
			// insert for(WorktimeDetail wd:worktimedetaillist)
			pstmt = conn.prepareStatement(sql_insert_WorktimeDetail.toString());
			for (WorktimeDetail wd : worktime.getWorktimeDetailList()) {
				// / setString.....
				pstmt.setString(1, wd.getDetailId());
				pstmt.setString(2, wd.getWorkName());
				pstmt.setString(3, wd.getWorkContent());
				pstmt.setInt(4, wd.getSunNormal());
				pstmt.setInt(5, wd.getSunOvertime());
				pstmt.setInt(6, wd.getMonNormal());
				pstmt.setInt(7, wd.getMonOvertime());
				pstmt.setInt(8, wd.getTueNormal());
				pstmt.setInt(9, wd.getTueOvertime());
				pstmt.setInt(10, wd.getWedNormal());
				pstmt.setInt(11, wd.getWedOvertime());
				pstmt.setInt(12, wd.getThuNormal());
				pstmt.setInt(13, wd.getThuOvertime());
				pstmt.setInt(14, wd.getFriNormal());
				pstmt.setInt(15, wd.getFriOvertime());
				pstmt.setInt(16, wd.getSatNormal());
				pstmt.setInt(17, wd.getSatOvertime());
				pstmt.executeUpdate();
				pstmt.clearParameters();
			}
			conn.commit();
			System.out.println("完成工時暫存");
		} catch (SQLException e) {
			System.err.println("連假更新失敗! " + e.getMessage());
			try {
				// 失敗則 回覆操作
				if (conn != null) {
					System.out.println("回復操作");
					conn.rollback();
				}

			} catch (SQLException e1) {
				System.err.println("資料庫錯誤. " + e1.getMessage());
				e1.printStackTrace();
			}
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			try {
				// 調回自動 COMMIT
				if (conn != null)
					conn.setAutoCommit(true);
			} catch (SQLException e) {
				System.err.println("資料庫錯誤. " + e.getMessage());
				e.printStackTrace();
			}
			close();
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO 自動產生的 catch 區塊
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public String getWorktimeStatus(String detailId) {
		Connection private_conn = null;
		PreparedStatement private_pstmt = null;
		ResultSet private_rs = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select status ");
		sql.append("from worktime ");
		sql.append("where DETAIL_ID =? ");
		private_conn = ConnectionHelper.getConnection();
		try {
			private_pstmt = private_conn.prepareStatement(sql.toString());
			private_pstmt.setString(1, detailId);
			private_rs = private_pstmt.executeQuery();

			if (private_rs.next()) {
				return private_rs.getString("status");
			}
		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			try {
				if (private_rs != null)
					private_rs.close();
				if (private_pstmt != null)
					private_pstmt.close();
				if (private_conn != null)
					private_conn.close();
			} catch (SQLException e) {
				e.printStackTrace(System.err);
			}
		}

		return null;
	}

	public List<String[]> getExcel(String yearMonth, String keyword) {
		List<String[]> worktimeList = new ArrayList<String[]>();
		String by = "name";
		if (keyword.matches("\\d*"))
			by = "empno";
		System.out.println("月:"+yearMonth+" by: "+by+" keyword: "+keyword);
		Date month = null;
		Date firstDate= null;
		Date endDate= null;
		Date lastDayOfMonth= null;
		StringBuilder sql = new StringBuilder();
		sql.append("select a.*,b.name ");
		sql.append("from worktime a,employee b ");
		sql.append("where a.empno=b.empno ");
		sql.append("and b." + by + " like ? ");
		if (!yearMonth.equals(""))
			sql.append("and to_char(a.week_first_day,'yyyy-mm-dd') between ? and ? ");
		sql.append("and status='已通過'");
		sql.append("order by a.WEEK_FIRST_DAY,b.empno ");

		if (!yearMonth.equals("")) {
			try {
				month = new Date(new SimpleDateFormat("yyyy-MM").parse(yearMonth).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Calendar cal = Calendar.getInstance();
			firstDate = new Date(month.getTime());
			cal.setFirstDayOfWeek(Calendar.SUNDAY);
			cal.setTime(firstDate);
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
			firstDate = new Date(cal.getTime().getTime());

			lastDayOfMonth = new java.util.Date(month.getTime());
			lastDayOfMonth.setMonth(lastDayOfMonth.getMonth() + 1);
			lastDayOfMonth.setDate(lastDayOfMonth.getDate() - 1);
			cal.setTime(lastDayOfMonth);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			endDate = new Date(cal.getTime().getTime());
		}

		this.conn = ConnectionHelper.getConnection();
		try {
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1, "%" + keyword + "%");
			if (!yearMonth.equals("")) {
				pstmt.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(firstDate));
				pstmt.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(endDate));
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
				java.sql.Date date = rs.getDate("WEEK_FIRST_DAY");
				String[] worktime = new String[17];
				worktime[0] = rs.getString("empno");
				worktime[1] = simpleDateFormat.format(date) + "~"
						+ simpleDateFormat.format(date.getTime() + 6 * 24 * 60 * 60 * 1000);
				worktime[2] = rs.getString("NAME");
				worktime[3] = String.valueOf(rs.getInt("SUN_NORMAL"));
				worktime[4] = String.valueOf(rs.getInt("SUN_OVERTIME"));
				worktime[5] = String.valueOf(rs.getInt("MON_NORMAL"));
				worktime[6] = String.valueOf(rs.getInt("MON_OVERTIME"));
				worktime[7] = String.valueOf(rs.getInt("TUE_NORMAL"));
				worktime[8] = String.valueOf(rs.getInt("TUE_OVERTIME"));
				worktime[9] = String.valueOf(rs.getInt("WED_NORMAL"));
				worktime[10] = String.valueOf(rs.getInt("WED_OVERTIME"));
				worktime[11] = String.valueOf(rs.getInt("THU_NORMAL"));
				worktime[12] = String.valueOf(rs.getInt("THU_OVERTIME"));
				worktime[13] = String.valueOf(rs.getInt("FRI_NORMAL"));
				worktime[14] = String.valueOf(rs.getInt("FRI_OVERTIME"));
				worktime[15] = String.valueOf(rs.getInt("SAT_NORMAL"));
				worktime[16] = String.valueOf(rs.getInt("SAT_OVERTIME"));

				worktimeList.add(worktime);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return worktimeList;
	}

	/************************************* 以上陳民錞 ****************************************/

	/************************************* 以下吳軒穎 ****************************************/

	@Override
	public int getWeekOfYear(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		int woy = cal.get(Calendar.WEEK_OF_YEAR);
		return woy;
	}

	/************************************* 以上吳軒穎 ****************************************/

	/************************************* 以下張芷瑄 ****************************************/

	@Override
	public Map<String, Object> getUnsubmitEmpList(Page page) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Map<String, String>> UnsubmitEmpList = new ArrayList<Map<String, String>>();
		// 算出 上一週 firstday
		Calendar firstDay = Calendar.getInstance();// 取得此刻日期
		firstDay.set(Calendar.DAY_OF_WEEK, firstDay.getFirstDayOfWeek()); // 換成
																			// 此刻
																			// 這週的星期天
		firstDay.add(Calendar.DAY_OF_WEEK, -7);// 在減七天 變上週

		String firstday = new SimpleDateFormat("yyyyMMdd").format(firstDay.getTime());
		System.out.println("工時催繳日期:" + firstday);

		// 取得未寫的，建立催繳表
		if (!hadCallWorktime(firstday)) {
			// 如果 當週 沒有催繳過
			insertUnWriteWorktimeEmp(firstday);// 幫 沒有 暫存的人建立工時，狀態為未繳交
			insertCallWorktime(firstday);// 將所有 未繳交者 建立催交表
		}

		// 從 催繳工時表 找出未繳交的人
		StringBuilder builder = new StringBuilder();
		builder.append("select c.week_first_day,c.calltimes,e.empno,e.name,e.email ");
		builder.append("from callworktime c, employee e ");
		builder.append("where e.empno=c.empno  ");

		this.conn = ConnectionHelper.getConnection();
		try {
			pstmt = conn.prepareStatement(builder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd");
			Calendar c = Calendar.getInstance();
			// 放這才不用每次都要跑

			PageResultSet pageResultSet = new PageResultSet(pstmt.executeQuery(), page);

			rs = pageResultSet.getResultSet();
			for (int i = 0; i < pageResultSet.getPageRowsCount(); i++) {
				Map<String, String> UnsubmitEmp = new HashMap<String, String>();

				UnsubmitEmp.put("empno", rs.getString("empno"));
				UnsubmitEmp.put("name", rs.getString("name"));
				UnsubmitEmp.put("calltimes", rs.getString("calltimes"));
				UnsubmitEmp.put("email", rs.getString("email"));
				UnsubmitEmp.put("firstday", new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("week_first_day")));

				UnsubmitEmpList.add(UnsubmitEmp);
				rs.next();
			}
			if (UnsubmitEmpList.isEmpty()) {
				return null;
			}

			dataMap.put("List<Map<String, String>>", UnsubmitEmpList);
			dataMap.put("page", new Page(pageResultSet));
			return dataMap;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	/**
	 * 取得“未繳交”者，並且建立 催交表
	 * 
	 * @param firstday
	 *            格式"yyyyMMdd"
	 */
	public void insertCallWorktime(String firstday) {
		StringBuilder sql_insert = new StringBuilder();
		StringBuilder sql_SubSelect = new StringBuilder();// 找出 當週
															// callWorktime表裡面
															// 所沒有的 所有員工(可排除掉
															// 有資料又新增的狀況)
		sql_SubSelect.append("select w.empno,w.WEEK_FIRST_DAY,to_number('0') calltimes ");
		sql_SubSelect.append("from worktime w,callworktime c ");
		sql_SubSelect.append("where w.empno=c.empno(+) ");
		sql_SubSelect.append("and w.week_first_day= to_date(?,'yyyyMMdd') ");
		sql_SubSelect.append("and w.status='未繳交' and c.empno is null ");

		sql_insert.append("insert into callworktime (empno ,week_first_day,calltimes ) (");
		sql_insert.append(sql_SubSelect.toString());// 子查詢
		sql_insert.append(") ");

		Connection private_conn = null;
		PreparedStatement private_pstmt = null;
		ResultSet private_rs = null;
		private_conn = ConnectionHelper.getConnection();

		try {
			private_pstmt = private_conn.prepareStatement(sql_insert.toString());
			private_pstmt.setString(1, firstday);

			int updateCount = private_pstmt.executeUpdate();
			System.out.println("新增" + updateCount + "筆 催繳表資料");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {

			try {
				if (private_rs != null)
					private_rs.close();
				if (private_pstmt != null)
					private_pstmt.close();
				if (private_conn != null)
					private_conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 取得未填寫未繳交的人，並建立當週工時表
	 * 
	 * @param firstday
	 *            格式"yyyyMMdd"
	 */
	public void insertUnWriteWorktimeEmp(String firstday) {
		List<String> empList = new ArrayList<>();
		StringBuilder sql_insert = new StringBuilder();
		StringBuilder sql_subSelect = new StringBuilder();// 找出 當週 需要填寫的人 (排除離職
															// &非員工 &當週還沒進公司)
		sql_subSelect
				.append("Select e.empno,TO_DATE(?,'yyyyMMdd') WEEK_FIRST_DAY,(e.empno||?) DETAIL_ID,'未繳交' status ");
		sql_subSelect.append("From  Worktime w,employee  e ");
		sql_subSelect.append("Where e.position like '%員工%' ");// 身份是員工
		sql_subSelect.append("And e.end is null ");// 還在職
		sql_subSelect.append("And e.empno=w.empno (+) ");// join 顯示出 沒有暫存 的人
		sql_subSelect.append("And w.week_first_day (+)=to_date(?,'yyyyMMdd') ");// 設定某週
		sql_subSelect.append("and (e.BEGIN+1-(TO_NUMBER(TO_CHAR(e.begin,'d'))))<=TO_DATE(?,'yyyyMMdd') ");// 排除當週還沒進公司的人
		sql_subSelect.append("and w.week_first_day is null ");// 找出沒有 暫存 的人

		sql_insert.append("insert into worktime(empno, WEEK_FIRST_DAY,DETAIL_ID,status) (");
		sql_insert.append(sql_subSelect.toString());// 子查詢
		sql_insert.append(")");

		this.conn = ConnectionHelper.getConnection();

		try {
			pstmt = conn.prepareStatement(sql_insert.toString());
			pstmt.setString(1, firstday);
			pstmt.setString(2, firstday);
			pstmt.setString(3, firstday);
			pstmt.setString(4, firstday);
			int UpdateCount = pstmt.executeUpdate();

			System.out.println("新增 " + firstday + "的" + UpdateCount + "筆未暫存的worktime");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}

	}

	@Override
	public void updateStatus(String[] emps, String[] date) {
		StringBuilder builder = new StringBuilder();
		builder.append("update worktime ");
		builder.append("set status='已催繳' ");
		builder.append("where empno =? ");
		builder.append("and week_first_day=? ");
		try {
			this.conn = ConnectionHelper.getConnection();
			conn.setAutoCommit(false);

			pstmt = conn.prepareStatement(builder.toString());
			for (int i = 0; i < emps.length; i++) {
				pstmt.setString(1, emps[i]);
				try {
					pstmt.setDate(2, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(date[i]).getTime()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				pstmt.executeUpdate();
				pstmt.clearParameters();

			}

			conn.commit();
		} catch (SQLException e) {
			System.err.println("催繳失敗! " + e.getMessage());
			try {
				// 失敗則 回覆操作
				if (conn != null) {
					System.out.println("回復操作");
					conn.rollback();
				}

			} catch (SQLException e1) {
				System.err.println("資料庫錯誤. " + e1.getMessage());
				e1.printStackTrace();
			}
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			try {
				// 調回自動 COMMIT
				if (conn != null)
					conn.setAutoCommit(true);
			} catch (SQLException e) {
				System.err.println("資料庫錯誤. " + e.getMessage());
				e.printStackTrace();
			}
			close();
		}

	}

	@Override
	public void addCalltimes(String[] emps, String[] date) {
		StringBuilder builder = new StringBuilder();
		builder.append("update CALLWORKTIME set calltimes= ");
		builder.append("(select calltimes+1 from CALLWORKTIME where EMPNO=? and WEEK_FIRST_DAY=?) ");
		builder.append("where EMPNO=? and WEEK_FIRST_DAY=? ");

		this.conn = ConnectionHelper.getConnection();
		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());

			for (int i = 0; i < emps.length; i++) {
				pstmt.setString(1, emps[i]);
				pstmt.setDate(2, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(date[i]).getTime()));
				pstmt.setString(3, emps[i]);
				pstmt.setDate(4, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(date[i]).getTime()));
				pstmt.executeUpdate();
				pstmt.clearParameters();
			}
			conn.commit();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			close();
		}
	}

	/**
	 * 是否 有催交過
	 * 
	 * @param firstDate
	 *            格式 'yyyyMMdd'
	 * @return
	 */
	private boolean hadCallWorktime(String firstDate) {
		Connection private_conn = null;
		PreparedStatement private_pstmt = null;
		ResultSet private_rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) ");
		sql.append("from callworktime c, employee e ");
		sql.append("where e.empno=c.empno(+) ");
		sql.append("and e.end is null ");
		sql.append("and e.POSITION like '%員工%' ");
		sql.append("and ( e.BEGIN+1-(TO_NUMBER(TO_CHAR(e.begin,'D'))) )<=TO_DATE(?,'yyyyMMdd') ");
		sql.append("and c.WEEK_FIRST_DAY(+)=TO_DATE(?,'yyyyMMdd') ");
		sql.append("and c.empno is null ");

		private_conn = ConnectionHelper.getConnection();
		try {
			private_pstmt = private_conn.prepareStatement(sql.toString());
			private_pstmt.setString(1, firstDate);
			private_pstmt.setString(2, firstDate);
			private_rs = private_pstmt.executeQuery();
			if (private_rs.next()) {
				if (private_rs.getInt("count(*)") == 0) {
					return true;
				}
			}
		} catch (SQLException e) {
			System.err.println("資料庫錯誤. " + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (private_rs != null)
					private_rs.close();
				if (private_pstmt != null)
					private_pstmt.close();
				if (private_conn != null)
					private_conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(System.err);
			}

		}
		return false;

	}

	/************************************* 以上張芷瑄 ****************************************/

	/************************************** 以下彥儒 ************************************/
	@Override
	public Map<String, Object> findWorktime(Page page) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Worktime> list = new ArrayList<Worktime>();

		StringBuilder builder = new StringBuilder();
		builder.append("select * ");
		builder.append("from WORKTIME a,EMPLOYEE b ");
		builder.append("where a.empno=b.empno ");
		builder.append("and a.status='已繳交' ");
		builder.append("order by 2,1");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			PageResultSet pageResultSet = new PageResultSet(pstmt.executeQuery(), page);

			rs = pageResultSet.getResultSet();
			for (int i = 0; i < pageResultSet.getPageRowsCount(); i++) {
				Worktime worktime = new Worktime();
				Employee employee = new Employee();

				worktime = createWorktime(rs);
				employee.setEmpno(rs.getString("EMPNO"));
				employee.setName(rs.getString("NAME"));
				worktime.setEmployee(employee);

				Date firstDayOfWeek = worktime.getWeekFirstDay();

				Calendar cal = Calendar.getInstance();
				cal.setTime(firstDayOfWeek);
				cal.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

				list.add(worktime);
				rs.next();
			}
			System.out.println("共 " + list.size() + " 筆待審核資料");
			dataMap.put("List<Worktime>", list);
			dataMap.put("page", new Page(pageResultSet));
		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}

		return dataMap;
	}

	@Override
	public Map<String, Object> findByEmpno(String dateAndWeek, String empno, Page page) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Worktime> list = new ArrayList<Worktime>();
		Calendar cal = Calendar.getInstance();

		StringBuilder builder = new StringBuilder();
		builder.append("select * ");
		builder.append("from WORKTIME a,EMPLOYEE b ");
		builder.append("where a.empno=b.empno ");
		if (empno != null && !empno.isEmpty()) {
			builder.append("and b.empno like ? ");
		}
		if (dateAndWeek != null && !dateAndWeek.isEmpty()) {
			builder.append("and a.WEEK_FIRST_DAY in (TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD')) ");
		}
		builder.append("and a.status='已繳交' ");
		builder.append("order by 2,1 ");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			int index = 1;
			if (empno != null && !empno.isEmpty()) {
				pstmt.setString(index++, "%" + empno + "%");
			}
			if (dateAndWeek != null && !dateAndWeek.isEmpty()) {
				String res[] = dateAndWeek.replace("W", "").split("-");
				int year = Integer.parseInt(res[0]);
				int week = Integer.parseInt(res[1]);
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.WEEK_OF_YEAR, week);
				cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
				Date searchTime = cal.getTime();
				Date secondWeek = new Date(searchTime.getTime() + 7 * 24 * 60 * 60 * 1000);

				SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY-MM-dd");

				pstmt.setString(index++, sdf1.format(searchTime));
				pstmt.setString(index++, sdf1.format(secondWeek));
			}
			PageResultSet pageResultSet = new PageResultSet(pstmt.executeQuery(), page);

			rs = pageResultSet.getResultSet();
			for (int i = 0; i < pageResultSet.getPageRowsCount(); i++) {
				Worktime worktime = new Worktime();
				Employee employee = new Employee();

				worktime = createWorktime(rs);
				employee.setEmpno(rs.getString("EMPNO"));
				employee.setName(rs.getString("NAME"));
				worktime.setEmployee(employee);

				list.add(worktime); // Store the row in the list
				rs.next();
			}
			System.out.println("共 " + list.size() + " 筆待審核資料");
			dataMap.put("List<Worktime>", list);
			dataMap.put("page", new Page(pageResultSet));
		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
		return dataMap;
	}

	@Override
	public Map<String, Object> findByName(String dateAndWeek, String name, Page page) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Worktime> list = new ArrayList<Worktime>();
		Calendar cal = Calendar.getInstance();

		StringBuilder builder = new StringBuilder();
		builder.append("select * ");
		builder.append("from WORKTIME a,EMPLOYEE b ");
		builder.append("where a.empno=b.empno ");
		if (name != null && !name.isEmpty()) {
			builder.append("and b.name like ? ");
		}
		if (dateAndWeek != null && !dateAndWeek.isEmpty()) {
			builder.append("and a.WEEK_FIRST_DAY in (TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'))  ");
		}
		builder.append("and a.status='已繳交' ");
		builder.append("order by 2,1 ");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			int index = 1;
			if (name != null && !name.isEmpty()) {
				pstmt.setString(index++, "%" + name + "%");
			}
			if (dateAndWeek != null && !dateAndWeek.isEmpty()) {
				String[] res = dateAndWeek.replace("W", "").split("-");

				int year = Integer.parseInt(res[0]);
				int week = Integer.parseInt(res[1]);

				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.WEEK_OF_YEAR, week);
				cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

				Date SearchTime = cal.getTime();
				Date secondWeek = new Date(SearchTime.getTime() + 7 * 24 * 60 * 60 * 1000);

				SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY-MM-dd");
				System.out.println("1."+sdf1.format(SearchTime));
				System.out.println("2."+sdf1.format(secondWeek));
				pstmt.setString(index++, sdf1.format(SearchTime));
				pstmt.setString(index++, sdf1.format(secondWeek));
			}

			PageResultSet pageResultSet = new PageResultSet(pstmt.executeQuery(), page);

			rs = pageResultSet.getResultSet();
			for (int i = 0; i < pageResultSet.getPageRowsCount(); i++) {
				Worktime worktime = new Worktime();
				Employee employee = new Employee();

				worktime = createWorktime(rs);
				employee.setEmpno(rs.getString("EMPNO"));
				employee.setName(rs.getString("NAME"));
				worktime.setEmployee(employee);

				Date firstDayOfWeek = worktime.getWeekFirstDay();
				cal.setTime(firstDayOfWeek);
				cal.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

				list.add(worktime); // Store the row in the list
				rs.next();
			}

			System.out.println("共 " + list.size() + " 筆待審核資料");
			dataMap.put("List<Worktime>", list);
			dataMap.put("page", new Page(pageResultSet));
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
		return dataMap;
	}

	public void checkWorktime(String detailId) {

		StringBuilder builder = new StringBuilder();
		builder.append("update worktime ");
		builder.append("set status = '已通過' ");
		builder.append("where DETAIL_ID = ? ");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());
			pstmt.setString(1, detailId);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	/************************************** 以上彥儒 ************************************/

	private Date calWeekFirstDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		return cal.getTime();
	}

	private Date parseDate(String Date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(Date);
		} catch (ParseException e) {
			// TODO 自動產生的 catch 區塊
			System.err.println("日期轉換錯誤: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
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

}