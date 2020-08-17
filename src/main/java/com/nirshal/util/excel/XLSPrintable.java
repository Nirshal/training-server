package com.nirshal.util.excel;

import java.util.Date;
import java.util.List;

public interface XLSPrintable {
    public List<List<XLSOutputCell>> toXLSRecord();
    public Date getXLSFormatDate();

    }
