package ctrl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Holiday;
import service.HolidayService;
import utils.ValidateUtils;

@Controller
// @RequestMapping(value="./Holiday.do",produces="text/plain;charset=UTF-8")
public class HolidayCtrl {

	private static final String HOLIDAY_PAGE = "/WEB-INF/views/holiday/holidayMaintain";
	private static final String LONG_HOLIDAY_PAGE = "/WEB-INF/views/holiday/longHoliday";
	private static final String ERROR_PAGE = "login";

	private HolidayService holidayService = new HolidayService();

	// 取得假日資料by月份
	@RequestMapping(value="/Holiday.do",method=RequestMethod.POST)
	public String getHolidys(Model model,HttpSession session,
			@RequestParam(required = false, value ="action") String action, 
			@RequestParam(required = false, value ="month") String month,
			@RequestParam(required = false, value = "reason") String reason,
			@RequestParam(required = false, value = "status") String status,
			@RequestParam(required = false, value = "time") String time,
			@RequestParam(required = false, value = "start") String startDate_str,
			@RequestParam(required = false, value = "end") String endDate_str,
			@RequestParam(required = false, value = "start_time") String start_time,
			@RequestParam(required = false, value = "end_time") String end_time) {

		if (session.getAttribute("login") == null) {
			return ERROR_PAGE;
		}
		
		System.out.println("holiday_action: " + action);
		String page =null;
		
		switch (action) {
		   
		case "modify_longHoliday":
		page = doModifyLongHoliday(model, startDate_str, endDate_str,
		start_time, end_time, status, reason);
		break;

		// 取得假日資料by月份
		case "getHolidays": 
		doGetHolidayByMonth(month, model);
		break;
	      
	 } 
		return page;
	}

	public @ResponseBody List<Holiday> doGetHolidayByMonth(String month, Model model) {

		List<Holiday> holidayList = holidayService.getHolidaysByMonth(month);
		return holidayList;

	}

	public String LingHoliday(Model model, String startDate_str, String endDate_str, String start_time, String end_time,
			String status, String reason) {

		int startTime = Integer.parseInt(start_time);
		int endTime = Integer.parseInt(end_time);

		Date startDate = parseDate(startDate_str);
		Date endDate = parseDate(endDate_str);

		System.out.println("放假類型: " + status + " ,原因: " + reason);
		System.out.println("開始日: " + startDate_str + " ,放: " + startTime + "小時");
		System.out.println("結束日: " + endDate_str + " ,放: " + endTime + "小時");

		Holiday start = new Holiday();
		Holiday end = new Holiday();
		start.setChangeDate(startDate);
		start.setHours(startTime);
		start.setReason(reason);
		start.setStatus(status);
		end.setChangeDate(endDate);
		end.setHours(endTime);
		end.setReason(reason);
		end.setStatus(status);

		if (endDate.before(startDate)) {
			model.addAttribute("result", "longHolidayError");
			model.addAttribute("startDate", startDate_str);
			model.addAttribute("endDate", endDate_str);
			model.addAttribute("start", start);
			model.addAttribute("end", end);
			model.addAttribute("errorMsgs", "起始日大於結束日。");
			return HOLIDAY_PAGE;
		}
		if (ValidateUtils.isBlank(reason)) {
			model.addAttribute("result", "longHolidayError");
			model.addAttribute("startDate", startDate_str);
			model.addAttribute("endDate", endDate_str);
			model.addAttribute("start", start);
			model.addAttribute("end", end);
			model.addAttribute("errorMsgs", "請輸入原因。");
			return HOLIDAY_PAGE;
		}

		holidayService.modifyLongHoliday(start, end);

		model.addAttribute("result", "success");
		return HOLIDAY_PAGE;
	}

	@RequestMapping(value = "/Holiday.do", method = RequestMethod.GET)
	public String Holiday(HttpSession session, Model model,
			@RequestParam(required = false, value = "action") String action,
			@RequestParam(required = false, value = "month") String month,
			@RequestParam(required = false, value = "date") String date,
			@RequestParam(required = false, value = "date") String Date_str,
			@RequestParam(required = false, value = "reason") String reason,
			@RequestParam(required = false, value = "status") String status,
			@RequestParam(required = false, value = "time") String time,
			@RequestParam(required = false, value = "start") String startDate_str,
			@RequestParam(required = false, value = "end") String endDate_str,
			@RequestParam(required = false, value = "start_time") String start_time,
			@RequestParam(required = false, value = "end_time") String end_time,
			@RequestParam(required = false, value = "year") String year) {
		if (session.getAttribute("login") == null) {

			return ERROR_PAGE;
		}
		System.out.println("holiday_action: " + action);
		String page = null;
		switch (action) {

		// 轉跳至假日維護畫面
		case "holiday_page":
			page = HOLIDAY_PAGE;
			break;

		// 修改假日
		case "modify_holiday":
			page = doModifyHoliday(month, Date_str, status, time, reason, model);
			break;

		// 轉跳至 連假維護畫面
		case "longHoliday_page":
			page = LONG_HOLIDAY_PAGE;
			break;

		// 新增年假
		case "updateYearHoliday":
			page = doUpdateYearHoliday(year, model);
			break;

		}

		return page;

	}

	protected String doModifyHoliday(String month, String Date_str, String status, String time, String reason,
			Model model) {
		Date date = null;

		if (!"HOLIDAY".equals(status))
			time = "0";

		System.out.println("date: " + Date_str + " ,status: " + status + " ,time: " + time + " ,reason: " + reason);

		date = parseDate(Date_str);

		Holiday holiday = new Holiday();
		holiday.setChangeDate(date);
		holiday.setHours(Integer.parseInt(time));
		holiday.setReason(reason);
		holiday.setStatus(status);

		// 如果沒有填原因
		if (ValidateUtils.isBlank(reason)) {
			model.addAttribute("Holiday", holiday);
			model.addAttribute("result", "normalError");
			model.addAttribute("date", new SimpleDateFormat("yyyy-MM-dd").format(holiday.getChangeDate()));
			model.addAttribute("errorMsgs", "請填入原因");
			return HOLIDAY_PAGE;
		}

		if (holidayService.hasHoliday(holiday)) {
			// 如果當天已有資料，則更新資料。
			holidayService.update(holiday);
		} else {
			// 否則新增資料
			holidayService.insert(holiday);
		}
		model.addAttribute("result", "success");

		return HOLIDAY_PAGE;
	}

	protected Date parseDate(String Date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(Date);
		} catch (ParseException e) {
			// TODO 自動產生的 catch 區塊
			System.err.println("日期轉換錯誤: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	protected String doModifyLongHoliday(Model model, String startDate_str, String endDate_str, String start_time,
			String end_time, String status, String reason) {

		int startTime = Integer.parseInt(start_time);
		int endTime = Integer.parseInt(end_time);

		Date startDate = parseDate(startDate_str);
		Date endDate = parseDate(endDate_str);

		System.out.println("放假類型: " + status + " ,原因: " + reason);
		System.out.println("開始日: " + startDate_str + " ,放: " + startTime + "小時");
		System.out.println("結束日: " + endDate_str + " ,放: " + endTime + "小時");

		Holiday start = new Holiday();
		Holiday end = new Holiday();
		start.setChangeDate(startDate);
		start.setHours(startTime);
		start.setReason(reason);
		start.setStatus(status);
		end.setChangeDate(endDate);
		end.setHours(endTime);
		end.setReason(reason);
		end.setStatus(status);

		if (endDate.before(startDate)) {
			model.addAttribute("result", "longHolidayError");
			model.addAttribute("startDate", startDate_str);
			model.addAttribute("endDate", endDate_str);
			model.addAttribute("start", start);
			model.addAttribute("end", end);
			model.addAttribute("errorMsgs", "起始日大於結束日。");
			return HOLIDAY_PAGE;
		}
		if (ValidateUtils.isBlank(reason)) {
			model.addAttribute("result", "longHolidayError");
			model.addAttribute("startDate", startDate_str);
			model.addAttribute("endDate", endDate_str);
			model.addAttribute("start", start);
			model.addAttribute("end", end);
			model.addAttribute("errorMsgs", "請輸入原因。");
			return HOLIDAY_PAGE;
		}

		holidayService.modifyLongHoliday(start, end);

		model.addAttribute("result", "success");
		return HOLIDAY_PAGE;
	}

	// 年假
	protected String doUpdateYearHoliday(String year, Model model) {
		System.out.println("修改123: " + year);
		holidayService.updateYearHoliday(year);
		model.addAttribute("result", "success");
		return HOLIDAY_PAGE;

	}

}