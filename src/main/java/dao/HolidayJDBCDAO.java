package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Holiday;

public class HolidayJDBCDAO implements HolidayDAO {

	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public HolidayJDBCDAO() {
		// TODO 自動產生的建構子 Stub
	}

	@Override
	public void insert(Holiday holiday) {
		StringBuilder builder = new StringBuilder();
		builder.append("insert into HOLIDAY (CHANGE_DATE,STATUS , REASON ,HOURS ) ");
		builder.append("values (?,?,?,?) ");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());

			pstmt.setDate(1, new Date(holiday.getChangeDate().getTime()));
			pstmt.setString(2, holiday.getStatus());
			pstmt.setString(3, holiday.getReason());
			pstmt.setInt(4, holiday.getHours());

			int count = pstmt.executeUpdate();
			System.out.println("新增 " + count + " 筆假日資料");

		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	@Override
	public void update(Holiday holiday) {
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE HOLIDAY SET ");
		builder.append("STATUS=? ");
		builder.append(",REASON=? ");
		builder.append(",HOURS=? ");
		builder.append("WHERE CHANGE_DATE=? ");

		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(builder.toString());

			pstmt.setString(1, holiday.getStatus());
			pstmt.setString(2, holiday.getReason());
			pstmt.setInt(3, holiday.getHours());
			pstmt.setDate(4, new Date(holiday.getChangeDate().getTime()));

			int count = pstmt.executeUpdate();
			System.out.println("更新 " + count + " 筆假日資料");

		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}

	}

	@Override
	public boolean hasHoliday(Holiday holiday) {
		// TODO 自動產生的方法 Stub
		Connection private_conn = null;
		PreparedStatement private_pstmt = null;
		ResultSet private_rs = null;
		StringBuilder builder = new StringBuilder();
		builder.append("select count(change_date) ");
		builder.append("from holiday ");
		builder.append("where change_date = ? ");
		try {
			private_conn = ConnectionHelper.getConnection();
			private_pstmt = private_conn.prepareStatement(builder.toString());
			private_pstmt.setDate(1, new Date(holiday.getChangeDate().getTime()));

			private_rs = private_pstmt.executeQuery();
			if (private_rs.next()) {
				int count = private_rs.getInt(1);
				if (count == 1)
					return true;
			}

			return false;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			try {
				if (private_rs != null)
					private_rs.close();
				if(private_pstmt!=null)
					private_pstmt.close();
				if(private_conn!=null)
					private_conn.close();
			} catch (SQLException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace(System.err);
			}

		}
	}

	@Override
	public void modifyLongHoliday(Holiday startDate, Holiday endDate) {
		Connection private_conn = null;
		Date start = new Date(startDate.getChangeDate().getTime());
		Date end = new Date(endDate.getChangeDate().getTime());
		Date indexDate = new Date(startDate.getChangeDate().getTime());
		int startTime = startDate.getHours();
		int endTime = endDate.getHours();
		int midTime = 0;
		String reason = startDate.getReason();
		String status = startDate.getStatus();
		Holiday holiday = new Holiday();

		if ("HOLIDAY".equals(status)){
			midTime = 8;
		}else{
			midTime = 0;
			startTime=0;
			endTime=0;
		}
			

		StringBuilder sql_update = new StringBuilder();
		StringBuilder sql_insert = new StringBuilder();
		sql_insert.append("insert into HOLIDAY (CHANGE_DATE,STATUS , REASON ,HOURS ) ");
		sql_insert.append("values (?,?,?,?) ");

		sql_update.append("UPDATE HOLIDAY SET ");
		sql_update.append("STATUS=? ");
		sql_update.append(",REASON=? ");
		sql_update.append(",HOURS=? ");
		sql_update.append("WHERE CHANGE_DATE=? ");
		try {
			this.conn = ConnectionHelper.getConnection();
			this.conn.setAutoCommit(false);

			// 開始修改 中間假日資料
			do {
				indexDate.setTime(indexDate.getTime() + (1 * 24 * 60 * 60 * 1000));
				holiday.setChangeDate(indexDate);
				if (hasHoliday(holiday)) {
					// 已經有資料則 更新資料
					pstmt = conn.prepareStatement(sql_update.toString());
					pstmt.setString(1, status);
					pstmt.setString(2, reason);
					pstmt.setInt(3, midTime);
					pstmt.setDate(4, indexDate);
				} else {
					// 否則 新增資料
					pstmt = conn.prepareStatement(sql_insert.toString());
					pstmt.setDate(1, indexDate);
					pstmt.setString(2, status);
					pstmt.setString(3, reason);
					pstmt.setInt(4, midTime);

				}
				pstmt.executeUpdate();
				pstmt.clearWarnings();

			} while (indexDate.before(new java.util.Date(end.getTime() - (1 * 24 * 60 * 60 * 1000))));

			// 修改 起始假日資料
			holiday.setChangeDate(start);
			if (hasHoliday(holiday)) {
				pstmt = conn.prepareStatement(sql_update.toString());
				pstmt.setString(1, status);
				pstmt.setString(2, reason);
				pstmt.setInt(3, startTime);
				pstmt.setDate(4, start);
			} else {
				pstmt = conn.prepareStatement(sql_insert.toString());
				pstmt.setDate(1, start);
				pstmt.setString(2, status);
				pstmt.setString(3, reason);
				pstmt.setInt(4, startTime);
			}
			pstmt.executeUpdate();
			pstmt.clearParameters();
			// 修改 結束假日資料
			holiday.setChangeDate(end);
			if (hasHoliday(holiday)) {
				pstmt = conn.prepareStatement(sql_update.toString());
				pstmt.setString(1, status);
				pstmt.setString(2, reason);
				pstmt.setInt(3, endTime);
				pstmt.setDate(4, end);
			} else {
				pstmt = conn.prepareStatement(sql_insert.toString());
				pstmt.setDate(1, end);
				pstmt.setString(2, status);
				pstmt.setString(3, reason);
				pstmt.setInt(4, endTime);
			}
			pstmt.executeUpdate();

			// 成功後 COMMIT
			System.out.println("完成連假修改!");
			conn.commit();
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
		}

	}

	@Override
	public List<Holiday> getHolidaysByMonth(String year_month) {
		List<Holiday> holidayList = new ArrayList<Holiday>();
		Date month = null;
		Date firstDate;
		Date endDate;

		try {
			month = new Date(new SimpleDateFormat("yyyy-MM").parse(year_month).getTime());
		} catch (ParseException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}

		System.out.print("查詢月份: " + new SimpleDateFormat("yyyy-MM").format(month));

		Calendar cal = Calendar.getInstance();

		// 計算開始日
		firstDate = new Date(month.getTime());
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.setTime(firstDate);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		firstDate = new Date(cal.getTime().getTime());

		// 計算結束日
		java.util.Date lastDayOfMonth = new java.util.Date(month.getTime());
		lastDayOfMonth.setMonth(lastDayOfMonth.getMonth() + 1);
		lastDayOfMonth.setDate(lastDayOfMonth.getDate() - 1);
		cal.setTime(lastDayOfMonth);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		endDate = new Date(cal.getTime().getTime());

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * ");
		sql.append("FROM HOLIDAY ");
		sql.append("WHERE CHANGE_DATE >= ? ");
		sql.append("AND CHANGE_DATE <= ? ");
		sql.append("ORDER BY CHANGE_DATE ");
		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setDate(1, firstDate);
			pstmt.setDate(2, endDate);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Holiday holiday = createHoliday(rs);
				holidayList.add(holiday);
			}
			System.out.println(" 當月總共 " + holidayList.size() + " 筆假日資料");

			return holidayList;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	@Override
	public Map<String, Holiday> getHolidaysByFirstday(Date firstDate) {
		Map<String, Holiday> Holidays = new HashMap<String, Holiday>();
		Holidays.put("sun", null);
		Holidays.put("mon", null);
		Holidays.put("tue", null);
		Holidays.put("wed", null);
		Holidays.put("thu", null);
		Holidays.put("fri", null);
		Holidays.put("sat", null);
		Date endDate;
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.setTime(firstDate);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		endDate = new Date(cal.getTime().getTime());

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * ");
		sql.append("FROM HOLIDAY ");
		sql.append("WHERE CHANGE_DATE >= ? ");
		sql.append("AND CHANGE_DATE <= ? ");
		sql.append("ORDER BY CHANGE_DATE ");
		try {
			this.conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setDate(1, firstDate);
			pstmt.setDate(2, endDate);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Holiday holiday = createHoliday(rs);
				switch (holiday.getChangeDate().getDay()) {
				case 0:
					Holidays.replace("sun", holiday);
					break;
				case 1:
					Holidays.replace("mon", holiday);
					break;
				case 2:
					Holidays.replace("tue", holiday);
					break;
				case 3:
					Holidays.replace("wed", holiday);
					break;
				case 4:
					Holidays.replace("thu", holiday);
					break;
				case 5:
					Holidays.replace("fri", holiday);
					break;
				case 6:
					Holidays.replace("sat", holiday);
					break;

				}

			}

			System.out.println("當週總共 " + Holidays.size() + " 筆假日資料");

			return Holidays;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			close();
		}
	}

	// 年假
	@Override
	public void updateYearHoliday(String year) {
		Connection private_conn = null;
		Date beginDateOfWeek = null;
		Date endDateOfYear = null;
		Date endDateOfWeek = null;

		try {
			beginDateOfWeek = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(year + "-01-01").getTime());
			endDateOfYear = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(year + "-12-31").getTime());
		} catch (ParseException e1) {
			System.out.println("日期轉換錯誤");
			e1.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();

		// 計算開始日
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.setTime(beginDateOfWeek);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		beginDateOfWeek = new Date(cal.getTime().getTime());

		// 計算結束日
		cal.setTime(endDateOfYear);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		endDateOfYear = new Date(cal.getTime().getTime());

		// StringBuilder sql_update = new StringBuilder();
		// sql_update.append("UPDATE HOLIDAY SET ");
		// sql_update.append("STATUS='HOLIDAY' ");
		// sql_update.append(",REASON=' ' ");
		// sql_update.append(",HOURS= 8 ");
		// sql_update.append("WHERE CHANGE_DATE=? ");

		StringBuilder sql_insert = new StringBuilder();
		sql_insert.append("insert into HOLIDAY (CHANGE_DATE,STATUS , REASON ,HOURS ) ");
		sql_insert.append("values (?,'HOLIDAY',' ',8) ");

		try {
			private_conn = ConnectionHelper.getConnection();
			private_conn.setAutoCommit(false);

			pstmt = private_conn.prepareStatement(sql_insert.toString());

			while (beginDateOfWeek.before(endDateOfYear)) {
				// 計算當周的最後一天:星期6
				cal.setTime(beginDateOfWeek);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				endDateOfWeek = new Date(cal.getTime().getTime());

				// beginDateOfWeek=某周的星期天
				// endDateOfWeek=某周的星期六
				Holiday sunday = new Holiday();
				Holiday saturday = new Holiday();
				sunday.setChangeDate(beginDateOfWeek);
				saturday.setChangeDate(endDateOfWeek);
				// 星期天
				if (!hasHoliday(sunday)) {
					// 若沒有假日資料則 新增
					pstmt.setDate(1, beginDateOfWeek);
					pstmt.executeUpdate();
					pstmt.clearParameters();
				}
				// 星期六
				if (!hasHoliday(saturday)) {
					// 若沒有假日資料則 新增
					pstmt.setDate(1, endDateOfWeek);
					pstmt.executeUpdate();
					pstmt.clearParameters();
				}

				// 做好更新假日後，某周+7天變下一周
				beginDateOfWeek.setTime(beginDateOfWeek.getTime() + (7 * 24 * 60 * 60 * 1000));
			}

			private_conn.commit();
			System.out.println("年假更新成功!");
		} catch (SQLException e) {
			System.err.println("年假更新失敗! " + e.getMessage());
			try {
				// 失敗則 回覆操作
				if (private_conn != null) {
					System.out.println("回復操作");
					private_conn.rollback();
				}

			} catch (SQLException e1) {
				System.err.println("資料庫錯誤. " + e1.getMessage());
				e1.printStackTrace();
			}
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		} finally {
			try {
				// 調回自動 COMMIT
				if (private_conn != null)
					private_conn.setAutoCommit(true);
			} catch (SQLException e) {
				System.err.println("資料庫錯誤. " + e.getMessage());
				e.printStackTrace();
			}
			close();
			if (private_conn != null) {
				try {
					private_conn.close();
				} catch (SQLException e) {
					// TODO 自動產生的 catch 區塊
					e.printStackTrace(System.err);
				}
			}
		}

	}

	public Holiday createHoliday(ResultSet rs) {
		Holiday holiday = new Holiday();

		try {
			holiday.setChangeDate(rs.getDate("CHANGE_DATE"));
			holiday.setStatus(rs.getString("status"));
			holiday.setReason(rs.getString("reason"));
			holiday.setHours(rs.getInt("hours"));
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		}

		return holiday;
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

	public static void main(String[] args) {
		HolidayJDBCDAO dao = new HolidayJDBCDAO();

		Holiday startDate = new Holiday();
		Holiday endDate = new Holiday();
		try {
			startDate.setChangeDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-04-13"));
			endDate.setChangeDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-6-15"));
		} catch (ParseException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
		startDate.setHours(8);
		startDate.setReason("asd");
		startDate.setStatus("Hosa");
		endDate.setHours(8);

		dao.modifyLongHoliday(startDate, endDate);
	}

}
