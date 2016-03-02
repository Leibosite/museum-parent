//package com.qingruan.museum.admin.web.alarm;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.text.SimpleDateFormat;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import lombok.extern.slf4j.Slf4j;
//import net.sf.json.JSONArray;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//
//import com.qingruan.museum.admin.utils.DownExcel;
// 
//
//
//@Slf4j
//@Controller
//@RequestMapping(value = "/doExcel")
//public class DownExcelController {
//	
//	@RequestMapping(value = "/down")
//	public void doExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		request.setCharacterEncoding("utf-8");
//		HttpSession session = request.getSession();
//		session.setAttribute("state", null);
//		String cc = request.getParameter("content").toString();
//		cc = new String(cc.getBytes("ISO-8859-1"), "utf-8");
//		System.out.println("GET方式获取的中文参数值：" + cc);
//		JSONArray json = JSONArray.fromObject(cc);
//		List<DownExcel> ccs = (List<DownExcel>) JSONArray.toCollection(json, DownExcel.class);
//		System.out.println(ccs.size() + "--------------------");
//		// 生成提示信息，
//		response.setContentType("application/vnd.ms-excel");
//		String codedFileName = null;
//		OutputStream fOut = null;
//		try {
//			// 进行转码，使其支持中文文件名
//			codedFileName = java.net.URLEncoder.encode("甘肃省博物馆告警详细信息", "UTF-8");
//			response.setHeader("content-disposition", "attachment;filename="
//					+ codedFileName + ".xls");
//			// response.addHeader("Content-Disposition",
//			// "attachment;   filename=" + codedFileName + ".xls");
//			// 产生工作簿对象
//			HSSFWorkbook workbook = new HSSFWorkbook();
//			// 产生工作表对象
//			HSSFSheet sheet = workbook.createSheet("甘肃省博物馆");
//			HSSFRow row = sheet.createRow(0);// 创建一行
//
//			HSSFCell cell = row.createCell((int) 0);// 创建一列
//			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell.setCellValue("内容");
//			cell = row.createCell((int) 1);// 创建一列
//			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell.setCellValue("日期");
//			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
//			for (int i = 0; i < ccs.size(); i++) {
//				DownExcel c = ccs.get(i);
//				row = sheet.createRow((int) (i + 1));// 创建一行
//
//				cell = row.createCell((int) 0);// 创建一列
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellValue(c.getCc());
//				cell = row.createCell((int) 1);// 创建一列
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellValue(format.format(c.getSendDate()));
//			}
//
//			fOut = response.getOutputStream();
//			workbook.write(fOut);
//
//		} catch (UnsupportedEncodingException e1) {
//		} catch (Exception e) {
//		} finally {
//			try {
//				fOut.flush();
//				fOut.close();
//			} catch (IOException e) {
//			}
//		}
//
//	}
//}
