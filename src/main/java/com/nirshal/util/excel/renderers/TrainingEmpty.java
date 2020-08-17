package com.nirshal.util.excel.renderers;

import com.nirshal.tutorials.xls.StyleTypes;
import com.nirshal.tutorials.xls.XLSOutputCell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.ArrayList;
import java.util.List;

public class TrainingEmpty extends Training {

    @Override
    public UnitsType getType() {
        return UnitsType.EMPTY;
    }

    @Override
    public List<List<String>> toCSVRecord(){
        List<String> row1 = new ArrayList<>();
        List<String> row2 = new ArrayList<>();
        List<String> row3 = new ArrayList<>();
        //0 - Settimana
        row1.add("");
        row2.add("");
        row3.add("");
        //1 - Data;
        row1.add(this.getReferenceDateString());
        row2.add("");
        row3.add("");
        //2 - Allenamento
        row1.add(this.getDescription());
        row2.add("");
        row3.add("");

        List<List<String>> record = new ArrayList<>();
        record.add(row1);
        record.add(row2);
        record.add(row3);
        return record;
    }

    @Override
    public List<List<XLSOutputCell>> toXLSRecord() {
        List<XLSOutputCell> row1 = new ArrayList<>();
        List<XLSOutputCell> row2 = new ArrayList<>();
        List<XLSOutputCell> row3 = new ArrayList<>();
        XLSOutputCell emptyCell = new XLSOutputCell(CellType.STRING, StyleTypes.TEXT,"");
        //0 - Settimana
        row1.add(emptyCell);
        row2.add(emptyCell);
        row3.add(emptyCell);
        //1 - Data;
        row1.add(new XLSOutputCell(
                CellType.NUMERIC,
                StyleTypes.DATE,
                this.getXLSFormatDate()));
        row2.add(emptyCell);
        row3.add(emptyCell);
        //2 - Allenamento
        row1.add(new XLSOutputCell(
                CellType.STRING,
                StyleTypes.TEXT,
                this.getDescription()));
        row2.add(emptyCell);
        row3.add(emptyCell);

        List<List<XLSOutputCell>> record = new ArrayList<>();
        record.add(row1);
//        record.add(row2);
//        record.add(row3);
        return record;
    }
}


