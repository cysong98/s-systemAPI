/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/


package com.anylogic.ssystem.common.excel.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import org.json.JSONArray;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


@Slf4j
@Service
public class ExcelDownService {

    private boolean isMerged(org.apache.poi.ss.usermodel.Sheet sheet, int rowIdx, int colIdx) {

        for(int i = 0; i < sheet.getNumMergedRegions(); ++i)
        {
            org.apache.poi.ss.util.CellRangeAddress range = sheet.getMergedRegion(i);

            String message = String.format("%d - %d - %d - %d", range.getFirstRow(), range.getLastRow(), range.getFirstColumn(), range.getLastColumn());
            System.out.println(message);

            if( rowIdx >= range.getFirstRow() && rowIdx <= range.getLastRow() && colIdx >= range.getFirstColumn() && colIdx <= range.getLastColumn() ) {
                return true;
            }
        }
        return false;
    }
    public void excelDownload(HttpServletResponse response, Map<String, Object> param, List<HashMap<String, Object>> datalist) throws ParseException, IOException {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(param.get("excelNm").toString());

        JSONArray array = new JSONArray(param.get("staticColumns").toString());  //json 배열
        List<Object> staticColumnList = array.toList();

        JSONArray array2 = new JSONArray(param.get("staticColumns").toString());  //json 배열
        List<Object> mergeColumnList = array2.toList();

        JSONArray array3 = new JSONArray(param.get("staticColumns").toString());  //json 배열
        List<Object> mergeColumnList2 = array3.toList();

        Row row = null;
        Cell cell = null;
        int rowNum = 0;
        row = sheet.createRow(rowNum);

        int startSize = staticColumnList.size();
        int mergeSize = 0;
        int mergePoint = 0;

        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.forInt(1));
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);
        //병합할 셀 위치와 길이를 구해놓는다
        boolean isMultimerge = false;
        int addMergeSize = 0;
        int[] mgPtArr;
        List<HashMap<String, Object>> mergeList = new ArrayList<>();
        for (int i = 0; i < staticColumnList.size(); i++) {
            HashMap<String, Object> columnData = (HashMap<String, Object>) staticColumnList.get(i);
            if (columnData.containsKey("children")) {
                List<Object> childrenColumnList = (List<Object>) columnData.get("children");
                HashMap<String, Object> mergeData = new HashMap<>();

                mergeSize = mergeSize + childrenColumnList.size();

                mergeData.put("mergeSize", childrenColumnList.size());
                if(isMultimerge){
                    mergeData.put("mergePoint", i + addMergeSize-1);
                }else{
                    mergeData.put("mergePoint", i);
                }

                mergeList.add(mergeData);
                isMultimerge = true;
                addMergeSize = childrenColumnList.size();
            }
        }

        //가로 셀 병합 해둠
        for(int i=0;i<mergeList.size();i++){
            sheet.addMergedRegion(new CellRangeAddress(0, 0,
                    Integer.valueOf(mergeList.get(i).get("mergePoint").toString()),
                    Integer.valueOf(mergeList.get(i).get("mergePoint").toString()) + Integer.valueOf(mergeList.get(i).get("mergeSize").toString())-1));
        }

        //merge 대상까지 병합한 임시 컬럼리스트 생성(세로셀 병합용)
        for (int i = 0; i < mergeColumnList.size(); i++) {
            HashMap<String, Object> columnData = (HashMap<String, Object>) mergeColumnList.get(i);
            if (columnData.containsKey("children")) {
                List<Object> childrenColumnList = (List<Object>) columnData.get("children");
                mergeColumnList.addAll(i+1, childrenColumnList);
                i= i+childrenColumnList.size();

            }
        }
        List<Object> tempList = new ArrayList<>();
        for(int i=0;i<mergeColumnList.size();i++){
            HashMap<String, Object> columnData = (HashMap<String, Object>) mergeColumnList.get(i);
            HashMap<String, Object> inputData = new HashMap<>();
            if(columnData.containsKey("field") && !columnData.containsKey("isChild")){
                //inputData = columnData;
                tempList.add(columnData);
            }

            if(columnData.containsKey("children")){
                tempList.add(columnData);
                List<Object> childrenColumnList = (List<Object>) columnData.get("children");
                for(int k=0;k<childrenColumnList.size()-1;k++){
                    tempList.add(columnData);
                }
            }

        }
        List<Object> mergeList2 = new ArrayList<>(mergeColumnList);
        //mergeList2 = mergeColumnList;

        Iterator<Object> iterator = mergeList2.iterator();

        while(iterator.hasNext()) {
            HashMap<String, Object> columnData = (HashMap<String, Object>) iterator.next();
            if(!columnData.containsKey("field")){
                iterator.remove();
            }
        }



        //세로셀 병합(가로 병합 셀 영역 제외)
      for(int i=0;i<mergeList2.size(); i++) {
            HashMap<String, Object> columnData = (HashMap<String, Object>) mergeList2.get(i);
            System.out.println("title : " + columnData.get("title"));

            if(columnData.containsKey("isChild") ){

            }else{
                sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
            }
        }



        //셀 타이틀 채움(병합 하위 셀 제외)
        int celPos = 0;
        for (int i = 0; i < tempList.size(); i++) {
            HashMap<String, Object> data = (HashMap<String, Object>) tempList.get(i);
            cell = row.createCell(i);
            System.out.println("title : " + data.get("title").toString());
            cell.setCellValue(data.get("title").toString());
            cell.setCellStyle(headerStyle);
         
        }

        //병합된 두번째 셀 세팅
        if(isMultimerge){
            rowNum = rowNum +1;
            row = sheet.createRow(rowNum);
            for(int i=0;i<mergeList2.size(); i++) {
                HashMap<String, Object> columnData = (HashMap<String, Object>) mergeList2.get(i);
                System.out.println("title : " + columnData.get("title"));
                cell = row.createCell(i);
                cell.setCellStyle(headerStyle);
                if(columnData.containsKey("isChild") ){
                    cell.setCellValue(columnData.get("title").toString());
                }else{

                }
            }
        }


        //cell setting
        for (int i = 0; i < mergeList2.size(); i++) {
            // cell = row.createCell(i);
            HashMap<String, Object> data = (HashMap<String, Object>) mergeList2.get(i);
            //cell.setCellValue(data.get("title").toString());
            int cellWidth = 0;
            if (data.containsKey("width")) {
                cellWidth = Integer.valueOf(data.get("width").toString().replace("px", "")) * 40;
            } else {
                cellWidth = 3000;
            }
            sheet.setColumnWidth(i, cellWidth);
        }

        for (int i = 0; i < datalist.size(); i++) {
            HashMap<String, Object> data = datalist.get(i);
            rowNum =  rowNum +1;
            row = sheet.createRow(rowNum);
            int cellNum = 0;
            for (int k = 0; k < mergeList2.size(); k++) {

                HashMap<String, Object> columnData = (HashMap<String, Object>) mergeList2.get(k);
                String key = "";
                if (columnData.containsKey("field")) {
                    key = columnData.get("field").toString();
                    if (data.get(key) == null || data.get(key).toString().equals("")) {
                        cell = row.createCell(cellNum);
                        cell.setCellStyle(bodyStyle);
                        cell.setCellValue("");
                        cellNum = cellNum + 1;

                    } else {

                        if (columnData.get("field").toString().equals(key)) {
                            cell = row.createCell(cellNum);
                            cell.setCellStyle(bodyStyle);
                            cell.setCellValue(data.get(key).toString());
                            cellNum = cellNum + 1;
                        }

                    }
                }
            }
        }
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        wb.write(response.getOutputStream());
        wb.close();

    }

}

