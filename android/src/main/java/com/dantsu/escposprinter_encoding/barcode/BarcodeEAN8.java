package com.dantsu.escposprinter_encoding.barcode;

import com.dantsu.escposprinter_encoding.EscPosPrinterCommands;
import com.dantsu.escposprinter_encoding.EscPosPrinterSize;
import com.dantsu.escposprinter_encoding.barcode.BarcodeNumber;
import com.dantsu.escposprinter_encoding.exceptions.EscPosBarcodeException;

public class BarcodeEAN8 extends BarcodeNumber {
    public BarcodeEAN8(EscPosPrinterSize printerSize, String code, float widthMM, float heightMM, int textPosition) throws EscPosBarcodeException {
        super(printerSize, EscPosPrinterCommands.BARCODE_TYPE_EAN8, code, widthMM, heightMM, textPosition);
    }

    @Override
    public int getCodeLength() {
        return 8;
    }
}
