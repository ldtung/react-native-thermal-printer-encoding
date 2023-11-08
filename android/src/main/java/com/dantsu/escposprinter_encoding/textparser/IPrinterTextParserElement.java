package com.dantsu.escposprinter_encoding.textparser;

import com.dantsu.escposprinter_encoding.EscPosPrinterCommands;
import com.dantsu.escposprinter_encoding.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter_encoding.exceptions.EscPosEncodingException;

public interface IPrinterTextParserElement {
    int length() throws EscPosEncodingException;
    IPrinterTextParserElement print(EscPosPrinterCommands printerSocket) throws EscPosEncodingException, EscPosConnectionException;
}
