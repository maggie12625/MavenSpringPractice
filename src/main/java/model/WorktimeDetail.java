package model;

import java.io.Serializable;


/**
 * The persistent class for the WORKTIME_DETAIL database table.
 * 
 */
public class WorktimeDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	private String detailId;
	
	private String workName;

	private Integer friNormal;

	private Integer friOvertime;

	private Integer monNormal;

	private Integer monOvertime;

	private Integer satNormal;

	private Integer satOvertime;

	private Integer sunNormal;

	private Integer sunOvertime;

	private Integer thuNormal;

	private Integer thuOvertime;

	private Integer tueNormal;

	private Integer tueOvertime;

	private Integer wedNormal;

	private Integer wedOvertime;

	private String workContent;

	public WorktimeDetail() {
	}

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getWorkName() {
		return workName;
	}


	public void setWorkName(String workName) {
		this.workName = workName;
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

	public String getWorkContent() {
		return this.workContent;
	}

	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}

}