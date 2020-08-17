package com.nirshal.util.excel.renderers.temp;

import com.nirshal.util.excel.StyleTypes;
import com.nirshal.util.excel.XLSOutputCell;
import com.nirshal.util.excel.renderers.UnitsType;
import org.apache.poi.ss.usermodel.CellType;

import java.util.ArrayList;
import java.util.List;

public class TrainingEmpty{

    public UnitsType getType() {
        return UnitsType.EMPTY;
    }

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


