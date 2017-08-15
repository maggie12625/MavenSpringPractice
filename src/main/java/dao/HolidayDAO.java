package dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import model.Holiday;

public interface HolidayDAO {
	void insert(Holiday holiday);

	void update(Holiday holiday);

	boolean hasHoliday(Holiday holiday);

	void modifyLongHoliday(Holiday startDate, Holiday endDate);

	List<Holiday> getHolidaysByMonth(String year_month);

	Map<String, Holiday> getHolidaysByFirstday(Date firstDate);

	// 年假
	void updateYearHoliday(String year);
}
