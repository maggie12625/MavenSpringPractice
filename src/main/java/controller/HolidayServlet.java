package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Employee;
import model.Holiday;
import service.HolidayService;
import utils.ValidateUtils;

/**
 * Servlet implementation class HolidayServlet
 */
public class HolidayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String HOLIDAY_PAGE = "/WEB-INF/views/holiday/holidayMaintain.jsp";
	private static final String LONG_HOLIDAY_PAGE = "/WEB-INF/views/holiday/longHoliday.jsp";

	private HolidayService holidayService = new HolidayService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		String page = null;

		if (request.getSession().getAttribute("login") == null) {
			request.getRequestDispatcher("./Logout.do").forward(request, response);
			return;
		}
		System.out.println("holiday_action: " + action);
		switch (action) {
		// 轉跳至假日維護畫面
		case "holiday_page":
			page = HOLIDAY_PAGE;
			break;
		// 修改假日
		case "modify_holiday":
			page = doModifyHoliday(request);
			break;

		// 取得假日資料by月份
		case "getHolidays": {
			doGetHolidayByMonth(request, response);
			return;
		}

		// 轉跳至 連假維護畫面
		case "longHoliday_page":
			page = LONG_HOLIDAY_PAGE;
			break;
		case "modify_longHoliday":
			page = doModifyLongHoliday(request);
			break;

		// 新增年假
		case "updateYearHoliday":
			page = doUpdateYearHoliday(request);
			break;

		}

		request.getRequestDispatcher(page).forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void doGetHolidayByMonth(HttpServletRequest request, HttpServletResponse response) {
		Date date = null;
		String Date_str = (String) request.getParameter("month");
		List<Holiday> holidayList = holidayService.getHolidaysByMonth(Date_str);

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String JsonString = gson.toJson(holidayList);
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(JsonString);
			response.flushBuffer();

		} catch (IOException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}

	}

	private String doModifyHoliday(HttpServletRequest request) {
		Date date = null;
		String Date_str=request.getParameter("date");
		String status=request.getParameter("status");
		String time=request.getParameter("time");
		String reason=request.getParameter("reason");
		if(!"HOLIDAY".equals(status))
			time="0";
		
		System.out.println("date: "+Date_str+" ,status: "+status+" ,time: "+time+" ,reason: "+reason);

		
		date=parseDate(Date_str);
		
		Holiday holiday=new Holiday();
		holiday.setChangeDate(date);
		holiday.setHours(Integer.parseInt(time));
		holiday.setReason(reason);   
		holiday.setStatus(status);
		
		//如果沒有填原因
		if(ValidateUtils.isBlank(reason)){
			request.setAttribute("Holiday", holiday);
			request.setAttribute("result", "normalError");
			request.setAttribute("date", new SimpleDateFormat("yyyy-MM-dd").format(holiday.getChangeDate()));
			request.setAttribute("errorMsgs", "請填入原因");
			return HOLIDAY_PAGE;
		}
		
		
		if(holidayService.hasHoliday(holiday)){
			//如果當天已有資料，則更新資料。
			holidayService.update(holiday);
		}else{
			//否則新增資料
			holidayService.insert(holiday);
		}
		request.setAttribute("result", "success");
		
		return HOLIDAY_PAGE;
	}
	
	
	private String doModifyLongHoliday(HttpServletRequest request) {
		
		String startDate_str=request.getParameter("start");
		String endDate_str=request.getParameter("end");
		int startTime=Integer.parseInt(request.getParameter("start_time"));
		int endTime=Integer.parseInt(request.getParameter("end_time"));
		String status=request.getParameter("status");
		String reason=request.getParameter("reason");
		
		Date startDate = parseDate(startDate_str);
		Date endDate = parseDate(endDate_str);
		

		
		System.out.println("放假類型: "+status+" ,原因: "+reason);
		System.out.println("開始日: "+startDate_str+" ,放: "+startTime+"小時");
		System.out.println("結束日: "+endDate_str+" ,放: "+endTime+"小時");
		
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
		
		if(endDate.before(startDate)){
			request.setAttribute("result", "longHolidayError");
			request.setAttribute("startDate", startDate_str);
			request.setAttribute("endDate", endDate_str);
			request.setAttribute("start", start);
			request.setAttribute("end", end);
			request.setAttribute("errorMsgs", "起始日大於結束日。");
			return HOLIDAY_PAGE;
		}
		if(ValidateUtils.isBlank(reason)){
			request.setAttribute("result", "longHolidayError");
			request.setAttribute("startDate", startDate_str);
			request.setAttribute("endDate", endDate_str);
			request.setAttribute("start", start);
			request.setAttribute("end", end);
			request.setAttribute("errorMsgs", "請輸入原因。");
			return HOLIDAY_PAGE;
		}
		
		holidayService.modifyLongHoliday(start, end);

		
		request.setAttribute("result", "success");
		return HOLIDAY_PAGE;
	}
	
	
	//年假
	private String doUpdateYearHoliday(HttpServletRequest request){
		String year=request.getParameter("year");
		System.out.println("修改: "+year);
		holidayService.updateYearHoliday(year);
		request.setAttribute("result", "success");
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

}
