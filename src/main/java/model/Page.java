package model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import utils.PageResultSet;

public class Page {

	public Page() {
		// TODO 自動產生的建構子 Stub
	}

	public Page(PageResultSet pRs) {
		this.pagesCount = pRs.getPageCount();
		this.rowsCount = pRs.getRowsCount();
		this.nowPage = pRs.getNowPage();
		this.pageRowsCount = pRs.getPageRowsCount();
		this.pageSize = pRs.getPageSize();
	}

	private int rowsCount;// 總筆數

	private int pageSize;// 一頁幾筆

	private int pagesCount;// 總頁數

	private int nowPage = 1;// 現在頁數

	private int pageRowsCount;// 此頁 資料筆數

	private String action;

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(int pagesCount) {
		this.pagesCount = pagesCount;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public int getPageRowsCount() {
		return pageRowsCount;
	}

	public void setPageRowsCount(int pageRowsCount) {
		this.pageRowsCount = pageRowsCount;
	}

	public void setAction(HttpServletRequest request) {
		StringBuilder uri = new StringBuilder();

		uri.append(request.getContextPath());
		uri.append(request.getServletPath() + "?");
		Enumeration enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String parameter = (String) enumeration.nextElement();
			if (parameter.equals("page"))
				continue;
			uri.append(parameter);
			uri.append("=");
			// String value = request.getParameter(parameter);
			//
			// try {
			// value = URLEncoder.encode(request.getParameter(parameter),
			// "utf-8");
			// } catch (UnsupportedEncodingException e) {
			// // TODO 自動產生的 catch 區塊
			// System.out.println("字體編碼錯誤:" + e.getMessage());
			// }
			//
			// value=new String(value);
			uri.append(request.getParameter(parameter));
			uri.append("&");
		}

		System.out.println(uri.toString());
		this.action = uri.toString();
	}

	public String getAction() {
		return action;
	}

}
