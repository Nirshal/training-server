package com.nirshal.util.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.CellType;

@AllArgsConstructor
public class XLSOutputCell {

    public static XLSOutputCell EMPTY = new XLSOutputCell(CellType.STRING, StyleTypes.TEXT,"");

    @Getter
    private CellType type;
    @Getter
    private StyleTypes style;
    @Getter
    private Object Value;

}
