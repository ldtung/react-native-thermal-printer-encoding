package com.dantsu.escposprinter_encoding.textparser;

import com.dantsu.escposprinter_encoding.EscPosPrinter;
import com.dantsu.escposprinter_encoding.EscPosPrinterCommands;
import com.dantsu.escposprinter_encoding.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter_encoding.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter_encoding.exceptions.EscPosParserException;
import com.dantsu.escposprinter_encoding.textparser.PrinterTextParser;
import com.dantsu.escposprinter_encoding.textparser.PrinterTextParserColumn;
import com.dantsu.escposprinter_encoding.textparser.PrinterTextParserImg;

import java.util.Hashtable;

public class PrinterTextParserQRCode extends PrinterTextParserImg {

    private static byte[] initConstructor(com.dantsu.escposprinter_encoding.textparser.PrinterTextParserColumn printerTextParserColumn,
                                          Hashtable<String, String> qrCodeAttributes, String data) throws EscPosParserException, EscPosBarcodeException {
        EscPosPrinter printer = printerTextParserColumn.getLine().getTextParser().getPrinter();
        data = data.trim();

        int size = printer.mmToPx(20f);

        if (qrCodeAttributes.containsKey(com.dantsu.escposprinter_encoding.textparser.PrinterTextParser.ATTR_QRCODE_SIZE)) {
            String qrCodeAttribute = qrCodeAttributes.get(com.dantsu.escposprinter_encoding.textparser.PrinterTextParser.ATTR_QRCODE_SIZE);
            if (qrCodeAttribute == null) {
                throw new EscPosParserException("Invalid QR code attribute : " + com.dantsu.escposprinter_encoding.textparser.PrinterTextParser.ATTR_QRCODE_SIZE);
            }
            try {
                size = printer.mmToPx(Float.parseFloat(qrCodeAttribute));
            } catch(NumberFormatException nfe) {
                throw new EscPosParserException("Invalid QR code " + PrinterTextParser.ATTR_QRCODE_SIZE + " value");
            }
        }

        return EscPosPrinterCommands.QRCodeDataToBytes(data, size);
    }

    public PrinterTextParserQRCode(PrinterTextParserColumn printerTextParserColumn, String textAlign,
                                   Hashtable<String, String> qrCodeAttributes, String data) throws EscPosParserException, EscPosBarcodeException {
        super(
                printerTextParserColumn,
                textAlign,
                PrinterTextParserQRCode.initConstructor(printerTextParserColumn, qrCodeAttributes, data)
        );
    }
}
