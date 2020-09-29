package com.autoTest.javaFxDataEncry.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel表格工具类
 * @author yangbihua
 */
public class ExcelUtils {

   private static final Logger logger= LoggerFactory.getLogger(ExcelUtils.class);
    public static Workbook workbook;
    public static List<Map<String,Object>> readExcelBySheetName(String path, String sheetName){
        InputStream is=null;
        List<Map<String,Object>> mapList = null;
        try {
            is=new FileInputStream(new File(path));
             mapList=readExcelBySheetName(is,sheetName);
        }catch (FileNotFoundException e){
            logger.error("文件读取失败");
        }finally {
            close(is,null);
        }
        return mapList;
    }

    /**关闭资源**/
    private static void close(InputStream is, OutputStream os){
        try {
            if(is!=null){
                is.close();
            }
            if(os!=null){
                os.close();
            }
        }catch (IOException e){
            logger.error("资源关闭失败"+e.getMessage());
        }
    }

    /**
     * 读取Excel文件的内容
     * @param inputStream excel文件，以InputStream的形式传入
     * @param sheetName sheet名字
     * @return 以List返回excel中内容
     */
    public static List<Map<String, Object>> readExcelBySheetName(InputStream inputStream, String sheetName) {

        //定义工作簿
        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            System.out.println("Excel data file cannot be found!");
        }

        //定义工作表
        XSSFSheet xssfSheet;
        if (sheetName.equals("")) {
            // 默认取第一个子表
            xssfSheet = xssfWorkbook.getSheetAt(0);
        } else {
            xssfSheet = xssfWorkbook.getSheet(sheetName);
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        //定义行
        //默认第一行为标题行，index = 0
        XSSFRow titleRow = xssfSheet.getRow(0);

        //循环取每行的数据
        for (int rowIndex = 1; rowIndex < xssfSheet.getPhysicalNumberOfRows(); rowIndex++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
            if (xssfRow == null) {
                continue;
            }

            Map<String, Object> map = new LinkedHashMap<String, Object>();
            //循环取每个单元格(cell)的数据
            for (int cellIndex = 0; cellIndex < xssfRow.getPhysicalNumberOfCells(); cellIndex++) {
                XSSFCell titleCell = titleRow.getCell(cellIndex);
                XSSFCell xssfCell = xssfRow.getCell(cellIndex);
                map.put(getString(titleCell),getString(xssfCell));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 把单元格的内容转为字符串
     * @param xssfCell 单元格
     * @return 字符串
     */
    public static String getString(XSSFCell xssfCell) {
        if (xssfCell == null) {
            return "";
        }
        if (xssfCell.getCellTypeEnum() == CellType.NUMERIC) {
            return String.valueOf(xssfCell.getNumericCellValue());
        } else if (xssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else {
            return xssfCell.getStringCellValue();
        }
    }


   /**向excel表中写数据**/
    public  static void writeCell(Row row,String data,int cellNull){
        Cell cell=row.getCell(cellNull);
        if(cell==null){
            cell=row.createCell(cellNull);
        }
        cell.setCellValue(data);
    }

    public static void writerResultExecel(List<Map<String,Object>> resultList,String filePath) throws IOException {
        InputStream is=null;
       /* Workbook wb;*/
        File file=new File(filePath);
        Sheet sheet = null;
        logger.info("读取Excel的内容");
        try{
            if(!file.exists()){
                file.createNewFile();
                workbook =new XSSFWorkbook();
                sheet= workbook.createSheet();
            }else {
                is=new FileInputStream(file);
                workbook = WorkbookFactory.create(is);
                /*wb= WorkbookFactory.create(is);*/
            }
            if(sheet == null){
                sheet=workbook.createSheet();
            }
        }catch (Exception e){
            throw  new IOException("读取文件失败",e);
        }
        writeDataToCell(resultList,sheet);
        writerExcelBydataPath(filePath,is,workbook);
    }

    private static void writeDataToCell(List<Map<String, Object>> stringObjectMap,Sheet sheet) {
        for(int i=0;i<stringObjectMap.size();i++){
            Map map;
            map=stringObjectMap.get(i);
            if(i==0){
              int  m=0;
              Row row=sheet.getRow(0);
                for(Object key : map.keySet()){
                    writeCell(row,key.toString(), m);
                    m++;
                }
            }else {
                int n=0;
                Row row=sheet.getRow(i);
                for(Object value : map.values()){
                    writeCell(row,value.toString(), n);
                    n++;
                }
            }
        }
    }

    public static void writerExcelBydataPath(String filePath,InputStream is,Workbook wb) throws IOException {
        FileOutputStream fileOutputStream=null;
        try{
            fileOutputStream=new FileOutputStream(filePath);
            wb.write(fileOutputStream);
        }catch (Exception e){
            throw new IOException("写入excel失败",e);
        }
        finally {
            try {
                if(is!=null){
                    is.close();
                }
                if(fileOutputStream!=null){
                    fileOutputStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
   }

}