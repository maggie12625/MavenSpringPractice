package model;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Map;


/**
 * weekFirstDay型態是 java.sql.Date 
 * 
 * 詳細工時資料ID，請透過get/setWorktimeDetail設定
 * 
 */
public class Worktime implements Serializable {
	private static final long serialVersionUID = 1L;

	private String empNo;
	
	private Date weekFirstDay;
	
	private String worktimeDetailId;
	
	private List<WorktimeDetail> worktimeDetailList;

	private Integer friNormal;

	private Integer friOvertime;

	private Integer monNormal;

	private Integer monOvertime;

	private String notPassReason;

	private Integer satNormal;

	private Integer satOvertime;

	private String status;

	private Integer sunNormal;

	private Integer sunOvertime;

	private Integer thuNormal;

	private Integer thuOvertime;

	private Integer tueNormal;

	private Integer tueOvertime;

	private Integer wedNormal;

	private Integer wedOvertime;

	private Employee employee;
	
	private Map<String,Holiday> holidays;

	public Worktime() {
	}

	public String getEmpNo() {
		return this.empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public Date getWeekFirstDay() {
		return weekFirstDay;
	}

	public void setWeekFirstDay(Date weekFirstDay) {
		this.weekFirstDay = weekFirstDay;
	}

//	public WorktimeDetail getWorktimeDetail() {
//		return worktimeDetail;
//	}
//
//	public void setWorktimeDetail(WorktimeDetail worktimeDetail) {
//		this.worktimeDetail = worktimeDetail;
//	}
	
	
	public String getWorktimeDetailId() {
		return worktimeDetailId;
	}

	public void setWorktimeDetailId(String worktimeDetailId) {
		this.worktimeDetailId = worktimeDetailId;
	}
	
	
	
	
	public Integer getFriNormal() {
		return this.friNormal;
	}

	public void setFriNormal(Integer friNormal) {
		this.friNormal = friNormal;
	}

	public Integer getFriOvertime() {
		return this.friOvertime;
	}

	public void setFriOvertime(Integer friOvertime) {
		this.friOvertime = friOvertime;
	}

	public Integer getMonNormal() {
		return this.monNormal;
	}

	public void setMonNormal(Integer monNormal) {
		this.monNormal = monNormal;
	}

	public Integer getMonOvertime() {
		return this.monOvertime;
	}

	public void setMonOvertime(Integer monOvertime) {
		this.monOvertime = monOvertime;
	}

	public String getNotPassReason() {
		return this.notPassReason;
	}

	public void setNotPassReason(String notPassReason) {
		this.notPassReason = notPassReason;
	}

	public Integer getSatNormal() {
		return this.satNormal;
	}

	public void setSatNormal(Integer satNormal) {
		this.satNormal = satNormal;
	}

	public Integer getSatOvertime() {
		return this.satOvertime;
	}

	public void setSatOvertime(Integer satOvertime) {
		this.satOvertime = satOvertime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSunNormal() {
		return this.sunNormal;
	}

	public void setSunNormal(Integer sunNormal) {
		this.sunNormal = sunNormal;
	}

	public Integer getSunOvertime() {
		return this.sunOvertime;
	}

	public void setSunOvertime(Integer sunOvertime) {
		this.sunOvertime = sunOvertime;
	}

	public Integer getThuNormal() {
		return this.thuNormal;
	}

	public void setThuNormal(Integer thuNormal) {
		this.thuNormal = thuNormal;
	}

	public Integer getThuOvertime() {
		return this.thuOvertime;
	}

	public void setThuOvertime(Integer thuOvertime) {
		this.thuOvertime = thuOvertime;
	}

	public Integer getTueNormal() {
		return this.tueNormal;
	}

	public void setTueNormal(Integer tueNormal) {
		this.tueNormal = tueNormal;
	}

	public Integer getTueOvertime() {
		return this.tueOvertime;
	}

	public void setTueOvertime(Integer tueOvertime) {
		this.tueOvertime = tueOvertime;
	}

	public Integer getWedNormal() {
		return this.wedNormal;
	}

	public void setWedNormal(Integer wedNormal) {
		this.wedNormal = wedNormal;
	}

	public Integer getWedOvertime() {
		return this.wedOvertime;
	}

	public void setWedOvertime(Integer wedOvertime) {
		this.wedOvertime = wedOvertime;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<WorktimeDetail> getWorktimeDetailList() {
		return worktimeDetailList;
	}

	public void setWorktimeDetailList(List<WorktimeDetail> worktimeDetailList) {
		this.worktimeDetailList = worktimeDetailList;
	}

	public Map<String, Holiday> getHolidays() {
		return holidays;
	}

	public void setHolidays(Map<String, Holiday> holidays) {
		this.holidays = holidays;
	}
	
	

}