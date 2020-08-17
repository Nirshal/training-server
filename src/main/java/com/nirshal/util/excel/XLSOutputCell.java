package com.nirshal.util.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.CellType;

@AllArgsConstructor
public class XLSOutputCell {
    @Getter
    private CellType type;
    @Getter
    private StyleTypes style;
    @Getter
    private Object Value;

}
