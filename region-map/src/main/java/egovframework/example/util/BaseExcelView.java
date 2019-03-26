package egovframework.example.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public abstract class BaseExcelView extends AbstractExcelView {
	HSSFCellStyle titleStyle = null;
	HSSFCellStyle dataCenterStyle = null;
	HSSFCellStyle dataLeftStyle = null;
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	Date currentTime = new Date();
	String dTime = formatter.format(currentTime);
	
	public void init(HSSFWorkbook workBook) {
		// style 가져오기
		titleStyle = getTitleStyle(workBook);
		dataCenterStyle = getDataCenterStyle(workBook);
		dataLeftStyle = getDataLeftStyle(workBook);
	}
	
	/**
	 * title style define
	 * @param wb
	 * @return
	 */
	public HSSFCellStyle getTitleStyle(HSSFWorkbook wb) {
		HSSFFont font = wb.createFont();
		font.setFontName("돋움");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.WHITE.index);

		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setFont(font);
		style.setFillForegroundColor(new HSSFColor.AQUA().getIndex());
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

		return style;
	}
	
	/**
	 * 중앙 정렬 data style define
	 * @param wb
	 * @return
	 */
	public HSSFCellStyle getDataCenterStyle(HSSFWorkbook wb) {
		HSSFFont font = wb.createFont();
		font.setFontName("돋움");

		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setFont(font);

		return style;
	}
	
	/**
	 * 왼쪽 정렬 data style define
	 * @param wb
	 * @return
	 */
	public HSSFCellStyle getDataLeftStyle(HSSFWorkbook wb) {
		HSSFFont font = wb.createFont();
		font.setFontName("돋움");
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style.setFont(font);
		
		return style;
	}
	
	
	/**
	 * column 생성
	 * @param sheet HSSFSheet
	 * @param columnArr 컬럼 명
	 */
	@SuppressWarnings("deprecation")
	public void createColumnLabel(HSSFWorkbook wb, HSSFSheet sheet, String columnArr[]) {
		HSSFRow row = sheet.createRow((short) 0);
		HSSFCell cell = null;
		int columnIdx = 0;

		for (int i = 0; columnArr != null && i < columnArr.length; i++) {
			cell = row.createCell((short) columnIdx++);
			cell.setCellValue(columnArr[i]);
			cell.setCellStyle(titleStyle);
		}
	}
}
