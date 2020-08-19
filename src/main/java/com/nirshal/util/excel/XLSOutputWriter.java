package com.nirshal.util.excel;

import com.nirshal.model.Training;
import com.nirshal.util.data.DateManager;
import com.nirshal.util.containers.Table;
import lombok.Cleanup;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    public static byte[] export(List<Training> data) throws IOException {
        try {
            @Cleanup XSSFWorkbook workbook =  new XSSFWorkbook();;

            Sheet  sheet = workbook.createSheet();

            styler = new XLSStyler(workbook);
            writeHeader(sheet);

            // Main loop to write the data.
            LocalDateTime latestDate = null;
            for (Training training : data) {
                if (latestDate != null) {
                    // We fill in the remaining gap in dates:
                    while (
                            ChronoUnit.DAYS.between
                                    (
                                            latestDate.atZone(ZoneId.of("Europe/Rome")).toInstant(),
                                            DateManager.zeroingTime(training.getCreationDate()).atZone(ZoneId.of("Europe/Rome")).toInstant()
                                    ) > 1
                    ){
                        latestDate = latestDate.plusDays(1L);
                        appendData(sheet,Renderer.emptyPlaceholder(latestDate));
                    }
                }
                latestDate = DateManager.zeroingTime(training.getCreationDate());
                appendData(sheet, Renderer.toXLSRecord(training));
            }

            setColumnsWidth(sheet);
            fixHeaderStyle(sheet);

            @Cleanup ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

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

        Table<XLSOutputCell> header = new Table<>(XLSOutputCell.EMPTY);

        int startColumn = 0;
        for (XLSOutputCell cell: HEADER){
            header.put(0, startColumn++, cell);
        }

        appendRow(sheet, header, 0);
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

    private static void appendRow(Sheet sheet, Table<XLSOutputCell> table, int rowNumber){
        Row row = sheet.createRow(sheet.getLastRowNum()+1);
        for (int columnNumber = 0; columnNumber < table.getColumnsSize(); columnNumber++){
            newCellAt(row, columnNumber, table.get(rowNumber, columnNumber));
        }
        //Update the number of columns
        numberOfColumns = Math.max(numberOfColumns, table.getColumnsSize());
    }

    private static void appendData(Sheet sheet, Table<XLSOutputCell> record){
        for (int i = 0; i< record.getRowsSize(); i++){
            appendRow(sheet, record, i);
        }
        if (record.getRowsSize() > 1) {
            int startMergeIndex = sheet.getLastRowNum() - record.getRowsSize() + 1;
            int endMergeIndex = sheet.getLastRowNum() + 1;
            for (int i = mergeColumnStartIndex; i < mergeColumnEndIndex; i++) {
                sheet.addMergedRegion(new CellRangeAddress(startMergeIndex, endMergeIndex - 1, i, i));
            }
        }
    }
}
