package com.nirshal.util.excel;

import lombok.Cleanup;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class XLSOutputWriter {

    private static XLSStyler styler;
    private static int numberOfColumns = 0;
    private static final int mergeColumnStartIndex = 0;
    private static final int mergeColumnEndIndex = 7; //Last excluded.
    private static final List<XLSOutputCell> HEADER = new ArrayList<>(
            Arrays.asList(
                    new XLSOutputCell(CellType.STRING, StyleTypes.HEADER,"Settimana"),
                    new XLSOutputCell(CellType.STRING, StyleTypes.HEADER,"Data"),
                    new XLSOutputCell(CellType.STRING, StyleTypes.HEADER,"Allenamento"),
                    new XLSOutputCell(CellType.STRING, StyleTypes.HEADER,"Sensazioni"),
                    new XLSOutputCell(CellType.STRING, StyleTypes.HEADER,"Tempo Totale"),
                    new XLSOutputCell(CellType.STRING, StyleTypes.HEADER,"Distanza Totale"),
                    new XLSOutputCell(CellType.STRING, StyleTypes.HEADER,"Ritmo Medio"),
                    new XLSOutputCell(CellType.STRING, StyleTypes.HEADER,"Splits")
            )
    );
    private static final int[] columnsSize = new int[] {
            3000,
            6500,
            25000,
            25000,
            4500
    };

    public static byte[] export(List<XLSPrintable> data) throws IOException {
//        boolean appending = FileManager.existsFileOrDirectory(outputFile,false);
        try {
            @Cleanup XSSFWorkbook workbook =  new XSSFWorkbook();;

//            if (appending) {
//                FileInputStream file = new FileInputStream(new File(outputFile));
//                workbook = new XSSFWorkbook(file);
//                sheet = workbook.getSheetAt(0);
//
//            } else {
            Sheet  sheet = workbook.createSheet();
//            }

            styler = new XLSStyler(workbook);
//            if (!appending)
                writeHeader(sheet);

            // Main loop to write the data.
            for (XLSPrintable training : data) {
                appendData(sheet, training);
            }

            setColumnsWidth(sheet);

            fixHeaderStyle(sheet);

            @Cleanup ByteArrayOutputStream out = new ByteArrayOutputStream();//new bytearrayo(outputFile);
            workbook.write(out);
//            workbook.close();
//            out.close();
            return out.toByteArray();

        } catch (IOException e) {
            System.err.println("Error in writing output file: " + e.getLocalizedMessage());
            throw e;
        }
    }
    private static void setColumnsWidth(Sheet sheet){
        //Old lines commented, preferring a fixed size columns strategy.
//        setAutoSizeAll(sheet, true);
//        sheet.setColumnWidth(3,sheet.getColumnWidth(2));

        for (int i = 0; i<numberOfColumns; i++ ){
            if (i<columnsSize.length){
                sheet.setColumnWidth(i, columnsSize[i]);
            } else {
                sheet.setColumnWidth(i, columnsSize[columnsSize.length-1]);
            }
        }
    }

    private static void writeHeader(Sheet sheet){
        appendRow(sheet,HEADER);
        // Shift the header up to avoid to leave an empty at the beginning.
        sheet.shiftRows(0,1,-1);
    }
    private static void setAutoSizeAll(Sheet sheet, boolean auto){
        for (int i = 0; i < numberOfColumns; i++) {
            sheet.autoSizeColumn(i,auto);
        }
    }
    private static void fixHeaderStyle(Sheet sheet){
        Row row = sheet.getRow(0);

        for (int i = HEADER.size(); i<numberOfColumns;i++){
            newCellAt(row, i, new XLSOutputCell(CellType.STRING, StyleTypes.HEADER,""));
        }
    }

    private static void newCellAt(Row row, int column, XLSOutputCell cell){
        Cell cellToAdd = row.createCell(column, cell.getType());
        styler.format(cellToAdd, cell.getStyle());
        switch (cell.getType()){
            case NUMERIC:
                cellToAdd.setCellValue((Date) cell.getValue());
                break;
            case STRING:
                cellToAdd.setCellValue((String) cell.getValue());
                break;
            default:
                cellToAdd.setCellValue("UNRECOGNIZED OBJECT");
        }
    }

    private static void appendRow(Sheet sheet, List<XLSOutputCell> rowData){
        Row row = sheet.createRow(sheet.getLastRowNum()+1);
        for (int i = 0; i<rowData.size(); i++){
            newCellAt(row, i, rowData.get(i));
        }
        //Update the number of columns
        numberOfColumns = (numberOfColumns < rowData.size() ? rowData.size() : numberOfColumns);
    }

    private static void appendData(Sheet sheet, XLSPrintable training){

        List<List<XLSOutputCell>> record = training.toXLSRecord();

        for (List<XLSOutputCell> rowData : record) {
            appendRow(sheet, rowData);
        }

        System.out.println(record.size());
        if (record.size() > 1) {
            int startMergeIndex = sheet.getLastRowNum() - record.size() + 1;
            int endMergeIndex = sheet.getLastRowNum() + 1;
            for (int i = mergeColumnStartIndex; i < mergeColumnEndIndex; i++) {
                sheet.addMergedRegion(new CellRangeAddress(startMergeIndex, endMergeIndex - 1, i, i));
            }
        }
    }
}
