package ctrl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value="",produces = "text/plain;charset=UTF-8",method=RequestMethod.GET)
public class SimpleController {

	private static final String HOLIDAY_PAGE = "/WEB-INF/views/holiday/holidayMaintain";
	private static final String LONG_HOLIDAY_PAGE = "/WEB-INF/views/holiday/longHoliday";
	private HolidayService holidayService = new HolidayService();

	// 轉跳至假日維護畫面
	@RequestMapping("/holiday_page")
	private String holiday_page() {
		return HOLIDAY_PAGE;
	}

	// 修改假日
	@RequestMapping(value = "/modify_holiday")
	private String doModifyHoliday(@RequestParam("date") String Date_str, @RequestParam("status") String status,
			@RequestParam("time") String time, @RequestParam("reason") String reason, Model model) {

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

	// 取得假日資料by月份
	@RequestMapping(value = "/getHolidays")
	private void doGetHolidayByMonth(@RequestParam("month") String Date_str,HttpServletResponse response) {

		Date date = null;
		List<Holiday> holidayList = holidayService.getHolidaysByMonth(Date_str);

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String JsonString = gson.toJson(holidayList);
		try {
			response.getWriter().write(JsonString);
			response.flushBuffer();

		} catch (IOException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
	}

	// 轉跳至 連假維護畫面
	@RequestMapping(value = "/longHoliday_page")
	private String longHoliday_pag() {
		return LONG_HOLIDAY_PAGE;
	}

	@RequestMapping(value = "/modify_longHoliday",method=RequestMethod.POST)
	private String doModifyLongHoliday(@RequestParam("start") String startDate_str,
			@RequestParam("end") String endDate_str, @RequestParam("start_time") int startTime,
			@RequestParam("end_time") int endTime, @RequestParam("status") String status,
			@RequestParam("reason") String reason,Model model) {

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
	// 新增年假
	@RequestMapping(value = "./updateYearHoliday")
	private String doUpdateYearHoliday(@RequestParam("year") String year,Model model){
		System.out.println("修改: "+year);
		holidayService.updateYearHoliday(year);
		model.addAttribute("result", "success");
		return HOLIDAY_PAGE;
	}
}
