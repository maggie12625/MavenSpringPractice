package service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import dao.WorktimeDAO;
import dao.WorktimeJDBCDAO;

public class excelService {
	private String path = null;

	private WorktimeDAO wDAO = new WorktimeJDBCDAO();

	public void getExcel(HttpServletRequest request, String yearMonth, String keyword, OutputStream output) {

		try {
			HSSFWorkbook workbook = creatExcel(request, yearMonth, keyword);

			workbook.write(output);
			workbook.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendExcel(HttpServletRequest request, String yearMonth, String keyword, String email, String fileName) {
		HSSFWorkbook workbook = creatExcel(request, yearMonth, keyword);
		ByteArrayOutputStream out =new ByteArrayOutputStream();		
		
		String title="工時系統-工時報表";
		StringBuilder html=new StringBuilder();
		String nowTime=new SimpleDateFormat("yyyy/MM/dd hh:mm ").format(new Date());
		String month=(yearMonth.equals("")) ? "至今":yearMonth.replace("-", "年")+"月";
		String by=(keyword.equals("")) ? "不限":keyword;
		html.append("<style>p{margin: 0 0;}p span{margin: 0 10px;}</style>");
		html.append("<center><h1>工時匯出</h1><hr>");
		html.append("<p>您在" + nowTime + " 匯出</p><br>");
		html.append("<p>時間為: ");
		html.append(month);
		html.append("</p><p><br>搜尋條件為: ");
		html.append(by);
		html.append("<br></p>的工時報表");
		html.append("</center>");
		try {
			workbook.write(out);
			
			new EmailService().sendExcelFileMail(email, title, html.toString(), fileName , out.toByteArray());
			
			out.close();
			workbook.close();
		} catch (AddressException e) {
			System.err.println("電子郵件地址設定錯誤: " + e.getMessage());
		} catch (MessagingException e) {
			System.err.println("電子郵件設定錯誤: " + e.getMessage());
		}catch (IOException e1) {
			System.err.println("I/O錯誤:"+e1.getMessage());
		}
		
	}

	private HSSFWorkbook creatExcel(HttpServletRequest request, String yearMonth, String keyword) {
		HSSFWorkbook workbook = null;
		this.path = request.getRealPath("/").replace("/", "\\") + "WEB-INF\\excel\\工時報表.xls";
		//System.out.println("path:" + path);
		try {
			FileInputStream input = new FileInputStream(path);

			workbook = new HSSFWorkbook(input);
			List<String[]> workTimelist = wDAO.getExcel(yearMonth, keyword);
			HSSFSheet worksheet = workbook.getSheetAt(0);

			for (int i = 1; i < workTimelist.size() + 1; i++) {
				String[] worktime = workTimelist.get(i - 1);
				HSSFRow row = worksheet.createRow(i);
				for (int j = 0; j < worktime.length; j++) {
					row.createCell((short) j).setCellValue(worktime[j]);
				}

			}
			for (int i = 0; i < workTimelist.get(0).length; i++) {
				worksheet.autoSizeColumn((short) i);
			}

			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("I/O錯誤:"+e.getMessage());
		}
		return workbook;
	}
}
