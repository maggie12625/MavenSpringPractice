package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Page;

public class PageResultSet {

	private ResultSet rs = null;

	private int rowsCount = 0;// 總筆數

	private int pageSize = 5;// 一頁幾筆

	private int pagesCount = 1;// 總頁數

	private int nowPage = 1;// 現在頁數

	public PageResultSet(ResultSet rs) throws SQLException {
		if (rs == null)
			throw new SQLException("ResultSet 是空的 ");

		rs.last();
		rowsCount = rs.getRow();
		rs.beforeFirst();
		this.rs = rs;

		// 算總頁數
		setPageSize(pageSize);
	}

	public PageResultSet(ResultSet rs, Page page) throws SQLException {
		if (rs == null)
			throw new SQLException("ResultSet 是空的 ");

		rs.last();
		rowsCount = rs.getRow();
		rs.beforeFirst();
		this.rs = rs;

		if (page.getPageSize() != 0)
			this.pageSize=page.getPageSize();// 算總頁數
		if (page.getNowPage() != 0)
			this.nowPage=page.getNowPage();//去那一頁
		
		setPageSize(this.pageSize);// 算總頁數
		gotoPage(this.nowPage);//去那一頁
		
	}

	public PageResultSet(ResultSet rs, int pageSize) throws SQLException {
		if (rs == null)
			throw new SQLException("ResultSet 是空的 ");

		// 算總筆數
		rs.last();
		this.rowsCount = rs.getRow();
		rs.beforeFirst();
		this.rs = rs;

		// 算總頁數
		setPageSize(pageSize);

	}

	/**
	 * 返回總頁數
	 * 
	 * @return
	 */
	public int getPageCount() {
		return pagesCount;
	};

	/**
	 * 返回當前頁的資料筆數
	 * 
	 * @return
	 */
	public int getPageRowsCount() {
		if (pageSize == 0)
			return rowsCount;

		if (rowsCount == 0)
			return 0;

		if (nowPage < getPageCount())
			return pageSize;

		return rowsCount - (nowPage-1) * pageSize;
	};

	/**
	 * 返回分頁大小
	 * 
	 */
	public int getPageSize() {
		return pageSize;

	};

	/**
	 * 返回 總資料筆數
	 * 
	 */
	public int getRowsCount() {
		return rowsCount;
	};

	/**
	 * 返回 當前是哪一頁
	 * 
	 * @return
	 * 
	 */
	public int getNowPage() {
		return nowPage;
	};

	/**
	 * 設置分頁大小
	 * 
	 */
	public void setPageSize(int pageSize) {
		if (pageSize >= 0) {

			this.pageSize = pageSize;

			if (this.rowsCount == 0) {
				//如果總筆數為0 則  總共1頁
				this.pagesCount = 1;
			} else if ((rowsCount % pageSize) == 0) {
				this.pagesCount = (this.rowsCount / pageSize);
			} else {
				this.pagesCount = (this.rowsCount / pageSize) + 1;
			}

		}
	};

	/**
	 * 轉到指定頁
	 * 
	 */
	public void gotoPage(int page) {
		if (rs == null)
			return;

		if (page < 1)
			page = 1;
		if (page > getPageCount())
			page = getPageCount();

		int row = (page - 1) * pageSize + 1;

		try {
			rs.absolute(row);
			this.nowPage = page;
		} catch (SQLException e) {
			System.out.println("資料庫錯誤: " + e.getMessage());
		}
	};

	

	public ResultSet getResultSet() {
		return this.rs;
	}
}
