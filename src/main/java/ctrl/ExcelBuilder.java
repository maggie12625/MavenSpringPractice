package ctrl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import service.ExcelService;
import service.ExcelService;
/**
 * This class builds an Excel spreadsheet document using Apache POI library.
 * @author www.codejava.net
 *
 */
public class ExcelBuilder extends AbstractXlsView {

	 

	@Override
	protected void buildExcelDocument(Map<String, Object> model, 
			Workbook workbook, 
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ExcelService servie= new ExcelService();
		HSSFWorkbook book = (HSSFWorkbook)workbook;
		
		HSSFSheet worksheet =book.createSheet();
		String yearMonth = (String) model.get("yearMonth");
		String fileName = (yearMonth.equals("") ? "至今" : yearMonth) + "工時報表";
		try {
			fileName = java.net.URLEncoder.encode(fileName, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO 自動產生的 catch 區塊
			System.err.println("編碼錯誤:" + e1.getMessage());
		}
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
		servie.fillDataIntoSheet(worksheet, yearMonth, "");
		
	}

}