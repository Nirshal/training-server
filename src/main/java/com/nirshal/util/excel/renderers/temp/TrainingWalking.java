package com.nirshal.util.excel.renderers.temp;

import com.nirshal.util.excel.StyleTypes;
import com.nirshal.util.excel.XLSOutputCell;
import com.nirshal.util.excel.renderers.Interval;
import com.nirshal.util.excel.renderers.UnitsType;
import org.apache.poi.ss.usermodel.CellType;
import java.util.ArrayList;
import java.util.List;

public class TrainingWalking {//extends ArrayList<Interval> implements Comparable<TrainingRun> {

    public UnitsType getType() {
        return UnitsType.WALKING;
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
        //3 - Sensazioni
        row1.add(new XLSOutputCell(
                CellType.STRING,
                StyleTypes.TEXT,
                this.getComment()));
        row2.add(emptyCell);
        row3.add(emptyCell);
        //4 - Tempo Totale
        row1.add(new XLSOutputCell(
                CellType.STRING,
                StyleTypes.CENTERED_TEXT,
                this.getTotalTimeString()));
        row2.add(emptyCell);
        row3.add(emptyCell);
        // 5 - Distanza Totale
        row1.add(new XLSOutputCell(
                CellType.STRING,
                StyleTypes.CENTERED_TEXT,
                this.getTotalDistanceString()));
        row2.add(emptyCell);
        row3.add(emptyCell);
        //6 - Ritmo Medio
        row1.add(new XLSOutputCell(
                CellType.STRING,
                StyleTypes.CENTERED_TEXT,
                this.getMeanPaceString()));
        row2.add(emptyCell);
        row3.add(emptyCell);
        //7 - Splits
        for  (Interval interval : this) {
                row1.add(new XLSOutputCell(
                        CellType.STRING,
                        StyleTypes.SUB_HEADER,
                        interval.getDistanceString()));
                row2.add(new XLSOutputCell(
                        CellType.STRING,
                        StyleTypes.TEXT,
                        interval.getTimeString()));
                row3.add(new XLSOutputCell(
                        CellType.STRING,
                        StyleTypes.TEXT,
                        interval.getPaceString()));
        }
        List<List<XLSOutputCell>> record = new ArrayList<>();
        record.add(row1);
        record.add(row2);
        record.add(row3);
        return record;
    }
}
