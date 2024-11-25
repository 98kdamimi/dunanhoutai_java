package com.junyang.utils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
/**
 * 
* @ClassName: ExcelUtils
* @Description: excle工具类
* @author chenqi
* @date 2018年11月17日
*
 */
public class ExcelUtils {
	  /**
     * 
    * @Title: importData
    * @Description: 导入excle 数据
    * @param file  文件
    * @param headerRows  忽略头行数
    * @param pojoClass   转换的实体
    * @return List<User>  返回的集合
     */
    public static <T> List<T> importData(MultipartFile file, Class<T> pojoClass){
       try {
    	   if (file == null) {
               return null;
           }
           List<T> list  = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, new ImportParams());
           return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
    }
    
    public static <T> List<T> importData(MultipartFile file, Class<T> pojoClass, ImportParams params){
        try {
     	   if (file == null) {
                return null;
            }
            List<T> list  = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
            return list;
 		} catch (Exception e) {
 			e.printStackTrace();
 			throw new RuntimeException();
 		}
     }
    
    
    /**
     * 
    * @Title: exportExcel
    * @Description: 导出excel
    * @param list  导出的数据
    * @param title  文件标题
    * @param sheetName  sheet名称
    * @param pojoClass  集合的类
    * @param fileName   文件名
    * @param response
    * @return void
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName,HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName), pojoClass, list);
        if (workbook != null) {
            try {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("content-Type", "application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
                workbook.write(response.getOutputStream());
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void exportExcelToDisk(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileSite) {
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName), pojoClass, list);
        if (workbook != null) {
            try {
            	FileOutputStream outputStream = new FileOutputStream(fileSite);
//                response.setCharacterEncoding("UTF-8");
//                response.setHeader("content-Type", "application/vnd.ms-excel");
//                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
                workbook.write(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
