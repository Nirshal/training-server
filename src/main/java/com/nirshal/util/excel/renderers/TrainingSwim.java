package com.nirshal.util.excel.renderers;

import com.nirshal.tutorials.xls.StyleTypes;
import com.nirshal.tutorials.xls.XLSOutputCell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.ArrayList;
import java.util.List;

public class TrainingSwim extends Training {

    @Override
    public UnitsType getType() {
        return UnitsType.SWIMMING;
    }

    @Override
    public List<List<String>> toCSVRecord() {
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
        //3 - Sensazioni
        row1.add(this.getComment());
        row2.add("");
        row3.add("");
        //4 - Tempo Totale
        row1.add(this.getTotalTimeString());
        row2.add("");
        row3.add("");
        // 5 - Distanza Totale
        row1.add(this.getTotalDistanceString());
        row2.add("");
        row3.add("");
        //6 - Ritmo Medio
        row1.add(this.getMeanPaceString());
        row2.add("");
        row3.add("");
        //7 - Splits
        for  (Interval interval : this) {
            if (interval.isFasterThan(this.getType().getRecoverySpeed())){
                row1.add(interval.getDistanceString() + " " + SwimStrokeData.getText(interval.getSwimStroke()));
                row2.add(interval.getTimeString());
                row3.add(interval.getPaceString());

            }else {
                row1.add("Rec.");
                row2.add(interval.getTimeString());
                row3.add("");
            }
        }
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
        List<XLSOutputCell> row4 = new ArrayList<>();

        XLSOutputCell emptyCell = new XLSOutputCell(CellType.STRING, StyleTypes.TEXT,"");
        //0 - Settimana
        row1.add(emptyCell);
        row2.add(emptyCell);
        row3.add(emptyCell);
        row4.add(emptyCell);
        //1 - Data;
        row1.add(new XLSOutputCell(
                CellType.NUMERIC,
                StyleTypes.DATE,
                this.getXLSFormatDate()));
        row2.add(emptyCell);
        row3.add(emptyCell);
        row4.add(emptyCell);
        //2 - Allenamento
        row1.add(new XLSOutputCell(
                CellType.STRING,
                StyleTypes.TEXT,
                this.getDescription()));
        row2.add(emptyCell);
        row3.add(emptyCell);
        row4.add(emptyCell);
        //3 - Sensazioni
        row1.add(new XLSOutputCell(
                CellType.STRING,
                StyleTypes.TEXT,
                this.getComment()));
        row2.add(emptyCell);
        row3.add(emptyCell);
        row4.add(emptyCell);
        //4 - Tempo Totale
        row1.add(new XLSOutputCell(
                CellType.STRING,
                StyleTypes.CENTERED_TEXT,
                this.getTotalTimeString()));
        row2.add(emptyCell);
        row3.add(emptyCell);
        row4.add(emptyCell);
        // 5 - Distanza Totale
        row1.add(new XLSOutputCell(
                CellType.STRING,
                StyleTypes.CENTERED_TEXT,
                this.getTotalDistanceString()));
        row2.add(emptyCell);
        row3.add(emptyCell);
        row4.add(emptyCell);
        //6 - Ritmo Medio
        row1.add(new XLSOutputCell(
                CellType.STRING,
                StyleTypes.CENTERED_TEXT,
                this.getMeanPaceString()));
        row2.add(emptyCell);
        row3.add(emptyCell);
        row4.add(emptyCell);
        //7 - Splits
        for  (Interval interval : this) {
            if (interval.isFasterThan(this.getType().getRecoverySpeed())){
                row1.add(new XLSOutputCell(
                        CellType.STRING,
                        StyleTypes.SUB_HEADER,
                        interval.getDistanceString()));
                row2.add(new XLSOutputCell(
                        CellType.STRING,
                        StyleTypes.SUB_HEADER,
                        SwimStrokeData.getText(interval.getSwimStroke())));
                row3.add(new XLSOutputCell(
                        CellType.STRING,
                        StyleTypes.TEXT,
                        interval.getTimeString()));
                row4.add(new XLSOutputCell(
                        CellType.STRING,
                        StyleTypes.TEXT,
                        interval.getPaceString()));
            }else {
                row1.add(new XLSOutputCell(
                        CellType.STRING,
                        StyleTypes.SUB_HEADER,
                        "Rec."));
                row2.add(new XLSOutputCell(
                        CellType.STRING,
                        StyleTypes.TEXT,
                        interval.getTimeString()));
                row3.add(emptyCell);
                row4.add(emptyCell);
            }
        }
        List<List<XLSOutputCell>> record = new ArrayList<>();
        record.add(row1);
        record.add(row2);
        record.add(row3);
        record.add(row4);

        return record;
    }
}
