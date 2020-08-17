package com.nirshal.util.excel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.Map;

public class XLSStyler {
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private Map<StyleTypes, XSSFCellStyle> styles;
    private XSSFWorkbook workbook;

    public XLSStyler(XSSFWorkbook workbook){
        this.workbook = workbook;
        this.styles = new HashMap<>();

        XSSFCellStyle textStyle = (XSSFCellStyle) workbook.createCellStyle();
        XSSFCellStyle dateStyle = (XSSFCellStyle) workbook.createCellStyle();
        XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
        XSSFCellStyle subHeaderStyle = (XSSFCellStyle) workbook.createCellStyle();
        XSSFCellStyle centeredTextStyle = (XSSFCellStyle) workbook.createCellStyle();

        setTextStyle(textStyle);
        setTextStyle(centeredTextStyle);
        centeredTextStyle.setAlignment(HorizontalAlignment.CENTER);
        setTextStyle(subHeaderStyle);
        subHeaderStyle.setFont(getFont("Arial Black", 9, false));


        setDateStyle(dateStyle);
        setHeaderStyle(headerStyle);

        styles.put(StyleTypes.TEXT, textStyle);
        styles.put(StyleTypes.DATE, dateStyle);
        styles.put(StyleTypes.HEADER, headerStyle);
        styles.put(StyleTypes.SUB_HEADER, subHeaderStyle);
        styles.put(StyleTypes.CENTERED_TEXT, centeredTextStyle);
    }
    public void format(Cell cell, StyleTypes style){
        cell.setCellStyle(styles.get(style));
    }
    private void setBaseStyle(XSSFCellStyle cellStyle){
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setFont(getFont("Helvetica",11, false));
    }
    private void setDateStyle(XSSFCellStyle cellStyle){
        setBaseStyle(cellStyle);
        XSSFCreationHelper createHelper = (XSSFCreationHelper) workbook.getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dddd d MMMM yyyy"));
    }
    private void setTextStyle(XSSFCellStyle cellStyle){
        setBaseStyle(cellStyle);
    }
    private XSSFFont getFont(String name, int size, boolean bold){
        XSSFFont font = workbook.createFont();

        font.setFontName(name); //Trebuchet MS Arial
        font.setFontHeightInPoints((short) size);
        font.setBold(bold);
        return font;
    }

    private void setHeaderStyle(XSSFCellStyle cellStyle){
        setBaseStyle(cellStyle);

        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        cellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cellStyle.setFont(getFont("Helvetica",13,true));
    }

}
