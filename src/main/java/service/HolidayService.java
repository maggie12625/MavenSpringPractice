package service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import dao.HolidayDAO;
import dao.HolidayJDBCDAO;
import model.Holiday;

public class HolidayService {

	private HolidayDAO hDao=new HolidayJDBCDAO();
	
	public HolidayService() {
		// TODO 自動產生的建構子 Stub
	}
	
	/**
	 * 新增一筆假日資料
	 * @param holiday
	 * 假日資料
	 */
	public void insert(Holiday holiday){
		hDao.insert(holiday);
	}

	/**
	 * 更新一筆假日資料
	 * @param holiday
	 * 假日資料
	 */
	public void update(Holiday holiday){
		hDao.update(holiday);
	}
	
	/**
	 * 連假修改
	 * @param startDate
	 * 開始的日期
	 * @param endDate
	 * 結束的日期
	 */
	public void modifyLongHoliday(Holiday startDate,Holiday endDate){
		hDao.modifyLongHoliday(startDate, endDate);
	}
	
	/**
	 * 取得假日資料by年月份
	 * @param year_month
	 * 傳入年月份。格式:yyyy-MM。<BR>例如 : 2017-3 或 2017-03
	 * @return
	 * {@link List}<{@link Holiday}>
	 */
	public List<Holiday> getHolidaysByMonth(String year_month){
		return hDao.getHolidaysByMonth(year_month);
	}
	
	/**
	 * 取得假日資料by首日<br><b>*注意: Date形態:java.sql.Date</b>
	 * @param firstDate
	 * 首日日期
	 * @return
	 * {@link List}<{@link Holiday}>
	 */
	public Map<String, Holiday> getHolidaysByFirstday(Date firstDate){
		return hDao.getHolidaysByFirstday(firstDate);
	}
	
	
	/**
	 * 回傳是否有當天是否有假日資料
	 * @param holiday
	 * 欲查詢的日子
	 * @return
	 * {@link Boolean}
	 */
	public boolean hasHoliday(Holiday holiday){
		return hDao.hasHoliday(holiday);
	}
	
	
	/**
	 * 新增年假
	 * @param year
	 * 須修改的年份
	 */
	public void updateYearHoliday(String year){
		hDao.updateYearHoliday(year);
	}
	

}
