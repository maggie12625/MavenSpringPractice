package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EMPLOYEE database table.
 * 
 */
public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;

	private String empno;

	private Date begin;

	private String email;

	private Date end;

	private String name;

	private String id;

	private String password;

	private String position;

	private List<Worktime> worktimes;

	public Employee() {
	}

	public String getEmpno() {
		return this.empno;
	}

	public void setEmpno(String empno) {
		this.empno = empno;
	}

	public Date getBegin() {
		return this.begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getEnd() {
		return this.end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public List<Worktime> getWorktimes() {
		return this.worktimes;
	}

	public void setWorktimes(List<Worktime> worktimes) {
		this.worktimes = worktimes;
	}

	public Worktime addWorktime(Worktime worktime) {
		getWorktimes().add(worktime);
		worktime.setEmployee(this);

		return worktime;
	}

	public Worktime removeWorktime(Worktime worktime) {
		getWorktimes().remove(worktime);
		worktime.setEmployee(null);

		return worktime;
	}

}